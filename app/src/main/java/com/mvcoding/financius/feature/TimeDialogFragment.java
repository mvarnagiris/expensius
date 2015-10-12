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

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.widget.TimePicker;

import com.mvcoding.financius.App;
import com.mvcoding.financius.util.rx.RxBus;

import org.joda.time.DateTime;

import javax.inject.Inject;

import lombok.Data;
import lombok.EqualsAndHashCode;
import rx.Observable;

public class TimeDialogFragment extends BaseDialogFragment implements TimePickerDialog.OnTimeSetListener {
    private static final String ARG_HOUR_OF_DAY = "ARG_HOUR_OF_DAY";
    private static final String ARG_MINUTE_OF_HOUR = "ARG_MINUTE_OF_HOUR";

    @Inject RxBus rxBus;

    public static Observable<TimeDialogResult> show(@NonNull FragmentManager fragmentManager, int requestCode, @NonNull RxBus rxBus, long timestamp) {
        final DateTime date = new DateTime(timestamp);
        return show(fragmentManager, requestCode, rxBus, date.getHourOfDay(), date.getMinuteOfHour());
    }

    public static Observable<TimeDialogResult> show(FragmentManager fragmentManager, int requestCode, @NonNull RxBus rxBus, int hourOfDay, int minuteOfHour) {
        final Bundle args = new Bundle();
        args.putInt(ARG_REQUEST_CODE, requestCode);
        args.putInt(ARG_HOUR_OF_DAY, hourOfDay);
        args.putInt(ARG_MINUTE_OF_HOUR, minuteOfHour);

        final TimeDialogFragment fragment = new TimeDialogFragment();
        fragment.setArguments(args);
        fragment.show(fragmentManager, TimeDialogFragment.class.getName());

        return rxBus.observe(TimeDialogResult.class);
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.with(getActivity()).getComponent().inject(this);
    }

    @NonNull @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
        final int hourOfDay = getArguments().getInt(ARG_HOUR_OF_DAY);
        final int minuteOfHour = getArguments().getInt(ARG_MINUTE_OF_HOUR);

        return new TimePickerDialog(getActivity(), this, hourOfDay, minuteOfHour, true);
    }

    @Override public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        rxBus.send(new TimeDialogResult(getArguments().getInt(ARG_REQUEST_CODE), hourOfDay, minute));
    }

    @Data @EqualsAndHashCode(callSuper = true) public static class TimeDialogResult extends DialogResult {
        private final int hourOfDay;
        private final int minuteOfHour;

        private TimeDialogResult(int requestCode, int hourOfDay, int minuteOfHour) {
            super(requestCode);
            this.hourOfDay = hourOfDay;
            this.minuteOfHour = minuteOfHour;
        }
    }
}
