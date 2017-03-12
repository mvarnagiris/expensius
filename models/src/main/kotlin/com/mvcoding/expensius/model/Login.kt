package com.mvcoding.expensius.model

sealed class Login {
    object AnonymousLogin : Login()
    data class GoogleLogin(val googleToken: GoogleToken, val forceLogin: Boolean) : Login()
}