package com.example.alea


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.widget.ImageView
import com.example.alea.ui.ScannerInfo.ScannerActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProfileActivity : BaseActivity.BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Recibir los datos desde el Intent
        val gender = intent.getStringExtra("gender") ?: "Desconocido"
        val age = intent.getStringExtra("age") ?: "N/A"
        val height = intent.getStringExtra("height") ?: "N/A"
        val weight = intent.getStringExtra("weight") ?: "N/A"
        val name = intent.getStringExtra("name") ?: "Sin Nombre"
        val activity = intent.getStringExtra("activity") ?: "N/A"
        val goal = intent.getStringExtra("goal") ?: "N/A"

        // Configurar los datos en los TextViews
        findViewById<TextView>(R.id.tvUsername).text = name
        findViewById<TextView>(R.id.tvGender).text = "Género: $gender"
        findViewById<TextView>(R.id.tvHeight).text = "Altura: $height cm"
        findViewById<TextView>(R.id.tvWeight).text = "Peso: $weight kg"

        // Si tienes un Spinner para "Actividad" y "Objetivo", considera agregar datos o actualizaciones a ellos.
        // Aquí, simplemente se mostrará el texto como ejemplo.
        findViewById<TextView>(R.id.tvActivityLevelLabel).text = "Nivel de Actividad: $activity"
        findViewById<TextView>(R.id.tvGoalLabel).text = "Objetivo: $goal"

        // Configurar el avatar según el género y peso
        val avatarView = findViewById<ImageView>(R.id.ivAvatar)
        val avatarResource = when (gender.lowercase()) {
            "masculino" -> when (weight.lowercase()) {
                "ligero" -> R.drawable.avatar_hombre_ligero
                "medio" -> R.drawable.avatar_hombre_medio
                "pesado" -> R.drawable.avatar_hombre_pesado
                else -> R.drawable.avatar_hombre_medio
            }
            "femenino" -> when (weight.lowercase()) {
                "ligero" -> R.drawable.avatar_mujer_ligera
                "medio" -> R.drawable.avatar_mujer_media
                "pesado" -> R.drawable.avatar_mujer_pesada
                else -> R.drawable.avatar_mujer_media
            }
            else -> R.drawable.avatar_hombre_ligero
        }
        avatarView.setImageResource(avatarResource)
    }
}