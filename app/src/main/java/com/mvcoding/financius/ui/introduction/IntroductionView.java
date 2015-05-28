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

package com.mvcoding.financius.ui.introduction;

import android.content.Context;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mvcoding.financius.R;

public class IntroductionView extends LinearLayout {
    private ImageView photoImageView;
    private TextView titleTextView;
    private TextView messageTextView;

    public IntroductionView(Context context) {
        this(context, null);
    }

    public IntroductionView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IntroductionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override protected void onFinishInflate() {
        super.onFinishInflate();

        photoImageView = (ImageView) findViewById(R.id.photoImageView);
        titleTextView = (TextView) findViewById(R.id.titleTextView);
        messageTextView = (TextView) findViewById(R.id.messageTextView);

        photoImageView.getBackground().setColorFilter(0x66ffffff, PorterDuff.Mode.SRC_IN);
    }

    public void setIntroduction(Introduction introduction) {
        Glide.with(getContext()).load(introduction.getImageResId()).dontAnimate().fitCenter().into(photoImageView);
        titleTextView.setText(introduction.getTitleResId());
        messageTextView.setText(introduction.getMessageResId());
    }
}