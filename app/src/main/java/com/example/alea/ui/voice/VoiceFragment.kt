/*
package com.example.alea.ui.voice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.alea.databinding.FragmentVoiceBinding
import org.json.JSONObject

class VoiceFragment : Fragment() {
    private var _binding: FragmentVoiceBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: VoiceViewModel
    private val chatGPTClient = ChatGPTClient() // Instancia del cliente

    // Adaptadores para las tres listas
    private lateinit var adapterCarbs: FoodExpandableListAdapter
    private lateinit var adapterProteins: FoodExpandableListAdapter
    private lateinit var adapterFats: FoodExpandableListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVoiceBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(VoiceViewModel::class.java)

        // Configurar botón de escucha
        binding.startListeningButton.setOnClickListener {
            // Capturar la voz
            viewModel.startListening { result ->
                if (result.isNotEmpty()) {
                    // Mostrar el texto transcrito en resultTextView
                    binding.resultTextView.text = result

                    // Enviar el texto a ChatGPTClient

                    //chatGPTClient.enviarPrompt(result) { response ->
                    //    requireActivity().runOnUiThread {
                    // Mostrar la respuesta de ChatGPT en responseTextView
                    // binding.responseTextView.text = response

                    // Procesar la respuesta JSON y actualizar las listas
                    //        val gruposDeAlimentos = procesarRespuestaJSON(response)
                    //        actualizarListas(gruposDeAlimentos)
                    //    }
                    //}

                    chatGPTClient.enviarPrompt(result) { response ->
                        requireActivity().runOnUiThread {
                            val (grupos, datosHijos) = procesarRespuestaJSON(response)

                            // Convertir Pair en Map
                            val gruposDeAlimentos = grupos.zip(datosHijos).toMap()

                            // Llamar a la función actualizarListas con el Map
                            actualizarListas(gruposDeAlimentos)

                            val adapter = FoodExpandableListAdapter(requireContext(), grupos, datosHijos)
                            binding.expandableListView.setAdapter(adapter)
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

        return binding.root
    }

    private fun procesarRespuestaJSON(respuesta: String): Pair<List<String>, List<List<FoodItem>>> {
        val grupos = mutableListOf<String>()
        val datosHijos = mutableListOf<List<FoodItem>>()

        val carbohidratos = mutableListOf<FoodItem>()
        val proteinas = mutableListOf<FoodItem>()
        val grasas = mutableListOf<FoodItem>()

        try {
            val jsonObject = JSONObject(respuesta)
            val alimentos = jsonObject.getJSONArray("alimentos")

            for (i in 0 until alimentos.length()) {
                val alimento = alimentos.getJSONObject(i)
                val nombre = alimento.getString("nombre")
                val cantidad = alimento.getInt("cantidad")
                val calorias = alimento.getInt("calorias")

                // Clasificación
                val categoriasArray = alimento.getJSONArray("clasificacion")
                val categorias = mutableListOf<String>()
                for (j in 0 until categoriasArray.length()) {
                    categorias.add(categoriasArray.getString(j))
                }

                // Crear FoodItem con categorías
                val item = FoodItem(nombre, cantidad, calorias, categorias)

                // Agregar a las listas correspondientes
                if ("carbohidratos" in categorias) carbohidratos.add(item)
                if ("proteínas" in categorias) proteinas.add(item)
                if ("grasas" in categorias) grasas.add(item)
            }

            // Asignar a las listas de grupos y datos
            if (carbohidratos.isNotEmpty()) {
                grupos.add("Carbohidratos")
                datosHijos.add(carbohidratos)
            }
            if (proteinas.isNotEmpty()) {
                grupos.add("Proteínas")
                datosHijos.add(proteinas)
            }
            if (grasas.isNotEmpty()) {
                grupos.add("Grasas")
                datosHijos.add(grasas)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return Pair(grupos, datosHijos)
    }


    private fun actualizarListas(gruposDeAlimentos: Map<String, List<FoodItem>>) {
        // Crear una lista de grupos
        val grupos = listOf("Carbohidratos", "Proteínas", "Grasas")

        // Crear una lista de datos hijos por cada grupo (las listas de FoodItem)
        val alimentosCarbs = gruposDeAlimentos["Carbohidratos"] ?: emptyList()
        val alimentosProteins = gruposDeAlimentos["Proteínas"] ?: emptyList()
        val alimentosFats = gruposDeAlimentos["Grasas"] ?: emptyList()

        // Cada grupo debe tener una lista de elementos (en este caso, los alimentos)
        val datosHijos = listOf(
            alimentosCarbs,
            alimentosProteins,
            alimentosFats
        )

        // Crear el adaptador y asignarlo a la lista desplegable
        adapterCarbs = FoodExpandableListAdapter(requireContext(), grupos, datosHijos)
        adapterProteins = FoodExpandableListAdapter(requireContext(), grupos, datosHijos)
        adapterFats = FoodExpandableListAdapter(requireContext(), grupos, datosHijos)

        // Asignar el adaptador a las vistas correspondientes
        binding.expandableCarbs.setAdapter(adapterCarbs)
        binding.expandableProteins.setAdapter(adapterProteins)
        binding.expandableFats.setAdapter(adapterFats)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
*/

