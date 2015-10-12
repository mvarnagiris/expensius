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

package com.mvcoding.financius.feature.introduction;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mvcoding.financius.R;

import java.util.ArrayList;
import java.util.List;

class IntroductionAdapter extends PagerAdapter {
    private final List<Introduction> introductions;

    public IntroductionAdapter() {
        introductions = new ArrayList<>();
        introductions.add(new Introduction(R.drawable.ic_action_chevron_right, R.string.app_name, R.string.app_name));
        introductions.add(new Introduction(R.drawable.ic_action_chevron_right, R.string.app_name, R.string.app_name));
        introductions.add(new Introduction(R.drawable.ic_action_chevron_right, R.string.app_name, R.string.app_name));
    }

    @Override public int getCount() {
        return introductions.size();
    }

    @Override public Object instantiateItem(ViewGroup container, int position) {
        final IntroductionView introductionView = (IntroductionView) LayoutInflater.from(container.getContext())
                .inflate(R.layout.view_introduction, container, false);
        introductionView.setIntroduction(introductions.get(position));
        container.addView(introductionView);
        return introductionView;
    }

    @Override public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
