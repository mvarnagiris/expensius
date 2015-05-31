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

import com.mvcoding.financius.ui.ActivityModule;
import com.mvcoding.financius.ui.UserSettings;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(addsTo = ActivityModule.class, complete = false, injects = IntroductionActivity.class)
class IntroductionModule {
    @Provides @Singleton public IntroductionPresenter provideIntroductionPresenter(UserSettings userSettings) {
        return new IntroductionPresenter(userSettings);
    }
}
