package com.mvcoding.financius.feature.tag

import com.mvcoding.financius.feature.Presenter
import rx.Observable

class TagPresenter(tag: Tag, tagsRepository: TagsRepository) : Presenter<TagPresenter.View>() {
    interface View : Presenter.View {
        fun showTitle(title: String)
        fun showColor(color: Int)
        fun showTitleCannotBeEmptyError()
        fun onTitleChanged(): Observable<String>
        fun onColorChanged(): Observable<Int>
        fun onSave(): Observable<Unit>
        fun startResult(tag: Tag)
    }
}