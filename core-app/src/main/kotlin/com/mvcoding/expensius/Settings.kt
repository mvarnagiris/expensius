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

package com.mvcoding.expensius

import com.mvcoding.expensius.feature.ReportStep
import com.mvcoding.expensius.model.Currency
import rx.Observable

interface Settings {
    var isIntroductionSeen: Boolean
    var subscriptionType: SubscriptionType
    var reportStep: ReportStep.Step
    var mainCurrency: Currency
    fun subscriptionTypes(): Observable<SubscriptionType>
    fun reportSteps(): Observable<ReportStep.Step>
}