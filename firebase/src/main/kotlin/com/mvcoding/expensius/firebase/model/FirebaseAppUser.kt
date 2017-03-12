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

package com.mvcoding.expensius.firebase.model

import com.mvcoding.expensius.model.*
import com.mvcoding.expensius.model.NullModels.noAppUser
import com.mvcoding.expensius.model.NullModels.noEmail
import com.mvcoding.expensius.model.NullModels.noImage
import com.mvcoding.expensius.model.NullModels.noName
import com.mvcoding.expensius.model.NullModels.noSettings

data class FirebaseAppUser(
        val id: String? = null,
        val name: String? = null,
        val photoUrl: String? = null,
        val email: String? = null,
        val settings: FirebaseSettings? = null) {

    fun toAppUser(authProviders: Set<AuthProvider>): AppUser {
        if (id.isNullOrBlank()) return noAppUser

        return AppUser(
                UserId(id!!),
                if (name.isNullOrBlank()) noName else Name(name!!),
                if (photoUrl.isNullOrBlank()) noImage else UriImage(photoUrl!!),
                if (email.isNullOrBlank()) noEmail else Email(email!!),
                settings?.toSettings() ?: noSettings,
                authProviders)
    }
}