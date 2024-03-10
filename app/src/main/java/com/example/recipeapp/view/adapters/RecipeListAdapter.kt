package com.example.recipeapp.view.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipeapp.R
import com.example.recipeapp.model.Recipe
import java.nio.file.DirectoryStream

class RecipeListAdapter(
    private val context: Context,
    private val recipeList: List<Recipe>,
    private var filteredRecipeList: List<Recipe> = recipeList.toList(),
    private val onClick : (Recipe) -> Unit
    ) : RecyclerView.Adapter<RecipeListAdapter.RecipeListViewHolder>(), Filterable {

    class RecipeListViewHolder(itemView: View, clickAtPos: (Int) -> Unit) : RecyclerView.ViewHolder(itemView) {
        val rvTitle = itemView.findViewById<TextView>(R.id.title)
        val rvImage = itemView.findViewById<ImageView>(R.id.image)

        init {
            itemView.setOnClickListener {
                clickAtPos(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeListViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.component_main_list_item, parent, false)
        return RecipeListViewHolder(itemView) {
            onClick(filteredRecipeList[it])
        }
    }

    override fun getItemCount(): Int {
        return filteredRecipeList.count()
    }

    override fun onBindViewHolder(holder: RecipeListViewHolder, position: Int) {
        holder.rvTitle.text = filteredRecipeList[position].title
        try {
            Glide.with(context)
                .load(filteredRecipeList[position].imgUrl)
                .centerCrop() // Scale type for the image
                .into(holder.rvImage)
        } catch (e : Exception){
            //Error
            Log.d("MyTag", e.toString())
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                var filteredList = listOf<Recipe>()
                if (constraint.isNullOrBlank()) {
                    filteredList = recipeList.toList()
                } else {
                    var filterType : String = constraint.toString()
                    filteredList = recipeList.filter {
                        it.recipeType == filterType
                    }
                }
                return FilterResults().apply { values = filteredList }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

                filteredRecipeList = if (results?.values == null)
                    listOf()
                else
                    results.values as List<Recipe>
                notifyDataSetChanged()
            }
        }
    }
}