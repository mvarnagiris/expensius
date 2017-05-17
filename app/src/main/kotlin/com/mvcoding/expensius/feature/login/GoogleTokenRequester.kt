package com.mvcoding.expensius.feature.login

import android.content.Intent
import android.support.v4.app.FragmentActivity
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.mvcoding.expensius.BuildConfig
import com.mvcoding.expensius.feature.login.LoginPresenter.GoogleTokenResult
import com.mvcoding.expensius.feature.login.LoginPresenter.GoogleTokenResult.FailedGoogleTokenResult
import com.mvcoding.expensius.feature.login.LoginPresenter.GoogleTokenResult.SuccessfulGoogleTokenResult
import com.mvcoding.expensius.model.GoogleToken
import rx.Observable
import rx.lang.kotlin.BehaviorSubject

class GoogleTokenRequester(private val activity: FragmentActivity) : GoogleApiClient.OnConnectionFailedListener {

    private val REQUEST_LOGIN_WITH_GOOGLE = 2712
    private val googleTokenResultSubject by lazy { BehaviorSubject<GoogleTokenResult>() }
    private val googleApiClient by lazy {
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(BuildConfig.GOOGLE_WEB_CLIENT_ID)
                .requestEmail()
                .build()
        GoogleApiClient.Builder(activity)
                .enableAutoManage(activity, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build()
    }

    private var isLoginRequested = false

    fun requestGoogleToken(): Observable<GoogleTokenResult> {
        showGoogleTokenRequestIfNecessary()
        return googleTokenResultSubject
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_LOGIN_WITH_GOOGLE) handleLoginWithGoogleResponse(data)
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        isLoginRequested = false
        val throwable = Throwable("Connection to Google Api Client failed: $connectionResult")
        googleTokenResultSubject.onNext(FailedGoogleTokenResult(throwable))
    }

    private fun showGoogleTokenRequestIfNecessary() {
        if (!isLoginRequested) {
            isLoginRequested = true
            val signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient)
            activity.startActivityForResult(signInIntent, REQUEST_LOGIN_WITH_GOOGLE)
        }
    }

    private fun handleLoginWithGoogleResponse(data: Intent?) {
        isLoginRequested = false
        val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
        if (result.isSuccess) {
            result.signInAccount?.idToken?.run {
                googleTokenResultSubject.onNext(SuccessfulGoogleTokenResult(GoogleToken(this)))
                googleTokenResultSubject.onCompleted()
            } ?: googleTokenResultSubject.onNext(FailedGoogleTokenResult(Throwable("Failed to login")))
        } else {
            googleTokenResultSubject.onNext(FailedGoogleTokenResult(Throwable("Failed to login")))
        }
    }
}