package com.example.alea

import android.content.Intent
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.alea.ui.ScannerInfo.ScannerActivity
import com.example.alea.ui.home.HomeFragment

class BaseActivity {
    open class BaseActivity : AppCompatActivity() {

        override fun setContentView(layoutResID: Int) {
            // Infla el diseño común para la base de todas las actividades
            val fullLayout = layoutInflater.inflate(R.layout.activity_base, null)
            val container = fullLayout.findViewById<FrameLayout>(R.id.activityContent)

            // Infla el diseño específico de la actividad y lo agrega al contenedor
            layoutInflater.inflate(layoutResID, container, true)
            super.setContentView(fullLayout)

            // Configurar el BottomNavigationView
            val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
            bottomNavigationView.setOnNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.nav_profile -> {
                        val intent = Intent(this, ProfileActivity::class.java)
                        startActivity(intent)
                        true
                    }
                    R.id.nav_home -> {
                        val fragment = HomeFragment()
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.activityContent, fragment) // Reemplaza el contenido actual con el fragmento
                            .commit()
                        true
                    }
                    R.id.nav_scan -> {
                        val intent = Intent(this, ScannerActivity::class.java)
                        startActivity(intent)
                        true
                    }
                    R.id.nav_food -> {
                        // Lógica para Configuración
                        val intent = Intent(this, CompatibilityActivity::class.java)
                        startActivity(intent)

                        true
                    }
                    else -> false
                }
            }
        }
    }

}