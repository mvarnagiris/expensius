/*
 * Copyright (C) 2018 Mantas Varnagiris.
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

package com.mvcoding.expensius.feature.tag

//class TagsActivity : BaseActivity(), TagsPresenter.View {
//
//    companion object {
//        private const val EXTRA_DISPLAY_TYPE = "EXTRA_DISPLAY_TYPE"
//
//        fun startView(context: Context): Unit = ActivityStarter(context, TagsActivity::class)
//                .extra(EXTRA_DISPLAY_TYPE, VIEW_NOT_ARCHIVED)
//                .start()
//
//        fun startArchived(context: Context): Unit = ActivityStarter(context, TagsActivity::class)
//                .extra(EXTRA_DISPLAY_TYPE, VIEW_ARCHIVED)
//                .start()
//    }
//
//    private val presenter by lazy { provideTagsPresenter(intent.getSerializableExtra(EXTRA_DISPLAY_TYPE) as ModelDisplayType) }
//    private val tagMoveSubject by lazy { PublishSubject.create<DragAndDropTouchHelperCallback.ItemMoved>() }
//    private val adapter by lazy { TagsAdapter() }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_tags)
//
//        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(this)
//        recyclerView.adapter = adapter
//        val touchCallback = DragAndDropTouchHelperCallback(adapter, tagMoveSubject, { _, target -> target.adapterPosition < adapter.itemCount - 1 })
//        ItemTouchHelper(touchCallback).attachToRecyclerView(recyclerView)
//
//        presenter.attach(this)
//    }
//
//    override fun onDestroy() {
//        presenter.detach(this)
//        super.onDestroy()
//    }
//
//    override fun showModelDisplayType(modelDisplayType: ModelDisplayType): Unit = with(supportActionBar) { title = generateTitle(modelDisplayType) }
//    override fun showArchivedTagsRequest(): Unit = with(adapter) { showArchivedTagsRequest = true }
//    override fun tagSelects(): Observable<Tag> = adapter.itemPositionClicks().filter { adapter.isTagPosition(it) }.map { adapter.getItem(it) }
//    override fun archivedTagsRequests(): Observable<Unit> = adapter.itemPositionClicks().filter { !adapter.isTagPosition(it) }.map { Unit }
//    override fun createTagRequests(): Observable<Unit> = createTagFloatingActionButton.clicks()
//    override fun tagMoves(): Observable<TagMove> = tagMoveSubject.map { TagMove(it.fromPosition, it.toPosition) }
//    override fun showItems(items: List<Tag>): Unit = adapter.setItems(items)
//    override fun showAddedItems(items: List<Tag>, position: Int): Unit = adapter.addItems(position, items)
//    override fun showChangedItems(items: List<Tag>, position: Int): Unit = adapter.changeItems(position, items)
//    override fun showRemovedItems(items: List<Tag>, position: Int): Unit = adapter.removeItems(position, items.size)
//    override fun showMovedItems(items: List<Tag>, fromPosition: Int, toPosition: Int): Unit = adapter.moveItem(fromPosition, toPosition)
//    override fun showLoading(): Unit = with(progressBar) { visibility = VISIBLE }
//    override fun hideLoading(): Unit = with(progressBar) { visibility = GONE }
//    override fun displayArchivedTags(): Unit = TagsActivity.startArchived(this)
//    override fun displayTagEdit(tag: Tag): Unit = TagActivity.start(this, tag)
//    private fun generateTitle(modelDisplayType: ModelDisplayType) = getString(if (modelDisplayType == VIEW_ARCHIVED) R.string.archived_tags else R.string.tags)
//}