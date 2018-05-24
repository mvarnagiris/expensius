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

package com.mvcoding.expensius.feature.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.transaction
import com.mvcoding.expensius.R
import com.mvcoding.expensius.extension.supportFragmentManager
import com.mvcoding.expensius.feature.BaseFragment
import com.mvcoding.expensius.feature.overview.OverviewFragment

class SplashFragment : BaseFragment(), SplashPresenter.View {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPresenter(provideSplashPresenter())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.splash_fragment, container, false)
    }

    override fun displayApp() = supportFragmentManager.transaction { replace(R.id.container, OverviewFragment()) }
}