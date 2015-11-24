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
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.ColorUtils
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v7.widget.Toolbar
import android.util.AttributeSet
import android.util.Pair
import android.widget.LinearLayout
import com.jakewharton.rxbinding.view.clicks
import com.jakewharton.rxbinding.widget.textChanges
import com.larswerkman.lobsterpicker.ColorAdapter
import com.larswerkman.lobsterpicker.LobsterPicker
import com.larswerkman.lobsterpicker.OnColorListener
import com.larswerkman.lobsterpicker.sliders.LobsterShadeSlider
import com.memoizrlabs.ShankModuleInitializer.initializeModules
import com.mvcoding.financius.R
import com.mvcoding.financius.extension.provideActivityScopedSingleton
import com.mvcoding.financius.extension.showSnackbar
import com.mvcoding.financius.extension.supportsLollipop
import com.mvcoding.financius.extension.toActivity
import kotlinx.android.synthetic.view_tag.view.*
import rx.Observable

class TagView : LinearLayout, TagPresenter.View {
    private val lobsterPicker by lazy { findViewById(R.id.lobsterPicker) as LobsterPicker }
    private val lobsterShadeSlider by lazy { findViewById(R.id.lobsterShadeSlider) as LobsterShadeSlider }

    private val presenter by lazy { provideActivityScopedSingleton(TagPresenter::class) }
    private val darkTextColor by lazy { ContextCompat.getColor(context, R.color.text_primary) }
    private val lightTextColor by lazy { ContextCompat.getColor(context, R.color.text_primary_inverse) }
    private var titleUpdatesAvailable = true
    private var colorAnimator: ValueAnimator? = null

    constructor(context: Context?) : this(context, null)

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    fun init(tag: Tag) {
        initializeModules(TagModule(tag))
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        lobsterPicker.colorAdapter = MaterialColorAdapter(context)
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
        val colors: Array<IntArray>

        init {
            val red = context.resources.getIntArray(R.array.reds)
            val deepPurple = context.resources.getIntArray(R.array.deep_purples)
            val lightBlue = context.resources.getIntArray(R.array.light_blues)
            val green = context.resources.getIntArray(R.array.greens)
            val yellow = context.resources.getIntArray(R.array.yellows)
            val deepOrange = context.resources.getIntArray(R.array.deep_oranges)
            val blueGrey = context.resources.getIntArray(R.array.blue_greys)
            val pink = context.resources.getIntArray(R.array.pinks)
            val indigo = context.resources.getIntArray(R.array.indigos)
            val cyan = context.resources.getIntArray(R.array.cyans)
            val lightGreen = context.resources.getIntArray(R.array.light_greens)
            val amber = context.resources.getIntArray(R.array.ambers)
            val brown = context.resources.getIntArray(R.array.browns)
            val purple = context.resources.getIntArray(R.array.purples)
            val blue = context.resources.getIntArray(R.array.blues)
            val teal = context.resources.getIntArray(R.array.teals)
            val lime = context.resources.getIntArray(R.array.limes)
            val orange = context.resources.getIntArray(R.array.oranges)
            val grey = context.resources.getIntArray(R.array.greys)

            colors = arrayOf(red, deepPurple, lightBlue, green, yellow, deepOrange, blueGrey, pink,
                    indigo, cyan, lightGreen, amber, brown, purple, blue, teal, lime, orange, grey)
        }

        override fun size(): Int {
            return colors.size
        }

        override fun shades(position: Int): Int {
            return colors[position].size
        }

        override fun color(position: Int, shade: Int): Int {
            return colors[position][shade]
        }
    }
}