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

package com.mvcoding.financius.feature.splash

import com.mvcoding.financius.feature.Presenter
import com.mvcoding.financius.feature.Session
import com.mvcoding.financius.feature.UserSettings

class SplashPresenter(val userSettings: UserSettings, val session: Session) : Presenter<SplashPresenter.View>() {
    override fun onAttachView(view: View) {
        super.onAttachView(view)

        if (session.isLoggedIn() || userSettings.isIntroductionSeen()) {
            view.startOverview()
        } else {
            view.startIntroduction()
        }
    }

    interface View : Presenter.View {
        fun startOverview()
        fun startIntroduction()
    }
}