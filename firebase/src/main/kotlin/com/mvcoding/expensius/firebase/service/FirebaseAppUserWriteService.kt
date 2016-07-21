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

package com.mvcoding.expensius.firebase.service

import com.mvcoding.expensius.firebase.model.FirebaseSettings
import com.mvcoding.expensius.firebase.model.FirebaseUserData
import com.mvcoding.expensius.firebase.userDatabaseReference
import com.mvcoding.expensius.model.Settings
import com.mvcoding.expensius.service.AppUserService
import com.mvcoding.expensius.service.AppUserWriteService
import rx.Observable

class FirebaseAppUserWriteService(private val appUserService: AppUserService) : AppUserWriteService {
    override fun saveSettings(settings: Settings): Observable<Unit> = appUserService.appUser().first().map {
        val userReference = userDatabaseReference(it.userId)
        userReference.setValue(FirebaseUserData(it.userId.id, settings.toFirebaseSettings()))
        Unit
    }

    private fun Settings.toFirebaseSettings() = FirebaseSettings(mainCurrency.code, subscriptionType.name)
}