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

package com.mvcoding.financius.ui.introduction;

import android.support.annotation.NonNull;

import com.mvcoding.financius.UserSettings;
import com.mvcoding.financius.ui.ActivityScope;
import com.mvcoding.financius.ui.Presenter;
import com.mvcoding.financius.ui.PresenterView;

import javax.inject.Inject;

import rx.Observable;

@ActivityScope class IntroductionPresenter extends Presenter<IntroductionPresenter.View> {
    private final UserSettings userSettings;

    @Inject IntroductionPresenter(@NonNull UserSettings userSettings) {
        this.userSettings = userSettings;
    }

    @Override protected void onViewAttached(@NonNull View view) {
        super.onViewAttached(view);

        unsubscribeOnDetach(view.onSkipLogin()
                                    .doOnNext(onClickEvent -> setIntroductionSeen())
                                    .subscribe(onClickEvent -> view.startOverviewAndClose()));

        unsubscribeOnDetach(view.onLogin()
                                    .doOnNext(onClickEvent -> setIntroductionSeen())
                                    .subscribe(onClickEvent -> view.startLoginAndClose()));
    }

    private void setIntroductionSeen() {
        userSettings.setIsIntroductionSeen(true);
    }

    public interface View extends PresenterView {
        @NonNull Observable<Object> onSkipLogin();
        @NonNull Observable<Object> onLogin();
        void startOverviewAndClose();
        void startLoginAndClose();
    }
}
