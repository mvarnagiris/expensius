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

package com.mvcoding.financius;

import android.content.Context;
import android.support.annotation.NonNull;

import com.mvcoding.financius.util.PreferencesUtils;

import java.util.Currency;
import java.util.Locale;

public class UserSettings {
    transient Context context;

    private boolean isIntroductionSeen;
    private String currency;

    private static void persist(@NonNull Context context, @NonNull UserSettings userSettings) {
        PreferencesUtils.put(context.getApplicationContext(), UserSettings.class.getName(), userSettings);
    }

    public boolean isIntroductionSeen() {
        return isIntroductionSeen;
    }

    public void setIsIntroductionSeen(boolean isIntroductionSeen) {
        this.isIntroductionSeen = isIntroductionSeen;
        persist(context, this);
    }

    public String getCurrency() {
        if (currency == null || currency.length() != 3) {
            try {
                currency = Currency.getInstance(Locale.getDefault()).getCurrencyCode();
            } catch (Exception e) {
                currency = "USD";
                e.printStackTrace();
            }
            persist(context, this);
        }
        return currency;
    }

    public void setCurrency(@NonNull String currency) {
        this.currency = currency;
        persist(context, this);
    }
}
