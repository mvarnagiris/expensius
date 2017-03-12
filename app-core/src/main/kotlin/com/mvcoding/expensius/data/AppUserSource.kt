package com.mvcoding.expensius.data

import com.mvcoding.expensius.model.AppUser
import rx.Observable

class AppUserSource(getAppUser: () -> Observable<AppUser>) : Cache<AppUser> {

    private val cache = MemoryCache(FunctionDataSource(getAppUser))

    override fun write(data: AppUser): Unit = cache.write(data)
    override fun data(): Observable<AppUser> = cache.data()
}