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
import android.app.PendingIntent
import android.content.*
import android.os.Bundle
import android.os.IBinder
import android.os.RemoteException
import android.util.Log
import com.android.vending.billing.IInAppBillingService
import com.mvcoding.billing.BillingException.Companion.billingException
import com.mvcoding.billing.BillingResult.Companion.BILLING_RESPONSE_RESULT_BILLING_UNAVAILABLE
import com.mvcoding.billing.BillingResult.Companion.BILLING_RESPONSE_RESULT_OK
import com.mvcoding.billing.BillingResult.Companion.billingResult
import com.mvcoding.billing.BillingResult.Companion.getResponseDescription
import com.mvcoding.billing.ProductType.SINGLE
import com.mvcoding.billing.ProductType.SUBSCRIPTION
import org.json.JSONException
import rx.Observable
import rx.Subscriber

class BillingHelper(private val context: Context, private val base64PublicKey: String, private val loggingEnabled: Boolean = false) {
    private val API_VERSION = 3
    private val API_VERSION_FOR_SUBSCRIPTION_UPDATE = 5

    private val BILLING_HELPER_REMOTE_EXCEPTION = -1001
    private val BILLING_HELPER_BAD_RESPONSE = -1002
    private val BILLING_HELPER_VERIFICATION_FAILED = -1003
    private val BILLING_HELPER_SEND_INTENT_FAILED = -1004
    private val BILLING_HELPER_USER_CANCELLED = -1005
    private val BILLING_HELPER_UNKNOWN_PURCHASE_RESPONSE = -1006
    private val BILLING_HELPER_MISSING_TOKEN = -1007;
    private val BILLING_HELPER_UNKNOWN_ERROR = -1008
    private val BILLING_HELPER_SUBSCRIPTIONS_NOT_AVAILABLE = -1009
    private val BILLING_HELPER_INVALID_CONSUMPTION = -1010;
    private val BILLING_HELPER_SUBSCRIPTION_UPDATE_NOT_AVAILABLE = -1011

    private val RESPONSE_CODE = "RESPONSE_CODE"
    private val RESPONSE_BUY_INTENT = "BUY_INTENT"
    private val RESPONSE_PURCHASE_DATA = "INAPP_PURCHASE_DATA"
    private val RESPONSE_DATA_SIGNATURE = "INAPP_DATA_SIGNATURE"
    private val RESPONSE_GET_SKU_DETAILS_LIST = "DETAILS_LIST";
    private val RESPONSE_IN_APP_ITEM_LIST = "INAPP_PURCHASE_ITEM_LIST"
    private val RESPONSE_IN_APP_PURCHASE_DATA_LIST = "INAPP_PURCHASE_DATA_LIST"
    private val RESPONSE_IN_APP_SIGNATURE_LIST = "INAPP_DATA_SIGNATURE_LIST"
    private val RESPONSE_IN_APP_CONTINUATION_TOKEN = "INAPP_CONTINUATION_TOKEN"

    private val GET_SKU_DETAILS_ITEM_LIST = "ITEM_ID_LIST";

    private var isDisposed = false
    private var isSetupDone = false
    private var isSubscriptionsSupported = false
    private var isSubscriptionsUpdateSupported = false
    private var purchaseSubscriber: Subscriber<in PurchaseResult>? = null
    private var purchaseRequestCode = 0
    private var purchaseProductType = SINGLE

    private lateinit var serviceConnection: ServiceConnection
    private lateinit var billingService: IInAppBillingService

    fun startSetup(): Observable<BillingResult> {
        makeSureItIsNotDisposed()
        makeSureSetupIsNotDone()

        return Observable.defer {
            Observable.create<BillingResult> {
                connectToBillingService(it)
            }
        }
    }

