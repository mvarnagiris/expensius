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

import com.mvcoding.expensius.RxSchedulers
import com.mvcoding.expensius.feature.Destroyable
import com.mvcoding.expensius.model.Tag
import com.mvcoding.expensius.model.Transaction
import com.mvcoding.expensius.service.TagsService
import com.mvcoding.mvp.Presenter
import rx.lang.kotlin.toSingletonObservable

class QuickTagsPresenter(
        transaction: Transaction,
        private val tagsService: TagsService,
        private val schedulers: RxSchedulers) : Presenter<QuickTagsPresenter.View>(), Destroyable {

    private val toggledTags = hashMapOf(*transaction.tags.map { it to true }.toTypedArray())

    override fun onViewAttached(view: View) {
        super.onViewAttached(view)

        tagsService.items().withLatestFrom(toggledTags.toSingletonObservable(),
                { tags, toggledTags -> tags.union(toggledTags.keys).toList().sortedBy { it.order } })
                .subscribeOn(schedulers.io)
                .map { it.toSelectableTags() }
                .observeOn(schedulers.main)
                .subscribeUntilDetached { view.showSelectableTags(it) }

        //        val selectedTags = view.onShowSelectedTags().doOnNext { it.forEach { toggledTags.put(it, true) } }
        //        val allTags = combineLatest(tagsService.tags(), selectedTags, {
        //            providerTags, selectedTags ->
        //            providerTags.plus(selectedTags.filterNot { providerTags.contains(it) }).sortedBy { it.order.value }
        //        })
        //
        //        unsubscribeOnDetach(allTags
        //                .subscribeOn(rxSchedulers.io)
        //                .map { toSelectableTags(it) }
        //                .observeOn(rxSchedulers.main)
        //                .subscribe { view.showSelectableTags(it) })
        //        unsubscribeOnDetach(view.onSelectableTagToggled()
        //                .doOnNext { toggledTags.put(it.tag, it.isSelected.not()) }
        //                .subscribe { view.showUpdatedSelectableTag(it, it.toggled()) })
    }

    override fun onDestroy() {
        tagsService.close()
    }

    private fun List<Tag>.toSelectableTags() = map { SelectableTag(it, toggledTags.getOrElse(it, { false })) }

    interface View : Presenter.View {
        //        fun onShowSelectedTags(): Observable<Set<Tag>>
        //        fun onSelectableTagToggled(): Observable<SelectableTag>
        fun showSelectableTags(selectableTags: List<SelectableTag>)
        //        fun showUpdatedSelectableTag(oldSelectableTag: SelectableTag, newSelectableTag: SelectableTag)
    }
}