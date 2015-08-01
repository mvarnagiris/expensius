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

package com.mvcoding.financius.data.paging;

import java.util.List;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor public class PageResult<T> {
    @Getter @NonNull private final Page page;
    @Getter @NonNull private final List<T> items;
    @Getter private final boolean isDataInvalidated;

    public boolean hasPrevious() {
        return page.getStart() > 0;
    }

    public boolean hasNext() {
        return page.getSize() == items.size();
    }
}
