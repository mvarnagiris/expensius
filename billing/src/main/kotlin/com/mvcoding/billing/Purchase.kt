/*
 * Copyright (C) 2017 Mantas Varnagiris.
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
import java.io.Serializable

data class Purchase(
        val productId: ProductId,
        val productType: ProductType,
        val orderId: OrderId,
        val packageName: String,
        val purchaseTimestamp: Long,
        val purchaseState: Int,
        val developerPayload: String,
        val token: String,
        val signature: String,
        val isAutoRenewing: Boolean) : Serializable {

    companion object {
        fun fromJson(productType: ProductType, signature: String, json: String) = Gson().fromJson(json, JsonObject::class.java).let {
            Purchase(
                    ProductId(it.get("productId").asString),
                    productType,
                    if (it.has("orderId")) OrderId(it.get("orderId").asString) else OrderId(""),
                    it.get("packageName").asString,
                    it.get("purchaseTime").asLong,
                    it.get("purchaseState").asInt,
                    if (it.has("developerPayload")) it.get("developerPayload").asString else "",
                    if (it.has("token")) it.get("token").asString else it.get("purchaseToken").asString,
                    signature,
                    if (it.has("autoRenewing")) it.get("autoRenewing").asBoolean else false)
        }
    }
}