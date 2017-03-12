package com.mvcoding.expensius.data

import com.mvcoding.expensius.model.anAppUser
import org.junit.Test

class AppUserSourceTest {

    @Test
    fun `behaves like memory data source`() {
        testMemoryDataSource(anAppUser()) { AppUserSource { it.data() } }
    }
}