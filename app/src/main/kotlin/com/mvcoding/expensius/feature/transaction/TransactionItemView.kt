package com.mvcoding.expensius.feature.transaction

import android.content.Context
import android.graphics.PorterDuff.Mode.SRC_ATOP
import android.graphics.Rect
import android.support.v4.content.ContextCompat.getDrawable
import android.text.SpannableStringBuilder
import android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
import android.text.style.ImageSpan
import android.text.style.ImageSpan.ALIGN_BASELINE
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.LinearLayout
import com.mvcoding.expensius.R
import com.mvcoding.expensius.extension.getColorFromTheme
import com.mvcoding.expensius.extension.inflate
import com.mvcoding.expensius.model.Tag
import com.mvcoding.expensius.model.Transaction
import com.mvcoding.expensius.model.TransactionType.EXPENSE
import com.mvcoding.expensius.provideAmountFormatter
import com.mvcoding.expensius.provideDateFormatter
import kotlinx.android.synthetic.main.item_view_transaction.view.*

class TransactionItemView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        LinearLayout(context, attrs, defStyleAttr) {

    private val NON_BREAKABLE_SPACE = "\u00A0"

    private val amountFormatter by lazy { provideAmountFormatter() }
    private val dateFormatter by lazy { provideDateFormatter() }
    private val expenseTextColor by lazy { getColorFromTheme(context, R.attr.colorExpense) }
    private val incomeTextColor by lazy { getColorFromTheme(context, R.attr.colorIncome) }
    private val tagDrawable by lazy { getDrawable(context, R.drawable.oval) }

    companion object {
        fun inflate(parent: ViewGroup) = parent.inflate<TransactionItemView>(R.layout.item_view_transaction)
    }

    fun setTransaction(transaction: Transaction) {
        noteTextView.text = transactionOrDefaultNote(transaction)
        tagsTextView.text = formatTags(transaction.tags)
        tagsTextView.visibility = if (tagsTextView.text.isBlank()) GONE else VISIBLE
        amountTextView.text = amountFormatter.format(transaction.amount, transaction.currency)
        amountTextView.setTextColor(amountTextColor(transaction))
        dateTextView.text = dateFormatter.formatDateRelativeToToday(transaction.timestamp)
    }

    private fun transactionOrDefaultNote(transaction: Transaction) =
            if (transaction.note.isNotBlank()) transaction.note
            else resources.getString(if (transaction.transactionType == EXPENSE) R.string.expense else R.string.income)

    private fun formatTags(tags: Set<Tag>): CharSequence {
        val ssb = SpannableStringBuilder()
        tags.sortedBy { it.order.value }.forEach {
            if (ssb.length > 0) ssb.append("  ")

            ssb.append("$NON_BREAKABLE_SPACE$NON_BREAKABLE_SPACE")
            ssb.append(it.title.text.replace(" ", NON_BREAKABLE_SPACE))

            val drawable = tagDrawable.constantState.newDrawable().mutate()
            val drawableSize = tagsTextView.paint.textSize - tagsTextView.paint.textSize * 0.25f
            drawable.setColorFilter(it.color.rgb, SRC_ATOP)
            drawable.bounds = Rect(0, 0, drawableSize.toInt(), drawableSize.toInt())
            ssb.setSpan(ImageSpan(drawable, ALIGN_BASELINE),
                    ssb.length - it.title.text.length - 2,
                    ssb.length - it.title.text.length - 1,
                    SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        return ssb
    }

    private fun amountTextColor(transaction: Transaction) = if (transaction.transactionType == EXPENSE) expenseTextColor else incomeTextColor
}