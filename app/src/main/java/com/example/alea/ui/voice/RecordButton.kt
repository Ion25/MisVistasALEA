package com.example.alea.ui.voice

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.ImageButton
import androidx.appcompat.widget.AppCompatImageButton

class RecordButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : AppCompatImageButton(context, attrs) {

    private var mediaRecorder: MediaRecorder? = null
    private var isRecording = false

    init {
        setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    vibrate() // Vibrar al inicio
                    startRecording() // Iniciar grabación
                    setPressed(true) // Cambiar apariencia
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    stopRecording() // Detener grabación
                    setPressed(false) // Restaurar apariencia
                }
            }
            true // Consumir el evento táctil
        }
    }

    private fun vibrate() {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(50)
        }
    }

    private fun startRecording() {
        if (isRecording) return
        isRecording = true
        mediaRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setOutputFile(context.getExternalFilesDir(null)?.absolutePath + "/audio_record.3gp")
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            prepare()
            start()
        }
    }

    private fun stopRecording() {
        if (!isRecording) return
        isRecording = false
        mediaRecorder?.apply {
            stop()
            release()
        }
        mediaRecorder = null
    }
}
