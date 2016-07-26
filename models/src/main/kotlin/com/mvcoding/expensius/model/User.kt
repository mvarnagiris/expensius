/*
 * Copyright (C) 2016 Mantas Varnagiris.
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

import com.mvcoding.expensius.model.AuthProvider.ANONYMOUS
import java.io.Serializable

data class UserId(val id: String) : Serializable
data class GoogleToken(val token: String) : Serializable
data class Email(val address: String) : Serializable
enum class AuthProvider { ANONYMOUS, GOOGLE }

interface User {
    val userId: UserId
}

data class AppUser(
        override val userId: UserId,
        val email: Email,
        val settings: Settings,
        val authProviders: Set<AuthProvider>) : User, Serializable {

    fun isLoggedIn(): Boolean = authProviders.isNotEmpty()
    fun isNotLoggedIn(): Boolean = !isLoggedIn()
    fun isWithProperAccount(): Boolean = authProviders.any { it != ANONYMOUS }
    fun isNotWithProperAccount(): Boolean = !isWithProperAccount()
}