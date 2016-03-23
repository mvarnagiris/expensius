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

class BillingInventory {
    val billingProducts = hashMapOf<String, BillingProduct>()
    val billingPurchases = hashMapOf<String, BillingPurchase>()

    fun hasBillingProduct(productId: String) = billingProducts.containsKey(productId)
    fun getBillingProduct(productId: String) = billingProducts[productId]
    fun addBillingProduct(billingProduct: BillingProduct) = billingProducts.put(billingProduct.productId, billingProduct)
    fun hasBillingPurchase(productId: String) = billingPurchases.containsKey(productId)
    fun getBillingPurchase(productId: String) = billingPurchases[productId]
    fun removeBillingPurchase(productId: String) = billingPurchases.remove(productId)
    fun addBillingPurchase(billingPurchase: BillingPurchase) = billingPurchases.put(billingPurchase.productId, billingPurchase)
    fun getAllProductIds() = billingPurchases.keys.toList()
    fun getAllProductIds(itemType: String) = billingPurchases.filter { it.value.itemType == itemType }.map { it.value.productId }
    fun getAllBillingPurchases() = billingPurchases.values.toList()
}
