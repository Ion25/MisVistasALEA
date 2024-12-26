package com.example.alea

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class PlateAdapter : RecyclerView.Adapter<PlateAdapter.PlateViewHolder>() {
    private val foods = mutableListOf<Int>()

    class PlateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val foodImage: ImageView = itemView.findViewById(R.id.foodImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlateViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_plate_food, parent, false)
        return PlateViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlateViewHolder, position: Int) {
        holder.foodImage.setImageResource(foods[position])
    }

    override fun getItemCount(): Int = foods.size

    fun addFood(foodImage: Int) {
        foods.add(foodImage)
        notifyItemInserted(foods.size - 1)
    }

    fun clearFoods() {
        foods.clear()
        notifyDataSetChanged()
    }
}
