package com.mvcoding.expensius.feature

import android.support.v7.widget.RecyclerView
import android.view.View

open class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    @Suppress("UNCHECKED_CAST")
    fun <VIEW_TYPE> getView() = itemView as VIEW_TYPE
}