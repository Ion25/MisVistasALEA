package com.example.midiariop.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.midiariop.R
import com.example.midiariop.databinding.FragmentHomeBinding
import com.example.midiariop.ui.home.date.DateAdapter
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView

class HomeFragment : Fragment(R.layout.fragment_home) {

    // Usamos viewBinding para evitar referencias nulas
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    // ViewModel
    private val viewModel: HomeViewModel by viewModels()

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

        return binding.root
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

