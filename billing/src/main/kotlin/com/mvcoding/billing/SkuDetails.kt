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

/**
 * Represents an in-app product's listing details.
 */
data class SkuDetails(
        val itemType: String,
        val sku: String,
        val type: String,
        val price: String,
        val title: String,
        val description: String) {

    companion object {
        @JvmStatic fun fromJson(itemType: String, json: String) = Gson().fromJson(json, JsonObject::class.java).let {
            SkuDetails(
                    itemType,
                    it.get("productId").asString,
                    it.get("type").asString,
                    it.get("price").asString,
                    it.get("title").asString,
                    it.get("description").asString)
        }
    }
}
