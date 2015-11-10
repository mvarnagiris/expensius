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
import android.support.design.widget.Snackbar
import android.util.AttributeSet
import android.widget.LinearLayout
import com.jakewharton.rxbinding.view.clicks
import com.jakewharton.rxbinding.widget.textChanges
import com.larswerkman.lobsterpicker.OnColorListener
import com.memoizrlabs.ShankModuleInitializer
import com.mvcoding.financius.R
import com.mvcoding.financius.shankWithBoundScope
import com.mvcoding.financius.showSnackbar
import com.mvcoding.financius.toActivity
import kotlinx.android.synthetic.view_tag.view.*
import rx.Observable

class TagView : LinearLayout, TagPresenter.View {
    val presenter by lazy { shankWithBoundScope(TagView::class, context)?.provide(TagPresenter::class.java) }
    var titleUpdatesAvailable = true

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
                    titleContainerView.setBackgroundColor(color)
                    toolbar.setBackgroundColor(color)
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
}