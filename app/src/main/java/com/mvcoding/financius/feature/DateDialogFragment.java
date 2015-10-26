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

package com.mvcoding.financius.feature;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.widget.DatePicker;

import com.mvcoding.financius.AppOld;
import com.mvcoding.financius.util.rx.RxBus;

import org.joda.time.DateTime;

import javax.inject.Inject;

import lombok.Data;
import lombok.EqualsAndHashCode;
import rx.Observable;

public class DateDialogFragment extends BaseDialogFragment implements DatePickerDialog.OnDateSetListener {
    private static final String ARG_YEAR = "ARG_YEAR";
    private static final String ARG_MONTH_OF_YEAR = "ARG_MONTH_OF_YEAR";
    private static final String ARG_DAY_OF_MONTH = "ARG_DAY_OF_MONTH";

    @Inject RxBus rxBus;

    public static Observable<DateDialogResult> show(@NonNull FragmentManager fragmentManager, int requestCode, @NonNull RxBus rxBus, long timestamp) {
        final DateTime date = new DateTime(timestamp);
        return show(fragmentManager, requestCode, rxBus, date.getYear(), date.getMonthOfYear(), date.getDayOfMonth());
    }

    public static Observable<DateDialogResult> show(FragmentManager fragmentManager, int requestCode, @NonNull RxBus rxBus, int year, int monthOfYear, int dayOfMonth) {
        final Bundle args = new Bundle();
        args.putInt(ARG_REQUEST_CODE, requestCode);
        args.putInt(ARG_YEAR, year);
        args.putInt(ARG_MONTH_OF_YEAR, monthOfYear);
        args.putInt(ARG_DAY_OF_MONTH, dayOfMonth);

        final DateDialogFragment fragment = new DateDialogFragment();
        fragment.setArguments(args);
        fragment.show(fragmentManager, DateDialogFragment.class.getName());

        return rxBus.observe(DateDialogResult.class);
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppOld.with(getActivity()).getComponent().inject(this);
    }

    @NonNull @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
        final int year = getArguments().getInt(ARG_YEAR);
        final int monthOfYear = getArguments().getInt(ARG_MONTH_OF_YEAR);
        final int dayOfMonth = getArguments().getInt(ARG_DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this, year, monthOfYear - 1, dayOfMonth);
    }

    @Override public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        rxBus.send(new DateDialogResult(getArguments().getInt(ARG_REQUEST_CODE), year, monthOfYear + 1, dayOfMonth));
    }

    @Data @EqualsAndHashCode(callSuper = true) public static class DateDialogResult extends DialogResult {
        private final int year;
        private final int monthOfYear;
        private final int dayOfMonth;

        private DateDialogResult(int requestCode, int year, int monthOfYear, int dayOfMonth) {
            super(requestCode);
            this.year = year;
            this.monthOfYear = monthOfYear;
            this.dayOfMonth = dayOfMonth;
        }
    }
}
