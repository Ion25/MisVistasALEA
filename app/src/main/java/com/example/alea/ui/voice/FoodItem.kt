package com.example.alea.ui.voice

// Clase de datos que representa un alimento
data class FoodItem(
    val nombre: String,            // Nombre del alimento
    val cantidad: Int,             // Cantidad en gramos
    val calorias: Int,             // Calorías del alimento
    val categorias: List<String>   // Lista de categorías (Carbohidratos, Proteínas, Grasas)
)
