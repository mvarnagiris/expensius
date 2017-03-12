package com.mvcoding.expensius.firebase.extensions

import com.mvcoding.expensius.aString
import com.mvcoding.expensius.aStringId
import com.mvcoding.expensius.firebase.model.FirebaseAppUser
import com.mvcoding.expensius.firebase.model.FirebaseSettings

fun aFirebaseAppUser() = FirebaseAppUser(
        id = aStringId(),
        name = aString("name"),
        photoUrl = aString("photoUrl"),
        email = aString("email"),
        settings = aFirebaseSettings())

fun FirebaseAppUser.withId(id: String?) = copy(id = id)
fun FirebaseAppUser.withName(name: String?) = copy(name = name)
fun FirebaseAppUser.withPhotoUrl(photoUrl: String?) = copy(photoUrl = photoUrl)
fun FirebaseAppUser.withEmail(email: String?) = copy(email = email)
fun FirebaseAppUser.withSettings(firebaseSettings: FirebaseSettings?) = copy(settings = firebaseSettings)