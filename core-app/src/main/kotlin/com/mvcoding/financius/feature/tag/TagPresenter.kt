package com.mvcoding.financius.feature.tag

import com.mvcoding.financius.feature.Presenter
import rx.Observable

class TagPresenter(private var tag: Tag, private val tagsRepository: TagsRepository) : Presenter<TagPresenter.View>() {
    override fun onAttachView(view: View) {
        super.onAttachView(view)

        val idObservable = Observable.just(tag.id)
        val titleObservable = view.onTitleChanged().startWith(tag.title).doOnNext { view.showTitle(it) }
        val colorObservable = view.onColorChanged().startWith(tag.color).doOnNext { view.showColor(it) }

        val tagObservable = Observable.combineLatest(idObservable, titleObservable, colorObservable, { id, title, color -> Tag(id, title, color) })
                .doOnNext { tag = it }

        unsubscribeOnDetach(view.onSave()
                .withLatestFrom(tagObservable, { action, tag -> tag })
                .filter { validate(it, view) }
                .doOnNext { tagsRepository.save(it) }
                .subscribe { view.startResult(it) })
    }

    private fun validate(tag: Tag, view: View): Boolean {
        if (tag.title.isBlank()) {
            view.showTitleCannotBeEmptyError()
            return false
        }
        return true
    }

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