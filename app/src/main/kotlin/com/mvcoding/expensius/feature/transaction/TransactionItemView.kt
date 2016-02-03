package com.mvcoding.expensius.feature.transaction

import android.content.Context
import android.text.SpannableStringBuilder
import android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.mvcoding.expensius.R
import com.mvcoding.expensius.extension.provideSingleton
import com.mvcoding.expensius.feature.AmountFormatter
import com.mvcoding.expensius.feature.DateFormatter
import com.mvcoding.expensius.feature.tag.Tag
import com.mvcoding.expensius.feature.transaction.TransactionType.EXPENSE

class TransactionItemView : LinearLayout {
    private val amountFormatter by lazy { provideSingleton(AmountFormatter::class) }
    private val dateFormatter by lazy { provideSingleton(DateFormatter::class) }

    private val tagsTextView by lazy { findViewById(R.id.tagsTextView) as TextView }
    private val noteTextView by lazy { findViewById(R.id.noteTextView) as TextView }
    private val amountTextView by lazy { findViewById(R.id.amountTextView) as TextView }
    private val dateTextView by lazy { findViewById(R.id.dateTextView) as TextView }

    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    companion object {
        fun inflate(parent: ViewGroup) = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_view_transaction, parent, false) as TransactionItemView
    }

    fun setTransaction(transaction: Transaction) {
        dateTextView.text = dateFormatter.formatDateRelativeToToday(transaction.timestamp)
        amountTextView.text = amountFormatter.format(transaction.amount, transaction.currency)
        noteTextView.text = transaction.note
        tagsTextView.text = formatTags(transaction.tags, transaction.transactionType)
    }

    private fun formatTags(tags: Set<Tag>, transactionType: TransactionType): CharSequence {
        val ssb = SpannableStringBuilder()
        tags.forEach {
            if (ssb.length > 0) ssb.append(" ")

            ssb.append(it.title)
            ssb.setSpan(ForegroundColorSpan(it.color), ssb.length - it.title.length, ssb.length, SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        if (ssb.length == 0) {
            ssb.append(resources.getString(if (transactionType == EXPENSE) R.string.expense else R.string.income))
        }

        return ssb
    }
}