    private fun connectToBillingService(it: Subscriber<in BillingResult>) {
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
            productId: ProductId,
            productType: ProductType,
            productIdsToReplace: List<ProductId>,
            developerPayload: String): Observable<PurchaseResult> {

        makeSureItIsNotDisposed()
        makeSureSetupIsDone()
        return Observable.create {
            purchaseSubscriber = it

            if (productType == SUBSCRIPTION && !isSubscriptionsSupported) {
                it.onError(billingException(BILLING_HELPER_SUBSCRIPTIONS_NOT_AVAILABLE, "Subscriptions are not available."))
            } else {
                tryToStartBuyActivity(it, activity, requestCode, productId, productType, productIdsToReplace, developerPayload)
            }
        }
    }

    private fun tryToStartBuyActivity(
            subscriber: Subscriber<in PurchaseResult>,
            activity: Activity,
            requestCode: Int,
            productId: ProductId,
            productType: ProductType,
            productIdsToReplace: List<ProductId>,
            developerPayload: String) {
        try {
            startBuyActivity(subscriber, activity, requestCode, productId, productType, productIdsToReplace, developerPayload)
        } catch (e: IntentSender.SendIntentException) {
            log("SendIntentException while launching purchase flow for sku $productId")
            e.printStackTrace()
            subscriber.onError(billingException(BILLING_HELPER_SEND_INTENT_FAILED, "Failed to send intent."))
        } catch (e: RemoteException) {
            log("RemoteException while launching purchase flow for sku $productId")
            e.printStackTrace()
            subscriber.onError(billingException(BILLING_HELPER_SEND_INTENT_FAILED, "Remote exception while starting purchase flow."))
        }
    }

    private fun startBuyActivity(
            subscriber: Subscriber<in PurchaseResult>,
            activity: Activity,
            requestCode: Int,
            productId: ProductId,
            productType: ProductType,
            productIdsToReplace: List<ProductId>,
            developerPayload: String) {
        log("Constructing buy intent for $productId, item type: $productType")
        val buyIntentBundle: Bundle

        if (productIdsToReplace.isEmpty()) {
            buyIntentBundle = billingService.getBuyIntent(
                    API_VERSION,
                    context.packageName,
                    productId.id,
                    productType.value,
                    developerPayload)
        } else {
            if (!isSubscriptionsUpdateSupported) {
                subscriber.onError(
                        billingException(BILLING_HELPER_SUBSCRIPTION_UPDATE_NOT_AVAILABLE, "Subscription updates are not available."))
                return
            }

            buyIntentBundle = billingService.getBuyIntentToReplaceSkus(
                    API_VERSION_FOR_SUBSCRIPTION_UPDATE,
                    context.packageName,
                    productIdsToReplace.map { it.id },
                    productId.id,
                    productType.value,
                    developerPayload)
        }

        val response = buyIntentBundle.getResponseCode()
        if (response != BILLING_RESPONSE_RESULT_OK) {
            log("Unable to buy item, Error response: ${getResponseDescription(response)}")
            subscriber.onError(billingException(response, "Unable to buy item."))
            return
        }

        val pendingIntent: PendingIntent = buyIntentBundle.getParcelable(RESPONSE_BUY_INTENT)
        log("Launching buy intent for $productId. Request code: $requestCode")
        purchaseRequestCode = requestCode
        purchaseProductType = productType
        activity.startIntentSenderForResult(pendingIntent.intentSender, requestCode, Intent(), 0, 0, 0)
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        if (requestCode != purchaseRequestCode) return false

        makeSureItIsNotDisposed()
        makeSureSetupIsDone()

        if (data == null) {
            log("Null data in IAB activity result.")
            purchaseSubscriber?.onError(billingException(BILLING_HELPER_BAD_RESPONSE, "Null data in IAB result."))
            return true
        }

        val responseCode = data.getResponseCode()
        val purchaseData = data.getStringExtra(RESPONSE_PURCHASE_DATA)
        val dataSignature = data.getStringExtra(RESPONSE_DATA_SIGNATURE)

        if (resultCode == Activity.RESULT_OK && responseCode == BILLING_RESPONSE_RESULT_OK) {
            logPurchaseActivityResult(data, dataSignature, purchaseData)

            if (purchaseData == null || dataSignature == null) {
                log("BUG: either purchaseData or dataSignature is null.")
                purchaseSubscriber?.onError(
                        billingException(BILLING_HELPER_UNKNOWN_ERROR, "IAB returned null purchaseData or dataSignature."))
                return true
            }

            val purchase: Purchase
            try {
                purchase = Purchase.fromJson(purchaseProductType, dataSignature, purchaseData)
                val productId = purchase.productId

                if (!Security.verifyPurchase(base64PublicKey, purchaseData, dataSignature)) {
                    log("Purchase signature verification FAILED for productId $productId")
                    purchaseSubscriber?.onError(
                            billingException(BILLING_HELPER_VERIFICATION_FAILED, "Signature verification failed for productId $productId"))
                    return true
                }
                log("Purchase signature successfully verified.")
            } catch (e: Exception) {
                log("Failed to parse purchase data.")
                e.printStackTrace()
                purchaseSubscriber?.onError(billingException(BILLING_HELPER_BAD_RESPONSE, "Failed to parse purchase data."))
                return true
            }

            purchaseSubscriber?.onNext(PurchaseResult(billingResult(BILLING_RESPONSE_RESULT_OK, "Success"), purchase))
            purchaseSubscriber?.onCompleted()
        } else if (resultCode == Activity.RESULT_OK) {
            log("Result code was OK but in-app billing response was not OK: ${getResponseDescription(responseCode)}")
            purchaseSubscriber?.onError(billingException(responseCode, "Problem purchasing item."))
        } else if (resultCode == Activity.RESULT_CANCELED) {
            log("Purchase canceled - Response: ${getResponseDescription(responseCode)}")
            purchaseSubscriber?.onError(billingException(BILLING_HELPER_USER_CANCELLED, "User canceled."))
        } else {
            log("Purchase failed. Result code: $resultCode. Response: ${getResponseDescription(responseCode)}")
            purchaseSubscriber?.onError(billingException(BILLING_HELPER_UNKNOWN_PURCHASE_RESPONSE, "Unknown purchase response."))
        }
        return true
    }

