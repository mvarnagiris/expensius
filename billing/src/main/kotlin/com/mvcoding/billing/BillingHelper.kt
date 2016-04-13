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
import rx.Observable
import rx.Subscriber

class BillingHelper(private val context: Context, private val base64PublicKey: String, private val loggingEnabled: Boolean = false) {
    private val API_VERSION = 3
    private val API_VERSION_FOR_SUBSCRIBTION_UPDATE = 5

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
    private val RESPONSE_INAPP_ITEM_LIST = "INAPP_PURCHASE_ITEM_LIST"
    private val RESPONSE_INAPP_PURCHASE_DATA_LIST = "INAPP_PURCHASE_DATA_LIST"
    private val RESPONSE_INAPP_SIGNATURE_LIST = "INAPP_DATA_SIGNATURE_LIST"
    private val RESPONSE_INAPP_CONTINUATION_TOKEN = "INAPP_CONTINUATION_TOKEN"

    private val GET_SKU_DETAILS_ITEM_LIST = "ITEM_ID_LIST";
    private val GET_SKU_DETAILS_ITEM_TYPE_LIST = "ITEM_TYPE_LIST";

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
                    API_VERSION_FOR_SUBSCRIBTION_UPDATE,
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

    //    fun queryInventory(
    //            queryProductDetails: Boolean,
    //            additionalProductIdsToQuery: List<ProductId>,
    //            additionalSubscribtionProductIdsToQuery: List<ProductId>): Inventory {
    //
    //        makeSureItIsNotDisposed()
    //        makeSureSetupIsDone()
    //
    //        try {
    ////            Inventory inv = new Inventory();
    //            val response = queryPurchases(inv, ITEM_TYPE_INAPP);
    //            if (r != BILLING_RESPONSE_RESULT_OK) {
    //                throw new IabException(r, "Error refreshing inventory (querying owned items).");
    //            }
    //
    //            if (querySkuDetails) {
    //                r = querySkuDetails(ITEM_TYPE_INAPP, inv, moreItemSkus);
    //                if (r != BILLING_RESPONSE_RESULT_OK) {
    //                    throw new IabException(r, "Error refreshing inventory (querying prices of items).");
    //                }
    //            }
    //
    //            // if subscriptions are supported, then also query for subscriptions
    //            if (mSubscriptionsSupported) {
    //                r = queryPurchases(inv, ITEM_TYPE_SUBS);
    //                if (r != BILLING_RESPONSE_RESULT_OK) {
    //                    throw new IabException(r, "Error refreshing inventory (querying owned subscriptions).");
    //                }
    //
    //                if (querySkuDetails) {
    //                    r = querySkuDetails(ITEM_TYPE_SUBS, inv, moreSubsSkus);
    //                    if (r != BILLING_RESPONSE_RESULT_OK) {
    //                        throw new IabException(r, "Error refreshing inventory (querying prices of subscriptions).");
    //                    }
    //                }
    //            }
    //
    //            return inv;
    //        } catch (e: RemoteException) {
    //            throw billingException(BILLING_HELPER_REMOTE_EXCEPTION, "Remote exception while refreshing inventory.");
    //        } catch (e: JSONException) {
    //            throw billingException(BILLING_HELPER_BAD_RESPONSE, "Error parsing JSON response while refreshing inventory.")
    //        }
    //    }

    fun queryPurchases(productType: ProductType): List<Purchase> {
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

            if (!ownedItemsBundle.containsKey(RESPONSE_INAPP_ITEM_LIST)
                || !ownedItemsBundle.containsKey(RESPONSE_INAPP_PURCHASE_DATA_LIST)
                || !ownedItemsBundle.containsKey(RESPONSE_INAPP_SIGNATURE_LIST)) {
                log("Bundle returned from getPurchases() doesn't contain required fields.")
                throw billingException(BILLING_HELPER_BAD_RESPONSE, "Error getting owned items.")
            }

            val ownedProductIds = ownedItemsBundle.getStringArrayList(RESPONSE_INAPP_ITEM_LIST)
            val purchases = ownedItemsBundle.getStringArrayList(RESPONSE_INAPP_PURCHASE_DATA_LIST)
            val signatures = ownedItemsBundle.getStringArrayList(RESPONSE_INAPP_SIGNATURE_LIST)

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

            continueToken = ownedItemsBundle.getString(RESPONSE_INAPP_CONTINUATION_TOKEN)
            log("Continuation token: $continueToken")
        } while (!continueToken.isNullOrEmpty())

