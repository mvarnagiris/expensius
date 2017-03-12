package com.mvcoding.expensius.data

import com.mvcoding.expensius.model.AppUser
import rx.Observable

class AppUserSource(getAppUser: () -> Observable<AppUser>) : DataSource<AppUser> {

    private val dataSource = MemoryDataSource(FunctionDataSource(getAppUser))

    override fun data(): Observable<AppUser> = dataSource.data()
}