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
import com.mvcoding.financius.ui.Presenter;
import com.mvcoding.financius.ui.PresenterView;

class SplashPresenter extends Presenter<SplashPresenter.View> {
    private final Session session;

    SplashPresenter(@NonNull Session session) {
        this.session = session;
    }

    @Override protected void onViewAttached(@NonNull View view) {
        super.onViewAttached(view);

        if (session.isLoggedIn()) {
            view.startOverviewAndClose();
        } else {
            view.startTutorialAndClose();
        }
    }

    public interface View extends PresenterView {
        void startOverviewAndClose();

        void startTutorialAndClose();
    }
}
