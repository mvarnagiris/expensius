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

package com.mvcoding.financius.feature.tag

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import rx.Observable

class TagView : FrameLayout, TagsPresenter.View {
    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onTagSelected(): Observable<Tag> {
        throw UnsupportedOperationException()
    }

    override fun onSave(): Observable<Unit> {
        throw UnsupportedOperationException()
    }

    override fun setDisplayType(displayType: TagsPresenter.DisplayType) {
        throw UnsupportedOperationException()
    }

    override fun showSelectedTags(selectedTags: Set<Tag>) {
        throw UnsupportedOperationException()
    }

    override fun showTags(tags: List<Tag>) {
        throw UnsupportedOperationException()
    }

    override fun startTagEdit(tag: Tag) {
        throw UnsupportedOperationException()
    }

    override fun startResult(tag: Set<Tag>) {
        throw UnsupportedOperationException()
    }
}