package com.example.alea.ui.home


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alea.ui.dashboard.SpeechHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// Estructura de los datos de cada fecha
data class Data(
    val progress: Float,
    val carbs: Int,
    val proteins: Int,
    val fats: Int
)

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val speechHelper = SpeechHelper(application)
    fun startListening(onResult: (String) -> Unit) {
        viewModelScope.launch(Dispatchers.Main) {
            speechHelper.startListening(
                onResult = { result -> onResult(result) },
                onError = { error -> onResult("Error: $error") }
            )
        }
    }

    // Fechas y la fecha seleccionada
    private val _dates = MutableLiveData<List<String>>()
    val dates: LiveData<List<String>> = _dates

    private val _selectedDate = MutableLiveData<String>()
    val selectedDate: LiveData<String> = _selectedDate

    // Mapa de datos por fecha
    private val dateData = mutableMapOf<String, Data>()

    // Función para seleccionar una fecha
    fun selectDate(date: String) {
        _selectedDate.value = date
    }

    // Obtener los datos de una fecha específica
    fun getDataForDate(date: String): Data {
        return dateData[date] ?: Data(0f, 0, 0, 0) // Valores predeterminados si no hay datos
    }

    // Guardar datos para una fecha
    fun saveDataForDate(date: String, data: Data) {
        dateData[date] = data
    }

    // Cargar las fechas
    fun loadDates() {
        _dates.value = listOf("AYER, 25 DIC", "HOY, 26 DIC", "MAÑANA, 27 DIC")
    }
}

/*
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text
}
 */