package com.example.alea.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.alea.R
import com.example.alea.databinding.FragmentCalorieTrackerBinding
import org.json.JSONObject


class CalorieTrackerFragment : Fragment() {
    private var _binding: FragmentCalorieTrackerBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: CalorieTrackerViewModel

    private lateinit var progressBar: ProgressBar
    private lateinit var tvCaloriesRemaining: TextView
    private lateinit var tvCaloriesConsumed: TextView


    private var totalDiario = 2245
    private var caloriasConsumidas = 0


    private var totalCarbohidratos = 0
    private var totalProteinas = 0
    private var totalGrasas = 0

    private val chatGPTClient = ChatGPTClient()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalorieTrackerBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(CalorieTrackerViewModel::class.java)

        progressBar = binding.progressCircular
        tvCaloriesRemaining = binding.tvCaloriesRemaining
        tvCaloriesConsumed = binding.tvCaloriesConsumed



        setupMicrophoneButton()
        //updateProgressBar()

        return binding.root
    }

    private fun setupMicrophoneButton() {
        binding.btnMicrophone.setOnClickListener {
            viewModel.startListening { result ->
                if (result.isNotEmpty()) {
                    // Enviar el texto a ChatGPTClient
                    chatGPTClient.enviarPrompt(result) { response ->
                        requireActivity().runOnUiThread {
                            //binding.responseTextView.text = response
                            try {
                                // Procesar los datos del JSON
                                val (carbs, proteins, fats) = procesarDatosAlimentos(response)

                                // Actualizar los TextViews
                                actualizarTextViews(carbs, proteins, fats)

                                // Extraer las calorías totales del JSON
                                val jsonObject = JSONObject(response)
                                caloriasConsumidas += jsonObject.getInt("calorias_totales")

                                // Actualizar las calorías consumidas
                                actualizarCaloriesConsumed(caloriasConsumidas)

                            } catch (e: Exception) {
                                e.printStackTrace()
                                Toast.makeText(requireContext(), "Error al procesar la respuesta.", Toast.LENGTH_SHORT).show()
                            }
                        }

                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "No se detectó voz. Inténtalo de nuevo.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }
        }
    }

    private fun procesarDatosAlimentos(respuesta: String): Triple<Int, Int, Int> {

        try {
            val jsonObject = JSONObject(respuesta)
            val alimentos = jsonObject.getJSONArray("alimentos")

            for (i in 0 until alimentos.length()) {
                val alimento = alimentos.getJSONObject(i)
                totalCarbohidratos += alimento.getInt("carbohidratos")
                totalProteinas += alimento.getInt("proteinas")
                totalGrasas += alimento.getInt("grasas")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            throw Exception("Error al procesar el JSON: ${e.message}")
        }

        return Triple(totalCarbohidratos, totalProteinas, totalGrasas)
    }

    private fun actualizarTextViews(carbs: Int, proteins: Int, fats: Int) {
        binding.tvCarbs.text = "$carbs g"
        binding.tvProteins.text = "$proteins g"
        binding.tvFats.text = "$fats g"
    }

    private fun actualizarCaloriesConsumed(calConsum: Int) {
        //val totalDiario = obtenerCaloriasRecomendadas() // Usa tu método existente para calcular las kcal diarias
        //val progreso = (caloriasConsumidas * 100) / totalDiario
        val progreso = caloriasConsumidas

        // Actualiza la barra de progreso
        progressBar.progress = progreso

        // Actualiza el texto de progreso
        binding.tvCaloriesConsumed.text = "$caloriasConsumidas"

        val caloriesRemaining = totalDiario - caloriasConsumidas
        binding.tvCaloriesRemaining.text = "$caloriesRemaining"
    }

}
