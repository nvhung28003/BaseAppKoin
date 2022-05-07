package com.example.baseappkoin.customview.datetimepicker

import android.content.Context
import android.util.AttributeSet
import com.example.baseappkoin.R
import com.example.baseappkoin.utils.CalendarConfig
import java.util.*

class WheelHourPicker @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    WheelPicker(context, attrs) {
    private var selectedHour: Int
    private fun updateSelectedYear() {
        setSelectedItemPosition(selectedHour, true)
    }

    override fun setData(data: List<String>) {
        throw UnsupportedOperationException("You can not invoke setData in WheelHourPicker")
    }

    fun getSelectedHour(): Int {
        return selectedHour
    }

    fun setSelectedHour(hour: Int) {
        selectedHour = hour
        updateSelectedYear()
    }

    override fun run() {
        super.run()
        selectedHour = getData()[getCurrentItemPosition()].filter { it.isDigit() }.toInt()
    }

    init {
        val data: MutableList<String> = ArrayList()
        for (i in 0..CalendarConfig.NUMBER_HOUR) data.add(i.toString() + context.getString(R.string.title_hour))
        super.setData(data)
        selectedHour = Calendar.getInstance()[Calendar.MONTH] + 1
        updateSelectedYear()
    }
}