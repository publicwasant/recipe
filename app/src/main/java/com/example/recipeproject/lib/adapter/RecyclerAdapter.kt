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
    class ViewHolder(
        private val v: View,
    ): RecyclerView.ViewHolder(v) {
        val tvDescription = this.v.tv_description
        val tvHeadline = this.v.tv_headline
        val ivImage = this.v.iv_image
        val tvName = this.v.tv_name
        val tvTime = this.v.tv_time
        val tvValues = this.v.tv_values
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater
            .from(this.context)
            .inflate(R.layout.recycler_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = this.items[position]

        holder.tvDescription.text = "Description: ${item.description}"
        holder.tvHeadline.text = item.headline
        holder.tvName.text = item.name
        holder.tvTime.text = item.time
        holder.tvValues.text = "Calories: ${item.calories} | Carbos: ${item.carbos} | Fats: ${item.fats} | Proteins: ${item.proteins} | Difficulty: ${item.difficulty.toString()}"

        Glide.with(this.context)
            .load(item.image)
            .centerCrop()
            .into(holder.ivImage)
    }

    override fun getItemCount(): Int {
        return this.items.size
    }
}