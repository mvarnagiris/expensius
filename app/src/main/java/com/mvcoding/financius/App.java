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

package com.mvcoding.financius;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

import dagger.ObjectGraph;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class App extends Application {
    private final Map<String, ObjectGraph> scopedObjectGraphs = new HashMap<>();

    private ObjectGraph objectGraph;

    public static App with(Context context) {
        return (App) context.getApplicationContext();
    }

    @Override public void onCreate() {
        super.onCreate();

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder().setDefaultFontPath("fonts/Roboto-Regular.ttf")
                                              .setFontAttrId(R.attr.fontPath)
                                              .build());

        //        LeakCanary.install(this);
        objectGraph = ObjectGraph.create(getModules());
    }

    public void inject(@NonNull Object o) {
        objectGraph.inject(o);
    }

    public ObjectGraph getScopedGraph(@NonNull String key) {
        return scopedObjectGraphs.get(key);
    }

    public ObjectGraph createScopedGraphAndCache(@NonNull String key, @Nullable Object... modules) {
        if (modules == null) {
            return objectGraph;
        }

        ObjectGraph scopedObjectGraph = scopedObjectGraphs.get(key);
        if (scopedObjectGraph == null) {
            scopedObjectGraph = objectGraph.plus(modules);
            scopedObjectGraphs.put(key, scopedObjectGraph);
        }

        return scopedObjectGraph;
    }

    public void removeScopedGraph(String key) {
        scopedObjectGraphs.remove(key);
    }

    @NonNull protected Object[] getModules() {
        return new Object[]{new AppModule(this)};
    }
}
