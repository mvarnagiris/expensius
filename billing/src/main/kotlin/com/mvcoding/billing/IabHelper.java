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

package com.mvcoding.billing;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;

import com.android.vending.billing.IInAppBillingService;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;


/**
 * After instantiating, you must perform setup in order to start using the object. To perform setup, call the {@link #startSetup} method and
 * provide a listener; that listener will be notified when setup is complete, after which (and not before) you may call other methods.
 * <p>
 * When you are done with this object, don't forget to call {@link #dispose} to ensure proper cleanup. This object holds a binding to the
 * in-app billing service, which will leak unless you dispose of it correctly.
 */
public class IabHelper {
    // Billing response codes
    public static final int BILLING_RESPONSE_RESULT_OK = 0;
    public static final int BILLING_RESPONSE_RESULT_USER_CANCELED = 1;
    public static final int BILLING_RESPONSE_RESULT_BILLING_UNAVAILABLE = 3;
    public static final int BILLING_RESPONSE_RESULT_ITEM_UNAVAILABLE = 4;
    public static final int BILLING_RESPONSE_RESULT_DEVELOPER_ERROR = 5;
    public static final int BILLING_RESPONSE_RESULT_ERROR = 6;
    public static final int BILLING_RESPONSE_RESULT_ITEM_ALREADY_OWNED = 7;
    public static final int BILLING_RESPONSE_RESULT_ITEM_NOT_OWNED = 8;

    // IAB Helper error codes
    public static final int IABHELPER_ERROR_BASE = -1000;
    public static final int IABHELPER_REMOTE_EXCEPTION = -1001;
    public static final int IABHELPER_BAD_RESPONSE = -1002;
    public static final int IABHELPER_VERIFICATION_FAILED = -1003;
    public static final int IABHELPER_SEND_INTENT_FAILED = -1004;
    public static final int IABHELPER_USER_CANCELLED = -1005;
    public static final int IABHELPER_UNKNOWN_PURCHASE_RESPONSE = -1006;
    public static final int IABHELPER_MISSING_TOKEN = -1007;
    public static final int IABHELPER_UNKNOWN_ERROR = -1008;
    public static final int IABHELPER_SUBSCRIPTIONS_NOT_AVAILABLE = -1009;
    public static final int IABHELPER_INVALID_CONSUMPTION = -1010;

    // Keys for the responses from InAppBillingService
    public static final String RESPONSE_CODE = "RESPONSE_CODE";
    public static final String RESPONSE_GET_SKU_DETAILS_LIST = "DETAILS_LIST";
    public static final String RESPONSE_BUY_INTENT = "BUY_INTENT";
    public static final String RESPONSE_INAPP_PURCHASE_DATA = "INAPP_PURCHASE_DATA";
    public static final String RESPONSE_INAPP_SIGNATURE = "INAPP_DATA_SIGNATURE";
    public static final String RESPONSE_INAPP_ITEM_LIST = "INAPP_PURCHASE_ITEM_LIST";
    public static final String RESPONSE_INAPP_PURCHASE_DATA_LIST = "INAPP_PURCHASE_DATA_LIST";
    public static final String RESPONSE_INAPP_SIGNATURE_LIST = "INAPP_DATA_SIGNATURE_LIST";
    public static final String INAPP_CONTINUATION_TOKEN = "INAPP_CONTINUATION_TOKEN";

    // Item types
    public static final String ITEM_TYPE_INAPP = "inapp";
    public static final String ITEM_TYPE_SUBS = "subs";

