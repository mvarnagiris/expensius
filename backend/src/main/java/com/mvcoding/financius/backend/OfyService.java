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

package com.mvcoding.financius.backend;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.impl.translate.opt.BigDecimalLongTranslatorFactory;
import com.mvcoding.financius.backend.entity.Place;
import com.mvcoding.financius.backend.entity.Tag;
import com.mvcoding.financius.backend.entity.Transaction;
import com.mvcoding.financius.backend.entity.UserAccount;

public class OfyService {
    public static Objectify ofy() {
        return ObjectifyService.ofy();
    }

    public static ObjectifyFactory factory() {
        return ObjectifyService.factory();
    }

    static {
        factory().getTranslators().add(new BigDecimalLongTranslatorFactory());
        ObjectifyService.register(UserAccount.class);
        ObjectifyService.register(Tag.class);
        ObjectifyService.register(Place.class);
        ObjectifyService.register(Transaction.class);
    }
}
