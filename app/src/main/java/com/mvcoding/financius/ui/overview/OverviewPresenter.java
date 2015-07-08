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

package com.mvcoding.financius.ui.overview;

import android.support.annotation.NonNull;

import com.mvcoding.financius.ui.ActivityScope;
import com.mvcoding.financius.ui.Presenter;
import com.mvcoding.financius.ui.PresenterView;

import javax.inject.Inject;

import rx.Observable;
import rx.android.view.OnClickEvent;

@ActivityScope class OverviewPresenter extends Presenter<OverviewPresenter.View> {
    @Inject OverviewPresenter() {
    }

    @Override protected void onViewAttached(@NonNull View view) {
        super.onViewAttached(view);
        unsubscribeOnDetach(view.onNewTransactionClick().subscribe(onClickEvent -> view.startNewTransaction()));
    }

    public interface View extends PresenterView {
        @NonNull Observable<OnClickEvent> onNewTransactionClick();

        void startNewTransaction();
    }
}
