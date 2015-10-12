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

package com.mvcoding.financius.feature;

import com.mvcoding.financius.BaseComponent;
import com.mvcoding.financius.feature.calculator.CalculatorComponent;
import com.mvcoding.financius.feature.calculator.CalculatorModule;
import com.mvcoding.financius.feature.introduction.IntroductionComponent;
import com.mvcoding.financius.feature.introduction.IntroductionModule;
import com.mvcoding.financius.feature.overview.OverviewComponent;
import com.mvcoding.financius.feature.overview.OverviewModule;
import com.mvcoding.financius.feature.splash.SplashComponent;
import com.mvcoding.financius.feature.splash.SplashModule;
import com.mvcoding.financius.feature.tag.TagComponent;
import com.mvcoding.financius.feature.tag.TagModule;
import com.mvcoding.financius.feature.tag.TagsComponent;
import com.mvcoding.financius.feature.tag.TagsModule;
import com.mvcoding.financius.feature.transaction.TransactionComponent;
import com.mvcoding.financius.feature.transaction.TransactionModule;

import dagger.Subcomponent;

@ActivityScope @Subcomponent(modules = ActivityModule.class) public interface ActivityComponent extends BaseComponent {
    SplashComponent plus(SplashModule module);
    IntroductionComponent plus(IntroductionModule module);
    OverviewComponent plus(OverviewModule module);
    CalculatorComponent plus(CalculatorModule module);
    TransactionComponent plus(TransactionModule module);
    TagComponent plus(TagModule module);
    TagsComponent plus(TagsModule module);
}
