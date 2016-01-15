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

package com.mvcoding.expensius.feature

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.widget.DatePicker
import com.mvcoding.expensius.RxBus
import com.mvcoding.expensius.extension.provideSingleton
import org.joda.time.DateTime
import rx.Observable

class DateDialogFragment : BaseDialogFragment(), DatePickerDialog.OnDateSetListener {
    private val rxBus = provideSingleton(RxBus::class)

    companion object {
        private const val ARG_YEAR = "ARG_YEAR"
        private const val ARG_MONTH_OF_YEAR = "ARG_MONTH_OF_YEAR"
        private const val ARG_DAY_OF_MONTH = "ARG_DAY_OF_MONTH"

        fun show(fragmentManager: FragmentManager, requestCode: Int, rxBus: RxBus, timestamp: Long): Observable<DateDialogResult> {
            val date = DateTime(timestamp)
            return show(fragmentManager, requestCode, rxBus, date.year, date.monthOfYear, date.dayOfMonth)
        }

        fun show(fragmentManager: FragmentManager,
                 requestCode: Int,
                 rxBus: RxBus,
                 year: Int,
                 monthOfYear: Int,
                 dayOfMonth: Int): Observable<DateDialogResult> {
            val args = Bundle()
            args.putInt(ARG_REQUEST_CODE, requestCode)
            args.putInt(ARG_YEAR, year)
            args.putInt(ARG_MONTH_OF_YEAR, monthOfYear)
            args.putInt(ARG_DAY_OF_MONTH, dayOfMonth)

            val fragment = DateDialogFragment()
            fragment.arguments = args
            fragment.show(fragmentManager, DateDialogFragment::class.java.name)

            return rxBus.observe(DateDialogResult::class.java)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle): Dialog {
        val year = arguments.getInt(ARG_YEAR)
        val monthOfYear = arguments.getInt(ARG_MONTH_OF_YEAR)
        val dayOfMonth = arguments.getInt(ARG_DAY_OF_MONTH)

        return DatePickerDialog(activity, this, year, monthOfYear - 1, dayOfMonth)
    }

    override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        rxBus.send(DateDialogResult(arguments.getInt(ARG_REQUEST_CODE), year, monthOfYear + 1, dayOfMonth))
    }

    class DateDialogResult(requestCode: Int, val year: Int, val monthOfYear: Int, val dayOfMonth: Int) : DialogResult(requestCode)
}