    fun queryInventory(
            queryProductDetails: Boolean,
            additionalProductIdsToQuery: List<ProductId> = emptyList(),
            additionalSubscriptionProductIdsToQuery: List<ProductId> = emptyList()): Inventory {

        makeSureItIsNotDisposed()
        makeSureSetupIsDone()

        try {
            val singlePurchases = queryPurchases(SINGLE)

            val singleProducts: List<Product>
            if (queryProductDetails) {
                singleProducts = queryProducts(SINGLE, singlePurchases.map { it.productId }, additionalProductIdsToQuery)
            } else {
                singleProducts = emptyList()
            }

            val subscriptionPurchases: List<Purchase>
            val subscriptionProducts: List<Product>
            if (isSubscriptionsSupported) {
                subscriptionPurchases = queryPurchases(SUBSCRIPTION)

                if (queryProductDetails) {
                    subscriptionProducts = queryProducts(
                            SUBSCRIPTION,
                            subscriptionPurchases.map { it.productId },
                            additionalSubscriptionProductIdsToQuery)
                } else {
                    subscriptionProducts = emptyList()
                }
            } else {
                subscriptionPurchases = emptyList()
                subscriptionProducts = emptyList()
            }

            return Inventory(singleProducts.plus(subscriptionProducts), singlePurchases.plus(subscriptionPurchases))
        } catch (e: RemoteException) {
            throw billingException(BILLING_HELPER_REMOTE_EXCEPTION, "Remote exception while refreshing inventory.");
        } catch (e: JSONException) {
            throw billingException(BILLING_HELPER_BAD_RESPONSE, "Error parsing JSON response while refreshing inventory.")
        }
    }

