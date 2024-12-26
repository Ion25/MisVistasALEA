/*
package com.example.alea

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.alea.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
}
*/

package com.example.alea

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.NumberPicker
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import android.content.Intent
import android.graphics.Color
import android.widget.Button
import android.widget.Toast
import android.widget.VideoView
import android.net.Uri
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatImageButton
import com.airbnb.lottie.LottieAnimationView

class MainActivity : AppCompatActivity() {

    private lateinit var avatarView: AvatarView  // Referencia al avatar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Referencia al AvatarView
        avatarView = findViewById(R.id.avatarView)

        val cardSexo = findViewById<CardView>(R.id.cardSexo)
        val tvSexo = findViewById<TextView>(R.id.tvSexo)

        val cardEdad = findViewById<CardView>(R.id.cardEdad)
        val tvEdad = findViewById<TextView>(R.id.tvEdad)

        val cardAltura = findViewById<CardView>(R.id.cardAltura)
        val tvAltura = findViewById<TextView>(R.id.tvAltura)

        val cardPeso = findViewById<CardView>(R.id.cardPeso)
        val tvPeso = findViewById<TextView>(R.id.tvPeso)



        // Botón para abrir ScannerAvatar
        val animationScannerView: LottieAnimationView = findViewById(R.id.animationScanner)
        animationScannerView.setOnClickListener {
            val intent = Intent(this, ScannerAvatar::class.java)
            startActivity(intent)
        }


        val primerFormulario: LinearLayout = findViewById(R.id.primerFormulario)
        val segundoFormulario: LinearLayout = findViewById(R.id.segundoFormulario)

        // Campos del formulario

        val edadField: TextView = findViewById(R.id.tvEdad)
        val pesoField: TextView = findViewById(R.id.tvPeso)
        val alturaField: TextView = findViewById(R.id.tvAltura)
        val generoField: TextView = findViewById(R.id.tvSexo)

        // Recibir datos del Intent
        val intent = intent
        val edad = intent.getStringExtra("edad")
        val peso = intent.getStringExtra("peso")
        val altura = intent.getStringExtra("altura")
        val genero = intent.getStringExtra("genero")
        val btnContinuar: LottieAnimationView = findViewById(R.id.btnContinuar)
        val btnRegresar: LottieAnimationView = findViewById(R.id.btnRegresar)

        val rootView = findViewById<View>(R.id.segundoFormulario) // Asegúrate de usar el ID del contenedor principal
        rootView.setOnTouchListener { _, _ ->
            hideKeyboard()
            false
        }



        edadField.text = "Edad: ${edad ?: ""}"
        pesoField.text = "Peso: ${peso ?: ""} kg"
        alturaField.text = "Altura: ${altura ?: ""} cm"
        generoField.text = "Género: ${genero ?: ""}"
        // Verificar que los datos del escaneo no sean nulos o estén en blanco
        if (!genero.isNullOrBlank() && !altura.isNullOrBlank() && !peso.isNullOrBlank()) {
            // Actualizar el avatar con los datos
            actualizarAvatar(sexo = genero, altura = altura, peso = peso)
            cardSexo.setCardBackgroundColor(Color.parseColor("#D1F8D7"))
        } else {
            // Mostrar un mensaje si los datos no están disponibles
            Toast.makeText(
                this,
                "Realiza un escaneo para actualizar el avatar.",
                Toast.LENGTH_SHORT
            ).show()
        }

        if (!edad.isNullOrBlank()) {
            tvEdad.text = "Edad: $edad años"
            cardEdad.setCardBackgroundColor(Color.parseColor("#D1F8D7"))

        }
        if (!altura.isNullOrBlank()) {
            tvAltura.text = "Altura: $altura cm"
            cardAltura.setCardBackgroundColor(Color.parseColor("#D1F8D7"))
        }
        if (!peso.isNullOrBlank()) {
            tvPeso.text = "Peso: $peso kg"
            cardPeso.setCardBackgroundColor(Color.parseColor("#D1F8D7"))
        }

