/*
 * Copyright (C) 2016 Mantas Varnagiris.
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

package com.mvcoding.expensius.firebase.service

//abstract class BaseFirebaseTransactionsService(
//        appUserService: AppUserService,
//        tagsService: TagsService,
//        archivedTagsService: TagsService,
//        private val databaseReference: (UserId) -> DatabaseReference) : TransactionsService {
//
//    private val allTags = combineLatest(tagsService.items(), archivedTagsService.items()) { tags, archivedTags ->
//        tags.union(archivedTags).associateBy { it.tagId.id }
//    }.replay(1).autoConnect()
//
//    private val firebaseItemsService = FirebaseItemsService(queries(appUserService), transformer())
//
//    override fun items(): Observable<List<Transaction>> = firebaseItemsService.items()
//    override fun addedItems(): Observable<AddedItems<Transaction>> = firebaseItemsService.addedItems()
//    override fun changedItems(): Observable<ChangedItems<Transaction>> = firebaseItemsService.changedItems()
//    override fun removedItems(): Observable<RemovedItems<Transaction>> = firebaseItemsService.removedItems()
//    override fun movedItem(): Observable<MovedItem<Transaction>> = firebaseItemsService.movedItem()
//
//    private fun queries(appUserService: AppUserService) = appUserService.appUser().map { it.userId }.distinctUntilChanged().map { query(it) }
//    private fun query(userId: UserId) = databaseReference(userId).orderByChild("timestampInverse")
//    private fun transformer(): Transformer<List<DataSnapshot>, List<Transaction>> = Transformer {
//        it.withLatestFrom(allTags) { dataSnapshots, tags ->
//            dataSnapshots.map { dataSnapshot -> dataSnapshot.getValue(FirebaseTransaction::class.java).toTransaction(NONE, tags) }
//        }
//    }
//}
//
//class FirebaseTransactionsService(appUserService: AppUserService, tagsService: TagsService, archivedTagsService: TagsService) :
//        BaseFirebaseTransactionsService(appUserService, tagsService, archivedTagsService, { transactionsDatabaseReference(it) })
//
//class FirebaseArchivedTransactionsService(appUserService: AppUserService, tagsService: TagsService, archivedTagsService: TagsService) :
//        BaseFirebaseTransactionsService(appUserService, tagsService, archivedTagsService, { archivedTransactionsDatabaseReference(it) })