    fun consume(purchase: Purchase) {
        makeSureItIsNotDisposed()
        makeSureSetupIsDone()

        if (purchase.productType != SINGLE) {
            throw billingException(BILLING_HELPER_INVALID_CONSUMPTION, "Items of type ${purchase.productType} cannot be consumed.")
        }

        try {
            if (purchase.token.isNullOrEmpty()) {
                log("Cannot consume $purchase. No token.")
                throw billingException(BILLING_HELPER_MISSING_TOKEN, "$purchase is missing token.")
            }

            log("Consuming purchase: $purchase")
            val response = billingService.consumePurchase(API_VERSION, context.packageName, purchase.token)
            if (response == BILLING_RESPONSE_RESULT_OK) {
                log("Successfully consumed $purchase")
            } else {
                log("Error consuming consuming $purchase. ${getResponseDescription(response)}")
                throw billingException(response, "Error consuming consuming $purchase")
            }
        } catch (e: RemoteException) {
            throw billingException(BILLING_HELPER_REMOTE_EXCEPTION, "Remote exception while consuming $purchase")
        }
    }

    private fun queryPurchases(productType: ProductType): List<Purchase> {
        log("Querying owned items, item type: $productType")
        var continueToken: String? = null

        val allPurchases = arrayListOf<Purchase>()
        do {
            log("Calling getPurchases with continuation token: $continueToken");
            val ownedItemsBundle = billingService.getPurchases(API_VERSION, context.packageName, productType.value, continueToken)

            val response = ownedItemsBundle.getResponseCode()
            log("Owned items response: $response")
            if (response != BILLING_RESPONSE_RESULT_OK) {
                log("getPurchases() failed: ${getResponseDescription(response)}")
                throw billingException(response, "Error getting owned items.")
            }

            if (!ownedItemsBundle.containsKey(RESPONSE_IN_APP_ITEM_LIST)
                || !ownedItemsBundle.containsKey(RESPONSE_IN_APP_PURCHASE_DATA_LIST)
                || !ownedItemsBundle.containsKey(RESPONSE_IN_APP_SIGNATURE_LIST)) {
                log("Bundle returned from getPurchases() doesn't contain required fields.")
                throw billingException(BILLING_HELPER_BAD_RESPONSE, "Error getting owned items.")
            }

            val ownedProductIds = ownedItemsBundle.getStringArrayList(RESPONSE_IN_APP_ITEM_LIST)
            val purchases = ownedItemsBundle.getStringArrayList(RESPONSE_IN_APP_PURCHASE_DATA_LIST)
            val signatures = ownedItemsBundle.getStringArrayList(RESPONSE_IN_APP_SIGNATURE_LIST)

            allPurchases.addAll(
                    purchases.mapIndexed { i, purchaseData ->
                        val signature = signatures[i]
                        val productId = ownedProductIds[i];

                        if (Security.verifyPurchase(base64PublicKey, purchaseData, signature)) {
                            log("Product id is owned: $productId")
                            val purchase = Purchase.fromJson(productType, signature, purchaseData)

                            if (purchase.token.isNullOrEmpty()) {
                                log("BUG: empty/null token!")
                                log("Purchase data: $purchaseData")
                            }

                            purchase
                        } else {
                            log("Purchase signature verification **FAILED**. Not adding item.")
                            log("Purchase data: $purchaseData")
                            log("Signature: $signature")
                            throw billingException(BILLING_HELPER_VERIFICATION_FAILED, "Error getting owned items.")
                        }
                    })

            continueToken = ownedItemsBundle.getString(RESPONSE_IN_APP_CONTINUATION_TOKEN)
            log("Continuation token: $continueToken")
        } while (!continueToken.isNullOrEmpty())

        return allPurchases
    }

