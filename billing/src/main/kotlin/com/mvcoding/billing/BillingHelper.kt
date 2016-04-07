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
import android.app.Activity.RESULT_OK
import android.app.PendingIntent
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
    private val BILLING_HELPER_BAD_RESPONSE = -1002
    private val BILLING_HELPER_VERIFICATION_FAILED = -1003
    private val BILLING_HELPER_SEND_INTENT_FAILED = -1004
    private val BILLING_HELPER_USER_CANCELLED = -1005
    private val BILLING_HELPER_UNKNOWN_PURCHASE_RESPONSE = -1006
    private val BILLING_HELPER_UNKNOWN_ERROR = -1008
    private val BILLING_HELPER_SUBSCRIPTIONS_NOT_AVAILABLE = -1009

    private val RESPONSE_CODE = "RESPONSE_CODE"
    private val RESPONSE_BUY_INTENT = "BUY_INTENT"
    private val RESPONSE_PURCHASE_DATA = "INAPP_PURCHASE_DATA"
    private val RESPONSE_DATA_SIGNATURE = "INAPP_DATA_SIGNATURE"

    private val ITEM_TYPE_SINGLE = "inapp"
    private val ITEM_TYPE_SUBSCRIPTION = "subs"

    private var isDisposed = false
    private var isSetupDone = false
    private var isSubscriptionsSupported = false
    private var purchaseSubject = PublishSubject<BillingPurchaseResult>()
    private var purchaseRequestCode = 0
    private var purchaseItemType = ITEM_TYPE_SINGLE

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
                    log("Unable to buy item, Error response: ${getResponseDescription(response)}")
                    purchaseSubject.onError(billingException(response, "Unable to buy item."))
                    purchaseSubject = PublishSubject()
                    return@doOnSubscribe
                }

                val pendingIntent: PendingIntent = buyIntentBundle.getParcelable(RESPONSE_BUY_INTENT)
                log("Launching buy intent for $productId. Request code: $requestCode")
                purchaseRequestCode = requestCode
                purchaseItemType = itemType
                activity.startIntentSenderForResult(pendingIntent.intentSender, requestCode, Intent(), 0, 0, 0)
            } catch (e: IntentSender.SendIntentException) {
                log("SendIntentException while launching purchase flow for sku $productId")
                e.printStackTrace()

                purchaseSubject.onError(billingException(BILLING_HELPER_SEND_INTENT_FAILED, "Failed to send intent."))
                purchaseSubject = PublishSubject()
            } catch (e: RemoteException) {
                log("RemoteException while launching purchase flow for sku $productId")
                e.printStackTrace()

                purchaseSubject.onError(billingException(
                        BILLING_HELPER_SEND_INTENT_FAILED,
                        "Remote exception while starting purchase flow."))
                purchaseSubject = PublishSubject()
            }
        }
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        if (requestCode != purchaseRequestCode) return false

        makeSureItIsNotDisposed()
        makeSureSetupIsDone()

        if (data == null) {
            log("Null data in IAB activity result.")
            purchaseSubject.onError(billingException(BILLING_HELPER_BAD_RESPONSE, "Null data in IAB result."))
            purchaseSubject = PublishSubject()
            return true
        }

        val responseCode = data.getResponseCode()
        val purchaseData = data.getStringExtra(RESPONSE_PURCHASE_DATA)
        val dataSignature = data.getStringExtra(RESPONSE_DATA_SIGNATURE)

        if (resultCode == RESULT_OK && responseCode == BILLING_RESPONSE_RESULT_OK) {
            log("Successful result code from purchase activity.")
            log("Purchase data: $purchaseData")
            log("Data signature: $dataSignature")
            log("Extras: ${data.extras}")
            log("Expected item type: $purchaseItemType")

            if (purchaseData == null || dataSignature == null) {
                log("BUG: either purchaseData or dataSignature is null.")
                purchaseSubject.onError(billingException(BILLING_HELPER_UNKNOWN_ERROR, "IAB returned null purchaseData or dataSignature."))
                purchaseSubject = PublishSubject()
                return true
            }

            val billingPurchase: BillingPurchase
            try {
                billingPurchase = BillingPurchase.fromJson(purchaseItemType, dataSignature, purchaseData)
                val productId = billingPurchase.productId

                if (!BillingSecurity.verifyPurchase(base64PublicKey, purchaseData, dataSignature)) {
                    log("Purchase signature verification FAILED for productId $productId")
                    purchaseSubject.onError(billingException(
                            BILLING_HELPER_VERIFICATION_FAILED,
                            "Signature verification failed for productId $productId"))
                    purchaseSubject = PublishSubject()
                    return true
                }
                log("Purchase signature successfully verified.")
            } catch (e: Exception) {
                log("Failed to parse purchase data.")
                e.printStackTrace()
                purchaseSubject.onError(billingException(BILLING_HELPER_BAD_RESPONSE, "Failed to parse purchase data."))
                purchaseSubject = PublishSubject()
                return true
            }

            purchaseSubject.onNext(BillingPurchaseResult(billingResult(BILLING_RESPONSE_RESULT_OK, "Success"), billingPurchase))
            purchaseSubject.onCompleted()
            purchaseSubject = PublishSubject()
        } else if (resultCode == RESULT_OK) {
            log("Result code was OK but in-app billing response was not OK: ${getResponseDescription(responseCode)}")
            purchaseSubject.onError(billingException(responseCode, "Problem purchasing item."))
            purchaseSubject = PublishSubject()
        } else if (resultCode == Activity.RESULT_CANCELED) {
            log("Purchase canceled - Response: ${getResponseDescription(responseCode)}")
            purchaseSubject.onError(billingException(BILLING_HELPER_USER_CANCELLED, "User canceled."))
            purchaseSubject = PublishSubject()
        } else {
            log("Purchase failed. Result code: $resultCode. Response: ${getResponseDescription(responseCode)}")
            purchaseSubject.onError(billingException(BILLING_HELPER_UNKNOWN_PURCHASE_RESPONSE, "Unknown purchase response."))
            purchaseSubject = PublishSubject()
        }
        return true
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

    private fun Intent.getResponseCode() = extras.getResponseCode()

    private fun getResponseDescription(responseCode: Int): String {
        return mapOf(
                0 to "0:OK",
                1 to "1:User Canceled",
                2 to "2:Unknown",
                3 to "3:Billing Unavailable",
                4 to "4:Item unavailable",
                5 to "5:Developer Error",
                6 to "6:Error",
                7 to "7:Item Already Owned",
                8 to "8:Item not owned",
                -1001 to "-1001:Remote exception during initialization",
                -1002 to "-1002:Bad response received",
                -1003 to "-1003:Purchase signature verification failed",
                -1004 to "-1004:Send intent failed",
                -1005 to "-1005:User cancelled",
                -1006 to "-1006:Unknown purchase response",
                -1007 to "-1007:Missing token",
                -1008 to "-1008:Unknown error",
                -1009 to "-1009:Subscriptions not available",
                -1010 to "-1010:Invalid consumption attempt"
        ).getOrElse(responseCode, { "$responseCode:Unknown" })
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