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

package com.mvcoding.expensius.feature.login

import android.content.Context
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import com.jakewharton.rxbinding.view.clicks
import com.mvcoding.expensius.R
import com.mvcoding.expensius.feature.ActivityStarter
import com.mvcoding.expensius.feature.BaseActivity
import com.mvcoding.expensius.feature.Error
import com.mvcoding.expensius.feature.ErrorDisplayer
import com.mvcoding.expensius.feature.overview.OverviewActivity
import kotlinx.android.synthetic.main.activity_login.*
import rx.Observable

class LoginActivity : BaseActivity(), LoginPresenter.View {
    companion object {
        fun start(context: Context) {
            ActivityStarter(context, LoginActivity::class).start()
        }
    }

    private val presenter by lazy { provideLoginPresenter() }
    private val errorDisplayer by lazy { ErrorDisplayer(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    override fun onStart() {
        super.onStart()
        presenter.attach(this)
    }

    override fun onStop() {
        super.onStop()
        presenter.detach(this)
    }

    override fun loginAnonymouslyRequests(): Observable<Unit> = skipButton.clicks()
    override fun showLoggingIn(): Unit = with(progressBar) { visibility = VISIBLE }
    override fun hideLoggingIn(): Unit = with(progressBar) { visibility = GONE }
    override fun showError(error: Error): Unit = errorDisplayer.show(error)

    override fun displayApp() {
        OverviewActivity.start(this)
        finish()
    }
}