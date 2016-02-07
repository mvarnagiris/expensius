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

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat.getColor
import android.support.v4.graphics.ColorUtils
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v7.widget.Toolbar
import android.util.AttributeSet
import android.util.Pair
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import com.jakewharton.rxbinding.support.v7.widget.itemClicks
import com.jakewharton.rxbinding.view.clicks
import com.jakewharton.rxbinding.widget.textChanges
import com.larswerkman.lobsterpicker.ColorAdapter
import com.larswerkman.lobsterpicker.LobsterPicker
import com.larswerkman.lobsterpicker.OnColorListener
import com.larswerkman.lobsterpicker.sliders.LobsterShadeSlider
import com.mvcoding.expensius.R
import com.mvcoding.expensius.extension.*
import rx.Observable
import rx.lang.kotlin.observable

class TagView : LinearLayout, TagPresenter.View {
    var isArchiveVisible = true

    private val toolbar by lazy { findViewById(R.id.toolbar) as Toolbar }
    private val lobsterPicker by lazy { findViewById(R.id.lobsterPicker) as LobsterPicker }
    private val lobsterShadeSlider by lazy { findViewById(R.id.lobsterShadeSlider) as LobsterShadeSlider }
    private val titleContainerView by lazy { findViewById(R.id.titleContainerView) }
    private val titleEditText by lazy { findViewById(R.id.titleEditText) as EditText }
    private val saveButton by lazy { findViewById(R.id.saveButton) as Button }

    private val darkTextColor by lazy { getColor(context, R.color.text_primary) }
    private val lightTextColor by lazy { getColor(context, R.color.text_primary_inverse) }
    private var titleUpdatesAvailable = true
    private var colorUpdatesAvailable = true
    private var colorAnimator: ValueAnimator? = null
    private lateinit var presenter: TagPresenter

    constructor(context: Context?) : this(context, null)

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    fun init(tag: Tag) {
        presenter = provideActivityScopedSingleton(TagPresenter::class, tag)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        lobsterPicker.colorAdapter = MaterialColorAdapter(context)
        lobsterPicker.addDecorator(lobsterShadeSlider)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        presenter.onAttachView(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        presenter.onDetachView(this)
    }

    override fun showArchiveEnabled(archiveEnabled: Boolean) {
        isArchiveVisible = archiveEnabled
        toolbar.menu.findItem(R.id.action_archive)?.isVisible = archiveEnabled
    }

    override fun showTitle(title: String) {
        titleUpdatesAvailable = false;
        if (!titleEditText.text.toString().equals(title)) {
            titleEditText.setText(title)
            titleEditText.setSelection(title.length)
        }
        titleUpdatesAvailable = true;
    }

    override fun showColor(color: Int) {
        if (colorAnimator == null) {
            onTagColorUpdated(color, false)
        }

        if (!colorUpdatesAvailable) {
            return
        }

        lobsterPicker.history = color;
        if (colorAnimator == null) {
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
        snackbar(R.string.error_title_empty, Snackbar.LENGTH_LONG).show()
    }

    override fun onTitleChanged(): Observable<String> {
        return titleEditText.textChanges().map { it.toString() }.filter { titleUpdatesAvailable }
    }

    override fun onColorChanged(): Observable<Int> {
        return observable {
            lobsterPicker.addOnColorListener(object : OnColorListener {
                override fun onColorChanged(color: Int) {
                    colorUpdatesAvailable = false
                    it.onNext(color)
                    onTagColorUpdated(color, true)
                    colorUpdatesAvailable = true
                }

                override fun onColorSelected(color: Int) {
                    colorUpdatesAvailable = false
                    it.onNext(color)
                    colorUpdatesAvailable = true
                }
            })
        }
    }

    override fun onSave(): Observable<Unit> {
        return saveButton.clicks()
    }

    override fun onArchive() = toolbar.itemClicks().filter { it.itemId == R.id.action_archive }.map { Unit }

    override fun displayResult(tag: Tag) {
        context.toBaseActivity().finish()
        // TODO: Set the result
    }

    private fun onTagColorUpdated(color: Int, animate: Boolean) {
        colorAnimator?.cancel()
        if (!animate) {
            setColorOnViews(color)
            return
        }

        val startColor = (titleContainerView.background as ColorDrawable).color
        val animator = ValueAnimator();
        animator.setIntValues(startColor, color);
        animator.setEvaluator(ArgbEvaluator());
        animator.addUpdateListener { setColorOnViews(it.animatedValue as Int) }
        animator.duration = 150;
        animator.start();
        colorAnimator = animator;
    }

    private fun setColorOnViews(color: Int) {
        titleContainerView.setBackgroundColor(color)
        toolbar.setBackgroundColor(color)

        val textColor = pickForegroundColor(color, lightTextColor, darkTextColor)
        titleEditText.setTextColor(textColor)
        titleEditText.setHintTextColor(ColorUtils.setAlphaComponent(textColor, 0x88))
        toolbar.setTitleTextColor(textColor)
        val navigationIcon = DrawableCompat.wrap(toolbar.navigationIcon?.mutate())
        DrawableCompat.setTint(navigationIcon, textColor)
        toolbar.navigationIcon = navigationIcon

        if (supportsLollipop()) {
            context.toBaseActivity().window.statusBarColor = color;
        }
    }

    private class MaterialColorAdapter(context: Context) : ColorAdapter {
        val colors: Array<IntArray>

        init {
            val resources = context.resources
            val red = resources.getIntArray(R.array.reds)
            val deepPurple = resources.getIntArray(R.array.deep_purples)
            val lightBlue = resources.getIntArray(R.array.light_blues)
            val green = resources.getIntArray(R.array.greens)
            val yellow = resources.getIntArray(R.array.yellows)
            val deepOrange = resources.getIntArray(R.array.deep_oranges)
            val blueGrey = resources.getIntArray(R.array.blue_greys)
            val pink = resources.getIntArray(R.array.pinks)
            val indigo = resources.getIntArray(R.array.indigos)
            val cyan = resources.getIntArray(R.array.cyans)
            val lightGreen = resources.getIntArray(R.array.light_greens)
            val amber = resources.getIntArray(R.array.ambers)
            val brown = resources.getIntArray(R.array.browns)
            val purple = resources.getIntArray(R.array.purples)
            val blue = resources.getIntArray(R.array.blues)
            val teal = resources.getIntArray(R.array.teals)
            val lime = resources.getIntArray(R.array.limes)
            val orange = resources.getIntArray(R.array.oranges)

            colors = arrayOf(red, deepPurple, lightBlue, green, yellow, deepOrange, blueGrey, pink, indigo, cyan, lightGreen, amber, brown,
                             purple, blue, teal, lime, orange)
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