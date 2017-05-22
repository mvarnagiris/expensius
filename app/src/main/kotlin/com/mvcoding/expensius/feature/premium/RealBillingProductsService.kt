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

package com.mvcoding.expensius.feature.premium

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.mvcoding.billing.BillingHelper
import com.mvcoding.billing.Inventory
import com.mvcoding.billing.Product
import com.mvcoding.billing.ProductId
import com.mvcoding.billing.ProductType.SINGLE
import com.mvcoding.expensius.model.SubscriptionType.FREE
import com.mvcoding.expensius.model.SubscriptionType.PREMIUM_PAID
import rx.Observable

class RealBillingProductsService(context: Context) : Billing {

    private val premiumItemsIds = listOf("premium_1", "premium_2", "premium_3")

    private val billingHelper = BillingHelper(
            context,
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAm5OUB6qySEUQnCDvVKEq3CXOvr2Pm7voQftfe2keqsk/0CsGFKJOzcaSwwI459cl65O3ns/DROvMGPRtyb"
            + "5WJz0nTinj/STPlBTb1DptOniIx/ex5+gXDyvVcxjC4A67bJcy12Wm6fWh10xKoBEWyKBIao5zM1Z5ZJnD4lgh6XyylhvwBZvL2jIKE27hTPSpnglfqlZbqH6SaV"
            + "4Rs+8Jscq72xUPJffrj8M8gFosLgsyNLZ0Ci7ubSpfuKEfkUiCq30R8A0vbeFsXaevu0Luv9BlaUxYBEgMTg4Qt+emVqWfrMYqc0k9IEmd0/hRapCSPMhYHY9Gfy"
            + "LU7pyUkMYmsQIDAQAB")

    override fun billingProducts(): Observable<List<BillingProduct>> = billingHelper.startSetup().map {
        val inventory = billingHelper.queryInventory(true, premiumItemsIds.map { ProductId(it) })
        inventory.products().map { it.toBillingProduct(inventory) }
    }

    override fun close() = billingHelper.dispose()

    override fun startPurchase(activity: Activity, requestCode: Int, productId: String): Observable<Unit> =
            billingHelper.launchPurchaseFlow(activity, requestCode, ProductId(productId), SINGLE, emptyList(), "").map { Unit }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) =
            billingHelper.onActivityResult(requestCode, resultCode, data)

    private fun ProductId.toSubscriptionType() = if (id.startsWith("premium")) FREE else PREMIUM_PAID
    private fun Product.toBillingProduct(inventory: Inventory) = BillingProduct(productId.id,
            productId.toSubscriptionType(),
            title,
            description,
            price.price,
            inventory.hasPurchase(productId))
}