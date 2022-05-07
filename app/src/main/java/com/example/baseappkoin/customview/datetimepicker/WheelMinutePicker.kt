package com.example.baseappkoin.customview.datetimepicker

import android.content.Context
import android.util.AttributeSet
import com.example.baseappkoin.R
import com.example.baseappkoin.utils.CalendarConfig
import java.util.*

class WheelMinutePicker @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    WheelPicker(context, attrs) {
    private var selectedMinute: Int
    private fun updateSelectedYear() {
        setSelectedItemPosition(selectedMinute, true)
    }

    override fun setData(data: List<String>) {
        throw UnsupportedOperationException("You can not invoke setData in WheelMinutePicker")
    }

    fun getSelectedMinute(): Int {
        return selectedMinute
    }

    fun setSelectedMinute(minute: Int) {
        selectedMinute = minute
        updateSelectedYear()
    }

    override fun run() {
        super.run()
        selectedMinute = getData()[getCurrentItemPosition()].filter { it.isDigit() }.toInt()
    }

    init {
        val data: MutableList<String> = ArrayList()
        for (i in 0..CalendarConfig.NUMBER_MINUTE) data.add(i.toString() + context.getString(R.string.title_minute))
        super.setData(data)
        selectedMinute = Calendar.getInstance()[Calendar.MONTH] + 1
        updateSelectedYear()
    }
}