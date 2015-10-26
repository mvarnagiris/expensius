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

package com.mvcoding.financius.feature

import org.junit.Test

class PresenterTest {
    val presenter: Presenter<Presenter.View> = PresenterForTest()

    @Test(expected = IllegalStateException::class)
    fun throwsIllegalStateExceptionWhenViewIsAlreadySet() {
        val view: Presenter.View = ViewForTest()
        presenter.onAttachView(view)

        presenter.onAttachView(view)
    }

    @Test(expected = IllegalStateException::class)
    fun throwsIllegalStateExceptionWhenViewWasNotAttached() {
        val view: Presenter.View = ViewForTest()
        presenter.onDetachView(view)
    }

    @Test(expected = IllegalStateException::class)
    fun throwsIllegalStateExceptionWhenViewWasAlreadyDetached() {
        val view: Presenter.View = ViewForTest()
        presenter.onAttachView(view)
        presenter.onDetachView(view)

        presenter.onDetachView(view)
    }

    @Test(expected = IllegalStateException::class)
    fun throwsIllegalStateExceptionWhenTryingToDetachDifferentView() {
        val view: Presenter.View = ViewForTest()
        val differentView: Presenter.View = ViewForTest()
        presenter.onAttachView(view)

        presenter.onDetachView(differentView)
    }

    class ViewForTest : Presenter.View;
    class PresenterForTest : Presenter<Presenter.View>()
}