    // some fields on the getSkuDetails response bundle
    public static final String GET_SKU_DETAILS_ITEM_LIST = "ITEM_ID_LIST";
    public static final String GET_SKU_DETAILS_ITEM_TYPE_LIST = "ITEM_TYPE_LIST";
    // Context we were passed during initialization
    private final Context context;
    // Public key for verifying signature, in base64 encoding
    private final String signatureBase64;
    // Is setup done?
    boolean isSetupDone = false;
    // Has this object been disposed of? (If so, we should ignore callbacks, etc)
    boolean isDisposed = false;
    // Are subscriptions supported?
    boolean isSubscriptionsSupported = false;
    // Connection to the service
    IInAppBillingService service;
    ServiceConnection serviceConnection;
    // The request code used to launch purchase flow
    int requestCode;
    // The item type of the current purchase flow
    String purchasingItemType;
    // The listener registered on launchPurchaseFlow, which we have to call back when
    // the purchase finishes
    OnBillingPurchaseFinishedListener purchaseListener;

    /**
     * @param context         Your application or Activity context. Needed to bind to the in-app billing service.
     * @param base64PublicKey Your application's public key, encoded in base64. This is used for verification of purchase signatures. You
     *                        can find your app's base64-encoded public key in your application's page on Google Play Developer Console.
     *                        Note that this is NOT your "developer public key".
     */
    public IabHelper(Context context, String base64PublicKey) {
        this.context = context.getApplicationContext();
        this.signatureBase64 = base64PublicKey;
    }

    public static String getResponseDescription(int responseCode) {
        String[] iab_msgs = ("0:OK/1:User Canceled/2:Unknown/" +
                "3:Billing Unavailable/4:Item unavailable/" +
                "5:Developer Error/6:Error/7:Item Already Owned/" +
                "8:Item not owned").split("/");
        String[] iabhelper_msgs = ("0:OK/-1001:Remote exception during initialization/" +
                "-1002:Bad response received/" +
                "-1003:Purchase signature verification failed/" +
                "-1004:Send intent failed/" +
                "-1005:User cancelled/" +
                "-1006:Unknown purchase response/" +
                "-1007:Missing token/" +
                "-1008:Unknown error/" +
                "-1009:Subscriptions not available/" +
                "-1010:Invalid consumption attempt").split("/");

        if (responseCode <= IABHELPER_ERROR_BASE) {
            int index = IABHELPER_ERROR_BASE - responseCode;
            if (index >= 0 && index < iabhelper_msgs.length) return iabhelper_msgs[index];
            else return String.valueOf(responseCode) + ":Unknown IAB Helper Error";
        } else if (responseCode < 0 || responseCode >= iab_msgs.length)
            return String.valueOf(responseCode) + ":Unknown";
        else
            return iab_msgs[responseCode];
    }

