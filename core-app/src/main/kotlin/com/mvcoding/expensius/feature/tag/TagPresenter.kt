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

package com.mvcoding.expensius.feature.tag

import com.mvcoding.expensius.feature.Presenter
import com.mvcoding.expensius.feature.color
import com.mvcoding.expensius.model.ModelState
import com.mvcoding.expensius.model.ModelState.ARCHIVED
import com.mvcoding.expensius.model.ModelState.NONE
import com.mvcoding.expensius.model.Tag
import com.mvcoding.expensius.model.generateModelId
import rx.Observable
import rx.Observable.combineLatest

class TagPresenter(private var tag: Tag, private val tagsProvider: TagsProvider) : Presenter<TagPresenter.View>() {
    companion object {
        internal val VERY_HIGH_ORDER = 1000
    }

    override fun onViewAttached(view: View) {
        super.onViewAttached(view)

        view.showArchiveEnabled(tag.isStored())
        view.showModelState(tag.modelState)

        val id = tag.let { if (it.isStored()) it.id else generateModelId() }
        val modelState = tag.modelState
        val order = tag.let { if (it.isStored()) it.order else VERY_HIGH_ORDER }

        val titles = view.onTitleChanged().startWith(tag.title).doOnNext { view.showTitle(it) }.map { it.trim() }
        val colors = view.onColorChanged().startWith(if (tag.color == 0) color(0x607d8b) else tag.color).doOnNext { view.showColor(it) }

        val tagObservable = combineLatest(
                titles,
                colors,
                { title, color -> Tag(id, modelState, title, color, order) })
                .doOnNext { tag = it }

        unsubscribeOnDetach(view.onSave()
                .withLatestFrom(tagObservable, { action, tag -> tag })
                .filter { validate(it, view) }
                .doOnNext { tagsProvider.save(setOf(it)) }
                .subscribe { view.displayResult(it) })

        unsubscribeOnDetach(view.onToggleArchive()
                .map { tagWithToggledArchiveState() }
                .doOnNext { tagsProvider.save(setOf(it)) }
                .subscribe { view.displayResult(it) })
    }

    private fun tagWithToggledArchiveState() = tag.withModelState(if (tag.modelState == NONE) ARCHIVED else NONE)

    private fun validate(tag: Tag, view: View) =
            if (tag.title.isBlank()) view.showTitleCannotBeEmptyError().let { false }
            else true

    interface View : Presenter.View {
        fun showTitle(title: String)
        fun showColor(color: Int)
        fun showModelState(modelState: ModelState)
        fun showTitleCannotBeEmptyError()
        fun showArchiveEnabled(archiveEnabled: Boolean)
        fun onTitleChanged(): Observable<String>
        fun onColorChanged(): Observable<Int>
        fun onToggleArchive(): Observable<Unit>
        fun onSave(): Observable<Unit>
        fun displayResult(tag: Tag)
    }
}