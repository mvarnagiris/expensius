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
import android.graphics.*
import android.graphics.Paint.Align.CENTER
import android.graphics.Paint.Style.FILL
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat.getColor
import android.text.Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
import android.text.SpannableStringBuilder
import android.text.style.ImageSpan
import android.util.AttributeSet
import android.widget.TextView
import com.mvcoding.expensius.R
import com.mvcoding.expensius.extension.provideActivityScopedSingleton
import rx.Observable
import rx.Observable.never

class QuickTagsView : TextView, QuickTagsPresenter.View {
    private val presenter = provideActivityScopedSingleton(QuickTagsPresenter::class)

    private var tagPaddingHorizontal = 0
    private var tagPaddingVertical = 0
    private var tagCornerRadius = 0.toFloat();

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        if (attrs != null) {
            val attributesArray = context.theme.obtainStyledAttributes(
                    attrs,
                    R.styleable.QuickTagsView,
                    defStyleAttr,
                    R.style.QuickTagsView);
            tagPaddingHorizontal = attributesArray.getDimensionPixelSize(R.styleable.QuickTagsView_tagPaddingHorizontal, 0);
            tagPaddingVertical = attributesArray.getDimensionPixelSize(R.styleable.QuickTagsView_tagPaddingVertical, 0);
            tagCornerRadius = attributesArray.getDimensionPixelSize(R.styleable.QuickTagsView_tagCornerRadius, 0).toFloat();
            attributesArray.recycle();
        }

        showSomethingInEditMode(context)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        presenter?.onAttachView(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        presenter?.onDetachView(this)
    }

    override fun onSelectableTagToggled(): Observable<SelectableTag> {
        return never()
    }

    override fun showSelectableTags(selectableTags: List<SelectableTag>) {
        setQuickTags(selectableTags.map { QuickTag(it.tag.title, it.tag.color) }, " ")
    }

    override fun showUpdatedSelectableTag(oldSelectableTag: SelectableTag, newSelectableTag: SelectableTag) {
    }

    private fun setQuickTags(quickTags: List<QuickTag>, separator: CharSequence) {
        val stringBuilder = SpannableStringBuilder();
        quickTags.forEachIndexed { index, quickTag ->
            stringBuilder.append(quickTag.text).setSpan(createSpan(quickTag),
                                                        stringBuilder.length - quickTag.text.length,
                                                        stringBuilder.length,
                                                        SPAN_EXCLUSIVE_EXCLUSIVE)
            if (index < quickTags.size - 1) {
                stringBuilder.append(separator)
            }
        }
        text = stringBuilder
    }

    private fun createSpan(quickTag: QuickTag) = QuickTagSpan(quickTag.text,
                                                              textSize,
                                                              currentTextColor,
                                                              typeface,
                                                              tagPaddingHorizontal,
                                                              tagPaddingVertical,
                                                              quickTag.color,
                                                              tagCornerRadius)

    private fun showSomethingInEditMode(context: Context) {
        if (isInEditMode) {
            setQuickTags(listOf(QuickTag("Essential", getColor(context, R.color.orange_500)),
                                QuickTag("Food", getColor(context, R.color.lime_500))), " ")
        }
    }

    data class QuickTag(val text: String, val color: Int)

    private class QuickTagSpan(
            text: String,
            textSize: Float,
            textColor: Int,
            typeface: Typeface,
            quickTagPaddingHorizontal: Int,
            quickTagPaddingVertical: Int,
            quickTagColor: Int,
            quickTagCornerRadius: Float) : ImageSpan(
            QuickTagDrawable(text,
                             textSize,
                             textColor,
                             typeface,
                             quickTagPaddingHorizontal,
                             quickTagPaddingVertical,
                             quickTagColor,
                             quickTagCornerRadius))

    private class QuickTagDrawable(
            private val text: String,
            textSize: Float,
            textColor: Int,
            typeface: Typeface,
            private val quickTagPaddingHorizontal: Int,
            private val quickTagPaddingVertical: Int,
            quickTagColor: Int,
            private val quickTagCornerRadius: Float) : Drawable() {
        private val textPaint = Paint()
        private val backgroundPaint = Paint()
        private val bounds: RectF

        init {
            textPaint.color = textColor
            textPaint.textSize = textSize
            textPaint.setTypeface(typeface)
            textPaint.isAntiAlias = true
            textPaint.style = FILL
            textPaint.textAlign = CENTER

            backgroundPaint.color = quickTagColor
            backgroundPaint.style = FILL
            backgroundPaint.isAntiAlias = true

            setBounds(
                    0,
                    0,
                    textPaint.measureText(text).toInt() + quickTagPaddingHorizontal * 2,
                    textPaint.textSize.toInt() + quickTagPaddingVertical * 2)
            bounds = RectF(getBounds())
        }

        override fun draw(canvas: Canvas) {
            canvas.drawRoundRect(bounds, quickTagCornerRadius, quickTagCornerRadius, backgroundPaint);
            canvas.drawText(text, bounds.centerX(), bounds.centerY() + (textPaint.textSize / 2) - (textPaint.textSize / 6), textPaint);
        }

        override fun setAlpha(alpha: Int) {
            textPaint.alpha = alpha;
            backgroundPaint.alpha = alpha;
        }

        override fun setColorFilter(colorFilter: ColorFilter) {
            textPaint.setColorFilter(colorFilter);
        }

        override fun getOpacity() = PixelFormat.TRANSLUCENT;
    }
}