    public void startSetup(final OnIabSetupFinishedListener listener) {
        // If already set up, can't do it again.
        checkNotDisposed();
        if (isSetupDone) throw new IllegalStateException("IAB helper is already set up.");

        // Connection to IAB service
        logDebug("Starting in-app billing setup.");
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceDisconnected(ComponentName name) {
                logDebug("Billing service disconnected.");
                service = null;
            }

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                if (isDisposed) return;
                logDebug("Billing service connected.");
                IabHelper.this.service = IInAppBillingService.Stub.asInterface(service);
                String packageName = context.getPackageName();
                try {
                    logDebug("Checking for in-app billing 3 support.");

                    // check for in-app billing v3 support
                    int response = IabHelper.this.service.isBillingSupported(3, packageName, ITEM_TYPE_INAPP);
                    if (response != BILLING_RESPONSE_RESULT_OK) {
                        if (listener != null) listener.onIabSetupFinished(new BillingResult(response,
                                "Error checking for billing v3 support."));

                        // if in-app purchases aren't supported, neither are subscriptions.
                        isSubscriptionsSupported = false;
                        return;
                    }
                    logDebug("In-app billing version 3 supported for " + packageName);

                    // check for v3 subscriptions support
                    response = IabHelper.this.service.isBillingSupported(3, packageName, ITEM_TYPE_SUBS);
                    if (response == BILLING_RESPONSE_RESULT_OK) {
                        logDebug("Subscriptions AVAILABLE.");
                        isSubscriptionsSupported = true;
                    } else {
                        logDebug("Subscriptions NOT AVAILABLE. Response: " + response);
                    }

                    isSetupDone = true;
                } catch (RemoteException e) {
                    if (listener != null) {
                        listener.onIabSetupFinished(new BillingResult(IABHELPER_REMOTE_EXCEPTION,
                                "RemoteException while setting up in-app billing."));
                    }
                    e.printStackTrace();
                    return;
                }

                if (listener != null) {
                    listener.onIabSetupFinished(new BillingResult(BILLING_RESPONSE_RESULT_OK, "Setup successful."));
                }
            }
        };

        Intent serviceIntent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
        serviceIntent.setPackage("com.android.vending");
        if (!context.getPackageManager().queryIntentServices(serviceIntent, 0).isEmpty()) {
            // service available to handle that Intent
            context.bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
        } else {
            // no service available to handle that Intent
            if (listener != null) {
                listener.onIabSetupFinished(
                        new BillingResult(BILLING_RESPONSE_RESULT_BILLING_UNAVAILABLE,
                                "Billing service unavailable on device."));
            }
        }
    }

    public void dispose() {
        isSetupDone = false;
        if (serviceConnection != null) {
            if (context != null) context.unbindService(serviceConnection);
        }
        isDisposed = true;
        serviceConnection = null;
        service = null;
        purchaseListener = null;
    }

    private void checkNotDisposed() {
        if (isDisposed) throw new IllegalStateException("IabHelper was disposed of, so it cannot be used.");
    }

    public boolean subscriptionsSupported() {
        checkNotDisposed();
        return isSubscriptionsSupported;
    }

    public void launchPurchaseFlow(Activity act, String sku, int requestCode, OnBillingPurchaseFinishedListener listener) {
        launchPurchaseFlow(act, sku, requestCode, listener, "");
    }

    public void launchPurchaseFlow(Activity act, String sku, int requestCode, OnBillingPurchaseFinishedListener listener, String extraData) {
        launchPurchaseFlow(act, sku, ITEM_TYPE_INAPP, requestCode, listener, extraData);
    }

    public void launchSubscriptionPurchaseFlow(Activity act, String sku, int requestCode, OnBillingPurchaseFinishedListener listener) {
        launchSubscriptionPurchaseFlow(act, sku, requestCode, listener, "");
    }

    public void launchSubscriptionPurchaseFlow(Activity act, String sku, int requestCode, OnBillingPurchaseFinishedListener listener, String extraData) {
        launchPurchaseFlow(act, sku, ITEM_TYPE_SUBS, requestCode, listener, extraData);
    }

    /**
     * Initiate the UI flow for an in-app purchase. Call this method to initiate an in-app purchase,
     * which will involve bringing up the Google Play screen. The calling activity will be paused while
     * the user interacts with Google Play, and the result will be delivered via the activity's
     * {@link Activity#onActivityResult} method, at which point you must call
     * this object's {@link #handleActivityResult} method to continue the purchase flow. This method
     * MUST be called from the UI thread of the Activity.
     *
     * @param act         The calling activity.
     * @param sku         The sku of the item to purchase.
     * @param itemType    indicates if it's a product or a subscription (ITEM_TYPE_INAPP or ITEM_TYPE_SUBS)
     * @param requestCode A request code (to differentiate from other responses --
     *                    as in {@link Activity#startActivityForResult}).
     * @param listener    The listener to notify when the purchase process finishes
     * @param extraData   Extra data (developer payload), which will be returned with the purchase data
     *                    when the purchase completes. This extra data will be permanently bound to that purchase
     *                    and will always be returned when the purchase is queried.
     */
    public void launchPurchaseFlow(Activity act, String sku, String itemType, int requestCode,
                                   OnBillingPurchaseFinishedListener listener, String extraData) {
        checkNotDisposed();
        checkSetupDone("launchPurchaseFlow");
        flagStartAsync("launchPurchaseFlow");
        BillingResult result;

        if (itemType.equals(ITEM_TYPE_SUBS) && !isSubscriptionsSupported) {
            BillingResult r = new BillingResult(IABHELPER_SUBSCRIPTIONS_NOT_AVAILABLE,
                    "Subscriptions are not available.");
            flagEndAsync();
            if (listener != null) listener.onIabPurchaseFinished(r, null);
            return;
        }

        try {
            logDebug("Constructing buy intent for " + sku + ", item type: " + itemType);
            Bundle buyIntentBundle = service.getBuyIntent(3, context.getPackageName(), sku, itemType, extraData);
            int response = getResponseCodeFromBundle(buyIntentBundle);
            if (response != BILLING_RESPONSE_RESULT_OK) {
                logError("Unable to buy item, Error response: " + getResponseDescription(response));
                flagEndAsync();
                result = new BillingResult(response, "Unable to buy item");
                if (listener != null) listener.onIabPurchaseFinished(result, null);
                return;
            }

            PendingIntent pendingIntent = buyIntentBundle.getParcelable(RESPONSE_BUY_INTENT);
            logDebug("Launching buy intent for " + sku + ". Request code: " + requestCode);
            this.requestCode = requestCode;
            purchaseListener = listener;
            purchasingItemType = itemType;
            act.startIntentSenderForResult(pendingIntent.getIntentSender(),
                    requestCode, new Intent(),
                    Integer.valueOf(0), Integer.valueOf(0),
                    Integer.valueOf(0));
        } catch (SendIntentException e) {
            logError("SendIntentException while launching purchase flow for sku " + sku);
            e.printStackTrace();
            flagEndAsync();

            result = new BillingResult(IABHELPER_SEND_INTENT_FAILED, "Failed to send intent.");
            if (listener != null) listener.onIabPurchaseFinished(result, null);
        } catch (RemoteException e) {
            logError("RemoteException while launching purchase flow for sku " + sku);
            e.printStackTrace();
            flagEndAsync();

            result = new BillingResult(IABHELPER_REMOTE_EXCEPTION, "Remote exception while starting purchase flow");
            if (listener != null) listener.onIabPurchaseFinished(result, null);
        }
    }

    /**
     * Handles an activity result that's part of the purchase flow in in-app billing. If you
     * are calling {@link #launchPurchaseFlow}, then you must call this method from your
     * Activity's {@link Activity@onActivityResult} method. This method
     * MUST be called from the UI thread of the Activity.
     *
     * @param requestCode The requestCode as you received it.
     * @param resultCode  The resultCode as you received it.
     * @param data        The data (Intent) as you received it.
     * @return Returns true if the result was related to a purchase flow and was handled;
     * false if the result was not related to a purchase, in which case you should
     * handle it normally.
     */
    public boolean handleActivityResult(int requestCode, int resultCode, Intent data) {
        BillingResult result;
        if (requestCode != this.requestCode) return false;

        checkNotDisposed();
        checkSetupDone("handleActivityResult");

        // end of async purchase operation that started on launchPurchaseFlow
        flagEndAsync();

        if (data == null) {
            logError("Null data in IAB activity result.");
            result = new BillingResult(IABHELPER_BAD_RESPONSE, "Null data in IAB result");
            if (purchaseListener != null) purchaseListener.onIabPurchaseFinished(result, null);
            return true;
        }

        int responseCode = getResponseCodeFromIntent(data);
        String purchaseData = data.getStringExtra(RESPONSE_INAPP_PURCHASE_DATA);
        String dataSignature = data.getStringExtra(RESPONSE_INAPP_SIGNATURE);

        if (resultCode == Activity.RESULT_OK && responseCode == BILLING_RESPONSE_RESULT_OK) {
            logDebug("Successful resultcode from purchase activity.");
            logDebug("Purchase data: " + purchaseData);
            logDebug("Data signature: " + dataSignature);
            logDebug("Extras: " + data.getExtras());
            logDebug("Expected item type: " + purchasingItemType);

            if (purchaseData == null || dataSignature == null) {
                logError("BUG: either purchaseData or dataSignature is null.");
                logDebug("Extras: " + data.getExtras().toString());
                result = new BillingResult(IABHELPER_UNKNOWN_ERROR, "IAB returned null purchaseData or dataSignature");
                if (purchaseListener != null) purchaseListener.onIabPurchaseFinished(result, null);
                return true;
            }

            BillingPurchase billingPurchase = null;
            try {
                billingPurchase = BillingPurchase.fromJson(purchasingItemType, dataSignature, purchaseData);
                String sku = billingPurchase.getSku();

                // Verify signature
                if (!BillingSecurity.INSTANCE.verifyPurchase(signatureBase64, purchaseData, dataSignature)) {
                    logError("Purchase signature verification FAILED for sku " + sku);
                    result = new BillingResult(IABHELPER_VERIFICATION_FAILED, "Signature verification failed for sku " + sku);
                    if (purchaseListener != null) purchaseListener.onIabPurchaseFinished(result, billingPurchase);
                    return true;
                }
                logDebug("Purchase signature successfully verified.");
            } catch (Exception e) {
                logError("Failed to parse purchase data.");
                e.printStackTrace();
                result = new BillingResult(IABHELPER_BAD_RESPONSE, "Failed to parse purchase data.");
                if (purchaseListener != null) purchaseListener.onIabPurchaseFinished(result, null);
                return true;
            }

            if (purchaseListener != null) {
                purchaseListener.onIabPurchaseFinished(new BillingResult(BILLING_RESPONSE_RESULT_OK, "Success"), billingPurchase);
            }
        } else if (resultCode == Activity.RESULT_OK) {
            // result code was OK, but in-app billing response was not OK.
            logDebug("Result code was OK but in-app billing response was not OK: " + getResponseDescription(responseCode));
            if (purchaseListener != null) {
                result = new BillingResult(responseCode, "Problem purchashing item.");
                purchaseListener.onIabPurchaseFinished(result, null);
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            logDebug("Purchase canceled - Response: " + getResponseDescription(responseCode));
            result = new BillingResult(IABHELPER_USER_CANCELLED, "User canceled.");
            if (purchaseListener != null) purchaseListener.onIabPurchaseFinished(result, null);
        } else {
            logError("Purchase failed. Result code: " + Integer.toString(resultCode)
                    + ". Response: " + getResponseDescription(responseCode));
            result = new BillingResult(IABHELPER_UNKNOWN_PURCHASE_RESPONSE, "Unknown purchase response.");
            if (purchaseListener != null) purchaseListener.onIabPurchaseFinished(result, null);
        }
        return true;
    }

    public BillingInventory queryInventory(boolean querySkuDetails, List<String> moreSkus) throws BillingException {
        return queryInventory(querySkuDetails, moreSkus, null);
    }

    /**
     * Queries the inventory. This will query all owned items from the server, as well as
     * information on additional skus, if specified. This method may block or take long to execute.
     * Do not call from a UI thread. For that, use the non-blocking version {@link #queryInventoryAsync(boolean, List, QueryInventoryFinishedListener)}.
     *
     * @param querySkuDetails if true, SKU details (price, description, etc) will be queried as well
     *                        as purchase information.
     * @param moreItemSkus    additional PRODUCT skus to query information on, regardless of ownership.
     *                        Ignored if null or if querySkuDetails is false.
     * @param moreSubsSkus    additional SUBSCRIPTIONS skus to query information on, regardless of ownership.
     *                        Ignored if null or if querySkuDetails is false.
     * @throws BillingException if a problem occurs while refreshing the inventory.
     */
    public BillingInventory queryInventory(boolean querySkuDetails, List<String> moreItemSkus,
                                           List<String> moreSubsSkus) throws BillingException {
        checkNotDisposed();
        checkSetupDone("queryInventory");
        try {
            BillingInventory inv = new BillingInventory();
            int r = queryPurchases(inv, ITEM_TYPE_INAPP);
            if (r != BILLING_RESPONSE_RESULT_OK) {
                throw new BillingException(r, "Error refreshing inventory (querying owned items).");
            }

            if (querySkuDetails) {
                r = querySkuDetails(ITEM_TYPE_INAPP, inv, moreItemSkus);
                if (r != BILLING_RESPONSE_RESULT_OK) {
                    throw new BillingException(r, "Error refreshing inventory (querying prices of items).");
                }
            }

            // if subscriptions are supported, then also query for subscriptions
            if (isSubscriptionsSupported) {
                r = queryPurchases(inv, ITEM_TYPE_SUBS);
                if (r != BILLING_RESPONSE_RESULT_OK) {
                    throw new BillingException(r, "Error refreshing inventory (querying owned subscriptions).");
                }

                if (querySkuDetails) {
                    r = querySkuDetails(ITEM_TYPE_SUBS, inv, moreItemSkus);
                    if (r != BILLING_RESPONSE_RESULT_OK) {
                        throw new BillingException(r, "Error refreshing inventory (querying prices of subscriptions).");
                    }
                }
            }

            return inv;
        } catch (RemoteException e) {
            throw new BillingException(IABHELPER_REMOTE_EXCEPTION, "Remote exception while refreshing inventory.", e);
        } catch (JSONException e) {
            throw new BillingException(IABHELPER_BAD_RESPONSE, "Error parsing JSON response while refreshing inventory.", e);
        }
    }

    /**
     * Consumes a given in-app product. Consuming can only be done on an item
     * that's owned, and as a result of consumption, the user will no longer own it.
     * This method may block or take long to return. Do not call from the UI thread.
     * For that, see {@link #consumeAsync}.
     *
     * @param itemInfo The PurchaseInfo that represents the item to consume.
     * @throws BillingException if there is a problem during consumption.
     */
    void consume(BillingPurchase itemInfo) throws BillingException {
        checkNotDisposed();
        checkSetupDone("consume");

        if (!itemInfo.getItemType().equals(ITEM_TYPE_INAPP)) {
            throw new BillingException(IABHELPER_INVALID_CONSUMPTION,
                    "Items of type '" + itemInfo.getItemType() + "' can't be consumed.");
        }

        try {
            String token = itemInfo.getToken();
            String sku = itemInfo.getSku();
            if (token == null || token.equals("")) {
                logError("Can't consume " + sku + ". No token.");
                throw new BillingException(IABHELPER_MISSING_TOKEN, "PurchaseInfo is missing token for sku: "
                        + sku + " " + itemInfo);
            }

            logDebug("Consuming sku: " + sku + ", token: " + token);
            int response = service.consumePurchase(3, context.getPackageName(), token);
            if (response == BILLING_RESPONSE_RESULT_OK) {
                logDebug("Successfully consumed sku: " + sku);
            } else {
                logDebug("Error consuming consuming sku " + sku + ". " + getResponseDescription(response));
                throw new BillingException(response, "Error consuming sku " + sku);
            }
        } catch (RemoteException e) {
            throw new BillingException(IABHELPER_REMOTE_EXCEPTION, "Remote exception while consuming. PurchaseInfo: " + itemInfo, e);
        }
    }

    // Checks that setup was done; if not, throws an exception.
    void checkSetupDone(String operation) {
        if (!isSetupDone) {
            logError("Illegal state for operation (" + operation + "): IAB helper is not set up.");
            throw new IllegalStateException("IAB helper is not set up. Can't perform operation: " + operation);
        }
    }

    // Workaround to bug where sometimes response codes come as Long instead of Integer
    int getResponseCodeFromBundle(Bundle b) {
        Object o = b.get(RESPONSE_CODE);
        if (o == null) {
            logDebug("Bundle with null response code, assuming OK (known issue)");
            return BILLING_RESPONSE_RESULT_OK;
        } else if (o instanceof Integer) return ((Integer) o).intValue();
        else if (o instanceof Long) return (int) ((Long) o).longValue();
        else {
            logError("Unexpected type for bundle response code.");
            logError(o.getClass().getName());
            throw new RuntimeException("Unexpected type for bundle response code: " + o.getClass().getName());
        }
    }

    // Workaround to bug where sometimes response codes come as Long instead of Integer
    int getResponseCodeFromIntent(Intent i) {
        Object o = i.getExtras().get(RESPONSE_CODE);
        if (o == null) {
            logError("Intent with no response code, assuming OK (known issue)");
            return BILLING_RESPONSE_RESULT_OK;
        } else if (o instanceof Integer) return ((Integer) o).intValue();
        else if (o instanceof Long) return (int) ((Long) o).longValue();
        else {
            logError("Unexpected type for intent response code.");
            logError(o.getClass().getName());
            throw new RuntimeException("Unexpected type for intent response code: " + o.getClass().getName());
        }
    }

    int queryPurchases(BillingInventory inv, String itemType) throws JSONException, RemoteException {
        // Query purchases
        logDebug("Querying owned items, item type: " + itemType);
        logDebug("Package name: " + context.getPackageName());
        boolean verificationFailed = false;
        String continueToken = null;

        do {
            logDebug("Calling getPurchases with continuation token: " + continueToken);
            Bundle ownedItems = service.getPurchases(3, context.getPackageName(),
                    itemType, continueToken);

            int response = getResponseCodeFromBundle(ownedItems);
            logDebug("Owned items response: " + String.valueOf(response));
            if (response != BILLING_RESPONSE_RESULT_OK) {
                logDebug("getPurchases() failed: " + getResponseDescription(response));
                return response;
            }
            if (!ownedItems.containsKey(RESPONSE_INAPP_ITEM_LIST)
                    || !ownedItems.containsKey(RESPONSE_INAPP_PURCHASE_DATA_LIST)
                    || !ownedItems.containsKey(RESPONSE_INAPP_SIGNATURE_LIST)) {
                logError("Bundle returned from getPurchases() doesn't contain required fields.");
                return IABHELPER_BAD_RESPONSE;
            }

            ArrayList<String> ownedSkus = ownedItems.getStringArrayList(
                    RESPONSE_INAPP_ITEM_LIST);
            ArrayList<String> purchaseDataList = ownedItems.getStringArrayList(
                    RESPONSE_INAPP_PURCHASE_DATA_LIST);
            ArrayList<String> signatureList = ownedItems.getStringArrayList(
                    RESPONSE_INAPP_SIGNATURE_LIST);

            for (int i = 0; i < purchaseDataList.size(); ++i) {
                String purchaseData = purchaseDataList.get(i);
                String signature = signatureList.get(i);
                String sku = ownedSkus.get(i);
                if (BillingSecurity.INSTANCE.verifyPurchase(signatureBase64, purchaseData, signature)) {
                    logDebug("Sku is owned: " + sku);
                    BillingPurchase billingPurchase = BillingPurchase.fromJson(itemType, signature, purchaseData);

                    if (TextUtils.isEmpty(billingPurchase.getToken())) {
                        logWarn("BUG: empty/null token!");
                        logDebug("Purchase data: " + purchaseData);
                    }

                    // Record ownership and token
                    inv.addBillingPurchase(billingPurchase);
                } else {
                    logWarn("Purchase signature verification **FAILED**. Not adding item.");
                    logDebug("   Purchase data: " + purchaseData);
                    logDebug("   Signature: " + signature);
                    verificationFailed = true;
                }
            }

            continueToken = ownedItems.getString(INAPP_CONTINUATION_TOKEN);
            logDebug("Continuation token: " + continueToken);
        } while (!TextUtils.isEmpty(continueToken));

        return verificationFailed ? IABHELPER_VERIFICATION_FAILED : BILLING_RESPONSE_RESULT_OK;
    }

    int querySkuDetails(String itemType, BillingInventory inv, List<String> moreSkus)
            throws RemoteException, JSONException {
        logDebug("Querying SKU details.");
        ArrayList<String> skuList = new ArrayList<String>();
        skuList.addAll(inv.getAllProductIds(itemType));
        if (moreSkus != null) {
            for (String sku : moreSkus) {
                if (!skuList.contains(sku)) {
                    skuList.add(sku);
                }
            }
        }

        if (skuList.size() == 0) {
            logDebug("queryPrices: nothing to do because there are no SKUs.");
            return BILLING_RESPONSE_RESULT_OK;
        }

        Bundle querySkus = new Bundle();
        querySkus.putStringArrayList(GET_SKU_DETAILS_ITEM_LIST, skuList);
        Bundle skuDetails = service.getSkuDetails(3, context.getPackageName(),
                itemType, querySkus);

        if (!skuDetails.containsKey(RESPONSE_GET_SKU_DETAILS_LIST)) {
            int response = getResponseCodeFromBundle(skuDetails);
            if (response != BILLING_RESPONSE_RESULT_OK) {
                logDebug("getSkuDetails() failed: " + getResponseDescription(response));
                return response;
            } else {
                logError("getSkuDetails() returned a bundle with neither an error nor a detail list.");
                return IABHELPER_BAD_RESPONSE;
            }
        }

        ArrayList<String> responseList = skuDetails.getStringArrayList(
                RESPONSE_GET_SKU_DETAILS_LIST);

        for (String thisResponse : responseList) {
            BillingProduct d = BillingProduct.fromJson(itemType, thisResponse);
            logDebug("Got sku details: " + d);
            inv.addBillingProduct(d);
        }
        return BILLING_RESPONSE_RESULT_OK;
    }

    /**
     * Callback for setup process. This listener's {@link #onIabSetupFinished} method is called
     * when the setup process is complete.
     */
    public interface OnIabSetupFinishedListener {
        /**
         * Called to notify that setup is complete.
         *
         * @param result The result of the setup process.
         */
        void onIabSetupFinished(BillingResult result);
    }


    /**
     * Callback that notifies when a purchase is finished.
     */
    public interface OnBillingPurchaseFinishedListener {
        /**
         * Called to notify that an in-app purchase finished. If the purchase was successful,
         * then the sku parameter specifies which item was purchased. If the purchase failed,
         * the sku and extraData parameters may or may not be null, depending on how far the purchase
         * process went.
         *
         * @param result The result of the purchase.
         * @param info   The purchase information (null if purchase failed)
         */
        void onIabPurchaseFinished(BillingResult result, BillingPurchase info);
    }

    /**
     * Listener that notifies when an inventory query operation completes.
     */
    public interface QueryInventoryFinishedListener {
        /**
         * Called to notify that an inventory query operation completed.
         *
         * @param result The result of the operation.
         * @param inv    The inventory.
         */
        void onQueryInventoryFinished(BillingResult result, BillingInventory inv);
    }

    /**
     * Callback that notifies when a consumption operation finishes.
     */
    public interface OnConsumeFinishedListener {
        /**
         * Called to notify that a consumption has finished.
         *
         * @param billingPurchase The purchase that was (or was to be) consumed.
         * @param result          The result of the consumption operation.
         */
        void onConsumeFinished(BillingPurchase billingPurchase, BillingResult result);
    }

    /**
     * Callback that notifies when a multi-item consumption operation finishes.
     */
    public interface OnConsumeMultiFinishedListener {
        /**
         * Called to notify that a consumption of multiple items has finished.
         *
         * @param billingPurchases The purchases that were (or were to be) consumed.
         * @param results          The results of each consumption operation, corresponding to each
         *                         sku.
         */
        void onConsumeMultiFinished(List<BillingPurchase> billingPurchases, List<BillingResult> results);
    }
}
