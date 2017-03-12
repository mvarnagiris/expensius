package com.mvcoding.expensius.data

import com.mvcoding.expensius.model.AppUser
import com.mvcoding.expensius.model.NullModels.noCurrency
import com.mvcoding.expensius.model.defaultCurrency
import rx.Observable

class AppUserSource(
        getAppUser: () -> Observable<AppUser>,
        private val updateAppUser: (AppUser) -> Unit) : Cache<AppUser> {

    private val cache = MemoryCache(FunctionDataSource(getAppUser))

    override fun data(): Observable<AppUser> = cache.data()

    override fun write(data: AppUser) {
        val appUserWithDefaultValues =
                if (data.settings.mainCurrency == noCurrency)
                    data.copy(settings = data.settings.copy(mainCurrency = defaultCurrency()))
                else data
        cache.write(appUserWithDefaultValues)
        updateAppUser(appUserWithDefaultValues)
    }
}