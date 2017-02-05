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

import com.memoizr.assertk.expect
import com.mvcoding.expensius.model.AuthProvider.ANONYMOUS
import com.mvcoding.expensius.model.AuthProvider.GOOGLE
import org.junit.Test

class UserTest {
    @Test
    fun appUserIsNotLoggedInWhenAuthProvidersAreEmpty() {
        expect that anAppUser().withNoAuthProviders().isLoggedIn() _is true
    }

    @Test
    fun appUserIsLoggedInWhenAuthProvidersAreNotEmpty() {
        expect that anAppUser().withAuthProvider(anAuthProvider()).isLoggedIn() _is true
    }

    @Test
    fun appUserIsWithProperAccountWhenAtLeastOneAuthProviderIsNotAnonymous() {
        expect that anAppUser().withAuthProvider(GOOGLE).isWithProperAccount() _is true
    }

    @Test
    fun appUserIsNotWithProperAccountWhenNotLoggedInOrAuthProviderIsAnonymous() {
        expect that anAppUser().withNoAuthProviders().isWithProperAccount() _is false
        expect that anAppUser().withAuthProvider(ANONYMOUS).isWithProperAccount() _is false
    }
}