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

package com.mvcoding.expensius.model

import java.io.Serializable

data class GoogleToken(val token: String) : Serializable

sealed class Login {
    object AnonymousLogin : Login()
    data class GoogleLogin(val googleToken: GoogleToken, val forceLogin: Boolean) : Login()
}

data class LoggedInUserDetails(val userId: UserId, val name: Name, val email: Email, val photo: UriImage)