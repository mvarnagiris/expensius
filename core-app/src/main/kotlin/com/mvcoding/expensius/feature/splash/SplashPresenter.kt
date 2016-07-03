/*
 * Copyright (C) 2015 Mantas Varnagiris.
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

package com.mvcoding.expensius.feature.splash

import com.mvcoding.expensius.Session
import com.mvcoding.expensius.Settings
import com.mvcoding.mvp.Presenter

class SplashPresenter(private val settings: Settings, private val session: Session) : Presenter<SplashPresenter.View>() {
    override fun onViewAttached(view: View) {
        super.onViewAttached(view)
        view.startMain()
    }

    interface View : Presenter.View {
        fun startMain()
        fun startIntro()
    }
}