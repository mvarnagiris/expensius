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

import com.mvcoding.expensius.feature.BaseActivity

class LoginActivity : BaseActivity()/*, LoginPresenter.View */ {

//    companion object {
//        private const val EXTRA_DESTINATION = "EXTRA_DESTINATION"
//
//        fun start(context: Context, destination: Destination): Unit = ActivityStarter(context, LoginActivity::class)
//                .extra(EXTRA_DESTINATION, destination)
//                .start()
//    }
//
//    private val presenter by lazy { provideLoginPresenter(intent.getSerializableExtra(EXTRA_DESTINATION) as Destination) }
//    private val errorDisplayer by lazy { ErrorDisplayer(this) }
//    private val resolvableErrorDisplayer by lazy { ResolvableErrorDisplayer(this) }
//    private val googleTokenRequester by lazy { GoogleTokenRequester(toBaseActivity()) }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_login)
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        googleTokenRequester.onActivityResult(requestCode, resultCode, data)
//    }
//
//    override fun onStart() {
//        super.onStart()
//        presenter.attach(this)
//    }
//
//    override fun onStop() {
//        super.onStop()
//        presenter.detach(this)
//    }
//
//    override fun skipLogins(): Observable<Unit> = skipLoginButton.clicks()
//    override fun googleLogins(): Observable<Unit> = googleLoginButton.clicks()
//    override fun showError(error: Error): Unit = errorDisplayer.show(error)
//    override fun showResolvableError(error: Error): Observable<Resolution> = resolvableErrorDisplayer.showError(error)
//    override fun showGoogleTokenRequest(): Observable<GoogleTokenResult> = googleTokenRequester.requestGoogleToken()
//
//    override fun showAllLoginOptions() {
//        progressBar.setGone()
//        skipLoginButton.setVisible()
//        googleLoginButton.setVisible()
//    }
//
//    override fun showAllLoginOptionsExceptSkip() {
//        progressBar.setGone()
//        skipLoginButton.setVisible()
//        googleLoginButton.setVisible()
//    }
//
//    override fun showLoggingIn(authProvider: AuthProvider) {
//        progressBar.setVisible()
//        googleLoginButton.setGone()
//        skipLoginButton.setGone()
//    }
//
//    override fun displayDestination(destination: Destination) {
//        if (destination == APP) OverviewActivity.start(this)
//        else if (destination == SUPPORT_DEVELOPER) PremiumActivity.start(this)
//        finish()
//    }
}