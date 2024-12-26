
package com.example.alea.ui.voice

import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

class ChatGPTClient {
    private val client = OkHttpClient()

    private val apiKey = "" // Reemplaza con tu clave válida

    fun enviarPrompt(textoUsuario: String, callback: (String) -> Unit) {
        /*
        val prompt = """
            Texto transcrito: "$textoUsuario".
            Prompt: "Con base en el texto anterior, identifica los alimentos, las cantidades y calcula las calorías aproximadas. Devuelve los resultados en formato JSON y evita las notas: 
            {
                'alimentos': [
                    {'nombre': 'string', 'cantidad': int, 'calorias': int}
                ],
                'calorias_totales': int
            }"
        """.trimIndent()
         */

        val prompt = """
            Texto transcrito: "$textoUsuario".
            Prompt: "Identifica los alimentos mencionados en el texto anterior. Para cada alimento, devuelve su nombre, cantidad aproximada en gramos o ml, y sus calorías aproximadas. Además, clasifica cada alimento en una o más de las siguientes categorías según corresponda: 'carbohidratos', 'proteínas' y 'grasas'. 
            Devuelve el resultado en formato JSON con la siguiente estructura:
            {
                'alimentos': [
                    {
                        'nombre': 'string',
                        'cantidad': int,
                        'calorias': int,
                        'clasificacion': ['carbohidratos', 'proteinas']
                    }
                ],
                'calorias_totales': int
            }
            No incluyas explicaciones adicionales."
        """.trimIndent()


        val jsonBody = JSONObject().apply {
            put("model", "gpt-4o-mini")
            put("messages", JSONArray().apply {
                put(JSONObject().apply {
                    put("role", "system")
                    put("content", "Eres un asistente que procesa datos de alimentos.")
                })
                put(JSONObject().apply {
                    put("role", "user")
                    put("content", prompt)
                })
            })
        }

        val requestBody = jsonBody.toString().toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url("https://api.openai.com/v1/chat/completions")
            .addHeader("Authorization", "Bearer $apiKey")
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback("Error: No se pudo conectar con el servidor. ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (response.isSuccessful) {
                        val respuesta = response.body?.string()
                        val resultado = parseResponse(respuesta)
                        callback(resultado)
                    } else {
                        callback("Error: ${response.code} - ${response.message}")
                    }
                }
            }
        })
    }

    private fun parseResponse(respuesta: String?): String {
        return try {
            val jsonResponse = JSONObject(respuesta ?: "")
            val choices = jsonResponse.getJSONArray("choices")
            val firstChoice = choices.getJSONObject(0)
            val message = firstChoice.getJSONObject("message")
            message.getString("content")
        } catch (e: Exception) {
            "Error al procesar la respuesta: ${e.message}"
        }
    }
}