        // Selector para Sexo
        cardSexo.setOnClickListener {
            val opciones = arrayOf("Masculino", "Femenino")
            AlertDialog.Builder(this)
                .setTitle("Selecciona tu genero")
                .setItems(opciones) { _, which ->
                    tvSexo.text = "Genero: ${opciones[which]}"
                    cardSexo.setCardBackgroundColor(Color.parseColor("#D1F8D7"))
                    actualizarAvatar(
                        tvSexo.text.toString(),
                        tvAltura.text.toString(),
                        tvPeso.text.toString()
                    )
                    actualizarEstadoFormulario()
                }.show()
        }

        // Selector para Edad
        cardEdad.setOnClickListener {
            showNumberPicker("Edad", 18, 99) { value ->
                tvEdad.text = "Edad: $value años"
                cardEdad.setCardBackgroundColor(Color.parseColor("#D1F8D7"))
                actualizarEstadoFormulario()
            }
        }

        // Selector para Altura
        cardAltura.setOnClickListener {
            showNumberPicker("Altura", 100, 250) { value ->
                tvAltura.text = "Altura: $value cm"
                cardEdad.setCardBackgroundColor(Color.parseColor("#D1F8D7"))
                actualizarEstadoFormulario()
                actualizarAvatar(
                    tvSexo.text.toString(),
                    tvAltura.text.toString(),
                    tvPeso.text.toString()
                )
                actualizarEstadoFormulario()
            }
        }

        // Selector para Peso
        cardPeso.setOnClickListener {
            showNumberPicker("Peso", 30, 200) { value ->
                tvPeso.text = "Peso: $value kg"
                cardAltura.setCardBackgroundColor(Color.parseColor("#D1F8D7"))
                actualizarAvatar(
                    tvSexo.text.toString(),
                    tvAltura.text.toString(),
                    tvPeso.text.toString()
                )
                actualizarEstadoFormulario()
            }
        }
        val videoView: VideoView = findViewById(R.id.videoViewFondo)

        // Ruta del video en la carpeta raw
        val videoUri = Uri.parse("android.resource://" + packageName + "/" + R.raw.fondo)

        // Configurar el VideoView para reproducir el video
        videoView.setVideoURI(videoUri)
        videoView.setOnPreparedListener { mediaPlayer ->
            mediaPlayer.isLooping = true // Hacer que el video se repita
        }

        videoView.start() // Iniciar la reproducción del video
        /*
        btnContinuar.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            intent.putExtra("gender", genero)
            intent.putExtra("height", altura)
            intent.putExtra("weight", peso)
            startActivity(intent)
        }


*/
        primerFormulario.visibility = View.VISIBLE
        segundoFormulario.visibility = View.GONE
        btnContinuar.visibility = View.GONE
        btnRegresar.visibility = View.GONE
        btnContinuar.setOnClickListener {
            primerFormulario.animate()
                .alpha(0f)
                .setDuration(300)
                .withEndAction {
                    primerFormulario.visibility = View.GONE
                    segundoFormulario.alpha = 0f
                    segundoFormulario.visibility = View.VISIBLE
                    segundoFormulario.animate()
                        .alpha(1f)
                        .setDuration(300)
                        .withEndAction {
                            btnContinuar.visibility = View.GONE
                            btnRegresar.visibility = View.VISIBLE// Ocultar después de la animación
                        }
                        .start()
                }
                .start()
        }


        // Botón "Regresar" - Vuelve al primer formulario
        btnRegresar.setOnClickListener {
            // Animar el segundo formulario para desaparecer
            segundoFormulario.animate()
                .alpha(0f)
                .setDuration(300)
                .withEndAction {
                    segundoFormulario.visibility = View.GONE // Ocultar el segundo formulario
                    primerFormulario.alpha = 0f // Prepara el primer formulario para la animación
                    primerFormulario.visibility = View.VISIBLE // Muestra el primer formulario
                    primerFormulario.animate()
                        .alpha(1f) // Aparece el primer formulario
                        .setDuration(300)
                        .withEndAction {
                            btnContinuar.visibility = View.VISIBLE // Mostrar el botón continuar
                            btnRegresar.visibility = View.GONE
                        }
                        .start()
                }
                .start()
        }
        // formulario 2
        val actividades = arrayOf("Sedentario", "Activo", "Muy Activo")
        val objetivos = arrayOf("Perder peso", "Mantener peso", "Ganar peso")

