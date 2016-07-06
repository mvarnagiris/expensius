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

import android.content.Context
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.AttributeSet
import android.widget.LinearLayout
import com.mvcoding.expensius.feature.DragAndDropTouchHelperCallback
import com.mvcoding.expensius.feature.ModelDisplayType
import com.mvcoding.expensius.model.Tag
import kotlinx.android.synthetic.main.view_tags.view.*
import rx.Observable
import rx.lang.kotlin.PublishSubject

class TagsView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        LinearLayout(context, attrs, defStyleAttr), TagsPresenter.View {

    private val adapter = TagsAdapter()
    private val tagMoveSubject = PublishSubject<DragAndDropTouchHelperCallback.ItemMoved>()

    private lateinit var createTagObservable: Observable<Unit>
    private lateinit var presenter: TagsPresenter

    fun init(modelDisplayType: ModelDisplayType, createTagObservable: Observable<Unit>) {
        this.createTagObservable = createTagObservable
        this.presenter = provideTagsPresenter(modelDisplayType)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = adapter
        val touchCallback = DragAndDropTouchHelperCallback(
                adapter,
                tagMoveSubject,
                { current, target -> target.adapterPosition < adapter.itemCount - 1 })
        ItemTouchHelper(touchCallback).attachToRecyclerView(recyclerView)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        presenter.attach(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        presenter.detach(this)
    }

    override fun showModelDisplayType(modelDisplayType: ModelDisplayType) = adapter.let { it.displayType = modelDisplayType }
    override fun showTags(tags: List<Tag>) = adapter.setItems(tags)

    override fun onTagSelected() = adapter.itemPositionClicks().filter { adapter.isTagPosition(it) }.map { adapter.getItem(it) }
    override fun onCreateTag() = createTagObservable
    override fun onDisplayArchivedTags() = adapter.itemPositionClicks().filter { !adapter.isTagPosition(it) }.map { Unit }
    override fun onTagMoved() = tagMoveSubject.map { TagsPresenter.TagMove(it.fromPosition, it.toPosition) }

    override fun displayTagEdit(tag: Tag) = TagActivity.start(context, tag)
    override fun displayArchivedTags() = TagsActivity.startArchived(context)
}