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

package com.mvcoding.expensius.feature

//class DividerItemDecoration(
//        color: Int,
//        private val size: Int) : RecyclerView.ItemDecoration() {
//    private val paint = Paint()
//    private val sizeHalf = size.toFloat() / 2
//
//    init {
//        paint.color = color
//    }
//
//    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
//        super.getItemOffsets(outRect, view, parent, state)
//
//        if (isNotFirstItem(parent, view)) {
//            outRect.top -= size
//        }
//    }
//
//    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State?) {
//        super.onDraw(canvas, parent, state)
//
//        val layoutManager = parent.layoutManager
//        parent.forEachChild {
//            if (isNotFirstItem(parent, it)) {
//                drawDivider(canvas, it, layoutManager)
//            }
//        }
//    }
//
//    private fun isNotFirstItem(parent: RecyclerView, view: View) = parent.getChildAdapterPosition(view) > 0
//
//    private fun drawDivider(canvas: Canvas, view: View, layoutManager: RecyclerView.LayoutManager) {
//        val startX = layoutManager.getDecoratedLeft(view).toFloat()
//        val endX = startX + layoutManager.getDecoratedMeasuredWidth(view)
//        val y = layoutManager.getDecoratedTop(view) + sizeHalf
//        canvas.drawLine(startX, y, endX, y, paint)
//    }
//}