package com.example.midiariop.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.midiariop.databinding.FragmentHomeBinding
import android.widget.Button
import com.example.midiariop.R


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

/*

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
*/
