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
import android.support.customtabs.CustomTabsIntent
import android.support.v4.widget.NestedScrollView
import android.support.v7.widget.ListPopupWindow
import android.util.AttributeSet
import android.widget.ArrayAdapter
import com.jakewharton.rxbinding.view.clicks
import com.mvcoding.expensius.BuildConfig
import com.mvcoding.expensius.R
import com.mvcoding.expensius.R.id.currencyCodeTextView
import com.mvcoding.expensius.R.layout.item_view_currency
import com.mvcoding.expensius.SubscriptionType
import com.mvcoding.expensius.SubscriptionType.FREE
import com.mvcoding.expensius.SubscriptionType.PREMIUM_PAID
import com.mvcoding.expensius.extension.doNotInEditMode
import com.mvcoding.expensius.extension.getColorFromTheme
import com.mvcoding.expensius.extension.getDimensionFromTheme
import com.mvcoding.expensius.extension.getString
import com.mvcoding.expensius.extension.toBaseActivity
import com.mvcoding.expensius.feature.ReportGroup
import com.mvcoding.expensius.feature.ReportGroup.DAY
import com.mvcoding.expensius.feature.ReportGroup.MONTH
import com.mvcoding.expensius.feature.ReportGroup.WEEK
import com.mvcoding.expensius.feature.ReportGroup.YEAR
import com.mvcoding.expensius.feature.premium.PremiumActivity
import com.mvcoding.expensius.model.Currency
import kotlinx.android.synthetic.main.view_settings.view.*
import org.chromium.customtabsclient.CustomTabsActivityHelper
import rx.Observable
import java.lang.Math.min

class SettingsView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        NestedScrollView(context, attrs, defStyleAttr), SettingsPresenter.View {

    private val presenter by lazy { provideSettingsPresenter() }

    override fun onFinishInflate() {
        super.onFinishInflate()

        with(mainCurrencySettingsItemView as SettingsItemView) {
            setTitle(context.getString(R.string.main_currency))
        }
        with(reportStepSettingsItemView as SettingsItemView) {
            setTitle(context.getString(R.string.report_grouping))
        }
        with(supportDeveloperSettingsItemView as SettingsItemView) {
            setTitle(context.getString(R.string.support_developer))
        }
        with(versionSettingsItemView as SettingsItemView) {
            setTitle(context.getString(R.string.about))
            setSubtitle("v${BuildConfig.VERSION_NAME}")
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        doNotInEditMode { presenter.attach(this) }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        presenter.detach(this)
    }

    override fun onMainCurrencySelected() = mainCurrencySettingsItemView.clicks()
    override fun onReportStepSelected() = reportStepSettingsItemView.clicks()
    override fun onSupportDeveloperSelected() = supportDeveloperSettingsItemView.clicks()
    override fun onAboutSelected() = versionSettingsItemView.clicks()

    override fun requestMainCurrency(currencies: List<Currency>): Observable<Currency> = Observable.create {
        val displayCurrencies = currencies.map { it.displayName() }
        val itemHeight = getDimensionFromTheme(context, R.attr.actionBarSize)
        val keyline = resources.getDimensionPixelSize(R.dimen.keyline)
        val keylineHalf = resources.getDimensionPixelOffset(R.dimen.keyline_half)
        val popupWindow = ListPopupWindow(context)
        popupWindow.anchorView = mainCurrencySettingsItemView
        popupWindow.setAdapter(ArrayAdapter<String>(context, item_view_currency, currencyCodeTextView, displayCurrencies))
        popupWindow.setOnItemClickListener { adapterView, view, position, id -> it.onNext(currencies[position]); popupWindow.dismiss() }
        popupWindow.setOnDismissListener { it.onCompleted() }
        popupWindow.width = width - keyline
        popupWindow.height = min(height - mainCurrencySettingsItemView.bottom - itemHeight - keylineHalf, itemHeight * 7)
        popupWindow.isModal = true
        popupWindow.horizontalOffset = keylineHalf
        popupWindow.show()
    }

    override fun showMainCurrency(mainCurrency: Currency) = with(mainCurrencySettingsItemView as SettingsItemView) {
        setSubtitle(mainCurrency.displayName())
    }

    override fun requestReportStep(reportGroups: List<ReportGroup>): Observable<ReportGroup> = Observable.create {
        val displayCurrencies = reportGroups.map { it.displayName() }
        val itemHeight = getDimensionFromTheme(context, R.attr.actionBarSize)
        val keyline = resources.getDimensionPixelSize(R.dimen.keyline)
        val keylineHalf = resources.getDimensionPixelOffset(R.dimen.keyline_half)
        val popupWindow = ListPopupWindow(context)
        popupWindow.anchorView = reportStepSettingsItemView
        popupWindow.setAdapter(ArrayAdapter<String>(context, item_view_currency, currencyCodeTextView, displayCurrencies))
        popupWindow.setOnItemClickListener { adapterView, view, position, id -> it.onNext(reportGroups[position]); popupWindow.dismiss() }
        popupWindow.setOnDismissListener { it.onCompleted() }
        popupWindow.width = width - keyline
        popupWindow.height = min(height - reportStepSettingsItemView.bottom - itemHeight - keylineHalf, itemHeight * reportGroups.size)
        popupWindow.isModal = true
        popupWindow.horizontalOffset = keylineHalf
        popupWindow.show()
    }

    override fun showReportStep(reportGroup: ReportGroup) = with(reportStepSettingsItemView as SettingsItemView) {
        setSubtitle(reportGroup.displayName())
    }

    override fun showSubscriptionType(subscriptionType: SubscriptionType) = with(supportDeveloperSettingsItemView as SettingsItemView) {
        setSubtitle(when (subscriptionType) {
            FREE -> getString(R.string.long_user_is_using_free_version)
            PREMIUM_PAID -> getString(R.string.long_user_is_using_premium_version)
        })
    }

    override fun displaySupportDeveloper() = PremiumActivity.start(context)

    override fun displayAbout() {
        CustomTabsActivityHelper.openCustomTab(
                context.toBaseActivity(),
                CustomTabsIntent.Builder()
                        .setToolbarColor(getColorFromTheme(R.attr.colorPrimary))
                        .setSecondaryToolbarColor(getColorFromTheme(R.attr.colorAccent))
                        .setShowTitle(false)
                        .enableUrlBarHiding()
                        .build(),
                Uri.parse("https://github.com/mvarnagiris/expensius/blob/dev/CHANGELOG.md"),
                { activity, uri -> openUrl(uri) })
    }

    private fun openUrl(uri: Uri) = context.startActivity(Intent(Intent.ACTION_VIEW, uri))

    private fun ReportGroup.displayName() = when (this) {
        DAY -> getString(R.string.day)
        WEEK -> getString(R.string.week)
        MONTH -> getString(R.string.month)
        YEAR -> getString(R.string.year)
    }
}