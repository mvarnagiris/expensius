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

package com.mvcoding.expensius.firebase.model

import com.memoizr.assertk.expect
import com.mvcoding.expensius.firebase.extensions.aFirebaseSettings
import com.mvcoding.expensius.firebase.extensions.withMainCurrency
import com.mvcoding.expensius.firebase.extensions.withSubscriptionType
import com.mvcoding.expensius.model.Currency
import com.mvcoding.expensius.model.NullModels.noSettings
import com.mvcoding.expensius.model.SubscriptionType
import com.mvcoding.expensius.model.SubscriptionType.FREE
import com.mvcoding.expensius.model.defaultCurrency
import com.mvcoding.expensius.model.extensions.aSettings
import org.junit.Test

class FirebaseSettingsTest {

    @Test
    fun `returns Settings with default values when not required fields ar null or empty`() {
        expect that aFirebaseSettings().withMainCurrency(null).toSettings() isNotEqualTo noSettings
        expect that aFirebaseSettings().withMainCurrency("").toSettings() isNotEqualTo noSettings
        expect that aFirebaseSettings().withMainCurrency(" ").toSettings() isNotEqualTo noSettings
        expect that aFirebaseSettings().withMainCurrency(null).toSettings().mainCurrency isEqualTo defaultCurrency()
        expect that aFirebaseSettings().withMainCurrency("").toSettings().mainCurrency isEqualTo defaultCurrency()
        expect that aFirebaseSettings().withMainCurrency(" ").toSettings().mainCurrency isEqualTo defaultCurrency()

        expect that aFirebaseSettings().withSubscriptionType(null).toSettings() isNotEqualTo noSettings
        expect that aFirebaseSettings().withSubscriptionType("").toSettings() isNotEqualTo noSettings
        expect that aFirebaseSettings().withSubscriptionType(" ").toSettings() isNotEqualTo noSettings
        expect that aFirebaseSettings().withSubscriptionType(null).toSettings().subscriptionType isEqualTo FREE
        expect that aFirebaseSettings().withSubscriptionType("").toSettings().subscriptionType isEqualTo FREE
        expect that aFirebaseSettings().withSubscriptionType(" ").toSettings().subscriptionType isEqualTo FREE
    }

    @Test
    fun `returns Settings when all fields are set`() {
        val firebaseSettings = aFirebaseSettings()

        val settings = firebaseSettings.toSettings()

        expect that settings.mainCurrency isEqualTo Currency(firebaseSettings.mainCurrency!!)
        expect that settings.subscriptionType isEqualTo SubscriptionType.valueOf(firebaseSettings.subscriptionType!!)
    }

    @Test
    fun `converts Settings to FirebaseSettings`() {
        val settings = aSettings()

        val firebaseSettings = settings.toFirebaseSettings()

        expect that firebaseSettings.mainCurrency isEqualTo settings.mainCurrency.code
        expect that firebaseSettings.subscriptionType isEqualTo settings.subscriptionType.name
    }
}