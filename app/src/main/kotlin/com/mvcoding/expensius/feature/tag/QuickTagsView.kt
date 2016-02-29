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

import android.content.Context
import android.support.v4.content.ContextCompat.getColor
import android.util.AttributeSet
import android.view.View
import android.view.View.MeasureSpec.getSize
import android.view.ViewGroup
import com.mvcoding.expensius.R
import com.mvcoding.expensius.extension.provideActivityScopedSingleton
import com.mvcoding.expensius.feature.tag.QuickTagView.QuickTag
import com.mvcoding.expensius.model.Tag
import rx.lang.kotlin.BehaviourSubject
import rx.lang.kotlin.PublishSubject
import java.lang.Math.max

class QuickTagsView : ViewGroup, QuickTagsPresenter.View {
    private val presenter by lazy { provideActivityScopedSingleton(QuickTagsPresenter::class) }

    private val selectedTagsSubject  by lazy { PublishSubject<Set<Tag>>() }
    private val selectableTagToggledSubject by lazy { PublishSubject<SelectableTag>() }
    private val selectedTagsUpdatedSubject by lazy { BehaviourSubject<Set<Tag>>() }
    private val selectableTags = arrayListOf<SelectableTag>()

    private var allowShowSelectedTags = true

    constructor(context: Context?) : this(context, null)

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun generateLayoutParams(attrs: AttributeSet?) = MarginLayoutParams(context, attrs)

    override fun onFinishInflate() {
        super.onFinishInflate()
        if (isInEditMode) {
            setQuickTags(listOf(QuickTag("Non-essential", getColor(context, R.color.red_500)),
                                QuickTag("Food", getColor(context, R.color.lime_500)),
                                QuickTag("Going out", getColor(context, R.color.blue_500)),
                                QuickTag("Some other very long tag that can take up a few lines, but how?",
                                         getColor(context, R.color.orange_500))))

            getChildAt(1).isSelected = true
            getChildAt(2).isSelected = true
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        presenter.onViewAttached(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        presenter.onViewDetached(this)
    }

    fun showSelectedTags(tags: Set<Tag>) {
        if (allowShowSelectedTags) {
            selectedTagsUpdatedSubject.onNext(tags)
        }
    }

    fun selectedTagsChanges() = selectedTagsSubject.asObservable()

    private fun setQuickTags(quickTags: List<QuickTag>) {
        removeAllViews()
        quickTags.forEachIndexed { index, quickTag ->
            val quickTagView = QuickTagView.inflate(this)
            addView(quickTagView)
            quickTagView.setQuickTag(quickTag)
            quickTagView.setOnClickListener { selectableTagToggledSubject.onNext(selectableTags[index]) };
        }
        requestLayout()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = getSize(widthMeasureSpec) - paddingLeft - paddingRight
        var measuredHeight = paddingTop
        var usedWidth = 0;
        var currentLineHeight = 0;

        0.rangeTo(childCount - 1).forEach {
            val child = getChildAt(it)
            val childLayoutParams = child.layoutParams as MarginLayoutParams
            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0)

            val totalChildWidth = totalChildWidth(child, childLayoutParams)
            val totalChildHeight = totalChildHeight(child, childLayoutParams)

            if (childExceedsAvailableWidth(totalChildWidth, usedWidth, width)) {
                measuredHeight += currentLineHeight
                currentLineHeight = totalChildHeight
                usedWidth = totalChildWidth
            } else {
                usedWidth += totalChildWidth
                currentLineHeight = max(currentLineHeight, totalChildHeight)
            }
        }
        measuredHeight += currentLineHeight + paddingBottom

        setMeasuredDimension(width, measuredHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val width = measuredWidth - paddingLeft - paddingRight
        var currentTop = paddingTop
        var usedWidth = 0;
        var currentLineHeight = 0;

        0.rangeTo(childCount - 1).forEach {
            val child = getChildAt(it)
            val childLayoutParams = child.layoutParams as MarginLayoutParams

            val totalChildWidth = totalChildWidth(child, childLayoutParams)
            val totalChildHeight = totalChildHeight(child, childLayoutParams)

            if (childExceedsAvailableWidth(totalChildWidth, usedWidth, width)) {
                currentTop += currentLineHeight
                currentLineHeight = totalChildHeight
                usedWidth = totalChildWidth
            } else {
                usedWidth += totalChildWidth
                currentLineHeight = max(currentLineHeight, totalChildHeight)
            }

            layoutChild(child, childLayoutParams, usedWidth - totalChildWidth, currentTop)
        }
    }

    override fun onSelectableTagToggled() = selectableTagToggledSubject

    override fun onShowSelectedTags() = selectedTagsUpdatedSubject

    override fun showSelectableTags(selectableTags: List<SelectableTag>) {
        this.selectableTags.clear()
        this.selectableTags.addAll(selectableTags)
        setQuickTags(selectableTags.map { QuickTag(it.tag.title, it.tag.color) })
        selectableTags.forEachIndexed { i, selectableTag -> getChildAt(i).isSelected = selectableTag.isSelected }
    }

    override fun showUpdatedSelectableTag(oldSelectableTag: SelectableTag, newSelectableTag: SelectableTag) {
        val index = selectableTags.indexOf(oldSelectableTag)
        selectableTags.removeAt(index)
        selectableTags.add(index, newSelectableTag)
        allowShowSelectedTags = false
        selectedTagsSubject.onNext(selectableTags.filter { it.isSelected }.map { it.tag }.toSet())
        allowShowSelectedTags = true
        getChildAt(index).isSelected = newSelectableTag.isSelected
    }

    private fun layoutChild(child: View, childLayoutParams: MarginLayoutParams, left: Int, top: Int) {
        val childLeft = left + childLayoutParams.leftMargin
        val childTop = top + childLayoutParams.topMargin
        child.layout(childLeft, childTop, childLeft + child.measuredWidth, childTop + child.measuredHeight)
    }

    private fun totalChildHeight(child: View, childLayoutParams: MarginLayoutParams) =
            childLayoutParams.topMargin + child.measuredHeight + childLayoutParams.bottomMargin

    private fun totalChildWidth(child: View, childLayoutParams: MarginLayoutParams) =
            childLayoutParams.leftMargin + child.measuredWidth + childLayoutParams.rightMargin

    private fun childExceedsAvailableWidth(childTakenWidth: Int, usedWidth: Int, width: Int) = width < usedWidth + childTakenWidth
}