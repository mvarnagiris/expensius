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

import com.mvcoding.financius.feature.Presenter
import rx.Observable

class TagsPresenter(val displayType: TagsPresenter.DisplayType = TagsPresenter.DisplayType.VIEW, val selectedTags: Set<Tag>? = null) : Presenter<TagsPresenter.View>() {
    override fun onAttachView(view: View) {
        super.onAttachView(view)

        view.setDisplayType(displayType)
        if (displayType === DisplayType.SINGLE_CHOICE || displayType === DisplayType.MULTI_CHOICE) {
            view.showSelectedTags(selectedTags.orEmpty())
        }
    }

    interface View : Presenter.View {
        fun onTagSelected(): Observable<Tag>
        fun setDisplayType(displayType: DisplayType)
        fun showSelectedTags(selectedTags: Set<Tag>)
    }

    enum class DisplayType {
        VIEW, SELECT, SINGLE_CHOICE, MULTI_CHOICE
    }
}