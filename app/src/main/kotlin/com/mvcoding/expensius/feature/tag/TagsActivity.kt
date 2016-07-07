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
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View.GONE
import android.view.View.VISIBLE
import com.mvcoding.expensius.R
import com.mvcoding.expensius.feature.ActivityStarter
import com.mvcoding.expensius.feature.BaseActivity
import com.mvcoding.expensius.feature.DragAndDropTouchHelperCallback
import com.mvcoding.expensius.feature.ModelDisplayType
import com.mvcoding.expensius.feature.ModelDisplayType.VIEW_ARCHIVED
import com.mvcoding.expensius.feature.ModelDisplayType.VIEW_NOT_ARCHIVED
import com.mvcoding.expensius.feature.tag.TagsPresenter.TagMove
import com.mvcoding.expensius.model.Tag
import kotlinx.android.synthetic.main.activity_tags.*
import rx.Observable
import rx.lang.kotlin.PublishSubject

class TagsActivity : BaseActivity(), TagsPresenter.View {
    companion object {
        private const val EXTRA_DISPLAY_TYPE = "EXTRA_DISPLAY_TYPE"

        fun startView(context: Context): Unit = ActivityStarter(context, TagsActivity::class)
                .extra(EXTRA_DISPLAY_TYPE, VIEW_NOT_ARCHIVED)
                .start()

        fun startArchived(context: Context): Unit = ActivityStarter(context, TagsActivity::class)
                .extra(EXTRA_DISPLAY_TYPE, VIEW_ARCHIVED)
                .start()
    }

    private val presenter by lazy { provideTagsPresenter(intent.getSerializableExtra(EXTRA_DISPLAY_TYPE) as ModelDisplayType) }
    private val tagMoveSubject by lazy { PublishSubject<DragAndDropTouchHelperCallback.ItemMoved>() }
    private val adapter by lazy { TagsAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tags)

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        val touchCallback = DragAndDropTouchHelperCallback(
                adapter,
                tagMoveSubject,
                { current, target -> target.adapterPosition < adapter.itemCount - 1 })
        ItemTouchHelper(touchCallback).attachToRecyclerView(recyclerView)
    }

    override fun onStart() {
        super.onStart()
        presenter.attach(this)
    }

    override fun onStop() {
        super.onStop()
        presenter.detach(this)
    }

    override fun showModelDisplayType(modelDisplayType: ModelDisplayType) {
        supportActionBar?.title = if (modelDisplayType == VIEW_ARCHIVED) getString(R.string.archived_tags) else getString(R.string.tags)
        adapter.modelDisplayType = modelDisplayType
    }

    override fun archivedTagsRequests(): Observable<Unit> = adapter.itemPositionClicks().filter { !adapter.isTagPosition(it) }.map { Unit }
    override fun tagMoves(): Observable<TagMove> = tagMoveSubject.map { TagMove(it.fromPosition, it.toPosition) }
    override fun showItems(items: List<Tag>): Unit = adapter.set(items)
    override fun showAddedItems(position: Int, items: List<Tag>): Unit = adapter.add(position, items)
    override fun showChangedItems(position: Int, items: List<Tag>): Unit = adapter.change(position, items)
    override fun showRemovedItems(position: Int, items: List<Tag>): Unit = adapter.remove(position, items.size)
    override fun showMovedItem(fromPosition: Int, toPosition: Int, item: Tag): Unit = adapter.move(fromPosition, toPosition)
    override fun showLoading(): Unit = with(progressBar) { visibility = VISIBLE }
    override fun hideLoading(): Unit = with(progressBar) { visibility = GONE }
    override fun displayArchivedTags(): Unit = TagsActivity.startArchived(this)
}