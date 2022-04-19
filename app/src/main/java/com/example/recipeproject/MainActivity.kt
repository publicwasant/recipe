package com.example.recipeproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeproject.lib.adapter.RecyclerAdapter
import com.example.recipeproject.model.RecipeModel
import com.example.recipeproject.viewModel.RecipeViewModel
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_main)

        this.fetchFromAPI {
            this.createRecyclerView(it)
            this.createItemTouchHelper(it)
        }
    }

    private fun createRecyclerView(recipes: ArrayList<RecipeModel>) {
        val recyclerItemAdapter = RecyclerAdapter(this, recipes)

        rv_recipes.layoutManager = LinearLayoutManager(this)
        rv_recipes.adapter = recyclerItemAdapter

        recyclerItemAdapter.notifyDataSetChanged()
    }

    private fun createItemTouchHelper(recipes: ArrayList<RecipeModel>) {
        val simpleCallback = object: ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP.or(ItemTouchHelper.DOWN), 0) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder,
            ): Boolean {
                val start = viewHolder.adapterPosition
                val end = target.adapterPosition

                Collections.swap(recipes, start, end)
                recyclerView.adapter?.notifyItemMoved(start, end)

                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}
        }

        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(rv_recipes)
    }

    private fun fetchFromDB(f: (ArrayList<RecipeModel>) -> Unit) {
    }

    private fun fetchFromAPI(f: (ArrayList<RecipeModel>) -> Unit) {
        val recipeViewModel = ViewModelProvider(this).get(RecipeViewModel::class.java)

        recipeViewModel.getObserver().observe(this) {
            it?.let {
                f(it)
            }
        }

        recipeViewModel.call()
    }
}