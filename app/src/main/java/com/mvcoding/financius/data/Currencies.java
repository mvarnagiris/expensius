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

package com.mvcoding.financius.data;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Currency;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.inject.Inject;

public class Currencies {
    @Inject public Currencies() {
    }

    @NonNull public List<String> getCurrencies() {
        final Set<String> currenciesSet = new HashSet<>();
        for (Locale loc : Locale.getAvailableLocales()) {
            try {
                currenciesSet.add(Currency.getInstance(loc).getCurrencyCode());
            } catch (Exception ignore) {
            }
        }

        final List<String> currencies = new ArrayList<>(currenciesSet);
        Collections.sort(currencies);
        return currencies;
    }
}
