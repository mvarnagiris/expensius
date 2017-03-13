package com.mvcoding.expensius.feature.login

import com.mvcoding.expensius.data.DataSource
import com.mvcoding.expensius.data.RawRealtimeData
import com.mvcoding.expensius.data.RealtimeList
import com.mvcoding.expensius.data.RealtimeListDataSource
import com.mvcoding.expensius.model.AppUser
import com.mvcoding.expensius.model.Tag
import com.mvcoding.expensius.model.UserId
import rx.Observable

class TagsSnapshotSource(
        private val appUserSource: DataSource<AppUser>,
        private val createRealtimeList: (UserId) -> RealtimeList<Tag>) : DataSource<List<Tag>> {

    override fun data(): Observable<List<Tag>> = appUserSource.data()
            .first()
            .map { createRealtimeList(it.userId) }
            .switchMap { realtimeList -> RealtimeListDataSource(realtimeList) { it.tagId.id }.data().doOnNext { realtimeList.close() } }
            .ofType(RawRealtimeData.AllItems::class.java)
            .map {
                @Suppress("UNCHECKED_CAST")
                it.items as List<Tag>
            }
            .first()
}