/*
 * Copyright (C) 2018 Mantas Varnagiris.
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

package com.mvcoding.expensius.feature.reports.tags

class TagsReportPresenterTest {
//    val tagsReport = aTagsReport()
//    val reportSettings = aReportSettings()
//    val remoteFilter = aRemoteFilter(reportSettings.reportPeriod)
//
//    val tagsReportSource = mock<DataSource<TagsReport>>()
//    val reportSettingsSource = mock<DataSource<ReportSettings>>().apply { whenever(data()).thenReturn(just(reportSettings)) }
//    val secondaryRemoteFilterCache = mock<Cache<RemoteFilter>>().apply { whenever(data()).thenReturn(just(remoteFilter)) }
//    val view = mock<TagsReportPresenter.View>()
//    val presenter = TagsReportPresenter(tagsReportSource, reportSettingsSource, secondaryRemoteFilterCache, rxSchedulers())
//
//    @Before
//    fun setUp() {
//        whenever(tagsReportSource.data()).thenReturn(just(tagsReport))
//    }
//
//    @Test
//    fun `secondary remote filter points to previous period`() {
//        verify(secondaryRemoteFilterCache).write(remoteFilter.withPreviousInterval(reportSettings.reportPeriod))
//    }
//
//    @Test
//    fun `shows tags report`() {
//        presenter.attach(view)
//
//        verify(view).showTagsReport(tagsReport)
//        verify(view).hideEmptyView()
//    }
//
//    @Test
//    fun `shows empty view when report is empty`() {
//        val emptyTagsReport = tagsReport.empty()
//        whenever(tagsReportSource.data()).thenReturn(just(emptyTagsReport))
//        presenter.attach(view)
//
//        verify(view).showTagsReport(emptyTagsReport)
//        verify(view).showEmptyView()
//    }
//
//    @Test
//    fun `ignores errors`() {
//        whenever(tagsReportSource.data()).thenReturn(error(Throwable()))
//
//        presenter.attach(view)
//
//        verify(view, never()).showTagsReport(any())
//    }
}