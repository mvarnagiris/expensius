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

package com.mvcoding.financius.ui.transaction.calculator;

import android.database.Observable;
import android.support.annotation.NonNull;

import com.mvcoding.financius.ui.Presenter;
import com.mvcoding.financius.ui.PresenterView;

import rx.android.view.OnClickEvent;

class CalculatorPresenter extends Presenter<CalculatorPresenter.View> {

    public interface View extends PresenterView {
        @NonNull Observable<OnClickEvent> on0Click();

        @NonNull Observable<OnClickEvent> on1Click();

        @NonNull Observable<OnClickEvent> on2Click();

        @NonNull Observable<OnClickEvent> on3Click();

        @NonNull Observable<OnClickEvent> on4Click();

        @NonNull Observable<OnClickEvent> on5Click();

        @NonNull Observable<OnClickEvent> on6Click();

        @NonNull Observable<OnClickEvent> on7Click();

        @NonNull Observable<OnClickEvent> on8Click();

        @NonNull Observable<OnClickEvent> on9Click();

        @NonNull Observable<OnClickEvent> onDecimalClick();

        @NonNull Observable<OnClickEvent> onEqualsClick();

        @NonNull Observable<OnClickEvent> onDivideClick();

        @NonNull Observable<OnClickEvent> onMultiplyClick();

        @NonNull Observable<OnClickEvent> onSubtractClick();

        @NonNull Observable<OnClickEvent> onAddClick();

        @NonNull Observable<OnClickEvent> onDeleteClick();
    }
}
