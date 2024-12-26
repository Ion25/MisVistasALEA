package com.example.alea

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView

data class Recipe(
    val imageRes: Int,
    val name: String,
    val calories: String,
    val proteins: String,
    val vitamins: String,
    val carbohydrates: String,
    val ingredients: String
)

class RecipeAdapter(private val allRecipes: List<Recipe>) : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    private var displayedRecipes: List<Recipe> = allRecipes.toList()
    private val favoriteRecipes = mutableListOf<Recipe>()

    class RecipeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val recipeImage: ImageView = view.findViewById(R.id.recipeImage)
        val recipeName: TextView = view.findViewById(R.id.recipeName)
        val recipeDetails: TextView = view.findViewById(R.id.recipeDetails)
        val recipeIngredients: TextView = view.findViewById(R.id.recipeIngredients)
        val starAnimation: LottieAnimationView = view.findViewById(R.id.starAnimation)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recipe, parent, false)
        return RecipeViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = displayedRecipes[position]
        holder.recipeImage.setImageResource(recipe.imageRes)
        holder.recipeName.text = recipe.name
        holder.recipeDetails.text = "Calorías: ${recipe.calories}\nProteínas: ${recipe.proteins}\nVitaminas: ${recipe.vitamins}\nCarbohidratos: ${recipe.carbohydrates}"
        holder.recipeIngredients.text = "Ingredientes: ${recipe.ingredients}"

        // Configurar estrella
        val isFavorite = favoriteRecipes.contains(recipe)
        holder.starAnimation.progress = if (isFavorite) 1f else 0f

        holder.starAnimation.setOnClickListener {
            if (isFavorite) {
                favoriteRecipes.remove(recipe)
                holder.starAnimation.cancelAnimation()
                holder.starAnimation.progress = 0f
                Toast.makeText(holder.itemView.context, "${recipe.name} eliminado de favoritos", Toast.LENGTH_SHORT).show()
            } else {
                favoriteRecipes.add(recipe)
                holder.starAnimation.playAnimation()
                Toast.makeText(holder.itemView.context, "${recipe.name} agregado a favoritos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getItemCount() = displayedRecipes.size

    fun getFavoriteRecipes(): List<Recipe> = favoriteRecipes

    fun updateRecipes(newRecipes: List<Recipe>) {
        displayedRecipes = newRecipes
        notifyDataSetChanged()
    }

    fun getDisplayedRecipes(): List<Recipe> = displayedRecipes
}
