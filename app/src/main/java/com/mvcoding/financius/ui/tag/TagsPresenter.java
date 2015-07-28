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
import com.mvcoding.financius.ui.ActivityScope;
import com.mvcoding.financius.ui.Presenter;
import com.mvcoding.financius.ui.PresenterView;
import com.mvcoding.financius.util.rx.RefreshEvent;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

@ActivityScope class TagsPresenter extends Presenter<TagsPresenter.View> {
    private final DataApi dataApi;

    @Inject public TagsPresenter(@NonNull DataApi dataApi) {
        this.dataApi = dataApi;
    }

    @Override protected void onViewAttached(@NonNull View view) {
        super.onViewAttached(view);
        view.setDisplayType(DisplayType.View);
    }

    public enum DisplayType {
        View, SingleChoice, MultiChoice
    }

    public enum Edge {
        Start, End
    }

    public interface View extends PresenterView {
        @NonNull Observable<Edge> onEdgeReached();
        @NonNull Observable<RefreshEvent> onRefresh();
        void setDisplayType(@NonNull DisplayType displayType);
        void show(@NonNull List<Tag> tags);
        void update(List<Tag> tags);
        void add(int position, List<Tag> tags);
        void remove(List<Tag> tags);
    }
}
