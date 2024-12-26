package com.example.alea.ui.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.alea.ui.dashboard.SpeechHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CalorieTrackerViewModel(application: Application) : AndroidViewModel(application)  {
    private val speechHelper = SpeechHelper(application)

    fun startListening(onResult: (String) -> Unit) {
        viewModelScope.launch(Dispatchers.Main) {
            speechHelper.startListening(
                onResult = { result -> onResult(result) },
                onError = { error -> onResult("Error: $error") }
            )
        }
    }
}