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
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.AttributeSet
import android.widget.Button
import android.widget.LinearLayout
import com.jakewharton.rxbinding.support.v7.widget.itemClicks
import com.jakewharton.rxbinding.view.clicks
import com.memoizrlabs.ShankModuleInitializer.initializeModules
import com.mvcoding.financius.R
import com.mvcoding.financius.extension.provideActivityScopedSingleton
import com.mvcoding.financius.feature.tag.TagsPresenter.DisplayType.VIEW
import rx.Observable

class TagsView : LinearLayout, TagsPresenter.View {
    private val toolbar by lazy { findViewById(R.id.toolbar) as Toolbar }
    private val recyclerView by lazy { findViewById(R.id.recyclerView) as RecyclerView }
    private val buttonBarView by lazy { findViewById(R.id.buttonBarView) }
    private val saveButton by lazy { findViewById(R.id.saveButton) as Button }

    private val presenter by lazy { provideActivityScopedSingleton(TagsPresenter::class) }
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
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        presenter?.onAttachView(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        presenter?.onDetachView(this)
    }

    override fun onTagSelected(): Observable<Tag> = adapter.onTagSelected()

    override fun onCreateTag(): Observable<Unit> {
        return toolbar.itemClicks().map { it.itemId }.filter { it == R.id.action_create }.map { Unit }
    }

    override fun onSave(): Observable<Unit> = saveButton.clicks()

    override fun setDisplayType(displayType: TagsPresenter.DisplayType) {
        adapter.displayType = displayType
        buttonBarView.visibility = if (displayType == VIEW) GONE else VISIBLE
    }

    override fun showSelectedTags(selectedTags: Set<Tag>) {
        adapter.selectedTags = selectedTags
    }

    override fun showTagSelected(tag: Tag, selected: Boolean) = adapter.setTagSelected(tag, selected)

    override fun showTags(tags: List<Tag>) {
        adapter.items = tags
    }

    override fun startTagEdit(tag: Tag) {
        TagActivity.start(context, tag)
    }

    override fun startResult(tag: Set<Tag>) {
        throw UnsupportedOperationException()
    }
}