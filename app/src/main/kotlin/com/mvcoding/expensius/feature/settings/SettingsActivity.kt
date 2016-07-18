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

package com.mvcoding.expensius.feature.settings

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.customtabs.CustomTabsIntent
import android.support.v7.widget.ListPopupWindow
import android.widget.ArrayAdapter
import com.jakewharton.rxbinding.view.clicks
import com.mvcoding.expensius.BuildConfig
import com.mvcoding.expensius.R
import com.mvcoding.expensius.extension.getColorFromTheme
import com.mvcoding.expensius.extension.getDimensionFromTheme
import com.mvcoding.expensius.extension.getString
import com.mvcoding.expensius.feature.ActivityStarter
import com.mvcoding.expensius.feature.BaseActivity
import com.mvcoding.expensius.feature.login.LoginActivity
import com.mvcoding.expensius.feature.login.LoginPresenter.Destination
import com.mvcoding.expensius.feature.premium.PremiumActivity
import com.mvcoding.expensius.model.Currency
import com.mvcoding.expensius.model.SubscriptionType
import kotlinx.android.synthetic.main.activity_settings.*
import org.chromium.customtabsclient.CustomTabsActivityHelper
import rx.Observable
import java.lang.Math.min

class SettingsActivity : BaseActivity(), SettingsPresenter.View {
    companion object {
        fun start(context: Context) = ActivityStarter(context, SettingsActivity::class).start()
    }

    private val presenter by lazy { provideSettingsPresenter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        with(mainCurrencySettingsItemView as SettingsItemView) {
            setTitle(getString(R.string.main_currency))
        }
        with(supportDeveloperSettingsItemView as SettingsItemView) {
            setTitle(getString(R.string.support_developer))
        }
        with(versionSettingsItemView as SettingsItemView) {
            setTitle(getString(R.string.about))
            setSubtitle("v${BuildConfig.VERSION_NAME}")
        }
    }

    override fun onStart() {
        super.onStart()
        presenter.attach(this)
    }

    override fun onStop() {
        super.onStop()
        presenter.detach(this)
    }

    override fun mainCurrencyRequests() = mainCurrencySettingsItemView.clicks()
    override fun supportDeveloperRequests() = supportDeveloperSettingsItemView.clicks()
    override fun aboutRequests() = versionSettingsItemView.clicks()

    override fun chooseMainCurrency(currencies: List<Currency>): Observable<Currency> = Observable.create {
        val displayCurrencies = currencies.map { it.displayName() }
        val itemHeight = getDimensionFromTheme(R.attr.actionBarSize)
        val keyline = resources.getDimensionPixelSize(R.dimen.keyline)
        val keylineHalf = resources.getDimensionPixelOffset(R.dimen.keyline_half)
        val popupWindow = ListPopupWindow(this)
        popupWindow.anchorView = mainCurrencySettingsItemView
        popupWindow.setAdapter(ArrayAdapter<String>(this, R.layout.item_view_currency, R.id.currencyCodeTextView, displayCurrencies))
        popupWindow.setOnItemClickListener { adapterView, view, position, id -> it.onNext(currencies[position]); popupWindow.dismiss() }
        popupWindow.setOnDismissListener { it.onCompleted() }
        popupWindow.width = contentView.width - keyline
        popupWindow.height = min(contentView.height - mainCurrencySettingsItemView.bottom - itemHeight - keylineHalf, itemHeight * 7)
        popupWindow.isModal = true
        popupWindow.horizontalOffset = keylineHalf
        popupWindow.show()
    }

    override fun showMainCurrency(mainCurrency: Currency) = with(mainCurrencySettingsItemView as SettingsItemView) { setSubtitle(mainCurrency.displayName()) }
    override fun showSubscriptionType(subscriptionType: SubscriptionType) = with(supportDeveloperSettingsItemView as SettingsItemView) {
        setSubtitle(when (subscriptionType) {
            SubscriptionType.FREE -> getString(R.string.long_user_is_using_free_version)
            SubscriptionType.PREMIUM_PAID -> getString(R.string.long_user_is_using_premium_version)
        })
    }

    override fun displaySupportDeveloper(): Unit = PremiumActivity.start(this)
    override fun displayLogin(destination: Destination): Unit = LoginActivity.start(this, destination)

    override fun displayAbout() {
        CustomTabsActivityHelper.openCustomTab(
                this,
                CustomTabsIntent.Builder()
                        .setToolbarColor(getColorFromTheme(R.attr.colorPrimary))
                        .setSecondaryToolbarColor(getColorFromTheme(R.attr.colorAccent))
                        .setShowTitle(false)
                        .enableUrlBarHiding()
                        .build(),
                Uri.parse("https://github.com/mvarnagiris/expensius/blob/dev/CHANGELOG.md"),
                { activity, uri -> openUrl(uri) })
    }

    private fun openUrl(uri: Uri): Unit = this.startActivity(Intent(Intent.ACTION_VIEW, uri))
}