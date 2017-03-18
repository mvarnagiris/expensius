package com.mvcoding.expensius.feature

import android.view.View
import rx.subjects.PublishSubject

open class ClickableViewHolder(itemView: View, clickSubject: PublishSubject<Pair<View, Int>>) : ViewHolder(itemView) {
    init {
        itemView.setOnClickListener { clickSubject.onNext(it to adapterPosition) }
    }
}