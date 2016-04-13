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

class BillingResult private constructor(val response: Int, val message: String) {
    val isSuccess = response == BILLING_RESPONSE_RESULT_OK
    val isFailure = !isSuccess

    companion object {
        internal val BILLING_RESPONSE_RESULT_OK = 0
        internal val BILLING_RESPONSE_RESULT_USER_CANCELED = 1
        internal val BILLING_RESPONSE_RESULT_SERVICE_UNAVAILABLE = 2
        internal val BILLING_RESPONSE_RESULT_BILLING_UNAVAILABLE = 3
        internal val BILLING_RESPONSE_RESULT_ITEM_UNAVAILABLE = 4
        internal val BILLING_RESPONSE_RESULT_DEVELOPER_ERROR = 5
        internal val BILLING_RESPONSE_RESULT_ERROR = 6
        internal val BILLING_RESPONSE_RESULT_ITEM_ALREADY_OWNED = 7
        internal val BILLING_RESPONSE_RESULT_ITEM_NOT_OWNED = 8

        fun billingResult(response: Int, message: String?) = BillingResult(response, buildMessage(response, message))

        private fun buildMessage(response: Int, message: String?) =
                "${if (!message.isNullOrBlank()) "$message " else ""} (response: ${getResponseDescription(response)})"

        internal fun getResponseDescription(responseCode: Int): String {
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
    }
}

