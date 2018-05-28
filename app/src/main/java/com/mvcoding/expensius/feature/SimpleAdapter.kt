/*
 * Copyright (C) 2018 Mantas Varnagiris.
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

package com.mvcoding.expensius.feature

import android.view.ViewGroup
import com.mvcoding.expensius.extension.inflate

class SimpleAdapter<ITEM>(private val layoutId: Int, private val bind: (ViewHolder, ITEM) -> Unit) : BaseAdapter<ITEM, ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(parent.inflate(layoutId))
    override fun onBindViewHolder(holder: ViewHolder, position: Int) = bind(holder, getItem(position))
}