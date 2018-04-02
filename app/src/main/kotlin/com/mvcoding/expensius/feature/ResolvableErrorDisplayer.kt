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

package com.mvcoding.expensius.feature

import android.app.Activity
import android.support.design.widget.Snackbar
import android.view.View
import com.mvcoding.expensius.R
import com.mvcoding.expensius.extension.snackbar
import com.mvcoding.expensius.feature.Resolution.NEGATIVE
import com.mvcoding.expensius.feature.Resolution.POSITIVE
import io.reactivex.Observable

class ResolvableErrorDisplayer(private val view: View) {
    constructor(activity: Activity) : this(activity.findViewById<View>(android.R.id.content))

    fun showError(error: Error): Observable<Resolution> = Observable.defer {
        Observable.create<Resolution> { subscriber ->
            view.snackbar(R.string.error_user_already_linked, Snackbar.LENGTH_LONG)
                    .action(R.string.login) { subscriber.onNext(POSITIVE) }
                    .onDismiss { subscriber.onNext(NEGATIVE) }
                    .show()
        }
    }
}