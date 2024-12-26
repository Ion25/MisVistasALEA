package com.example.midiariop.ui.home

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.midiariop.R
import com.example.midiariop.databinding.FragmentHomeBinding
import com.example.midiariop.ui.home.date.DateAdapter
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.midiariop.ui.home.microphone.ChatGPTClient
import org.json.JSONObject

class HomeFragment : Fragment(R.layout.fragment_home) {

    // Usamos viewBinding para evitar referencias nulas
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    // ViewModel
    private val viewModel: HomeViewModel by viewModels()

    // Chat
    private val chatGPTClient = ChatGPTClient()
    private lateinit var progressBar: ProgressBar
    private lateinit var tvCaloriesRemaining: TextView
    private lateinit var tvCaloriesConsumed: TextView


    private var totalDiario = 2245
    private var caloriasConsumidas = 0


    private var totalCarbohidratos = 0
    private var totalProteinas = 0
    private var totalGrasas = 0


    // Adapters
    private lateinit var dateAdapter: DateAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inicialización de binding
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        // RecyclerView y Adapter
        dateAdapter = DateAdapter(emptyList()) { selectedDate ->
            // Guardar datos de la fecha seleccionada previamente
            val previousDate = viewModel.selectedDate.value
            if (previousDate != null) {
                val currentData = Data(
                    progress = binding.circularProgressBar.getProgress(),
                    carbs = binding.carbohydratesProgressBar.progress,
                    proteins = binding.proteinsProgressBar.progress,
                    fats = binding.fatsProgressBar.progress
                )
                viewModel.saveDataForDate(previousDate, currentData)
            }

            // Actualiza la nueva fecha
            viewModel.selectDate(selectedDate)

        }

        // Configurar RecyclerView
        binding.dateRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.dateRecyclerView.adapter = dateAdapter

