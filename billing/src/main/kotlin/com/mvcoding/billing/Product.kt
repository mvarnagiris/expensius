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
import java.io.Serializable

data class Product(
        val productId: ProductId,
        val productType: ProductType,
        val type: String,
        val title: String,
        val description: String,
        val price: Price) : Serializable {

    companion object {
        fun fromJson(productType: ProductType, json: String) = Gson().fromJson(json, JsonObject::class.java).let {
            Product(
                    ProductId(it.get("productId").asString),
                    productType,
                    it.get("type").asString,
                    it.get("title").asString,
                    it.get("description").asString,
                    Price(it.get("price").asString, it.get("price_amount_micros").asLong, it.get("price_currency_code").asString))
        }
    }
}

data class Price(val price: String, val priceAmountMicros: Long, val currencyCode: String) : Serializable