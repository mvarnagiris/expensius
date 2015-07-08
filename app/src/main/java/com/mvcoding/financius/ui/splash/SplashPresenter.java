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

package com.mvcoding.financius.ui.splash;

import android.support.annotation.NonNull;

import com.mvcoding.financius.api.Session;
import com.mvcoding.financius.ui.ActivityScope;
import com.mvcoding.financius.ui.Presenter;
import com.mvcoding.financius.ui.PresenterView;
import com.mvcoding.financius.ui.UserSettings;

import javax.inject.Inject;

@ActivityScope class SplashPresenter extends Presenter<SplashPresenter.View> {
    private final Session session;
    private final UserSettings userSettings;

    @Inject SplashPresenter(@NonNull Session session, @NonNull UserSettings userSettings) {
        this.session = session;
        this.userSettings = userSettings;
    }

    @Override protected void onViewAttached(@NonNull View view) {
        super.onViewAttached(view);

        if (session.isLoggedIn() || userSettings.isIntroductionSeen()) {
            view.startOverviewAndClose();
        } else {
            view.startIntroductionAndClose();
        }
    }

    public interface View extends PresenterView {
        void startOverviewAndClose();

        void startIntroductionAndClose();
    }
}
