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
import rx.Observable.combineLatest

class QuickTagsPresenter(private val tagsProvider: TagsProvider) : Presenter<QuickTagsPresenter.View>() {
    private val toggledTags = hashMapOf<Tag, Boolean>()

    override fun onAttachView(view: View) {
        super.onAttachView(view)

        val selectedTags = view.onShowSelectedTags().doOnNext { it.forEach { toggledTags.put(it, true) } }
        val allTags = combineLatest(tagsProvider.tags(), selectedTags, {
            providerTags, selectedTags ->
            providerTags.plus(selectedTags.filterNot { providerTags.contains(it) })
        })

        unsubscribeOnDetach(allTags.map { toSelectableTags(it) }.subscribe { view.showSelectableTags(it) })
        unsubscribeOnDetach(view.onSelectableTagToggled()
                                    .doOnNext { toggledTags.put(it.tag, it.isSelected.not()) }
                                    .subscribe { view.showUpdatedSelectableTag(it, it.toggled()) })
    }

    private fun toSelectableTags(tags: List<Tag>) = tags.map { SelectableTag(it, toggledTags.getOrElse(it, { false })) }

    interface View : Presenter.View {
        fun onShowSelectedTags(): Observable<Set<Tag>>
        fun onSelectableTagToggled(): Observable<SelectableTag>
        fun showSelectableTags(selectableTags: List<SelectableTag>)
        fun showUpdatedSelectableTag(oldSelectableTag: SelectableTag, newSelectableTag: SelectableTag)
    }
}