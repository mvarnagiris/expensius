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

import com.mvcoding.financius.BaseComponent;
import com.mvcoding.financius.ui.calculator.CalculatorComponent;
import com.mvcoding.financius.ui.calculator.CalculatorModule;
import com.mvcoding.financius.ui.introduction.IntroductionComponent;
import com.mvcoding.financius.ui.introduction.IntroductionModule;
import com.mvcoding.financius.ui.overview.OverviewComponent;
import com.mvcoding.financius.ui.overview.OverviewModule;
import com.mvcoding.financius.ui.splash.SplashComponent;
import com.mvcoding.financius.ui.splash.SplashModule;
import com.mvcoding.financius.ui.tag.TagComponent;
import com.mvcoding.financius.ui.tag.TagModule;
import com.mvcoding.financius.ui.tag.TagsComponent;
import com.mvcoding.financius.ui.tag.TagsModule;
import com.mvcoding.financius.ui.transaction.TransactionComponent;
import com.mvcoding.financius.ui.transaction.TransactionModule;

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
