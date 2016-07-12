/*
 * Copyright (C) 2015 Mantas Varnagiris.
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
import com.mvcoding.expensius.extension.app
import com.mvcoding.expensius.feature.BaseActivity
import com.mvcoding.expensius.feature.login.LoginActivity
import com.mvcoding.expensius.feature.login.LoginPresenter.Destination
import com.mvcoding.expensius.feature.overview.OverviewActivity

class SplashActivity : BaseActivity(), SplashPresenter.View {

    private val presenter by lazy { provideSplashPresenter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app().setupFirebase()
    }

    override fun onStart() {
        super.onStart()
        presenter.attach(this)
    }

    override fun onStop() {
        super.onStop()
        presenter.detach(this)
    }

    override fun displayLogin(destination: Destination) {
        LoginActivity.start(this, destination)
        finish()
    }

    override fun displayApp() {
        OverviewActivity.start(this)
        finish()
    }
}