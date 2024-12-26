package com.example.alea

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import androidx.camera.core.CameraXConfig
import android.animation.ObjectAnimator
import android.view.View
import android.animation.Animator
import androidx.camera.camera2.Camera2Config


class ScannerAvatar : AppCompatActivity(), CameraXConfig.Provider {

    override fun getCameraXConfig(): CameraXConfig {
        return CameraXConfig.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .build()
    }
 /*   override fun getCameraXConfig() : CameraXConfig {
        return Camera2Config.defaultConfig()
    }*/

    // Declaración de vistas y variables
    private lateinit var previewView: PreviewView
    private lateinit var btnScan: Button
    private lateinit var btnSwitchCamera: ImageButton
    private lateinit var cameraExecutor: ExecutorService
    private var camera: androidx.camera.core.Camera? = null
    private var lensFacing: Int = CameraSelector.LENS_FACING_BACK
    private lateinit var scanBar: View
    private var isScanning = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanner_avatar)

        // Inicialización de vistas
        previewView = findViewById(R.id.previewView)
        btnScan = findViewById(R.id.btnScan)
        btnSwitchCamera = findViewById(R.id.btnSwitchCamera)
        scanBar = findViewById(R.id.scanBar)

        // Inicialización del executor para operaciones de cámara
        cameraExecutor = Executors.newSingleThreadExecutor()

        // Iniciar la cámara
        startCamera()

        // Acción del botón "Escanear"
        btnScan.setOnClickListener {
            Toast.makeText(this, "Escaneo iniciado (pendiente implementación)", Toast.LENGTH_SHORT).show()
        }

        // Acción del botón para cambiar la cámara
        btnSwitchCamera.setOnClickListener {
            toggleCamera()
        }
        btnScan.setOnClickListener {
            if (!isScanning) {
                startScanAnimation()
            } else {
                Toast.makeText(this, "Escaneo ya en progreso", Toast.LENGTH_SHORT).show()
            }
        }

    }
    private fun startScanAnimation() {
        isScanning = true
        scanBar.visibility = View.VISIBLE // Muestra la barra

        // Configura la animación (la barra baja desde arriba hasta el fondo)
        val animator = ObjectAnimator.ofFloat(scanBar, "translationY", 0f, previewView.height.toFloat())
        animator.duration = 3000 // Duración del escaneo (3 segundos)
        animator.start()

        // Finaliza la animación después de la duración
        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}

            override fun onAnimationEnd(animation: Animator) {
                scanBar.visibility = View.GONE // Oculta la barra
                scanBar.translationY = 0f // Resetea la posición de la barra
                isScanning = false
                Toast.makeText(this@ScannerAvatar, "Escaneo completo", Toast.LENGTH_SHORT).show()

                //funcion para regresar al menu principal
                scanBar.visibility = View.GONE // Oculta la barra
                scanBar.translationY = 0f // Resetea la posición de la barra
                isScanning = false

                // Generar datos aleatorios
                val randomData = generateRandomData()

                // Iniciar el MainActivity con los datos generados
                val intent = Intent(this@ScannerAvatar, MainActivity::class.java).apply {

                    putExtra("edad", randomData["edad"])
                    putExtra("peso", randomData["peso"])
                    putExtra("altura", randomData["altura"])
                    putExtra("genero", randomData["genero"])
                }
                startActivity(intent)
                finish()
            }

            override fun onAnimationCancel(animation: Animator) {}

            override fun onAnimationRepeat(animation: Animator) {}
        })

    }
    private fun generateRandomData(): Map<String, String> {

        val generos = listOf("Masculino", "Femenino")
        val pesos = (50..100).map { it.toString() } // Pesos entre 50 y 100 kg
        val alturas = (150..200).map { it.toString() } // Alturas entre 150 y 200 cm
        val edades = (18..60).map { it.toString() } // Edades entre 18 y 60 años

        return mapOf(

            "genero" to generos.random(),
            "peso" to pesos.random(),
            "altura" to alturas.random(),
            "edad" to edades.random()
        )
    }

    /**
     * Método para iniciar la cámara
     */
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            // Configuración de la vista previa
            val preview = androidx.camera.core.Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            // Selección de cámara basada en la orientación actual
            val cameraSelector = CameraSelector.Builder()
                .requireLensFacing(lensFacing)
                .build()

            try {
                // Desvincula cualquier uso previo de la cámara antes de volver a vincularla
                cameraProvider.unbindAll()

                // Vincula la cámara al ciclo de vida del Activity
                camera = cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview
                )
            } catch (e: Exception) {
                Log.e("CameraX", "Error al iniciar la cámara", e)
                Toast.makeText(this, "Error al iniciar la cámara", Toast.LENGTH_SHORT).show()
            }
        }, ContextCompat.getMainExecutor(this))
    }

    /**
     * Método para cambiar entre cámara trasera y delantera
     */
    private fun toggleCamera() {
        lensFacing = if (lensFacing == CameraSelector.LENS_FACING_BACK) {
            Toast.makeText(this, "Cambiando a cámara delantera", Toast.LENGTH_SHORT).show()
            CameraSelector.LENS_FACING_FRONT
        } else {
            Toast.makeText(this, "Cambiando a cámara trasera", Toast.LENGTH_SHORT).show()
            CameraSelector.LENS_FACING_BACK
        }
        startCamera() // Reinicia la cámara con la nueva configuración
    }

    /**
     * Liberar recursos del executor al destruir la actividad
     */
    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}
