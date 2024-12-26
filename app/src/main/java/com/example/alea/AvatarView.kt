package com.example.alea

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView

class AvatarView(context: Context, attrs: AttributeSet) : AppCompatImageView(context, attrs) {

    fun updateAvatar(gender: String, height: String, weight: String) {
        val avatarResource = when (gender) {
            "Masculino" -> when (weight) {
                "Ligero" -> R.drawable.avatar_hombre_ligero
                "Medio" -> R.drawable.avatar_hombre_medio
                "Pesado" -> R.drawable.avatar_hombre_pesado
                else -> R.drawable.avatar_hombre_medio
            }
            "Mujer" -> when (weight) {
                "Ligero" -> R.drawable.avatar_mujer_ligera
                "Medio" -> R.drawable.avatar_mujer_media
                "Pesado" -> R.drawable.avatar_mujer_pesada
                else -> R.drawable.avatar_mujer_media
            }
            else -> R.drawable.avatar_hombre_ligero

        }
        setImageResource(avatarResource)
    }
}
