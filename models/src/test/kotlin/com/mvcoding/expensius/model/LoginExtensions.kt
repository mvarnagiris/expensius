package com.mvcoding.expensius.model

import com.mvcoding.expensius.aBoolean
import com.mvcoding.expensius.aString
import com.mvcoding.expensius.model.Login.AnonymousLogin
import com.mvcoding.expensius.model.Login.GoogleLogin

fun aGoogleToken() = GoogleToken(aString("token"))
fun anAnonymousLogin() = AnonymousLogin
fun aGoogleLogin() = GoogleLogin(aGoogleToken(), aBoolean())
fun aLoggedInUserDetails() = LoggedInUserDetails(aUserId(), aName(), anEmail(), anImage())