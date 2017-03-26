/*
 * Copyright (C) 2017 Mantas Varnagiris.
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

package com.mvcoding.expensius.firebase.model

import com.memoizr.assertk.expect
import com.mvcoding.expensius.aStringId
import com.mvcoding.expensius.firebase.extensions.*
import com.mvcoding.expensius.model.*
import com.mvcoding.expensius.model.NullModels.noColor
import com.mvcoding.expensius.model.NullModels.noOrder
import com.mvcoding.expensius.model.NullModels.noTag
import org.junit.Test

class FirebaseTagTest {

    val modelState = aModelState()

    @Test
    fun `returns noTag when required fields ar null or empty`() {
        expect that aFirebaseTag().withId(null).toTag(modelState) isEqualTo noTag
        expect that aFirebaseTag().withId("").toTag(modelState) isEqualTo noTag
        expect that aFirebaseTag().withId(" ").toTag(modelState) isEqualTo noTag

        expect that aFirebaseTag().withTitle(null).toTag(modelState) isEqualTo noTag
        expect that aFirebaseTag().withTitle("").toTag(modelState) isEqualTo noTag
        expect that aFirebaseTag().withTitle(" ").toTag(modelState) isEqualTo noTag
    }

    @Test
    fun `returns Tag with default values when not required fields ar null or empty`() {
        expect that aFirebaseTag().withColor(null).toTag(modelState) isNotEqualTo noTag
        expect that aFirebaseTag().withColor(null).toTag(modelState).color isEqualTo noColor

        expect that aFirebaseTag().withOrder(null).toTag(modelState) isNotEqualTo noTag
        expect that aFirebaseTag().withOrder(null).toTag(modelState).order isEqualTo noOrder
    }

    @Test
    fun `returns Tag when all fields are set`() {
        val firebaseTag = aFirebaseTag()

        val tag = firebaseTag.toTag(modelState)

        expect that tag.tagId isEqualTo TagId(firebaseTag.id!!)
        expect that tag.title isEqualTo Title(firebaseTag.title!!)
        expect that tag.color isEqualTo Color(firebaseTag.color!!)
        expect that tag.order isEqualTo Order(firebaseTag.order!!)
        expect that tag.modelState isEqualTo modelState
    }

    @Test
    fun `converts CreateTag to FirebaseTag`() {
        val id = aStringId()
        val createTag = aCreateTag()

        val firebaseTag = createTag.toFirebaseTag(id)

        expect that firebaseTag.id isEqualTo id
        expect that firebaseTag.color isEqualTo createTag.color.rgb
        expect that firebaseTag.title isEqualTo createTag.title.text
        expect that firebaseTag.order isEqualTo createTag.order.value
    }

    @Test
    fun `converts Tag to firebase map`() {
        val tag = aTag()

        val firebaseMap = tag.toFirebaseMap()

        expect that firebaseMap.keys containsOnly setOf("id", "title", "color", "order")
        expect that firebaseMap["id"] isEqualTo tag.tagId.id
        expect that firebaseMap["title"] isEqualTo tag.title.text
        expect that firebaseMap["color"] isEqualTo tag.color.rgb
        expect that firebaseMap["order"] isEqualTo tag.order.value
    }
}