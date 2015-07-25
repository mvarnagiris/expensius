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

package com.mvcoding.financius.ui.tag;

import android.support.annotation.NonNull;

import com.mvcoding.financius.data.DataApi;
import com.mvcoding.financius.data.model.Tag;
import com.mvcoding.financius.ui.CloseablePresenterView;
import com.mvcoding.financius.ui.ErrorPresenterView;
import com.mvcoding.financius.ui.Presenter;
import com.mvcoding.financius.ui.PresenterView;
import com.mvcoding.financius.util.rx.Event;

import javax.inject.Named;

import rx.Observable;
import rx.Scheduler;

public class TagPresenter extends Presenter<TagPresenter.View> {
    private final Tag tag;
    private final DataApi dataApi;
    private final Scheduler uiScheduler;
    private final Scheduler ioScheduler;

    public TagPresenter(@NonNull Tag tag, @NonNull DataApi dataApi, @NonNull @Named("ui") Scheduler uiScheduler, @NonNull @Named("io") Scheduler ioScheduler) {
        this.tag = tag;
        this.dataApi = dataApi;
        this.uiScheduler = uiScheduler;
        this.ioScheduler = ioScheduler;
    }

    @Override protected void onViewAttached(@NonNull View view) {
        super.onViewAttached(view);

        unsubscribeOnDetach(view.onSave()
                                    .withLatestFrom(tagObservable(view).doOnNext(view::showTag), (event, tag) -> tag)
                                    .filter(this::validate)
                                    .observeOn(ioScheduler)
                                    .flatMap(dataApi::saveTag)
                                    .observeOn(uiScheduler)
                                    .subscribeOn(uiScheduler)
                                    .subscribe(view::startResult, throwable -> showFatalError(throwable, view, view, uiScheduler)));
    }

    @NonNull private Observable<Tag> tagObservable(@NonNull View view) {
        final Observable<Tag> tagObservable = Observable.just(tag);
        final Observable<String> titleObservable = view.onTitleChanged().startWith(tag.getTitle());
        final Observable<Integer> colorObservable = view.onColorChanged().startWith(tag.getColor());
        return Observable.combineLatest(tagObservable, titleObservable, colorObservable, (tag, title, color) -> {
            tag.setTitle(title);
            tag.setColor(color);
            return tag;
        });
    }

    private boolean validate(@NonNull Tag tag) {
        try {
            tag.validate();
        } catch (RuntimeException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public interface View extends PresenterView, ErrorPresenterView, CloseablePresenterView {
        @NonNull Observable<String> onTitleChanged();
        @NonNull Observable<Integer> onColorChanged();
        @NonNull Observable<Event> onSave();
        void showTag(@NonNull Tag tag);
        void startResult(@NonNull Tag tag);
    }
}
