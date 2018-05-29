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

import java.io.Serializable

data class AccountId(val id: String) : Serializable

sealed class Account : Serializable {
    abstract val balance: Money
}

sealed class VirtualAccount : Account()

data class TotalAccount(override val balance: Money) : VirtualAccount()

sealed class RealAccount : Account() {
    abstract val accountId: AccountId
    abstract val title: Title
    abstract val color: Color
}

data class LocalAccount(
        override val accountId: AccountId,
        override val title: Title,
        override val color: Color,
        override val balance: Money) : RealAccount()

data class RemoteAccount(
        override val accountId: AccountId,
        override val title: Title,
        override val color: Color,
        override val balance: Money) : RealAccount()