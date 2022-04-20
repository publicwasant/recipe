package com.example.recipeproject.lib.helper

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeproject.model.RecipeModel
import java.util.*
import kotlin.collections.ArrayList

class RecyclerItemTouchHelper: ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP.or(ItemTouchHelper.DOWN), 0) {
    private lateinit var items: ArrayList<RecipeModel>

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder,
    ): Boolean {
        val start = viewHolder.adapterPosition
        val end = target.adapterPosition

        Collections.swap(items, start, end)
        recyclerView.adapter?.notifyItemMoved(start, end)

        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}

    fun getItems(): ArrayList<RecipeModel> {
        return this.items
    }

    fun setItems(items: ArrayList<RecipeModel>) {
        this.items = items
    }
}