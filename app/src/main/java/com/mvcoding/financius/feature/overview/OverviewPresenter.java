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

package com.mvcoding.financius.feature.overview;

import android.support.annotation.NonNull;

import com.mvcoding.financius.feature.ActivityScope;
import com.mvcoding.financius.feature.Presenter;
import com.mvcoding.financius.feature.PresenterView;

import javax.inject.Inject;

import rx.Observable;

@ActivityScope class OverviewPresenter extends Presenter<OverviewPresenter.View> {
    @Inject OverviewPresenter() {
    }

    @Override protected void onViewAttached(@NonNull View view) {
        super.onViewAttached(view);
        unsubscribeOnDetach(view.onNewTransactionClick().subscribe(onClickEvent -> view.startNewTransaction()));
    }

    public interface View extends PresenterView {
        @NonNull Observable<Object> onNewTransactionClick();
        void startNewTransaction();
    }
}
