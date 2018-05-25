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

package com.mvcoding.expensius.model.extensions

import com.mvcoding.expensius.model.*

fun aSettings() = Settings(aCurrency(), ReportPeriod.MONTH, ReportGroup.DAY, SubscriptionType.values().aRandomItem())
fun aUserId() = UserId(aStringId())
fun anEmail() = Email(aString("email"))
fun aName() = Name(aString("name"))
fun anAuthProvider() = AuthProvider.values().aRandomItem()
fun anAppUser() = AppUser(aUserId(), aName(), anImage(), anEmail(), aSettings(), setOf(anAuthProvider()))
fun AppUser.withNoAuthProviders() = copy(authProviders = emptySet())
fun AppUser.withAuthProvider(authProvider: AuthProvider) = copy(authProviders = setOf(authProvider))
fun AppUser.withId(id: String) = copy(userId = UserId(id))
fun AppUser.withReportPeriod(reportPeriod: ReportPeriod) = copy(settings = settings.copy(reportPeriod = reportPeriod))