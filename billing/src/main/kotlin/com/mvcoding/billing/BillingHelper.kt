/*
 * Copyright (C) 2016 Mantas Varnagiris.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */

package com.mvcoding.billing

import android.app.Activity
import android.content.*
import android.os.Bundle
import android.os.IBinder
import android.os.RemoteException
import android.util.Log
import com.android.vending.billing.IInAppBillingService
import com.mvcoding.billing.BillingException.Companion.billingException
import com.mvcoding.billing.BillingResult.Companion.billingResult
import rx.Observable
import rx.Subscriber
import rx.lang.kotlin.PublishSubject

class BillingHelper(private val context: Context, private val base64PublicKey: String, private val loggingEnabled: Boolean = false) {
    private val API_VERSION = 3

    private val BILLING_RESPONSE_RESULT_OK = 0
    private val BILLING_RESPONSE_RESULT_BILLING_UNAVAILABLE = 3

    private val BILLING_HELPER_REMOTE_EXCEPTION = -1001
    private val BILLING_HELPER_SUBSCRIPTIONS_NOT_AVAILABLE = -1009

    private val RESPONSE_CODE = "RESPONSE_CODE"

    private val ITEM_TYPE_SINGLE = "inapp"
    private val ITEM_TYPE_SUBSCRIPTION = "subs"

    private var isDisposed = false
    private var isSetupDone = false
    private var isSubscriptionsSupported = false
    private var purchaseSubject = PublishSubject<BillingPurchaseResult>()

    private lateinit var serviceConnection: ServiceConnection
    private lateinit var billingService: IInAppBillingService

