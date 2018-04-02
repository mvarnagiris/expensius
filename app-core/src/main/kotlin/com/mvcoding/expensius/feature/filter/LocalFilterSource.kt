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

package com.mvcoding.expensius.feature.filter

import com.mvcoding.expensius.data.DataSource
import com.mvcoding.expensius.model.LocalFilter
import com.mvcoding.expensius.model.NullModels.noLocalFilter
import com.mvcoding.expensius.model.TransactionState.CONFIRMED
import com.mvcoding.expensius.model.TransactionType.EXPENSE
import io.reactivex.Observable
import io.reactivex.Observable.just

class LocalFilterSource : DataSource<LocalFilter> {

    override fun data(): Observable<LocalFilter> = just(noLocalFilter.withTransactionType(EXPENSE).withTransactionState(CONFIRMED))
}