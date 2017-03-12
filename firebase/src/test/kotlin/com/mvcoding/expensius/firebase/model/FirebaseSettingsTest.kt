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
}