        // Referencias a los TextView
        val tvActividad: TextView = findViewById(R.id.tvActividad)
        val tvObjetivo: TextView = findViewById(R.id.tvObjetivo)
        val etNombre: EditText = findViewById(R.id.etNombre)
        val btnCrearPerfil: Button = findViewById(R.id.btnCrearPerfil)
        val cardObjetivo: CardView = findViewById(R.id.cardObjetivo)
        val cardActividad: CardView = findViewById(R.id.cardActividad)
        val cardNombre: CardView = findViewById(R.id.cardNombre)

        // Cambiar el fondo al llenar `etNombre`
        etNombre.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (etNombre.text.isNotEmpty()) {
                    cardNombre.setBackgroundColor(Color.parseColor("#D1F8D7")) // Verde claro
                } else {
                    cardNombre.setBackgroundColor(Color.WHITE) // Fondo blanco
                }
                actualizarEstadoFormulario()
            }
            override fun afterTextChanged(s: Editable?) {}
        })
// Mostrar diálogo para Nivel de Actividad Física
        tvActividad.setOnClickListener {
            val builder = androidx.appcompat.app.AlertDialog.Builder(this)
            builder.setTitle("Selecciona tu nivel de actividad")
            builder.setItems(actividades) { _, which ->
                tvActividad.text = actividades[which] // Actualiza el TextView
                cardActividad.setBackgroundColor(Color.parseColor("#D1F8D7")) // Cambiar a verde claro
                actualizarEstadoFormulario()
            }
            builder.show()
        }

