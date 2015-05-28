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

package com.mvcoding.financius.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.mvcoding.financius.App;
import com.mvcoding.financius.R;

import dagger.ObjectGraph;
import icepick.Icepick;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public abstract class BaseActivity<V extends PresenterView> extends AppCompatActivity implements CloseablePresenterView {
    @LayoutRes protected abstract int getLayoutId();

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        setupToolbar();

        Icepick.restoreInstanceState(this, savedInstanceState);

        // Init object graph
        final App app = App.with(this);
        final String scopedGraphKey = getScopedGraphKey();
        ObjectGraph objectGraph = app.getScopedGraph(scopedGraphKey);
        if (objectGraph == null) {
            objectGraph = app.createScopedGraphAndCache(scopedGraphKey, getModules());
        }
        objectGraph.inject(this);

        // Init presenter
        if (savedInstanceState == null) {
            getPresenter().onCreate();
        }

        onViewCreated(savedInstanceState);

        // Presenter view created
        final V view = getPresenterView();
        if (view != null) {
            getPresenter().onViewAttached(view);
        }
    }

    protected void onViewCreated(@Nullable Bundle savedInstanceState) {
    }

    @Override protected void onResume() {
        super.onResume();

        // Presenter view visible
        final V view = getPresenterView();
        if (view != null) {
            getPresenter().onViewResumed(view);
        }
    }

    @Override protected void onPause() {
        super.onPause();

        // Presenter view invisible
        V view = getPresenterView();
        if (view != null) {
            getPresenter().onViewPaused(view);
        }
    }

    @Override protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @Override protected void onDestroy() {
        super.onDestroy();

        // Presenter view destroyed
        V presenterView = getPresenterView();
        if (presenterView != null) {
            getPresenter().onViewDetached(presenterView);
        }

        if (isFinishing()) {
            getPresenter().onDestroy();
        }

        // Destroy object graph.
        if (isFinishing()) {
            App.with(this).removeScopedGraph(getScopedGraphKey());
        }
    }

    @Override protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override public void close() {
        finish();
    }

    @NonNull protected Presenter<V> getPresenter() {
        return Presenter.empty();
    }

    @Nullable protected V getPresenterView() {
        return null;
    }

    @Nullable protected Object[] getModules() {
        return null;
    }

    protected void setupToolbar() {
        final Toolbar toolbar = getToolbar();
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            //noinspection ConstantConditions
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    @Nullable protected Toolbar getToolbar() {
        return (Toolbar) findViewById(R.id.toolbar);
    }

    private String getScopedGraphKey() {
        return getClass().getName();
    }
}