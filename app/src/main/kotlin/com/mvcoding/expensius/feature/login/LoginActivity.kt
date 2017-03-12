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
import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInOptions.DEFAULT_SIGN_IN
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.crash.FirebaseCrash
import com.jakewharton.rxbinding.view.clicks
import com.mvcoding.expensius.BuildConfig.GOOGLE_WEB_CLIENT_ID
import com.mvcoding.expensius.R
import com.mvcoding.expensius.feature.*
import com.mvcoding.expensius.feature.login.LoginPresenter.Destination
import com.mvcoding.expensius.feature.login.LoginPresenter.Destination.APP
import com.mvcoding.expensius.feature.login.LoginPresenter.Destination.SUPPORT_DEVELOPER
import com.mvcoding.expensius.feature.overview.OverviewActivity
import com.mvcoding.expensius.feature.premium.PremiumActivity
import com.mvcoding.expensius.model.GoogleToken
import kotlinx.android.synthetic.main.activity_login.*
import rx.Observable
import rx.lang.kotlin.BehaviorSubject
import rx.subjects.BehaviorSubject

class LoginActivity : BaseActivity(), LoginPresenter.View, GoogleApiClient.OnConnectionFailedListener {

    companion object {
        private const val REQUEST_LOGIN_WITH_GOOGLE = 1
        private const val EXTRA_DESTINATION = "EXTRA_DESTINATION"

        fun start(context: Context, destination: Destination): Unit = ActivityStarter(context, LoginActivity::class)
                .extra(EXTRA_DESTINATION, destination)
                .start()
    }

    private val presenter by lazy { provideLoginPresenter(intent.getSerializableExtra(EXTRA_DESTINATION) as Destination) }
    private val errorDisplayer by lazy { ErrorDisplayer(this) }
    private val resolvableErrorDisplayer by lazy { ResolvableErrorDisplayer(this) }
    private val googleApiClient by lazy {
        val googleSignInOptions = GoogleSignInOptions.Builder(DEFAULT_SIGN_IN).requestIdToken(GOOGLE_WEB_CLIENT_ID).requestEmail().build()
        GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions).build()
    }

    private var isLoginRequested = false
    private var loginWithGoogleSubject: BehaviorSubject<GoogleToken> = BehaviorSubject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_LOGIN_WITH_GOOGLE) handleLoginWithGoogleResponse(data)
    }

    override fun onStart() {
        super.onStart()
        presenter.attach(this)
    }

    override fun onStop() {
        super.onStop()
        presenter.detach(this)
    }

    override fun showGoogleTokenRequest(): Observable<GoogleToken> {
        throw UnsupportedOperationException("Not implemented")
    }

    override fun showAllLoginOptions() {
        throw UnsupportedOperationException("Not implemented")
    }

    override fun showAllLoginOptionsExceptSkip() {
        throw UnsupportedOperationException("Not implemented")
    }

    override fun showLoggingInWithGoogle() {
        throw UnsupportedOperationException("Not implemented")
    }

    override fun showLoggingInAnonymously() {
        throw UnsupportedOperationException("Not implemented")
    }

    override fun loginWithGoogleRequests(): Observable<Unit> = googleLoginButton.clicks()
    override fun skipLogins(): Observable<Unit> = skipLoginButton.clicks()
    override fun showError(error: Error): Unit = errorDisplayer.show(error)
    override fun showResolvableError(error: Error): Observable<Resolution> = resolvableErrorDisplayer.showError(error)

//    override fun showLoading(): Unit = setLoading(true)
//    override fun hideLoading(): Unit = setLoading(false)
//    override fun showSkipEnabled(): Unit = with(skipLoginButton) { visibility = VISIBLE }
//    override fun showSkipDisabled(): Unit = with(skipLoginButton) { visibility = GONE }
//
//    override fun showLoginWithGoogle(): Observable<GoogleToken> {
//        showGoogleLoginIfNecessary()
//        return loginWithGoogleSubject
//    }

    override fun displayDestination(destination: Destination) {
        when (destination) {
            APP -> OverviewActivity.start(this)
            SUPPORT_DEVELOPER -> PremiumActivity.start(this)
        }
        finish()
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        FirebaseCrash.report(Throwable("Connection to Google Api Client failed: $connectionResult"))
        finish()
    }

    private fun setLoading(loading: Boolean) {
        progressBar.visibility = if (loading) VISIBLE else GONE
        googleLoginButton.visibility = if (loading) GONE else VISIBLE
        skipLoginButton.visibility = if (loading) GONE else VISIBLE
    }

    private fun showGoogleLoginIfNecessary() {
        if (!isLoginRequested) {
            isLoginRequested = true
            val signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient)
            startActivityForResult(signInIntent, REQUEST_LOGIN_WITH_GOOGLE)
        }
    }

    private fun handleLoginWithGoogleResponse(data: Intent?) {
        isLoginRequested = false
        val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
        if (result.isSuccess) {
            result.signInAccount?.idToken?.run {
                loginWithGoogleSubject.onNext(GoogleToken(this))
                loginWithGoogleSubject.onCompleted()
            } ?: loginWithGoogleSubject.onError(Throwable())
        } else {
            loginWithGoogleSubject.onError(Throwable())
        }
        loginWithGoogleSubject = BehaviorSubject()
    }
}