    fun startSetup(): Observable<BillingResult> {
        makeSureItIsNotDisposed()
        makeSureSetupIsNotDone()

        return Observable.defer {
            Observable.create<BillingResult> {
                log("Starting in-app billing setup.")
                serviceConnection = createServiceConnection(it)

                val serviceIntent = Intent("com.android.vending.billing.InAppBillingService.BIND")
                serviceIntent.`package` = "com.android.vending"
                if (!context.packageManager.queryIntentServices(serviceIntent, 0).isEmpty()) {
                    context.bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE)
                } else {
                    it.onError(billingException(BILLING_RESPONSE_RESULT_BILLING_UNAVAILABLE, "Billing service unavailable on device."))
                }
            }
        }
    }

    fun dispose() {
        makeSureSetupIsDone()
        makeSureItIsNotDisposed()
        isSetupDone = false
        context.unbindService(serviceConnection)
        isDisposed = true
    }

    fun isSubscriptionsSupported() = isSubscriptionsSupported

    fun launchPurchaseFlow(
            activity: Activity,
            requestCode: Int,
            productId: String,
            itemType: String,
            developerPayload: String): Observable<BillingPurchaseResult> {

        makeSureItIsNotDisposed()
        makeSureSetupIsDone()
        return purchaseSubject.doOnSubscribe {
            if (itemType == ITEM_TYPE_SUBSCRIPTION && !isSubscriptionsSupported) {
                purchaseSubject.onError(billingException(BILLING_HELPER_SUBSCRIPTIONS_NOT_AVAILABLE, "Subscriptions are not available."))
                purchaseSubject = PublishSubject()
                return@doOnSubscribe
            }

            try {
                log("Constructing buy intent for $productId, item type: $itemType")
                val buyIntentBundle = billingService.getBuyIntent(API_VERSION, context.packageName, productId, itemType, developerPayload)
                val response = buyIntentBundle.getResponseCode()
                if (response != BILLING_RESPONSE_RESULT_OK) {
                    logError("Unable to buy item, Error response: " + getResponseDescription(response))
                    flagEndAsync()
                    result = BillingResult(response, "Unable to buy item")
                    if (listener != null) listener!!.onIabPurchaseFinished(result, null)
                    return
                }

                val pendingIntent = buyIntentBundle.getParcelable(RESPONSE_BUY_INTENT)
                logDebug("Launching buy intent for $productId. Request code: $requestCode")
                this.requestCode = requestCode
                purchaseListener = listener
                purchasingItemType = itemType
                activity.startIntentSenderForResult(pendingIntent.getIntentSender(),
                        requestCode, Intent(),
                        Integer.valueOf(0)!!, Integer.valueOf(0)!!,
                        Integer.valueOf(0)!!)
            } catch (e: IntentSender.SendIntentException) {
                logError("SendIntentException while launching purchase flow for sku " + productId)
                e.printStackTrace()
                flagEndAsync()

                result = BillingResult(IABHELPER_SEND_INTENT_FAILED, "Failed to send intent.")
                if (listener != null) listener!!.onIabPurchaseFinished(result, null)
            } catch (e: RemoteException) {
                logError("RemoteException while launching purchase flow for sku " + productId)
                e.printStackTrace()
                flagEndAsync()

                result = BillingResult(IABHELPER_REMOTE_EXCEPTION, "Remote exception while starting purchase flow")
                if (listener != null) listener!!.onIabPurchaseFinished(result, null)
            }
        }
    }

    private fun Bundle.getResponseCode() = get(RESPONSE_CODE).let {
        if (it == null) {
            log("Bundle with null response code, assuming OK (known issue)")
            BILLING_RESPONSE_RESULT_OK
        } else if (it is Int)
            it.toInt()
        else if (it is Long)
            it.toLong().toInt()
        else {
            log("Unexpected type for bundle response code: ${it.javaClass.name}.")
            throw RuntimeException("Unexpected type for bundle response code: ${it.javaClass.name}")
        }
    }

    private fun createServiceConnection(subscriber: Subscriber<in BillingResult>): ServiceConnection {
        return object : ServiceConnection {
            override fun onServiceDisconnected(name: ComponentName) {
                log("Billing service disconnected.")
            }

            override fun onServiceConnected(name: ComponentName, service: IBinder) {
                if (isDisposed) return

                log("Billing service connected.")
                billingService = IInAppBillingService.Stub.asInterface(service)

                log("Checking for in-app billing 3 support.")
                val packageName = context.packageName
                try {
                    var response = billingService.isBillingSupported(API_VERSION, packageName, ITEM_TYPE_SINGLE)
                    if (response != BILLING_RESPONSE_RESULT_OK) {
                        subscriber.onError(billingException(response, "Error checking for billing v3 support."))
                        isSubscriptionsSupported = false
                        return
                    }

                    log("In-app billing version 3 supported for " + packageName)


                    response = billingService.isBillingSupported(API_VERSION, packageName, ITEM_TYPE_SUBSCRIPTION)
                    if (response == BILLING_RESPONSE_RESULT_OK) {
                        log("Subscriptions AVAILABLE.")
                        isSubscriptionsSupported = true
                    } else {
                        log("Subscriptions NOT AVAILABLE. Response: " + response)
                    }

                    isSetupDone = true
                } catch (e: RemoteException) {
                    subscriber.onError(billingException(
                            BILLING_HELPER_REMOTE_EXCEPTION,
                            "RemoteException while setting up in-app billing."))
                    e.printStackTrace()
                    return
                }

                subscriber.onNext(billingResult(BILLING_RESPONSE_RESULT_OK, "Setup successful."))
                subscriber.onCompleted()
            }
        }
    }

    private fun makeSureItIsNotDisposed() {
        if (isDisposed) throw IllegalStateException("${BillingHelper::class.java.simpleName} was disposed of, so it cannot be used.")
    }

    private fun makeSureSetupIsDone() {
        if (!isSetupDone) throw IllegalStateException("${BillingHelper::class.java.simpleName} has not been set up yet.")
    }

    private fun makeSureSetupIsNotDone() {
        if (isSetupDone) throw IllegalStateException("${BillingHelper::class.java.simpleName} is already set up.")
    }

    private fun log(message: String) {
        if (loggingEnabled) Log.d(BillingHelper::class.java.simpleName, message)
    }
}