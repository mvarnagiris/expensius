package com.mvcoding.expensius.feature

import android.view.View
import rx.subjects.PublishSubject

class ClickableViewHolder<V : View>(view: V, itemPositionSelectedSubject: PublishSubject<Int>) : ViewHolder<V>(view) {
    init {
        view.setOnClickListener { itemPositionSelectedSubject.onNext(adapterPosition) }
    }
}