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

import com.mvcoding.expensius.model.AuthProvider.ANONYMOUS
import java.io.Serializable

data class UserId(val id: String) : Serializable
data class Email(val address: String) : Serializable
data class Name(val displayName: String) : Serializable
enum class AuthProvider { ANONYMOUS, GOOGLE }

interface User {
    val userId: UserId
    val name: Name
    val photo: Image
}

data class AppUser(
        override val userId: UserId,
        override val name: Name,
        override val photo: UriImage,
        val email: Email,
        val settings: Settings,
        val authProviders: Set<AuthProvider>) : User, Serializable {

    fun isLoggedIn(): Boolean = authProviders.isNotEmpty()
    fun isNotLoggedIn(): Boolean = !isLoggedIn()
    fun isNotAnonymous(): Boolean = authProviders.any { it != ANONYMOUS }
    fun isAnonymous(): Boolean = !isNotAnonymous()
    fun withLoggedInUserDetails(loggedInUserDetails: LoggedInUserDetails) = copy(
            userId = loggedInUserDetails.userId,
            name = loggedInUserDetails.name,
            email = loggedInUserDetails.email,
            photo = loggedInUserDetails.photo)
}