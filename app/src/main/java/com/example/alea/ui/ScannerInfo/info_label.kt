package com.example.alea.ui.ScannerInfo

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.alea.R
import kotlinx.coroutines.launch

class InfoLabelActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var textView: TextView
    private val chatGPTClient = ScannerChatGPTClient() // Asegúrate de que esta clase está correctamente definida

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_info_label)

        // Inicializar los elementos de la interfaz
        textView = findViewById(R.id.text_view)

        // Capturar el texto enviado desde la otra actividad
        val receivedText = intent.getStringExtra("processed_text") // Captura el texto desde el Intent
        if (!receivedText.isNullOrEmpty()) {
            // Mostrar el texto recibido en el TextView
            textView.text = "Procesando: $receivedText"

            // Enviar el texto a ChatGPTClient
            chatGPTClient.enviarPrompt(receivedText) { response ->
                runOnUiThread {
                    if (!response.isNullOrEmpty()) {
                        // Mostrar la respuesta en el TextView
                        textView.text = response
                    } else {
                        Toast.makeText(
                            this@InfoLabelActivity,
                            "Error al recibir respuesta de ChatGPT.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        } else {
            // Manejar el caso en el que no se recibe texto
            textView.text = "No se recibió ningún texto."
            Toast.makeText(
                this@InfoLabelActivity,
                "No se detectó texto para procesar. Inténtalo de nuevo.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}

/*
// Mostrar el texto en el TextView
if (receivedText != null) {
    textView.text = receivedText // Asignar el texto recibido al TextView
} else {
    textView.text = "No se recibió ningún texto" // Manejo en caso de que no haya datos
}*/



