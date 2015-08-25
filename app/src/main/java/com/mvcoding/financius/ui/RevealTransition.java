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

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.util.ArrayMap;
import android.transition.TransitionValues;
import android.transition.Visibility;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;

import java.util.ArrayList;

@TargetApi(Build.VERSION_CODES.LOLLIPOP) public class RevealTransition extends Visibility {
    public RevealTransition() {
    }

    public RevealTransition(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    static float calculateMaxRadius(View view) {
        float widthSquared = view.getWidth() * view.getWidth();
        float heightSquared = view.getHeight() * view.getHeight();
        return (float) (Math.sqrt(widthSquared + heightSquared) / 2);
    }

    @Override public Animator onAppear(ViewGroup sceneRoot, final View view, TransitionValues startValues, TransitionValues endValues) {
        float radius = calculateMaxRadius(view);
        final float originalAlpha = 1; // TODO: For some reason this returns 0 view.getAlpha();
        view.setAlpha(0f);

        Animator reveal = createAnimator(view, 0, radius);
        reveal.addListener(new AnimatorListenerAdapter() {
            @Override public void onAnimationStart(Animator animation) {
                view.setAlpha(originalAlpha);
            }
        });
        return reveal;
    }

    @Override public Animator onDisappear(ViewGroup sceneRoot, View view, TransitionValues startValues, TransitionValues endValues) {
        float radius = calculateMaxRadius(view);
        return createAnimator(view, radius, 0);
    }

    private Animator createAnimator(View view, float startRadius, float endRadius) {
        int centerX = view.getWidth() / 2;
        int centerY = view.getHeight() / 2;

        Animator reveal = ViewAnimationUtils.createCircularReveal(view, centerX, centerY, startRadius, endRadius);
        return new NoPauseAnimator(reveal);
    }

    private static class NoPauseAnimator extends Animator {
        private final Animator mAnimator;
        private final ArrayMap<AnimatorListener, AnimatorListener> mListeners = new ArrayMap<>();

        public NoPauseAnimator(Animator animator) {
            mAnimator = animator;
        }

        @Override public void addListener(AnimatorListener listener) {
            AnimatorListener wrapper = new AnimatorListenerWrapper(this, listener);
            if (!mListeners.containsKey(listener)) {
                mListeners.put(listener, wrapper);
                mAnimator.addListener(wrapper);
            }
        }

        @Override public void cancel() {
            mAnimator.cancel();
        }

        @Override public void end() {
            mAnimator.end();
        }

        @Override public long getDuration() {
            return mAnimator.getDuration();
        }

        @Override public TimeInterpolator getInterpolator() {
            return mAnimator.getInterpolator();
        }

        @Override public void setInterpolator(TimeInterpolator timeInterpolator) {
            mAnimator.setInterpolator(timeInterpolator);
        }

        @Override public ArrayList<AnimatorListener> getListeners() {
            return new ArrayList<>(mListeners.keySet());
        }

        @Override public long getStartDelay() {
            return mAnimator.getStartDelay();
        }

        @Override public void setStartDelay(long delayMS) {
            mAnimator.setStartDelay(delayMS);
        }

        @Override public boolean isPaused() {
            return mAnimator.isPaused();
        }

        @Override public boolean isRunning() {
            return mAnimator.isRunning();
        }

        @Override public boolean isStarted() {
            return mAnimator.isStarted();
        }

        /* We don't want to override pause or resume methods because we don't want them
         * to affect mAnimator.
        public void pause();
        public void resume();
        public void addPauseListener(AnimatorPauseListener listener);
        public void removePauseListener(AnimatorPauseListener listener);
        */

        @Override public void removeAllListeners() {
            mListeners.clear();
            mAnimator.removeAllListeners();
        }

        @Override public void removeListener(AnimatorListener listener) {
            AnimatorListener wrapper = mListeners.get(listener);
            if (wrapper != null) {
                mListeners.remove(listener);
                mAnimator.removeListener(wrapper);
            }
        }

        @Override public Animator setDuration(long durationMS) {
            mAnimator.setDuration(durationMS);
            return this;
        }

        @Override public void setTarget(Object target) {
            mAnimator.setTarget(target);
        }

        @Override public void setupEndValues() {
            mAnimator.setupEndValues();
        }

        @Override public void setupStartValues() {
            mAnimator.setupStartValues();
        }

        @Override public void start() {
            mAnimator.start();
        }
    }

    private static class AnimatorListenerWrapper implements Animator.AnimatorListener {
        private final Animator mAnimator;
        private final Animator.AnimatorListener mListener;

        public AnimatorListenerWrapper(Animator animator, Animator.AnimatorListener listener) {
            mAnimator = animator;
            mListener = listener;
        }

        @Override public void onAnimationStart(Animator animator) {
            mListener.onAnimationStart(mAnimator);
        }

        @Override public void onAnimationEnd(Animator animator) {
            mListener.onAnimationEnd(mAnimator);
        }

        @Override public void onAnimationCancel(Animator animator) {
            mListener.onAnimationCancel(mAnimator);
        }

        @Override public void onAnimationRepeat(Animator animator) {
            mListener.onAnimationRepeat(mAnimator);
        }
    }
}
