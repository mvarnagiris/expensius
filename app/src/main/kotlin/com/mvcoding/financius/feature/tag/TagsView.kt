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
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.LinearLayout.GONE
import android.widget.LinearLayout.VISIBLE
import com.jakewharton.rxbinding.view.clicks
import com.memoizrlabs.ShankModuleInitializer
import com.mvcoding.financius.shankWithBoundScope
import kotlinx.android.synthetic.view_tags.view.buttonBarView
import kotlinx.android.synthetic.view_tags.view.recyclerView
import kotlinx.android.synthetic.view_tags.view.saveButton
import rx.Observable

class TagsView : LinearLayout, TagsPresenter.View {
    private val presenter: TagsPresenter? by lazy { shankWithBoundScope(TagsView::class, context)?.provide(TagsPresenter::class.java) }
    private val adapter = TagsAdapter()

    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    fun init(displayType: TagsPresenter.DisplayType, selectedTags: Set<Tag>) {
        ShankModuleInitializer.initializeModules(TagsModule(displayType, selectedTags))
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

    override fun onSave(): Observable<Unit> = saveButton.clicks()

    override fun setDisplayType(displayType: TagsPresenter.DisplayType) {
        adapter.displayType = displayType
        buttonBarView.visibility = if (displayType == TagsPresenter.DisplayType.VIEW) GONE else VISIBLE
    }

    override fun showSelectedTags(selectedTags: Set<Tag>) {
        adapter.selectedTags = selectedTags
    }

    override fun showTagSelected(tag: Tag, selected: Boolean) = adapter.setTagSelected(tag, selected)

    override fun showTags(tags: List<Tag>) {
        adapter.items = tags
    }

    override fun startTagEdit(tag: Tag) {
        TagActivity.start(context)
    }

    override fun startResult(tag: Set<Tag>) {
        throw UnsupportedOperationException()
    }
}