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

package com.mvcoding.expensius.model.extensions

import com.mvcoding.expensius.model.GoogleToken
import com.mvcoding.expensius.model.LoggedInUserDetails
import com.mvcoding.expensius.model.Login

fun aGoogleToken() = GoogleToken(aString("token"))
fun anAnonymousLogin() = Login.AnonymousLogin
fun aGoogleLogin() = Login.GoogleLogin(aGoogleToken(), aBoolean())
fun aLoggedInUserDetails() = LoggedInUserDetails(aUserId(), aName(), anEmail(), anImage())