package com.mvcoding.expensius.data

import com.mvcoding.expensius.model.anAppUser
import org.junit.Test

class AppUserSourceTest {

    @Test
    fun `behaves like memory cache`() {
        testMemoryCache(anAppUser(), anAppUser()) { AppUserSource { it.data() } }
    }
}