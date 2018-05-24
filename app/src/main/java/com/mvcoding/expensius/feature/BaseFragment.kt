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

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.memoizrlabs.Scope
import com.mvcoding.expensius.shank.Scoped
import com.mvcoding.mvp.Presenter
import java.io.Closeable
import java.util.*

private const val STATE_SCOPE_ID = "FRAGMENT_STATE_SCOPE_ID"

abstract class BaseFragment : Fragment(), Scoped {

    private lateinit var scopeUUID: UUID

    private val finalAction: (Any) -> Unit = {
        when (it) {
            is Closeable -> it.close()
        }
    }

    override val scope: Scope by lazy { Scope.scope(scopeUUID) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scopeUUID = savedInstanceState?.getSerializable(STATE_SCOPE_ID) as UUID? ?: UUID.randomUUID()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(STATE_SCOPE_ID, scopeUUID)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isRemoving || activity?.isFinishing == true) {
            scope.clearWithFinalAction { finalAction(it) }
        }
    }

    protected fun <T : Presenter.View> T.addPresenter(presenter: Presenter<T>) {
        lifecycle.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
            fun onResume() {
                presenter attach this@addPresenter
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
            fun onPause() {
                presenter detach this@addPresenter
            }
        })
    }
}