import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.alea.R

data class FoodItem(
    val imageRes: Int, // ID del recurso de imagen
    val name: String   // Nombre del alimento
)

class FoodAdapter(private val foods: List<FoodItem>, private val onFoodSelected: (FoodItem) -> Unit) : RecyclerView.Adapter<FoodAdapter.FoodViewHolder>() {

    class FoodViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val foodImage: ImageView = view.findViewById(R.id.foodImage)
        val foodName: TextView = view.findViewById(R.id.foodName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_food, parent, false)
        return FoodViewHolder(view)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val food = foods[position]
        holder.foodImage.setImageResource(food.imageRes)
        holder.foodName.text = food.name

        // Configurar el clic para notificar la selección
        holder.itemView.setOnClickListener {
            onFoodSelected(food)
        }
    }

    override fun getItemCount() = foods.size
}
