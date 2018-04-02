/*
 * Copyright (C) 2018 Mantas Varnagiris.
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

package com.mvcoding.expensius.feature.login

import android.content.Context
import android.support.v4.content.ContextCompat
import com.mvcoding.expensius.R
import com.mvcoding.expensius.data.DataSource
import com.mvcoding.expensius.model.Color
import com.mvcoding.expensius.model.CreateTag
import com.mvcoding.expensius.model.Order
import com.mvcoding.expensius.model.Title
import io.reactivex.Observable
import io.reactivex.Observable.just

class TranslatedDefaultTagsSource(private val context: Context) : DataSource<List<CreateTag>> {

    override fun data(): Observable<List<CreateTag>> = just(listOf(
            getString(R.string.tag_fixed) to getColor(R.color.red_300),
            getString(R.string.tag_essential) to getColor(R.color.red_500),
            getString(R.string.tag_non_essential) to getColor(R.color.red_900),
            getString(R.string.tag_food) to getColor(R.color.light_green_500),
            getString(R.string.tag_leisure) to getColor(R.color.light_blue_500),
            getString(R.string.tag_clothes) to getColor(R.color.orange_500),
            getString(R.string.tag_transport) to getColor(R.color.yellow_500),
            getString(R.string.tag_household) to getColor(R.color.purple_500),
            getString(R.string.tag_health_and_beauty) to getColor(R.color.pink_500),
            getString(R.string.tag_bills_and_utilities) to getColor(R.color.brown_500),
            getString(R.string.tag_pets) to getColor(R.color.teal_500))
            .mapIndexed { position, titleAndColor -> CreateTag(Title(titleAndColor.first), Color(titleAndColor.second), Order(position)) })

    private fun getString(resId: Int) = context.getString(resId)
    private fun getColor(resId: Int) = ContextCompat.getColor(context, resId)
}