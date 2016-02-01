/*
 * Copyright (C) 2016 Mantas Varnagiris.
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
import rx.Subscription
import rx.lang.kotlin.PublishSubject
import rx.subjects.PublishSubject

abstract class BaseClickableAdapter<T, VH : ClickableViewHolder<*>> : BaseAdapter<T, VH>() {
    private val itemClickedSubject = PublishSubject<T>()
    private val positionClickedSubject = PublishSubject<Int>()
    private lateinit var subscription: Subscription

    override fun onViewAttachedToWindow(holder: VH) {
        super.onViewAttachedToWindow(holder)
        subscription = positionClickedSubject.map { getItem(it) }.subscribe(itemClickedSubject)
    }

    override fun onViewDetachedFromWindow(holder: VH) {
        super.onViewDetachedFromWindow(holder)
        subscription.unsubscribe()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = onCreateViewHolder(parent, viewType, positionClickedSubject)

    fun itemClicks() = itemClickedSubject.asObservable()

    protected abstract fun onCreateViewHolder(parent: ViewGroup, viewType: Int, positionClickedSubject: PublishSubject<Int>): VH
}