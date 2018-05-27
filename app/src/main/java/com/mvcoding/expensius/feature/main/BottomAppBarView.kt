/*
 * Copyright (C) 2018 Mantas Varnagiris.
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

package com.mvcoding.expensius.feature.main

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapePathModel
import com.mvcoding.expensius.R
import kotlinx.android.synthetic.main.bottom_app_bar_view.view.*

class BottomAppBarView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        ConstraintLayout(context, attrs, defStyleAttr) {

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        val grid1x = resources.getDimension(R.dimen.grid_1x)
        val cradleDiameter = createFab.measuredWidth + grid1x * 2

        val shapePathModel = ShapePathModel()
        shapePathModel.topEdge = AppBarTopEdgeTreatment(
                cradleDiameter,
                grid1x * 4f,
                createFab.measuredHeight / 2f + paddingTop)
        val bg = MaterialShapeDrawable(shapePathModel).apply {
            setUseTintColorForShadow(false)
            isShadowEnabled = true
            setShadowColor(Color.BLACK)
            shadowElevation = 5
            shadowRadius = 20
        }
        ViewCompat.setBackground(this, bg)
    }
}