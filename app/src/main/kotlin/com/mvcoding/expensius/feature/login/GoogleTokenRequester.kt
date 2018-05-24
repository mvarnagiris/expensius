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

import android.support.v4.app.FragmentActivity

class GoogleTokenRequester(private val activity: FragmentActivity)/* : GoogleApiClient.OnConnectionFailedListener*/ {

//    private val REQUEST_LOGIN_WITH_GOOGLE = 2712
//    private val googleTokenResultSubject by lazy { BehaviorSubject.create<GoogleTokenResult>() }
//    private val googleApiClient by lazy {
//        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(BuildConfig.GOOGLE_WEB_CLIENT_ID)
//                .requestEmail()
//                .build()
//        GoogleApiClient.Builder(activity)
//                .enableAutoManage(activity, this)
//                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
//                .build()
//    }
//
//    private var isLoginRequested = false
//
//    fun requestGoogleToken(): Observable<GoogleTokenResult> {
//        showGoogleTokenRequestIfNecessary()
//        return googleTokenResultSubject
//    }
//
//    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        if (requestCode == REQUEST_LOGIN_WITH_GOOGLE) handleLoginWithGoogleResponse(data)
//    }
//
//    override fun onConnectionFailed(connectionResult: ConnectionResult) {
//        isLoginRequested = false
//        val throwable = Throwable("Connection to Google Api Client failed: $connectionResult")
//        googleTokenResultSubject.onNext(FailedGoogleTokenResult(throwable))
//    }
//
//    private fun showGoogleTokenRequestIfNecessary() {
//        if (!isLoginRequested) {
//            isLoginRequested = true
//            val signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient)
//            activity.startActivityForResult(signInIntent, REQUEST_LOGIN_WITH_GOOGLE)
//        }
//    }
//
//    private fun handleLoginWithGoogleResponse(data: Intent?) {
//        isLoginRequested = false
//        val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
//        if (result.isSuccess) {
//            result.signInAccount?.idToken?.run {
//                googleTokenResultSubject.onNext(SuccessfulGoogleTokenResult(GoogleToken(this)))
//                googleTokenResultSubject.onComplete()
//            } ?: googleTokenResultSubject.onNext(FailedGoogleTokenResult(Throwable("Failed to login")))
//        } else {
//            googleTokenResultSubject.onNext(FailedGoogleTokenResult(Throwable("Failed to login")))
//        }
//    }
}