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

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.os.RemoteException
import android.util.Log
import com.android.vending.billing.IInAppBillingService
import com.mvcoding.billing.BillingException.Companion.billingException

class BillingHelper(private val context: Context, private val loggingEnabled: Boolean = false) {
    private val BILLING_RESPONSE_RESULT_OK = 0
    private val BILLING_HELPER_REMOTE_EXCEPTION = -1001
    private val BILLING_RESPONSE_RESULT_BILLING_UNAVAILABLE = 3

    private val ITEM_TYPE_INAPP = "inapp"
    private val ITEM_TYPE_SUBS = "subs"

    private var isDisposed = false
    private var isSetupDone = false
    private var isSubscriptionsSupported = false

    private lateinit var serviceConnection: ServiceConnection
    private lateinit var billingService: IInAppBillingService

    fun startSetup() {
        makeSureItIsNotDisposed()
        makeSureSetupIsNotDone()

        Observable.defer {
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

    }

    private fun createServiceConnection(it: Subscriber<in BillingResult>): ServiceConnection {
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
                    var response = billingService.isBillingSupported(3, packageName, ITEM_TYPE_INAPP)
                    if (response != BILLING_RESPONSE_RESULT_OK) {
                        it.onError(billingException(response, "Error checking for billing v3 support."))
                        isSubscriptionsSupported = false
                        return
                    }

                    log("In-app billing version 3 supported for " + packageName)


                    response = billingService.isBillingSupported(3, packageName, ITEM_TYPE_SUBS)
                    if (response == BILLING_RESPONSE_RESULT_OK) {
                        log("Subscriptions AVAILABLE.")
                        isSubscriptionsSupported = true
                    } else {
                        log("Subscriptions NOT AVAILABLE. Response: " + response)
                    }

                    isSetupDone = true
                } catch (e: RemoteException) {
                    it.onError(
                            billingException(BILLING_HELPER_REMOTE_EXCEPTION, "RemoteException while setting up in-app billing."))
                    e.printStackTrace()
                    return
                }

                it.onNext(BillingResult.billingResult(BILLING_RESPONSE_RESULT_OK, "Setup successful."))
            }
        }
    }

    private fun makeSureItIsNotDisposed() {
        if (isDisposed) throw IllegalStateException("${BillingHelper::class.java.simpleName} was disposed of, so it cannot be used.")
    }

    private fun makeSureSetupIsNotDone() {
        if (isSetupDone) throw IllegalStateException("${BillingHelper::class.java.simpleName} is already set up.")
    }

    private fun log(message: String) {
        if (loggingEnabled) Log.d(BillingHelper::class.java.simpleName, message)
    }
}