        // Agregar PagerSnapHelper para el comportamiento de desplazamiento por página
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding.dateRecyclerView)

        // Añade OnScrollListener para manejar el cambio de página
        binding.dateRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    // Encuentra la posición central visible
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val visiblePosition = layoutManager.findFirstCompletelyVisibleItemPosition()
                    if (visiblePosition != RecyclerView.NO_POSITION) {
                        val selectedDate = viewModel.dates.value?.get(visiblePosition)
                        if (selectedDate != null) {
                            viewModel.selectDate(selectedDate)
                        }
                    }
                }
            }
        })


        // Observa la lista de fechas
        viewModel.dates.observe(viewLifecycleOwner) { dates ->
            dateAdapter.updateDates(dates)
        }

        // Observa la fecha seleccionada
        viewModel.selectedDate.observe(viewLifecycleOwner) { selectedDate ->
            val data = viewModel.getDataForDate(selectedDate)
            binding.circularProgressBar.setProgress(data.progress)
            binding.carbohydratesProgressBar.progress = data.carbs
            binding.proteinsProgressBar.progress = data.proteins
            binding.fatsProgressBar.progress = data.fats
        }

        // Cargar las fechas
        viewModel.loadDates()

        // Botón para actualizar el progreso
        val btnUpdateProgress: Button = binding.btnUpdateProgress
        btnUpdateProgress.setOnClickListener {
            binding.circularProgressBar.setProgressWithAnimation(35f) // Cambia este valor dinámicamente
        }

        setupMicrophoneButton()

        return binding.root
    }

    private fun setupMicrophoneButton() {
        binding.circularButton.setOnClickListener {
            viewModel.startListening { result ->
                if (result.isNotEmpty()) {
                    // Enviar el texto a ChatGPTClient
                    Log.d("VOZ", "Grupos: $result")
                    chatGPTClient.enviarPrompt(result) { response ->
                        requireActivity().runOnUiThread {
                            //binding.responseTextView.text = response
                            Log.d("API CHAT GPT", "Grupos: $response")
                            try {
                                // Procesar los datos del JSON
                                val (carbs, proteins, fats) = procesarDatosAlimentos(response)

                                Log.d("AGARRE1", "Grupos: $response")

                                // Actualizar los TextViews
                                actualizarTextViews(carbs, proteins, fats)

                                Log.d("AGARRE2", "Grupos: $response")

                                // Extraer las calorías totales del JSON
                                val jsonObject = JSONObject(response)
                                caloriasConsumidas += jsonObject.getInt("calorias_totales")

                                Log.d("AGARRE3", "Grupos: $response")

                                // Actualizar las calorías consumidas
                                actualizarCaloriesConsumed(caloriasConsumidas)

                                Log.d("AGARRE4", "Grupos: $response")

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
        binding.carbohydratesConsumed.text = "$carbs"
        binding.proteinsConsumed.text = "$proteins"
        binding.fatsConsumed.text = "$fats"

        //actualizar barritas
        binding.carbohydratesProgressBar.progress = carbs.toInt()
        binding.proteinsProgressBar.progress = proteins.toInt()
        binding.fatsProgressBar.progress = fats.toInt()

    }

    private fun actualizarCaloriesConsumed(calConsum: Int) {
        //val totalDiario = obtenerCaloriasRecomendadas() // Usa tu metodo existente para calcular las kcal diarias
        //val progreso = (caloriasConsumidas * 100) / totalDiario
        val progreso = caloriasConsumidas

        // Actualiza la barra de progreso
        //binding.circularProgressBar.setProgress(progreso.toFloat())
        binding.circularProgressBar.setProgressWithAnimation(progreso.toFloat())

        // Actualiza el texto de progreso
        binding.kcalNumberConsumidas.text = "$caloriasConsumidas"

        val caloriesRemaining = totalDiario - caloriasConsumidas
        binding.kcalNumberRestantes.text = "$caloriesRemaining"

    }

    // Evita filtraciones de memoria
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}


/*
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import com.example.midiariop.R
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.midiariop.databinding.FragmentHomeBinding
import com.example.midiariop.ui.home.date.DateAdapter


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dateAdapter = DateAdapter(emptyList()) { selectedDate ->
            val previousDate = viewModel.selectedDate.value
            if (previousDate != null) {
                // Guarda los datos de la fecha actual antes de cambiar
                val currentData = Data(
                    progress = binding.circularProgressBar.getProgress(),
                    carbs = binding.carbohydratesProgressBar.progress,
                    proteins = binding.proteinsProgressBar.progress,
                    fats = binding.fatsProgressBar.progress
                )
                viewModel.saveDataForDate(previousDate, currentData)
            }

            // Actualiza la nueva fecha
            viewModel.selectDate(selectedDate)
        }
        /*
        binding.dateRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.dateRecyclerView.adapter = dateAdapter
        */
        viewModel.dates.observe(viewLifecycleOwner) { dates ->
            dateAdapter.updateDates(dates)
        }

        viewModel.selectedDate.observe(viewLifecycleOwner) { selectedDate ->
            val data = viewModel.getDataForDate(selectedDate)
            binding.circularProgressBar.setProgress(data.progress)
            binding.carbohydratesProgressBar.progress = data.carbs
            binding.proteinsProgressBar.progress = data.proteins
            binding.fatsProgressBar.progress = data.fats
        }

        viewModel.loadDates()
    }

}




class HomeFragment : Fragment() {

    private lateinit var circularProgressBar: CircularProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Inicializa la barra de progreso
        circularProgressBar = view.findViewById(R.id.circularProgressBar)

        // Botón para actualizar el progreso
        val btnUpdateProgress: Button = view.findViewById(R.id.btnUpdateProgress)
        btnUpdateProgress.setOnClickListener {
            // Actualiza el progreso con animación
            circularProgressBar.setProgressWithAnimation(35f) // Cambia este valor dinámicamente
        }

        return view
    }


}
*/

