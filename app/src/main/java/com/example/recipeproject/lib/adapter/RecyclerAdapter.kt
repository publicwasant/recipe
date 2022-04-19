package com.example.recipeproject.lib.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipeproject.R
import com.example.recipeproject.model.RecipeModel
import kotlinx.android.synthetic.main.recycler_item.view.*

class RecyclerAdapter(
    private val context: Context,
    private val items: ArrayList<RecipeModel>,
): RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {
    private var clickItemTask: ((item: RecipeModel) -> Unit)? = null

    class ViewHolder(
        private val v: View,
    ): RecyclerView.ViewHolder(v) {
        val cvRecipe = this.v.cv_recipe
        val ivThumb = this.v.iv_thumb
        val tvNameAndHeadline = this.v.tv_nameAndHeadline
        val tvValues = this.v.tv_values
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater
            .from(this.context)
            .inflate(R.layout.recycler_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = this.items[position]

        Glide.with(this.context)
            .load(item.thumb)
            .centerCrop()
            .into(holder.ivThumb)

        holder.tvNameAndHeadline.text = "${item.name} ${item.headline}"
        holder.tvValues.text = "Calories: ${item.calories} | Proteins: ${item.proteins}"

        holder.cvRecipe.setOnClickListener {
            this.clickItemTask!!(item)
        }
    }

    override fun getItemCount(): Int {
        return this.items.size
    }

    fun setOnItemClick(clickItemTask: (item: RecipeModel) -> Unit) {
        this.clickItemTask = clickItemTask
    }
}