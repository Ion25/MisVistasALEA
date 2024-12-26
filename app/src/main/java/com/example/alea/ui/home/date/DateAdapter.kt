package com.example.alea.ui.home.date

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.alea.R

class DateAdapter(
    private var dates: List<String>,
    private val onDateSelected: (String) -> Unit
) : RecyclerView.Adapter<DateAdapter.DateViewHolder>() {

    private var selectedPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_date, parent, false)
        return DateViewHolder(view)
    }

    override fun onBindViewHolder(holder: DateViewHolder, position: Int) {
        val date = dates[position]
        holder.bind(date, position == selectedPosition)

        holder.itemView.setOnClickListener {
            val previousPosition = selectedPosition
            selectedPosition = holder.adapterPosition  // Usar holder.adapterPosition
            notifyItemChanged(previousPosition)
            notifyItemChanged(selectedPosition)
            onDateSelected(date)
        }
    }


    override fun getItemCount(): Int = dates.size

    fun updateDates(newDates: List<String>) {
        dates = newDates
        notifyDataSetChanged()
    }

    class DateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val dateText: TextView = itemView.findViewById(R.id.date_text)

        fun bind(date: String, isSelected: Boolean) {
            dateText.text = date

            // Cambiar color de texto según el estado seleccionado
            dateText.setTextColor(
                if (isSelected) itemView.context.getColor(R.color.white) // Blanco para texto
                else itemView.context.getColor(R.color.black) // Negro para texto
            )

            // Cambiar padding para resaltar el elemento seleccionado
            val padding = if (isSelected) {
                itemView.context.resources.getDimensionPixelSize(R.dimen.selected_padding_jhon)
            } else {
                itemView.context.resources.getDimensionPixelSize(R.dimen.unselected_padding_jhon)
            }
            dateText.setPadding(padding, padding, padding, padding)

            // Cambiar fondo según el estado seleccionado
            dateText.background = if (isSelected) {
                itemView.context.getDrawable(R.drawable.selected_date_background_jhon) // Fondo personalizado
            } else {
                null // Sin fondo
            }
        }

    }
}
