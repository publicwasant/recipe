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
import com.example.recipeproject.lib.adapter.RecyclerAdapter
import com.example.recipeproject.lib.helper.RecyclerItemTouchHelper
import com.example.recipeproject.model.FavoriteModel
import com.example.recipeproject.model.PackModel
import com.example.recipeproject.model.RecipeModel
import com.example.recipeproject.view.DetailView
import com.example.recipeproject.viewModel.RecipeViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var actionBar: ActionBar? = null
    private lateinit var detailViewIntent: Intent

    private lateinit var recyclerAdapter: RecyclerAdapter
    private lateinit var recyclerItemTouchHelper: RecyclerItemTouchHelper

    lateinit var recipeViewModel: RecipeViewModel
    private var pack: PackModel = PackModel(arrayListOf(), arrayListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_main)

        this.rv_recipes.layoutManager = LinearLayoutManager(this)

        this.actionBar = supportActionBar
        this.actionBar?.title = "All Recipe"
        this.detailViewIntent = Intent(this, DetailView::class.java)

        this.recyclerAdapter = RecyclerAdapter(this)
        this.recyclerAdapter.setOnItemClick {
            this.setOnClickRecyclerItem(it)
        }

        this.recyclerItemTouchHelper = RecyclerItemTouchHelper()
        this.recyclerItemTouchHelper.let {
            ItemTouchHelper(it).attachToRecyclerView(this.rv_recipes)
        }

        this.recipeViewModel = ViewModelProvider(this).get(RecipeViewModel::class.java)
        this.recipeViewModel.setBackgroundTask {
            this.backup(it)
        }
        this.recipeViewModel.startBackgroundTask()

        this.sw_recipe.isRefreshing = true
        this.sw_recipe.setOnRefreshListener {
            this.backup(0)
            this.fetch {
                this.apply(it)
            }
        }

        this.fetch {
            if (it.recipes.isEmpty()) {
                this.backup(0)
            }

            this.apply(it)
        }
    }

    private fun setOnClickRecyclerItem(item: RecipeModel) {
        this.detailViewIntent.putExtra("calories", item.calories)
        this.detailViewIntent.putExtra("carbos", item.carbos)
        this.detailViewIntent.putExtra("description", item.description)
        this.detailViewIntent.putExtra("difficulty", item.difficulty)
        this.detailViewIntent.putExtra("fats", item.fats)
        this.detailViewIntent.putExtra("headline", item.headline)
        this.detailViewIntent.putExtra("id", item.id)
        this.detailViewIntent.putExtra("name", item.name)
        this.detailViewIntent.putExtra("proteins", item.proteins)
        this.detailViewIntent.putExtra("thumb", item.thumb)
        this.detailViewIntent.putExtra("time", item.time)
        this.detailViewIntent.putExtra("image", item.image)
        this.detailViewIntent.putExtra("favorite", FavoriteModel(item.id) in this.pack.favorites)
        this.startActivity(detailViewIntent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.im_sort -> {
                val imSort = findViewById<View>(R.id.im_sort)
                val popup: PopupMenu = PopupMenu(this, imSort)

                popup.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.im_sortByName -> {
                            this.pack.recipes.sortBy { it.name }
                            this.pack.let {
                                this.apply(it)
                            }
                            Toast.makeText(this, "Sort by name", Toast.LENGTH_SHORT).show()
                            true
                        }
                        R.id.im_sortByCalories -> {
                            this.pack.recipes.sortBy { it.calories }
                            this.pack.let {
                                this.apply(it)
                            }
                            Toast.makeText(this, "Sort by calories", Toast.LENGTH_SHORT).show()
                            true
                        }
                        R.id.im_sortByProteins -> {
                            this.pack.recipes.sortBy { it.proteins }
                            this.pack.let {
                                this.apply(it)
                            }
                            Toast.makeText(this, "Sort by proteins", Toast.LENGTH_SHORT).show()
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

    private fun apply(p: PackModel) {
        this.pack = p
        this.recyclerItemTouchHelper.setItems(p.recipes)
        this.recyclerAdapter.setItems(p.recipes)
        this.recyclerAdapter.setFavorites(p.favorites)
        this.recyclerAdapter.let {
            this.rv_recipes.adapter = it
        }

        this.actionBar?.title = "All Recipe (${p.recipes.size})"
        this.sw_recipe.isRefreshing = false
    }

    private fun fetch(next: (PackModel) -> Unit) {
        if (this.pack.recipes.isNotEmpty()) {
            this.recipeViewModel.getDB().backup(this.pack.recipes)
        }

        this.recipeViewModel.getDB().readFavorites { f ->
            f.observe(this) { fitems ->
            this.recipeViewModel.getDB().read { r ->
                r.observe(this) { ritems ->
                    next(PackModel(
                        this.recipeViewModel.getDB().toFavoriteModel(fitems),
                        this.recipeViewModel.getDB().toRecipeModel(ritems)))
                    }
                }
            }
        }
    }

    private fun backup(count: Int) {
        this.sw_recipe.isRefreshing = true
        this.recipeViewModel.getAPI().readAll { live ->
            live.observe(this) {
                this.recipeViewModel.getDB().backup(it)
                this.sw_recipe.isRefreshing = false
            }
        }
    }
}
