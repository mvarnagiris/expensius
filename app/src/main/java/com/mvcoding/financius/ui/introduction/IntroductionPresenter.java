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

import com.mvcoding.financius.ui.ActivityScope;
import com.mvcoding.financius.ui.Presenter;
import com.mvcoding.financius.ui.PresenterView;
import com.mvcoding.financius.ui.UserSettings;

import javax.inject.Inject;

import rx.Observable;
import rx.android.view.OnClickEvent;

@ActivityScope class IntroductionPresenter extends Presenter<IntroductionPresenter.View> {
    private final UserSettings userSettings;

    @Inject IntroductionPresenter(@NonNull UserSettings userSettings) {
        this.userSettings = userSettings;
    }

    @Override protected void onViewAttached(@NonNull View view) {
        super.onViewAttached(view);

        unsubscribeOnDetach(view.onSkipLoginClick()
                                    .doOnNext(onClickEvent -> setIntroductionSeen())
                                    .subscribe(onClickEvent -> view.startOverviewAndClose()));

        unsubscribeOnDetach(view.onLoginClick()
                                    .doOnNext(onClickEvent -> setIntroductionSeen())
                                    .subscribe(onClickEvent -> view.startLoginAndClose()));
    }

    private void setIntroductionSeen() {
        userSettings.setIsIntroductionSeen(true);
    }

    public interface View extends PresenterView {
        @NonNull Observable<OnClickEvent> onSkipLoginClick();

        @NonNull Observable<OnClickEvent> onLoginClick();

        void startOverviewAndClose();

        void startLoginAndClose();
    }
}
