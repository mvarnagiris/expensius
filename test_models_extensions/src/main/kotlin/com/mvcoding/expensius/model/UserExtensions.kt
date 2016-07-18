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

fun aSettings() = Settings(aCurrency(), SubscriptionType.values().aRandomItem())
fun aUserId() = UserId(aStringId())
fun anAuthProvider() = AuthProvider.values().aRandomItem()
fun aGoogleToken() = GoogleToken(aString("token"))
fun anAppUser() = AppUser(aUserId(), aSettings(), setOf(anAuthProvider()))
fun AppUser.withNoAuthProviders() = copy(authProviders = emptySet())
fun AppUser.withAuthProvider(authProvider: AuthProvider) = copy(authProviders = setOf(authProvider))