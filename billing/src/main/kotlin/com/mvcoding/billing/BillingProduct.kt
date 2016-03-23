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

data class BillingProduct(
        val itemType: String,
        val productId: String,
        val type: String,
        val price: String,
        val title: String,
        val description: String) {

    companion object {
        fun fromJson(itemType: String, json: String) = Gson().fromJson(json, JsonObject::class.java).let {
            BillingProduct(
                    itemType,
                    it.get("productId").asString,
                    it.get("type").asString,
                    it.get("price").asString,
                    it.get("title").asString,
                    it.get("description").asString)
        }
    }
}
