package com.mvcoding.financius.extension

import android.app.Activity
import com.mvcoding.financius.feature.BaseActivity
import rx.Observable

fun Activity.observeFinish(): Observable<Unit> {
    if (this !is BaseActivity) {
        throw IllegalStateException("Activity must extend ${BaseActivity::class.java.name}")
    }

    return this.finishSubject
}