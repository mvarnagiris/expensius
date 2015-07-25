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

package com.mvcoding.financius.ui;

import android.support.v4.app.DialogFragment;

import com.mvcoding.financius.util.rx.RxBus;

import javax.inject.Inject;

import lombok.Data;

public class BaseDialogFragment extends DialogFragment {
    protected static final String ARG_REQUEST_CODE = "ARG_REQUEST_CODE";

    @Inject RxBus rxBus;

    @Data public static class DialogResult {
        private final int requestCode;

        public DialogResult(int requestCode) {
            this.requestCode = requestCode;
        }
    }
}