// Mostrar diálogo para Objetivo
        tvObjetivo.setOnClickListener {
            val builder = androidx.appcompat.app.AlertDialog.Builder(this)
            builder.setTitle("¿Cuál es tu objetivo?")
            builder.setItems(objetivos) { _, which ->
                tvObjetivo.text = objetivos[which] // Actualiza el TextView
                cardObjetivo.setCardBackgroundColor(Color.parseColor("#D1F8D7")) // Cambiar a verde claro
                actualizarEstadoFormulario()
            }
            builder.show()
        }

        findViewById<Button>(R.id.btnCrearPerfil).setOnClickListener {
            val nombre = findViewById<EditText>(R.id.etNombre).text.toString()

            if (nombre.isBlank()) {
                Toast.makeText(this, "Por favor ingresa tu nombre", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Mostrar animación
            val lottieAnimation = LottieAnimationView(this).apply {
                setAnimation(R.raw.loading_animation)
                loop(true)
                playAnimation()
            }
            val dialog = androidx.appcompat.app.AlertDialog.Builder(this)
                .setView(lottieAnimation)
                .setCancelable(false)
                .create()

            dialog.show()

            // Simular proceso
            Handler().postDelayed({
                dialog.dismiss()
                val intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)
                overridePendingTransition(
                    android.R.anim.slide_in_left,
                    android.R.anim.slide_out_right
                )
                finish()
            }, 3000) // 3 segundos de simulación
        }
        btnCrearPerfil.setOnClickListener {
            // Capturar datos del primer formulario
            val genero = tvSexo.text.toString().removePrefix("Genero: ")
            val edad = tvEdad.text.toString().removePrefix("Edad: ").removeSuffix(" años")
            val altura = tvAltura.text.toString().removePrefix("Altura: ").removeSuffix(" cm")
            val peso = tvPeso.text.toString().removePrefix("Peso: ").removeSuffix(" kg")

            // Capturar datos del segundo formulario
            val nombre = etNombre.text.toString()
            val actividad = tvActividad.text.toString()
            val objetivo = tvObjetivo.text.toString()

            // Validar que todos los campos estén llenos
            if (nombre.isBlank() || actividad == "Selecciona actividad" || objetivo == "Selecciona objetivo") {
                Toast.makeText(this, "Por favor completa todos los campos del formulario", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Crear un Intent para pasar los datos a ProfileActivity
            val intent = Intent(this, ProfileActivity::class.java).apply {
                putExtra("gender", genero)
                putExtra("age", edad)
                putExtra("height", altura)
                putExtra("weight", peso)
                putExtra("name", nombre)
                putExtra("activity", actividad)
                putExtra("goal", objetivo)
            }

            // Iniciar ProfileActivity
            startActivity(intent)
        }

        // Función para verificar si todos los campos están llenos
        fun verificarCamposLlenos() {
            val camposLlenos = tvSexo.text.toString() != "Género: " &&
                    tvEdad.text.toString() != "Edad: " &&
                    tvAltura.text.toString() != "Altura: cm" &&
                    tvPeso.text.toString() != "Peso: kg" &&
                    tvSexo.text.isNotEmpty() &&
                    tvEdad.text.isNotEmpty() &&
                    tvAltura.text.isNotEmpty() &&
                    tvPeso.text.isNotEmpty()

            if (camposLlenos) {
                btnContinuar.visibility = View.VISIBLE
                btnContinuar.playAnimation()
            } else {
                btnContinuar.visibility = View.GONE
                btnContinuar.cancelAnimation()
            }
        }

        actualizarEstadoFormulario()
        // Listeners para detectar cambios en los campos
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                verificarCamposLlenos()
            }
            override fun afterTextChanged(s: Editable?) {}
        }

        tvPeso.addTextChangedListener(textWatcher)
        tvEdad.addTextChangedListener(textWatcher)
        tvAltura.addTextChangedListener(textWatcher)
        tvSexo.addTextChangedListener(textWatcher)
        verificarCamposLlenos()
    }

    private fun showNumberPicker(title: String, min: Int, max: Int, onValueSelected: (Int) -> Unit) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_number_picker, null)
        val numberPicker = dialogView.findViewById<NumberPicker>(R.id.numberPicker)
        numberPicker.minValue = min
        numberPicker.maxValue = max
        numberPicker.value = min

        AlertDialog.Builder(this)
            .setTitle(title)
            .setView(dialogView)
            .setPositiveButton("OK") { _, _ ->
                onValueSelected(numberPicker.value)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    // Método para actualizar el avatar dinámicamente
    private fun actualizarAvatar(sexo: String, altura: String, peso: String) {
        val genero = if (sexo.contains("Masculino")) "Masculino" else "Femenino"

        // Convertir peso a número entero
        val pesoNumerico = peso.filter { it.isDigit() }.toIntOrNull() ?: 0

        // Categorizar peso en rangos
        val pesoSimplificado = when {
            pesoNumerico in 30..60 -> "Ligero"
            pesoNumerico in 61..75 -> "Medio"
            pesoNumerico > 76 -> "Pesado"
            else -> "Pesado" // Para valores fuera del rango esperado
        }

        avatarView.updateAvatar(genero, altura, pesoSimplificado)
    }
    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        currentFocus?.let {
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }
    private fun actualizarEstadoFormulario() {
        val etNombre: EditText = findViewById(R.id.etNombre)
        val tvActividad: TextView = findViewById(R.id.tvActividad)
        val tvObjetivo: TextView = findViewById(R.id.tvObjetivo)
        val btnCrearPerfil: Button = findViewById(R.id.btnCrearPerfil)

        // Verificar si los campos están llenos
        val camposLlenos = etNombre.text.isNotEmpty() &&
                tvActividad.text != "Selecciona actividad" &&
                tvObjetivo.text != "Selecciona objetivo"

        // Actualizar el estado del botón
        if (camposLlenos) {
            btnCrearPerfil.isEnabled = true
            btnCrearPerfil.setBackgroundColor(Color.parseColor("#22BA37")) // Verde
        } else {
            btnCrearPerfil.isEnabled = false
            btnCrearPerfil.setBackgroundColor(Color.GRAY) // Gris
        }
    }
    override fun onResume() {
        super.onResume()
        val videoView: VideoView = findViewById(R.id.videoViewFondo)
        val videoUri = Uri.parse("android.resource://" + packageName + "/" + R.raw.fondo)
        videoView.setVideoURI(videoUri)
        videoView.setOnPreparedListener { mediaPlayer ->
            mediaPlayer.isLooping = true
        }
        videoView.start() // Reiniciar el video
        avatarView.visibility = View.VISIBLE
    }


}
