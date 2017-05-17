package com.mvcoding.expensius.model

import java.io.Serializable

data class GoogleToken(val token: String) : Serializable

sealed class Login {
    object AnonymousLogin : Login()
    data class GoogleLogin(val googleToken: GoogleToken, val forceLogin: Boolean) : Login()
}

data class LoggedInUserDetails(val userId: UserId, val name: Name, val email: Email, val photo: UriImage)