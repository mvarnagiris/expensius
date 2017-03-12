/*
 * Copyright (C) 2017 Mantas Varnagiris.
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

import com.mvcoding.expensius.aRandomItem
import com.mvcoding.expensius.aString
import com.mvcoding.expensius.aStringId

fun aSettings() = Settings(aCurrency(), ReportPeriod.MONTH, ReportGroup.DAY, SubscriptionType.values().aRandomItem())
fun aUserId() = UserId(aStringId())
fun anEmail() = Email(aString("email"))
fun aName() = Name(aString("name"))
fun anAuthProvider() = AuthProvider.values().aRandomItem()
fun anAppUser() = AppUser(aUserId(), aName(), anImage(), anEmail(), aSettings(), setOf(anAuthProvider()))
fun AppUser.withNoAuthProviders() = copy(authProviders = emptySet())
fun AppUser.withAuthProvider(authProvider: AuthProvider) = copy(authProviders = setOf(authProvider))