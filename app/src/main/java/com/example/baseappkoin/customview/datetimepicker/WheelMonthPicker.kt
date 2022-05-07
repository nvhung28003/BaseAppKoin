package com.example.baseappkoin.customview.datetimepicker

import android.content.Context
import android.util.AttributeSet
import com.example.baseappkoin.R
import com.example.baseappkoin.utils.CalendarConfig
import java.util.*

class WheelMonthPicker @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    WheelPicker(context, attrs) {
    private var selectedMonth: Int
    private fun updateSelectedMonth() {
        setSelectedItemPosition(selectedMonth - 1, true)
    }

    override fun setData(data: List<String>) {
        throw UnsupportedOperationException("You can not invoke setData in WheelMonthPicker")
    }

    fun getSelectedMonth(): Int {
        return selectedMonth
    }

    fun setSelectedMonth(month: Int) {
        selectedMonth = month
        updateSelectedMonth()
    }

    override fun run() {
        super.run()
        selectedMonth = getData()[getCurrentItemPosition()].filter { it.isDigit() }.toInt()
    }

    init {
        val data: MutableList<String> = ArrayList()
        for (i in 1..CalendarConfig.NUMBER_MONTH_IN_YEAR) data.add(
            i.toString() + context.getString(
                R.string.title_month
            )
        )
        super.setData(data)
        selectedMonth = Calendar.getInstance()[Calendar.MONTH] + 1
        updateSelectedMonth()
    }
}