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

import com.mvcoding.expensius.feature.color
import com.mvcoding.expensius.model.*
import com.mvcoding.expensius.model.ModelState.ARCHIVED
import com.mvcoding.expensius.model.ModelState.NONE
import com.mvcoding.expensius.model.NullModels.noColor
import com.mvcoding.expensius.model.NullModels.noTagId
import com.mvcoding.mvp.Presenter
import rx.Observable
import rx.Observable.combineLatest

class TagPresenter(private var tag: Tag/*, private val tagsWriteService: TagsWriteService*/) : Presenter<TagPresenter.View>() {
    companion object {
        internal val VERY_HIGH_ORDER = 1000
    }

    override fun onViewAttached(view: View) {
        super.onViewAttached(view)

        view.showArchiveEnabled(tag.isExisting())
        view.showModelState(tag.modelState)

        val titles = view.titleChanges().map { Title(it) }.startWith(tag.title).doOnNext { view.showTitle(it) }.map { it.copy(text = it.text.trim()) }
        val colors = view.colorChanges().map { Color(it) }.startWith(tagColorOrDefault()).doOnNext { view.showColor(it) }
        val order = if (tag.isExisting()) tag.order else Order(VERY_HIGH_ORDER)

        val tag = combineLatest(titles, colors, { title, color -> tag.copy(title = title, color = color, order = order) }).doOnNext { tag = it }

//        view.saveRequests()
//                .withLatestFrom(tag, { unit, tag -> tag })
//                .filter { validate(it, view) }
//                .switchMap { if (it.isExisting()) tagsWriteService.saveTags(setOf(it)) else tagsWriteService.createTags(setOf(it.toCreateTag())) }
//                .subscribeUntilDetached { view.displayResult() }

//        view.archiveToggles()
//                .map { tagWithToggledArchiveState() }
//                .switchMap { tagsWriteService.saveTags(setOf(it)) }
//                .subscribeUntilDetached { view.displayResult() }
    }

    private fun tagColorOrDefault() = if (tag.color == noColor) Color(color(0x607d8b)) else tag.color
    private fun tagWithToggledArchiveState() = tag.copy(modelState = if (tag.modelState == NONE) ARCHIVED else NONE)

    private fun validate(tag: Tag, view: View) =
            if (tag.title.text.isBlank()) view.showTitleCannotBeEmptyError().let { false }
            else true

    private fun Tag.isExisting() = this.tagId != noTagId
    private fun Tag.toCreateTag() = CreateTag(title, color, order)

    interface View : Presenter.View {
        fun titleChanges(): Observable<String>
        fun colorChanges(): Observable<Int>
        fun archiveToggles(): Observable<Unit>
        fun saveRequests(): Observable<Unit>

        fun showTitle(title: Title)
        fun showColor(color: Color)
        fun showModelState(modelState: ModelState)
        fun showTitleCannotBeEmptyError()
        fun showArchiveEnabled(archiveEnabled: Boolean)

        fun displayResult()
    }
}