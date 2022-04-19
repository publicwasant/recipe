package com.example.recipeproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeproject.lib.adapter.RecyclerAdapter
import com.example.recipeproject.model.RecipeModel
import com.example.recipeproject.view.DetailView
import com.example.recipeproject.viewModel.RecipeViewModel
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    private var actionBar: ActionBar? = null
    private var detailViewIntent: Intent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_main)

        this.actionBar = supportActionBar
        this.actionBar!!.title = "Recipe Application"
        this.detailViewIntent = Intent(this, DetailView::class.java)

        this.fetch {
            this.setContextViews(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.im_refresh -> {
                this.fetch {
                    this.setContextViews(it)
                }
            }
            R.id.im_sort -> {
                val v = findViewById<View>(R.id.im_sort)
                val popup: PopupMenu = PopupMenu(this, v)
                
                popup.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.im_sortByName -> {
                            this.fetch {
                                it.sortBy {
                                    it.name
                                }

                                this.setContextViews(it)
                            }
                            true
                        }
                        R.id.im_sortByCalories -> {
                            this.fetch {
                                it.sortBy {
                                    it.calories
                                }

                                this.setContextViews(it)
                            }
                            true
                        }
                        R.id.im_sortByProteins -> {
                            this.fetch {
                                it.sortBy {
                                    it.proteins
                                }

                                this.setContextViews(it)
                            }
                            true
                        }
                        else -> false
                    }
                }
                popup.menuInflater.inflate(R.menu.sort_item_menu, popup.menu)
                popup.show()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun setRecyclerView(recipes: ArrayList<RecipeModel>) {
        val recyclerAdapter = RecyclerAdapter(this, recipes)

        rv_recipes.layoutManager = LinearLayoutManager(this)
        rv_recipes.adapter = recyclerAdapter

        recyclerAdapter.notifyDataSetChanged()
        recyclerAdapter.setOnItemClick {
            this.detailViewIntent?.putExtra("calories", it.calories)
            this.detailViewIntent?.putExtra("carbos", it.carbos)
            this.detailViewIntent?.putExtra("description", it.description)
            this.detailViewIntent?.putExtra("difficulty", it.difficulty)
            this.detailViewIntent?.putExtra("fats", it.fats)
            this.detailViewIntent?.putExtra("headline", it.headline)
            this.detailViewIntent?.putExtra("id", it.id)
            this.detailViewIntent?.putExtra("name", it.name)
            this.detailViewIntent?.putExtra("proteins", it.proteins)
            this.detailViewIntent?.putExtra("thumb", it.thumb)
            this.detailViewIntent?.putExtra("time", it.time)
            this.detailViewIntent?.putExtra("image", it.image)
            this.startActivity(detailViewIntent)
        }
    }

    private fun setItemTouchHelper(recipes: ArrayList<RecipeModel>) {
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

    private fun setContextViews(recipes: ArrayList<RecipeModel>) {
        this.actionBar!!.title = "Recipe Application (${recipes.size})"
        this.setRecyclerView(recipes)
        this.setItemTouchHelper(recipes)
    }

    private fun fetch(f: (ArrayList<RecipeModel>) -> Unit) {
        this.fetchFromAPI {
            f(it)
        }
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