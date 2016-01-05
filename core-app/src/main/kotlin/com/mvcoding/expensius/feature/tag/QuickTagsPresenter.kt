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

package com.mvcoding.expensius.feature.tag

import com.mvcoding.expensius.feature.Presenter
import rx.Observable

class QuickTagsPresenter(private val tagsCache: TagsCache) : Presenter<QuickTagsPresenter.View>() {
    private var toggledTags = hashMapOf<Tag, Boolean>().withDefault { false }

    override fun onAttachView(view: View) {
        super.onAttachView(view)

        unsubscribeOnDetach(tagsCache.tags()
                .map { toSelectableTags(it) }
                .subscribe { view.showSelectableTags(it) })

        unsubscribeOnDetach(view.onSelectableTagToggled()
                .doOnNext { toggledTags.put(it.tag, it.isSelected.not()) }
                .subscribe { view.showUpdatedSelectableTag(it, it.toggled()) })
    }

    private fun toSelectableTags(tags: List<Tag>) = tags.map { SelectableTag(it, toggledTags.getOrImplicitDefault(it)) }

    interface View : Presenter.View {
        fun onSelectableTagToggled(): Observable<SelectableTag>
        fun showSelectableTags(selectableTags: List<SelectableTag>)
        fun showUpdatedSelectableTag(oldSelectableTag: SelectableTag, newSelectableTag: SelectableTag)
    }
}