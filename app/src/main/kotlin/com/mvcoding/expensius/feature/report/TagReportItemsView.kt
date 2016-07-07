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

package com.mvcoding.expensius.feature.report

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.mvcoding.expensius.model.Tag
import java.math.BigDecimal

class TagReportItemsView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        LinearLayout(context, attrs, defStyleAttr) {

    init {
        orientation = VERTICAL
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        //        val random = Random()
        //        val tags = listOf(
        //                Tag(generateModelId(), ModelState.NONE, getString(R.string.tag_fixed), ContextCompat.getColor(context, R.color.red_300)),
        //                Tag(generateModelId(),
        //                        ModelState.NONE,
        //                        getString(R.string.tag_essential),
        //                        ContextCompat.getColor(context, R.color.red_500)),
        //                Tag(generateModelId(),
        //                        ModelState.NONE,
        //                        getString(R.string.tag_non_essential),
        //                        ContextCompat.getColor(context, R.color.red_900)),
        //                Tag(generateModelId(),
        //                        ModelState.NONE,
        //                        getString(R.string.tag_food),
        //                        ContextCompat.getColor(context, R.color.light_green_500)),
        //                Tag(generateModelId(),
        //                        ModelState.NONE,
        //                        getString(R.string.tag_leisure),
        //                        ContextCompat.getColor(context, R.color.light_blue_500)),
        //                Tag(generateModelId(),
        //                        ModelState.NONE,
        //                        getString(R.string.tag_clothes),
        //                        ContextCompat.getColor(context, R.color.orange_500)),
        //                Tag(generateModelId(),
        //                        ModelState.NONE,
        //                        getString(R.string.tag_transport),
        //                        ContextCompat.getColor(context, R.color.yellow_500)),
        //                Tag(generateModelId(),
        //                        ModelState.NONE,
        //                        getString(R.string.tag_household),
        //                        ContextCompat.getColor(context, R.color.purple_500)),
        //                Tag(generateModelId(),
        //                        ModelState.NONE,
        //                        getString(R.string.tag_health_and_beauty),
        //                        ContextCompat.getColor(context, R.color.pink_500)),
        //                Tag(generateModelId(),
        //                        ModelState.NONE,
        //                        getString(R.string.tag_bills_and_utilities),
        //                        ContextCompat.getColor(context, R.color.brown_500)),
        //                Tag(generateModelId(), ModelState.NONE, getString(R.string.tag_pets), ContextCompat.getColor(context, R.color.teal_500)))
        //                .sortedWith(Comparator { left, right -> -1 + random.nextInt(3) })
        //
        //        val tagsToShow = (1 + random.nextInt(tags.size - 1)).downTo(1)
        //                .map { TagAmount(tags[it], BigDecimal(3000 * random.nextDouble())) }
        //                .sortedWith(Comparator { left, right -> right.amount.compareTo(left.amount) })
        //
        //        val maxAmount = tagsToShow[0].amount
        //        tagsToShow.forEach {
        //            val tagReportItemView = inflate<TagReportItemView>(R.layout.view_tag_report_item)
        //            addView(tagReportItemView)
        //            tagReportItemView.setTag(it.tag)
        //            tagReportItemView.setAmount(it.amount)
        //            tagReportItemView.setProgress(it.amount.toFloat() / maxAmount.toFloat())
        //        }
    }

    private data class TagAmount(val tag: Tag, val amount: BigDecimal)
}