package com.example.alea.ui.voice

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.alea.databinding.FragmentVoiceBinding
import org.json.JSONObject

class VoiceFragment : Fragment() {
    private var _binding: FragmentVoiceBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: VoiceViewModel
    private val chatGPTClient = ChatGPTClient() // Instancia del cliente

    // Adaptadores para las tres listas
    private lateinit var adapterCarbs: FoodExpandableListAdapter
    private lateinit var adapterProteins: FoodExpandableListAdapter
    private lateinit var adapterFats: FoodExpandableListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVoiceBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(VoiceViewModel::class.java)

        // Configurar botón de escucha
        binding.startListeningButton.setOnClickListener {
            // Capturar la voz
            viewModel.startListening { result ->
                if (result.isNotEmpty()) {
                    // Mostrar el texto transcrito en resultTextView
                    binding.resultTextView.text = result

                    // Enviar el texto a ChatGPTClient
                    chatGPTClient.enviarPrompt(result) { response ->
                        requireActivity().runOnUiThread {
                            // Mostrar la respuesta de ChatGPT en responseTextView
                            binding.responseTextView.text = response
                            Log.d("OpenAIResponse", "Respuesta completa: $response")

                            val (grupos, datosHijos) = procesarRespuestaJSON(response)

                            Log.d("Grupos", "Grupos: $grupos")
                            Log.d("datosHijos", "datosHijos: $datosHijos")

                            // Convertir Pair en Map
                            val gruposDeAlimentos = grupos.zip(datosHijos).toMap()
                            Log.d("gruposDeAlimentos", "gruposDeAlimentos: $gruposDeAlimentos")

                            val adapter = FoodExpandableListAdapter(requireContext(), grupos, datosHijos)
                            Log.d("Adapter", "Adapter: $adapter")


                            // Colocar el adapter en el ExpandableListView "expandableListView"
                            binding.expandableListView.setAdapter(adapter)
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

        return binding.root
    }

    private fun procesarRespuestaJSON(respuesta: String): Pair<List<String>, List<List<FoodItem>>> {
        val grupos = mutableListOf<String>()
        val datosHijos = mutableListOf<List<FoodItem>>()

        val carbohidratos = mutableListOf<FoodItem>()
        val proteinas = mutableListOf<FoodItem>()
        val grasas = mutableListOf<FoodItem>()

        try {
            val jsonObject = JSONObject(respuesta)
            val alimentos = jsonObject.getJSONArray("alimentos")

            for (i in 0 until alimentos.length()) {
                val alimento = alimentos.getJSONObject(i)
                val nombre = alimento.getString("nombre")
                val cantidad = alimento.getInt("cantidad")
                val calorias = alimento.getInt("calorias")

                // Clasificación
                val categoriasArray = alimento.getJSONArray("clasificacion")
                val categorias = mutableListOf<String>()
                for (j in 0 until categoriasArray.length()) {
                    categorias.add(categoriasArray.getString(j))
                }

                // Crear FoodItem con categorías
                val item = FoodItem(nombre, cantidad, calorias, categorias)

                // Agregar a las listas correspondientes
                if ("carbohidratos" in categorias) carbohidratos.add(item)
                if ("proteínas" in categorias) proteinas.add(item)
                if ("grasas" in categorias) grasas.add(item)
            }

            // Asignar a las listas de grupos y datos
            if (carbohidratos.isNotEmpty()) {
                grupos.add("Carbohidratos")
                datosHijos.add(carbohidratos)
            }
            if (proteinas.isNotEmpty()) {
                grupos.add("Proteínas")
                datosHijos.add(proteinas)
            }
            if (grasas.isNotEmpty()) {
                grupos.add("Grasas")
                datosHijos.add(grasas)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return Pair(grupos, datosHijos)
    }


    private fun actualizarListas(gruposDeAlimentos: Map<String, List<FoodItem>>) {
        // Crear una lista de grupos
        val grupos = listOf("Carbohidratos", "Proteínas", "Grasas")

        // Crear una lista de datos hijos por cada grupo (las listas de FoodItem)
        val alimentosCarbs = gruposDeAlimentos["Carbohidratos"] ?: emptyList()
        val alimentosProteins = gruposDeAlimentos["Proteínas"] ?: emptyList()
        val alimentosFats = gruposDeAlimentos["Grasas"] ?: emptyList()

        // Cada grupo debe tener una lista de elementos (en este caso, los alimentos)
        val datosHijos = listOf(
            alimentosCarbs,
            alimentosProteins,
            alimentosFats
        )

        // Crear el adaptador y asignarlo a la lista desplegable
        adapterCarbs = FoodExpandableListAdapter(requireContext(), grupos, datosHijos)
        adapterProteins = FoodExpandableListAdapter(requireContext(), grupos, datosHijos)
        adapterFats = FoodExpandableListAdapter(requireContext(), grupos, datosHijos)

        // Asignar el adaptador a las vistas correspondientes
        binding.expandableCarbs.setAdapter(adapterCarbs)
        binding.expandableProteins.setAdapter(adapterProteins)
        binding.expandableFats.setAdapter(adapterFats)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}