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

//class TagActivity : BaseActivity(), TagPresenter.View {
//    companion object {
//        private const val EXTRA_TAG = "EXTRA_TAG"
//
//        fun start(context: Context, tag: Tag) = ActivityStarter(context, TagActivity::class)
//                .extra(EXTRA_TAG, tag)
//                .start()
//    }
//
//    private val presenter by lazy { provideTagPresenter(intent.getSerializableExtra(TagActivity.EXTRA_TAG) as Tag) }
//    private val darkTextColor by lazy { getColor(this, R.color.text_dark) }
//    private val lightTextColor by lazy { getColor(this, R.color.text_light) }
//    private var colorAnimator: ValueAnimator? = null
//    private var isArchiveToggleVisible = true
//    private var archiveToggleTitle: String? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_tag)
//
//        lobsterPicker.colorAdapter = MaterialColorAdapter(this)
//        lobsterPicker.addDecorator(lobsterShadeSlider)
//    }
//
//    override fun onStart() {
//        super.onStart()
//        presenter.attach(this)
//    }
//
//    override fun onStop() {
//        super.onStop()
//        presenter.detach(this)
//    }
//
//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        super.onCreateOptionsMenu(menu)
//        menuInflater.inflate(R.menu.tag, menu)
//        val menuItem = menu.findItem(R.id.action_archive)
//        menuItem.isVisible = isArchiveToggleVisible
//        menuItem.title = archiveToggleTitle
//        return true
//    }
//
//    override fun titleChanges(): Observable<String> = titleEditText.textChanges().map { it.toString() }.distinctUntilChanged()
//    override fun colorChanges(): Observable<Int> = Observable.create<Int> { withColorPickerListener(it) }.distinctUntilChanged()
//    override fun archiveToggles(): Observable<Unit> = toolbar.itemClicks().filter { it.itemId == R.id.action_archive }.map { Unit }
//    override fun saveRequests(): Observable<Unit> = saveButton.clicks()
//    override fun showTitle(title: Title): Unit = titleEditText.setTextIfChanged(title.text)
//    override fun displayResult(): Unit = finish()
//
//    override fun showColor(color: Color) {
//        if (colorAnimator == null) onTagColorUpdated(color.rgb, false)
//
//        lobsterPicker.history = color.rgb
//        if (colorAnimator == null) {
//            val colorAdapter = lobsterPicker.colorAdapter
//            colorAdapter.size().minus(1)
//                    .downTo(0)
//                    .flatMap { colorPosition ->
//                        colorAdapter.shades(colorPosition).minus(1).downTo(0).map { Pair(colorPosition, it) }
//                    }
//                    .find { colorAdapter.color(it.first, it.second) == color.rgb }
//                    ?.let {
//                        lobsterPicker.colorPosition = it.first
//                        lobsterShadeSlider.shadePosition = it.second
//                    }
//        }
//    }
//
//    override fun showModelState(modelState: ModelState) {
//        archiveToggleTitle = resources.getString(if (modelState == NONE) R.string.archive else R.string.restore)
//        toolbar.menu.findItem(R.id.action_archive)?.title = archiveToggleTitle
//    }
//
//    override fun showTitleCannotBeEmptyError() {
//        snackbar(R.string.error_title_empty, Snackbar.LENGTH_LONG).show()
//    }
//
//    override fun showArchiveEnabled(archiveEnabled: Boolean) {
//        isArchiveToggleVisible = archiveEnabled
//        toolbar.menu.findItem(R.id.action_archive)?.isVisible = archiveEnabled
//    }
//
//    private fun onTagColorUpdated(rgb: Int, animate: Boolean) {
//        colorAnimator?.cancel()
//        if (!animate) {
//            setColorOnViews(rgb)
//            return
//        }
//
//        val startColor = (titleContainerView.background as ColorDrawable).color
//        val animator = ValueAnimator()
//        animator.setIntValues(startColor, rgb)
//        animator.setEvaluator(ArgbEvaluator())
//        animator.addUpdateListener { setColorOnViews(it.animatedValue as Int) }
//        animator.duration = 150
//        animator.start()
//        colorAnimator = animator
//    }
//
//    private fun setColorOnViews(color: Int) {
//        titleContainerView.setBackgroundColor(color)
//        toolbar.setBackgroundColor(color)
//
//        val textColor = pickForegroundColor(color, lightTextColor, darkTextColor)
//        titleEditText.setTextColor(textColor)
//        titleEditText.setHintTextColor(ColorUtils.setAlphaComponent(textColor, 0x88))
//        toolbar.setTitleTextColor(textColor)
//        val navigationIcon = DrawableCompat.wrap(toolbar.navigationIcon!!.mutate())
//        DrawableCompat.setTint(navigationIcon, textColor)
//        toolbar.navigationIcon = navigationIcon
//        window.statusBarColor = color
//    }
//
//    private fun withColorPickerListener(subscriber: ObservableEmitter<in Int>) {
//        lobsterPicker.addOnColorListener(object : OnColorListener {
//            override fun onColorChanged(color: Int) {
//                subscriber.onNext(color)
//                onTagColorUpdated(color, true)
//            }
//
//            override fun onColorSelected(color: Int) {
//                subscriber.onNext(color)
//            }
//        })
//    }
//
//    private class MaterialColorAdapter(context: Context) : ColorAdapter {
//        val colors: Array<IntArray>
//
//        init {
//            val resources = context.resources
//            val red = resources.getIntArray(R.array.reds)
//            val deepPurple = resources.getIntArray(R.array.deep_purples)
//            val lightBlue = resources.getIntArray(R.array.light_blues)
//            val green = resources.getIntArray(R.array.greens)
//            val yellow = resources.getIntArray(R.array.yellows)
//            val deepOrange = resources.getIntArray(R.array.deep_oranges)
//            val blueGrey = resources.getIntArray(R.array.blue_greys)
//            val pink = resources.getIntArray(R.array.pinks)
//            val indigo = resources.getIntArray(R.array.indigos)
//            val cyan = resources.getIntArray(R.array.cyans)
//            val lightGreen = resources.getIntArray(R.array.light_greens)
//            val amber = resources.getIntArray(R.array.ambers)
//            val brown = resources.getIntArray(R.array.browns)
//            val purple = resources.getIntArray(R.array.purples)
//            val blue = resources.getIntArray(R.array.blues)
//            val teal = resources.getIntArray(R.array.teals)
//            val lime = resources.getIntArray(R.array.limes)
//            val orange = resources.getIntArray(R.array.oranges)
//
//            colors = arrayOf(red, deepPurple, lightBlue, green, yellow, deepOrange, blueGrey, pink, indigo, cyan, lightGreen, amber, brown,
//                    purple, blue, teal, lime, orange)
//        }
//
//        override fun size(): Int {
//            return colors.size
//        }
//
//        override fun shades(position: Int): Int {
//            return colors[position].size
//        }
//
//        override fun color(position: Int, shade: Int): Int {
//            return colors[position][shade]
//        }
//    }
//}