        return allPurchases
    }

    fun queryProducts(
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
                    response = billingService.isBillingSupported(API_VERSION_FOR_SUBSCRIBTION_UPDATE, packageName, SUBSCRIPTION.value)
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


    //    // Keys for the responses from InAppBillingService
    //    public static final String RESPONSE_CODE = "RESPONSE_CODE";
    //    public static final String RESPONSE_GET_SKU_DETAILS_LIST = "DETAILS_LIST";
    //    public static final String RESPONSE_BUY_INTENT = "BUY_INTENT";
    //
    //
    //    // some fields on the getSkuDetails response bundle
    //
    //
    //
    //    /**
    //     * Consumes a given in-app product. Consuming can only be done on an item
    //     * that's owned, and as a result of consumption, the user will no longer own it.
    //     * This method may block or take long to return. Do not call from the UI thread.
    //     * For that, see {@link #consumeAsync}.
    //     *
    //     * @param itemInfo The PurchaseInfo that represents the item to consume.
    //     * @throws IabException if there is a problem during consumption.
    //     */
    //    void consume(Purchase itemInfo) throws IabException {
    //        checkNotDisposed();
    //        checkSetupDone("consume");
    //
    //        if (!itemInfo.mItemType.equals(ITEM_TYPE_INAPP)) {
    //            throw new IabException(IABHELPER_INVALID_CONSUMPTION,
    //            "Items of type '" + itemInfo.mItemType + "' can't be consumed.");
    //        }
    //
    //        try {
    //            String token = itemInfo.getToken();
    //            String sku = itemInfo.getSku();
    //            if (token == null || token.equals("")) {
    //                logError("Can't consume "+ sku + ". No token.");
    //                throw new IabException(IABHELPER_MISSING_TOKEN, "PurchaseInfo is missing token for sku: "
    //                + sku + " " + itemInfo);
    //            }
    //
    //            logDebug("Consuming sku: " + sku + ", token: " + token);
    //            int response = mService.consumePurchase(3, mContext.getPackageName(), token);
    //            if (response == BILLING_RESPONSE_RESULT_OK) {
    //                logDebug("Successfully consumed sku: " + sku);
    //            }
    //            else {
    //                logDebug("Error consuming consuming sku " + sku + ". " + getResponseDesc(response));
    //                throw new IabException(response, "Error consuming sku " + sku);
    //            }
    //        }
    //        catch (RemoteException e) {
    //            throw new IabException(IABHELPER_REMOTE_EXCEPTION, "Remote exception while consuming. PurchaseInfo: " + itemInfo, e);
    //        }
    //    }
    //
    //    /**
    //     * Callback that notifies when a consumption operation finishes.
    //     */
    //    public interface OnConsumeFinishedListener {
    //        /**
    //         * Called to notify that a consumption has finished.
    //         *
    //         * @param purchase The purchase that was (or was to be) consumed.
    //         * @param result The result of the consumption operation.
    //         */
    //        void onConsumeFinished(Purchase purchase, IabResult result);
    //    }
    //
    //    /**
    //     * Callback that notifies when a multi-item consumption operation finishes.
    //     */
    //    public interface OnConsumeMultiFinishedListener {
    //        /**
    //         * Called to notify that a consumption of multiple items has finished.
    //         *
    //         * @param purchases The purchases that were (or were to be) consumed.
    //         * @param results The results of each consumption operation, corresponding to each
    //         *     sku.
    //         */
    //        void onConsumeMultiFinished(List<Purchase> purchases, List<IabResult> results);
    //    }
    //
    //    /**
    //     * Asynchronous wrapper to item consumption. Works like {@link #consume}, but
    //     * performs the consumption in the background and notifies completion through
    //     * the provided listener. This method is safe to call from a UI thread.
    //     *
    //     * @param purchase The purchase to be consumed.
    //     * @param listener The listener to notify when the consumption operation finishes.
    //     */
    //    public void consumeAsync(Purchase purchase, OnConsumeFinishedListener listener)
    //    throws IabAsyncInProgressException {
    //        checkNotDisposed();
    //        checkSetupDone("consume");
    //        java.util.List<Purchase> purchases = new ArrayList<Purchase>();
    //        purchases.add(purchase);
    //        consumeAsyncInternal(purchases, listener, null);
    //    }
    //
    //    /**
    //     * Same as {@link #consumeAsync}, but for multiple items at once.
    //     * @param purchases The list of PurchaseInfo objects representing the purchases to consume.
    //     * @param listener The listener to notify when the consumption operation finishes.
    //     */
    //    public void consumeAsync(List<Purchase> purchases, OnConsumeMultiFinishedListener listener)
    //    throws IabAsyncInProgressException {
    //        checkNotDisposed();
    //        checkSetupDone("consume");
    //        consumeAsyncInternal(purchases, null, listener);
    //    }
    //
    //
    //
    //    int querySkuDetails(String itemType, Inventory inv, List<String> moreSkus)
    //    throws RemoteException, JSONException {
    //        logDebug("Querying SKU details.");
    //        ArrayList<String> skuList = new ArrayList<String>();
    //        skuList.addAll(inv.getAllOwnedSkus(itemType));
    //        if (moreSkus != null) {
    //            for (String sku : moreSkus) {
    //                if (!skuList.contains(sku)) {
    //                    skuList.add(sku);
    //                }
    //            }
    //        }
    //
    //        if (skuList.size() == 0) {
    //            logDebug("queryPrices: nothing to do because there are no SKUs.");
    //            return BILLING_RESPONSE_RESULT_OK;
    //        }
    //
    //        // Split the sku list in blocks of no more than 20 elements.
    //        ArrayList<ArrayList<String>> packs = new ArrayList<ArrayList<String>>();
    //        ArrayList<String> tempList;
    //        int n = skuList.size() / 20;
    //        int mod = skuList.size() % 20;
    //        for (int i = 0; i < n; i++) {
    //            tempList = new ArrayList<String>();
    //            for (String s : skuList.subList(i * 20, i * 20 + 20)) {
    //            tempList.add(s);
    //        }
    //            packs.add(tempList);
    //        }
    //        if (mod != 0) {
    //            tempList = new ArrayList<String>();
    //            for (String s : skuList.subList(n * 20, n * 20 + mod)) {
    //                tempList.add(s);
    //            }
    //            packs.add(tempList);
    //        }
    //
    //        for (ArrayList<String> skuPartList : packs) {
    //            Bundle querySkus = new Bundle();
    //            querySkus.putStringArrayList(GET_SKU_DETAILS_ITEM_LIST, skuPartList);
    //            Bundle skuDetails = mService.getSkuDetails(3, mContext.getPackageName(),
    //            itemType, querySkus);
    //
    //            if (!skuDetails.containsKey(RESPONSE_GET_SKU_DETAILS_LIST)) {
    //                int response = getResponseCodeFromBundle(skuDetails);
    //                if (response != BILLING_RESPONSE_RESULT_OK) {
    //                    logDebug("getSkuDetails() failed: " + getResponseDesc(response));
    //                    return response;
    //                } else {
    //                    logError("getSkuDetails() returned a bundle with neither an error nor a detail list.");
    //                    return IABHELPER_BAD_RESPONSE;
    //                }
    //            }
    //
    //            ArrayList<String> responseList = skuDetails.getStringArrayList(
    //                    RESPONSE_GET_SKU_DETAILS_LIST);
    //
    //            for (String thisResponse : responseList) {
    //            SkuDetails d = new SkuDetails(itemType, thisResponse);
    //            logDebug("Got sku details: " + d);
    //            inv.addSkuDetails(d);
    //        }
    //        }
    //
    //        return BILLING_RESPONSE_RESULT_OK;
    //    }
    //
    //    void consumeAsyncInternal(final List<Purchase> purchases,
    //    final OnConsumeFinishedListener singleListener,
    //    final OnConsumeMultiFinishedListener multiListener)
    //    throws IabAsyncInProgressException {
    //        final Handler handler = new Handler();
    //        flagStartAsync("consume");
    //        (new Thread(new Runnable() {
    //            public void run() {
    //                final List<IabResult> results = new ArrayList<IabResult>();
    //                for (Purchase purchase : purchases) {
    //                try {
    //                    consume(purchase);
    //                    results.add(new IabResult(BILLING_RESPONSE_RESULT_OK, "Successful consume of sku " + purchase.getSku()));
    //                }
    //                catch (IabException ex) {
    //                    results.add(ex.getResult());
    //                }
    //            }
    //
    //                flagEndAsync();
    //                if (!mDisposed && singleListener != null) {
    //                    handler.post(new Runnable() {
    //                        public void run() {
    //                            singleListener.onConsumeFinished(purchases.get(0), results.get(0));
    //                        }
    //                    });
    //                }
    //                if (!mDisposed && multiListener != null) {
    //                    handler.post(new Runnable() {
    //                        public void run() {
    //                            multiListener.onConsumeMultiFinished(purchases, results);
    //                        }
    //                    });
    //                }
    //            }
    //        })).start();
    //    }
    //
    //    void logDebug(String msg) {
    //        if (mDebugLog) Log.d(mDebugTag, msg);
    //    }
    //
    //    void logError(String msg) {
    //        Log.e(mDebugTag, "In-app billing error: " + msg);
    //    }
    //
    //    void logWarn(String msg) {
    //        Log.w(mDebugTag, "In-app billing warning: " + msg);
    //    }
}