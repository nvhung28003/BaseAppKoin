package com.example.baseappkoin.customview.datetimepicker

import android.content.Context
import android.util.AttributeSet
import com.example.baseappkoin.R
import com.example.baseappkoin.utils.CalendarConfig
import java.util.*

class WheelSecondPicker @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    WheelPicker(context, attrs) {
    private var selectedSecond: Int
    private fun updateSelectedSecond() {
        setSelectedItemPosition(selectedSecond, true)
    }

    override fun setData(data: List<String>) {
        throw UnsupportedOperationException("You can not invoke setData in WheelMonthPicker")
    }

    fun getSelectedSecond(): Int {
        return selectedSecond
    }

    fun setSelectedSecond(second: Int) {
        selectedSecond = second
        updateSelectedSecond()
    }

    override fun run() {
        super.run()
        selectedSecond = getData()[getCurrentItemPosition()].filter { it.isDigit() }.toInt()
    }

    init {
        val data: MutableList<String> = ArrayList()
        for (i in 0..CalendarConfig.NUMBER_SECOND) data.add(i.toString() + context.getString(R.string.title_second))
        super.setData(data)
        selectedSecond = Calendar.getInstance()[Calendar.MONTH] + 1
        updateSelectedSecond()
    }
}