package com.example.recipeproject.lib.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipeproject.R
import com.example.recipeproject.model.FavoriteModel
import com.example.recipeproject.model.RecipeModel
import kotlinx.android.synthetic.main.recycler_item.view.*

class RecyclerAdapter(private val context: Context): RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {
    private lateinit var favorites: ArrayList<FavoriteModel>
    private lateinit var items: ArrayList<RecipeModel>

    private lateinit var clickItemTask: ((item: RecipeModel) -> Unit)

    class ViewHolder(private val v: View): RecyclerView.ViewHolder(v) {
        val cvRecipe: CardView = this.v.cv_recipe
        val ivThumb: ImageView = this.v.iv_thumb
        val tvNameAndHeadline: TextView = this.v.tv_nameAndHeadline
        val tvValues: TextView = this.v.tv_values
        val tvHint: TextView = this.v.tv_hint
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater
            .from(this.context)
            .inflate(R.layout.recycler_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        this.items.let {
            val recipe = it[position]

            Glide.with(this.context)
                .load(recipe.thumb)
                .centerCrop()
                .into(holder.ivThumb)

            holder.tvNameAndHeadline.text = "${recipe.name} ${recipe.headline}"
            holder.tvValues.text = "Calories: ${recipe.calories} | Proteins: ${recipe.proteins}"

            if (FavoriteModel(recipe.id) in this.favorites) {
                holder.tvHint.text = "❤ Favorite | Click to view detail."
            }

            holder.cvRecipe.setOnClickListener {
                this.clickItemTask(recipe)
            }
        }
    }

    override fun getItemCount(): Int {
        return this.items.size
    }

    fun setFavorites(favorites: ArrayList<FavoriteModel>) {
        this.favorites = favorites
        notifyDataSetChanged()
    }

    fun setItems(items: ArrayList<RecipeModel>) {
        this.items = items
        notifyDataSetChanged()
    }

    fun setOnItemClick(clickItemTask: (item: RecipeModel) -> Unit) {
        this.clickItemTask = clickItemTask
    }
}