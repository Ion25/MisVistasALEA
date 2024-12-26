package com.example.midiariop.ui.home.date

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DateViewModel : ViewModel() {

    private val _selectedDate = MutableLiveData<DateItem>()
    val selectedDate: LiveData<DateItem> get() = _selectedDate

    private val _dates = MutableLiveData<List<DateItem>>()
    val dates: LiveData<List<DateItem>> get() = _dates

    init {
        _dates.value = generateDates()
        _selectedDate.value = _dates.value?.firstOrNull { it.isSelected }
    }

    fun selectDate(dateItem: DateItem) {
        _dates.value = _dates.value?.map {
            it.copy(isSelected = it.date == dateItem.date)
        }
        _selectedDate.value = dateItem
    }

    private fun generateDates(): List<DateItem> {
        return listOf(
            DateItem("AYER, 21 DIC", false),
            DateItem("HOY, 22 DIC", true),
            DateItem("MAÑANA, 23 DIC", false),
            DateItem("SÁBADO, 24 DIC", false)
        )
    }
}