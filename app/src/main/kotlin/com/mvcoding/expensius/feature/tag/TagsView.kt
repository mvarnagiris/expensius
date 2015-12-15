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
import android.support.design.widget.Snackbar
import android.support.design.widget.Snackbar.LENGTH_LONG
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.AttributeSet
import android.widget.Button
import android.widget.LinearLayout
import com.jakewharton.rxbinding.support.v7.widget.itemClicks
import com.jakewharton.rxbinding.view.clicks
import com.memoizrlabs.ShankModuleInitializer.initializeModules
import com.mvcoding.expensius.R
import com.mvcoding.expensius.extension.provideActivityScopedNamedSingleton
import com.mvcoding.expensius.extension.snackbar
import com.mvcoding.expensius.feature.tag.TagsPresenter.DisplayType.ARCHIVED
import com.mvcoding.expensius.feature.tag.TagsPresenter.DisplayType.VIEW
import rx.lang.kotlin.PublishSubject

class TagsView : LinearLayout, TagsPresenter.View {
    private val toolbar by lazy { findViewById(R.id.toolbar) as Toolbar }
    private val recyclerView by lazy { findViewById(R.id.recyclerView) as RecyclerView }
    private val buttonBarView by lazy { findViewById(R.id.buttonBarView) }
    private val saveButton by lazy { findViewById(R.id.saveButton) as Button }
    private var snackbar: Snackbar? = null

    private val archiveTagObservable = PublishSubject<Tag>()
    private val commitArchiveObservable = PublishSubject<Unit>()
    private val undoArchiveObservable = PublishSubject<Unit>()

    private val presenter by lazy { provideActivityScopedNamedSingleton(TagsPresenter::class, adapter.displayType.name) }
    private val adapter = TagsAdapter()

    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    fun init(displayType: TagsPresenter.DisplayType, selectedTags: Set<Tag>) {
        initializeModules(TagsModule(displayType, selectedTags))
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = adapter

        val itemTouchCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, target: RecyclerView.ViewHolder?) = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                archiveTagObservable.onNext(adapter.getItem(viewHolder.adapterPosition))
            }
        }
        val itemTouchHelper = ItemTouchHelper(itemTouchCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        presenter?.onAttachView(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        if (snackbar?.isShown ?: false) {
            commitArchiveObservable.onNext(Unit)
        }

        presenter?.onDetachView(this)
    }

    override fun setDisplayType(displayType: TagsPresenter.DisplayType) {
        adapter.displayType = displayType
        buttonBarView.visibility = if (displayType == VIEW) GONE else VISIBLE
    }

    override fun setSelectedTags(selectedTags: Set<Tag>) {
        adapter.selectedTags = selectedTags
    }

    override fun setTagSelected(tag: Tag, selected: Boolean) = adapter.setTagSelected(tag, selected)

    override fun setTags(tags: List<Tag>) {
        adapter.setItems(tags)
    }

    override fun removeTag(tag: Tag) {
        adapter.remove(tag)
    }

    override fun insertTag(tag: Tag, position: Int) {
        adapter.insert(tag, position)
    }

    override fun showUndoForRemovedTag() {
        dismissSnackbarIfVisible()
        snackbar = snackbar(if (adapter.displayType == ARCHIVED) R.string.tag_restored else R.string.tag_archived, LENGTH_LONG)
                .action(R.string.undo, Runnable { undoArchiveObservable.onNext(Unit) })
                .onDismiss(Runnable { commitArchiveObservable.onNext(Unit) })
                .show()
    }

    override fun hideUndoForRemovedTag() {
        dismissSnackbarIfVisible()
    }

    override fun onTagSelected() = adapter.onTagSelected()

    override fun onCreateTag() = toolbar.itemClicks().map { it.itemId }.filter { it == R.id.action_create }.map { Unit }

    override fun onSave() = saveButton.clicks()

    override fun onArchivedTags() = toolbar.itemClicks().map { it.itemId }.filter { it == R.id.action_archived }.map { Unit }

    override fun onRemoveTag() = archiveTagObservable

    override fun onCommitRemove() = commitArchiveObservable

    override fun onUndoRemove() = undoArchiveObservable

    override fun startTagEdit(tag: Tag) {
        TagActivity.start(context, tag)
    }

    override fun startResult(tags: Set<Tag>) {
        throw UnsupportedOperationException()
    }

    override fun startArchivedTags() {
        TagsActivity.startArchived(context)
    }

    private fun dismissSnackbarIfVisible() {
        snackbar?.dismiss()
        snackbar = null
    }
}