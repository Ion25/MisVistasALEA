package com.example.alea.ui.voice

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class VoiceViewModel(application: Application) : AndroidViewModel(application) {
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
