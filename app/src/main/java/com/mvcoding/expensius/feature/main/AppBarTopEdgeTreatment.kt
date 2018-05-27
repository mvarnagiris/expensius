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

import com.google.android.material.shape.EdgeTreatment
import com.google.android.material.shape.ShapePath
import kotlin.math.asin
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

class AppBarTopEdgeTreatment(
        private val cradleDiameter: Float = 0f,
        private val roundedCornerRadius: Float = 0f,
        private val backgroundVerticalOffset: Float = 0f,
        var cradleVerticalOffset: Float = 0f,
        var cradleHorizontalOffset: Float = 0f) : EdgeTreatment() {

    init {
        if (cradleVerticalOffset < 0.0f) {
            throw IllegalArgumentException("cradleVerticalOffset must be positive.")
        } else {
            this.cradleHorizontalOffset = 0.0f
        }
    }

    override fun getEdgePath(length: Float, interpolation: Float, shapePath: ShapePath) {
        val middle = length / 2.0f + cradleHorizontalOffset
        val cradleRadius = interpolation * cradleDiameter / 2.0f
        val roundedCornerRadius = interpolation * roundedCornerRadius
        val roundedCornerRectCenterOffset = sqrt((cradleRadius + roundedCornerRadius).pow(2) - roundedCornerRadius.pow(2))

        shapePath.lineTo(0f, backgroundVerticalOffset)
        shapePath.lineTo(middle - roundedCornerRectCenterOffset, backgroundVerticalOffset)

        val roundedCornerDegrees = Math.toDegrees(asin(sin(Math.toRadians(90.0)) * roundedCornerRectCenterOffset / (cradleRadius + roundedCornerRadius))).toFloat()
        shapePath.addArc(
                middle - roundedCornerRectCenterOffset - roundedCornerRadius,
                backgroundVerticalOffset,
                middle - roundedCornerRectCenterOffset + roundedCornerRadius,
                backgroundVerticalOffset + roundedCornerRadius * 2,
                270f,
                roundedCornerDegrees)

        val cradleDegrees = 90 - roundedCornerDegrees
        val cradleDegreesOffset = 180 - cradleDegrees
        shapePath.addArc(
                middle - cradleRadius,
                backgroundVerticalOffset - cradleRadius,
                middle + cradleRadius,
                backgroundVerticalOffset + cradleRadius,
                cradleDegreesOffset,
                -(180 - cradleDegrees * 2))

        shapePath.addArc(
                middle + roundedCornerRectCenterOffset - roundedCornerRadius,
                backgroundVerticalOffset,
                middle + roundedCornerRectCenterOffset + roundedCornerRadius,
                backgroundVerticalOffset + roundedCornerRadius * 2,
                270f - roundedCornerDegrees,
                roundedCornerDegrees)

        shapePath.lineTo(length, backgroundVerticalOffset)

//        val roundedCornerOffset = interpolation * this.roundedCornerRadius
//        val middle = length / 2.0f + this.cradleHorizontalOffset
//        val verticalOffset = interpolation * this.cradleVerticalOffset
//        val verticalOffsetRatio = verticalOffset / cradleRadius
//        if (verticalOffsetRatio >= 1.0f) {
//            shapePath.lineTo(0f, backgroundVerticalOffset)
//            shapePath.lineTo(length, backgroundVerticalOffset)
//        } else {
//            val offsetSquared = verticalOffset * verticalOffset
//            val cutWidth = Math.sqrt((cradleRadius * cradleRadius - offsetSquared).toDouble()).toFloat()
//            val lowerCurveLeft = middle - cutWidth
//            val lineLeft = lowerCurveLeft - roundedCornerOffset
//            val lowerCurveRight = middle + cutWidth
//            val lineRight = lowerCurveRight + roundedCornerOffset
//            shapePath.lineTo(lineLeft, backgroundVerticalOffset)
//            shapePath.addArc(lineLeft, backgroundVerticalOffset, lowerCurveLeft, roundedCornerOffset + backgroundVerticalOffset, 270.0f, 90.0f)
//            val top = -cradleRadius - verticalOffset + backgroundVerticalOffset
//            val bottom = cradleRadius - verticalOffset + backgroundVerticalOffset
//            shapePath.addArc(middle - cradleRadius, top, middle + cradleRadius, bottom, 180.0f, -180.0f)
//            shapePath.addArc(lowerCurveRight, backgroundVerticalOffset, lineRight, roundedCornerOffset + backgroundVerticalOffset, 180.0f, 90.0f)
//            shapePath.lineTo(length, 0f)
//        }
    }
}