    private fun queryProducts(
            productType: ProductType,
            ownedProductIds: List<ProductId>,
            additionalProductIdsToQuery: List<ProductId>): List<Product> {

        log("Querying SKU details.")
        val productIdsToQuery = ownedProductIds.toSet().plus(additionalProductIdsToQuery).toList()

        if (productIdsToQuery.isEmpty()) {
            log("queryProducts(): nothing to do because there are no SKUs.")
            return emptyList()
        }

        val productIdsToQueryGroups = productIdsToQuery.groupBy { productIdsToQuery.indexOf(it) / 20 }.values
        return productIdsToQueryGroups.map {
            val queryProductIds = Bundle()
            queryProductIds.putStringArrayList(GET_SKU_DETAILS_ITEM_LIST, arrayListOf(*it.map { it.id }.toTypedArray()))
            val productsBundle = billingService.getSkuDetails(API_VERSION, context.packageName, productType.value, queryProductIds)

            if (!productsBundle.containsKey(RESPONSE_GET_SKU_DETAILS_LIST)) {
                val response = productsBundle.getResponseCode()
                if (response != BILLING_RESPONSE_RESULT_OK) {
                    log("queryProducts() failed: ${getResponseDescription(response)}")
                    throw billingException(response, "Failed to get product details.")
                } else {
                    log("queryProducts() returned a bundle with neither an error nor a detail list.")
                    throw billingException(BILLING_HELPER_BAD_RESPONSE, "Failed to get product details.")
                }
            }

            val responseList = productsBundle.getStringArrayList(RESPONSE_GET_SKU_DETAILS_LIST)
            responseList.map {
                val product = Product.fromJson(productType, it)
                log("Got sku details: $product")
                product
            }
        }.flatten()
    }

    private fun logPurchaseActivityResult(data: Intent, dataSignature: String?, purchaseData: String?) {
        log("Successful result code from purchase activity.")
        log("Purchase data: $purchaseData")
        log("Data signature: $dataSignature")
        log("Extras: ${data.extras}")
        log("Expected item type: $purchaseProductType")
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

    private fun createServiceConnection(subscriber: Subscriber<in BillingResult>): ServiceConnection {
        return object : ServiceConnection {
            override fun onServiceDisconnected(name: ComponentName) {
                log("Billing service disconnected.")
            }

            override fun onServiceConnected(name: ComponentName, service: IBinder) {
                if (isDisposed) return

                log("Billing service connected.")
                billingService = IInAppBillingService.Stub.asInterface(service)

                val packageName = context.packageName
                try {
                    log("Checking for in-app billing 3 support.")
                    var response = billingService.isBillingSupported(API_VERSION, packageName, SINGLE.value)
                    if (response != BILLING_RESPONSE_RESULT_OK) {
                        subscriber.onError(billingException(response, "Error checking for billing v3 support."))
                        isSubscriptionsSupported = false
                        isSubscriptionsUpdateSupported = false
                        return
                    }
                    log("In-app billing version 3 supported for " + packageName)

                    log("Checking for in-app billing 5 support for subscriptions.")
                    response = billingService.isBillingSupported(API_VERSION_FOR_SUBSCRIPTION_UPDATE, packageName, SUBSCRIPTION.value)
                    if (response == BILLING_RESPONSE_RESULT_OK) {
                        log("Subscription re-signup AVAILABLE.")
                        isSubscriptionsUpdateSupported = true
                    } else {
                        log("Subscription re-signup not available.")
                        isSubscriptionsUpdateSupported = false
                    }

                    if (isSubscriptionsUpdateSupported) {
                        isSubscriptionsSupported = true
                    } else {
                        response = billingService.isBillingSupported(API_VERSION, packageName, SUBSCRIPTION.value)
                        if (response == BILLING_RESPONSE_RESULT_OK) {
                            log("Subscriptions AVAILABLE.")
                            isSubscriptionsSupported = true
                        } else {
                            isSubscriptionsSupported = false
                            isSubscriptionsUpdateSupported = false
                            log("Subscriptions NOT AVAILABLE. Response: " + response)
                        }
                    }

                    isSetupDone = true
                } catch (e: RemoteException) {
                    subscriber.onError(
                            billingException(BILLING_HELPER_REMOTE_EXCEPTION, "RemoteException while setting up in-app billing."))
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