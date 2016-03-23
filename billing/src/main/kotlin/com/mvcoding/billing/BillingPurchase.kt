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

import com.google.gson.Gson
import com.google.gson.JsonObject

data class BillingPurchase(
        val itemType: String,
        val signature: String,
        val orderId: String,
        val packageName: String,
        val productId: String,
        val purchaseTime: Long,
        val purchaseState: Int,
        val developerPayload: String,
        val token: String) {

    companion object {
        fun fromJson(itemType: String, signature: String, json: String) = Gson().fromJson(json, JsonObject::class.java).let {
            BillingPurchase(
                    itemType,
                    signature,
                    it.get("orderId").asString,
                    it.get("packageName").asString,
                    it.get("productId").asString,
                    it.get("purchaseTime").asLong,
                    it.get("purchaseState").asInt,
                    it.get("developerPayload").asString,
                    if (it.has("token")) it.get("token").asString else it.get("purchaseToken").asString)
        }
    }
}
