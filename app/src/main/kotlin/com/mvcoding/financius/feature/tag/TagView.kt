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

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.util.Pair
import android.widget.LinearLayout
import com.mvcoding.financius.R
import com.mvcoding.financius.extension.provideActivityScopedSingleton
import com.mvcoding.financius.extension.showSnackbar
import com.mvcoding.financius.extension.supportsLollipop
import com.mvcoding.financius.extension.toActivity
import kotlinx.android.synthetic.view_tag.view.*

class TagView : LinearLayout, TagPresenter.View {
    val presenter by lazy { provideActivityScopedSingleton(TagPresenter::class) }
    val darkTextColor by lazy { ContextCompat.getColor(context, R.color.text_primary) }
    val lightTextColor by lazy { ContextCompat.getColor(context, R.color.text_primary_inverse) }
    var titleUpdatesAvailable = true
    var colorAnimator: ValueAnimator? = null

    constructor(context: Context?) : this(context, null)

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    fun init(tag: Tag) {
        ShankModuleInitializer.initializeModules(TagModule(tag))
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        lobsterPicker.addDecorator(lobsterShadeSlider)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        presenter?.onAttachView(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        presenter?.onDetachView(this)
    }

    override fun showTitle(title: String) {
        titleUpdatesAvailable = false;
        titleEditText.setText(title)
        titleEditText.setSelection(title.length)
        titleUpdatesAvailable = true;
    }

    override fun showColor(color: Int) {
        lobsterPicker.history = color;
        if (colorAnimator == null) {
            onTagColorUpdated(color, false)

            val colorAdapter = lobsterPicker.colorAdapter
            colorAdapter.size().minus(1)
                    .downTo(0)
                    .flatMap {
                        colorPosition ->
                        colorAdapter.shades(colorPosition).minus(1).downTo(0).map { Pair(colorPosition, it) }
                    }
                    .find { colorAdapter.color(it.first, it.second) == color }
                    ?.let {
                        lobsterPicker.colorPosition = it.first
                        lobsterShadeSlider.shadePosition = it.second
                    }
        }
    }

    override fun showTitleCannotBeEmptyError() {
        showSnackbar(R.string.error_title_empty, Snackbar.LENGTH_LONG)
    }

    override fun onTitleChanged(): Observable<String> {
        return titleEditText.textChanges().map { it.toString() }.filter { titleUpdatesAvailable }
    }

    override fun onColorChanged(): Observable<Int> {
        return Observable.create {
            lobsterPicker.addOnColorListener(object : OnColorListener {
                override fun onColorChanged(color: Int) {
                    it.onNext(color)
                    onTagColorUpdated(color, true)
                }

                override fun onColorSelected(color: Int) {
                    it.onNext(color)
                }
            })
        }
    }

    override fun onSave(): Observable<Unit> {
        return saveButton.clicks()
    }

    override fun startResult(tag: Tag) {
        context.toActivity().finish()
        // TODO: Set the result
    }

    fun onTagColorUpdated(color: Int, animate: Boolean) {
        colorAnimator?.cancel()
        if (!animate) {
            setColorOnViews(color)
            return
        }

        val startColor = (titleContainerView.background as ColorDrawable).color
        val animator = ValueAnimator();
        animator.setIntValues(startColor, color);
        animator.setEvaluator(ArgbEvaluator());
        animator.addUpdateListener(ValueAnimator.AnimatorUpdateListener({
            setColorOnViews(it.animatedValue as Int)
        }))
        animator.setDuration(150);
        animator.start();
        colorAnimator = animator;
    }

    private fun setColorOnViews(color: Int) {
        val toolbarView = toolbar as Toolbar

        titleContainerView.setBackgroundColor(color)
        toolbarView.setBackgroundColor(color)

        val textColor = calculateTextColor(color)
        titleEditText.setTextColor(textColor)
        toolbarView.setTitleTextColor(textColor)
        val navigationIcon = DrawableCompat.wrap(toolbarView.navigationIcon.mutate())
        DrawableCompat.setTint(navigationIcon, textColor)
        toolbarView.navigationIcon = navigationIcon

        if (supportsLollipop()) {
            context.toActivity().window.statusBarColor = color;
        }
    }

    private fun calculateTextColor(color: Int): Int {
        if (ColorUtils.calculateContrast(lightTextColor, color) > 2) {
            return lightTextColor
        } else {
            return darkTextColor
        }
    }

    private class MaterialColorAdapter(context: Context) : ColorAdapter {
        val colors: Array<Int>

        init {
            val red = context.resources.getIntArray(R.array.reds)
            val deepPurple = context.resources.getIntArray(R.array.reds)
            val lightBlue = context.resources.getIntArray(R.array.reds)
            val green = context.resources.getIntArray(R.array.reds)
            val yellow = context.resources.getIntArray(R.array.reds)
            val deepOrange = context.resources.getIntArray(R.array.reds)
            val blueGrey = context.resources.getIntArray(R.array.reds)
            val pink = context.resources.getIntArray(R.array.reds)
            val indigo = context.resources.getIntArray(R.array.reds)
            val cyan = context.resources.getIntArray(R.array.reds)

            colors = arrayOf()
        }

        override fun size(): Int {
            throw UnsupportedOperationException()
        }

        override fun shades(position: Int): Int {
            throw UnsupportedOperationException()
        }

        override fun color(position: Int, shade: Int): Int {
            throw UnsupportedOperationException()
        }
    }
}