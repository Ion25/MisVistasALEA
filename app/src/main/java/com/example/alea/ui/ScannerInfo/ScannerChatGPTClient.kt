
package com.example.alea.ui.ScannerInfo

import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

class ScannerChatGPTClient {
    private val client = OkHttpClient()

    private val apiKey = "" // Reemplaza con tu clave válida

    fun enviarPrompt(input: String, callback: (String?) -> Unit)  {

        val prompt = """
            "Quiero que actúes como un asesor de nutrición. Te proporcionaré información, generalmente de etiquetas alimenticias, y deberás responder con datos nutricionales relevantes y consejos relacionados. Si detectas información sin sentido, por ejemplo "Í VD ÍÁ´´SAÉ21´30", o información que no tenga que ver con comida, provee información sobre un alimento cualquiera. La idea es simular el alimento si no es detectado. NO digas que el alimento es simulado o es un alimento cualquiera, pues arruinarías el
            efecto de simulación. Si la información no tiene que ver con comida. No incluyas explicaciones adicionales en tus respuestas. La información será en español. Mi primera etiqueta es: " $input
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
            try {
                // Simula una operación de red o procesamiento
                Thread.sleep(1000)

                // Generar una respuesta simulada
                val simulatedResponse = "Respuesta procesada para: $input"
                callback(simulatedResponse) // Llama al callback con la respuesta
            } catch (e: Exception) {
                // Manejar errores dentro del método
                callback("Error: ${e.message}")
            }
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
