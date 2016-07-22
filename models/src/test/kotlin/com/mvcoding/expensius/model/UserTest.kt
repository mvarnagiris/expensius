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
import com.mvcoding.expensius.model.AuthProvider.GOOGLE
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.Test

class UserTest {
    @Test
    fun appUserIsNotLoggedInWhenAuthProvidersAreEmpty() {
        assertThat(anAppUser().withNoAuthProviders().isLoggedIn(), equalTo(false))
    }

    @Test
    fun appUserIsLoggedInWhenAuthProvidersAreNotEmpty() {
        assertThat(anAppUser().withAuthProvider(anAuthProvider()).isLoggedIn(), equalTo(true))
    }

    @Test
    fun appUserIsWithProperAccountWhenAtLeastOneAuthProviderIsNotAnonymous() {
        assertThat(anAppUser().withAuthProvider(GOOGLE).isWithProperAccount(), equalTo(true))
    }

    @Test
    fun appUserIsNotWithProperAccountWhenNotLoggedInOrAuthProviderIsAnonymous() {
        assertThat(anAppUser().withNoAuthProviders().isWithProperAccount(), equalTo(false))
        assertThat(anAppUser().withAuthProvider(ANONYMOUS).isWithProperAccount(), equalTo(false))
    }
}