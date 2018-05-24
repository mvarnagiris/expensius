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

//abstract class BaseClickableAdapter<T, VH : ViewHolder> : BaseAdapter<T, VH>() {
//    private val positionClickedSubject = PublishSubject.create<Pair<View, Int>>()
//
//    fun itemPositionClicks(): Observable<Int> = positionClickedSubject.map { it.second }
//    fun itemClicks(): Observable<T> = positionClickedSubject.map { getItem(it.second) }
//    fun itemViewClicks(): Observable<Triple<View, Int, T>> = positionClickedSubject.map { Triple(it.first, it.second, getItem(it.second)) }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = onCreateViewHolder(parent, viewType, positionClickedSubject)
//
//    protected abstract fun onCreateViewHolder(parent: ViewGroup, viewType: Int, clickSubject: PublishSubject<Pair<View, Int>>): VH
//}