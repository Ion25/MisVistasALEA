package com.example.alea.ui.home.date

data class DateData(
    val date: String, // Fecha en formato "YYYY-MM-DD"
    val label: String, // "HOY", "AYER", "MAÑANA", o nombre del día
    var consumedCalories: Int = 0, // Ejemplo de dato
    var burnedCalories: Int = 0, // Otro ejemplo de dato
    var progressData: Map<String, Int> = mapOf() // Macronutrientes o barras de progreso
)
