package com.mvcoding.expensius.firebase.model

import com.memoizr.assertk.expect
import com.mvcoding.expensius.firebase.extensions.*
import com.mvcoding.expensius.model.*
import com.mvcoding.expensius.model.NullModels.noAppUser
import com.mvcoding.expensius.model.NullModels.noEmail
import com.mvcoding.expensius.model.NullModels.noImage
import com.mvcoding.expensius.model.NullModels.noName
import com.mvcoding.expensius.model.NullModels.noSettings
import org.junit.Test

class FirebaseAppUserTest {

    val authProviders = setOf(AuthProvider.GOOGLE)

    @Test
    fun `returns noAppUser when required fields ar null or empty`() {
        expect that aFirebaseAppUser().withId(null).toAppUser(authProviders) isEqualTo noAppUser
        expect that aFirebaseAppUser().withId("").toAppUser(authProviders) isEqualTo noAppUser
        expect that aFirebaseAppUser().withId(" ").toAppUser(authProviders) isEqualTo noAppUser
    }

    @Test
    fun `returns AppUser with default values when not required fields ar null or empty`() {
        expect that aFirebaseAppUser().withName(null).toAppUser(authProviders) isNotEqualTo noAppUser
        expect that aFirebaseAppUser().withName("").toAppUser(authProviders) isNotEqualTo noAppUser
        expect that aFirebaseAppUser().withName(" ").toAppUser(authProviders) isNotEqualTo noAppUser
        expect that aFirebaseAppUser().withName(null).toAppUser(authProviders).name isEqualTo noName
        expect that aFirebaseAppUser().withName("").toAppUser(authProviders).name isEqualTo noName
        expect that aFirebaseAppUser().withName(" ").toAppUser(authProviders).name isEqualTo noName

        expect that aFirebaseAppUser().withEmail(null).toAppUser(authProviders) isNotEqualTo noAppUser
        expect that aFirebaseAppUser().withEmail("").toAppUser(authProviders) isNotEqualTo noAppUser
        expect that aFirebaseAppUser().withEmail(" ").toAppUser(authProviders) isNotEqualTo noAppUser
        expect that aFirebaseAppUser().withEmail(null).toAppUser(authProviders).email isEqualTo noEmail
        expect that aFirebaseAppUser().withEmail("").toAppUser(authProviders).email isEqualTo noEmail
        expect that aFirebaseAppUser().withEmail(" ").toAppUser(authProviders).email isEqualTo noEmail

        expect that aFirebaseAppUser().withPhotoUrl(null).toAppUser(authProviders) isNotEqualTo noAppUser
        expect that aFirebaseAppUser().withPhotoUrl("").toAppUser(authProviders) isNotEqualTo noAppUser
        expect that aFirebaseAppUser().withPhotoUrl(" ").toAppUser(authProviders) isNotEqualTo noAppUser
        expect that aFirebaseAppUser().withPhotoUrl(null).toAppUser(authProviders).photo isEqualTo noImage
        expect that aFirebaseAppUser().withPhotoUrl("").toAppUser(authProviders).photo isEqualTo noImage
        expect that aFirebaseAppUser().withPhotoUrl(" ").toAppUser(authProviders).photo isEqualTo noImage

        expect that aFirebaseAppUser().withSettings(null).toAppUser(authProviders) isNotEqualTo noAppUser
        expect that aFirebaseAppUser().withSettings(null).toAppUser(authProviders).settings isEqualTo noSettings
    }

    @Test
    fun `returns AppUser when all fields are set`() {
        val firebaseAppUser = aFirebaseAppUser()

        val appUser = firebaseAppUser.toAppUser(authProviders)

        expect that appUser.userId isEqualTo UserId(firebaseAppUser.id!!)
        expect that appUser.name isEqualTo Name(firebaseAppUser.name!!)
        expect that appUser.photo isEqualTo UriImage(firebaseAppUser.photoUrl!!)
        expect that appUser.email isEqualTo Email(firebaseAppUser.email!!)
        expect that appUser.settings isEqualTo firebaseAppUser.settings!!.toSettings()
        expect that appUser.authProviders isEqualTo authProviders
    }
}