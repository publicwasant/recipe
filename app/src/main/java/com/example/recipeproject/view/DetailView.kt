package com.example.recipeproject.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.recipeproject.R
import com.example.recipeproject.viewModel.RecipeViewModel
import kotlinx.android.synthetic.main.activity_detail_view.*

class DetailView : AppCompatActivity() {
    private lateinit var recipeViewModel: RecipeViewModel

    private var id: String? = null
    private var calories: String? = null
    private var carbos: String? = null
    private var description: String? = null
    private var difficulty: Int? = null
    private var fats: String? = null
    private var headline: String? = null
    private var name: String? = null
    private var proteins: String? = null
    private var time: String? = null
    private var image: String? = null
    private var favorite: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_detail_view)

        this.recipeViewModel = ViewModelProvider(this).get(RecipeViewModel::class.java)

        this.id = intent.getStringExtra("id").toString()
        this.calories = intent.getStringExtra("calories").toString()
        this.carbos = intent.getStringExtra("carbos").toString()
        this.description = intent.getStringExtra("description").toString()
        this.difficulty = intent.getIntExtra("difficulty", 0).toInt()
        this.fats = intent.getStringExtra("fats").toString()
        this.headline = intent.getStringExtra("headline").toString()
        this.name = intent.getStringExtra("name").toString()
        this.proteins = intent.getStringExtra("proteins").toString()
        this.time = intent.getStringExtra("time").toString()
        this.image = intent.getStringExtra("image").toString()
        this.favorite = intent.getBooleanExtra("favorite", false)

        Glide.with(this)
            .load(this.image)
            .fitCenter()
            .into(this.iv_detailImage)

        this.tv_detailNameAndHeadline.text = "${name} ${headline}"
        this.tv_detailDescription.text = "${description}"
        this.tv_detailValues1.text = "◦ Difficulty: ${difficulty} | Time: ${time}"
        this.tv_detailValues2.text = "◦ Calories: ${calories} | Carbos: ${carbos}"
        this.tv_detailValues3.text = "◦ Fats: ${fats} | Proteins: ${proteins}"

        this.bt_favorite.setOnClickListener {
            if (this.favorite!!) {
                this.recipeViewModel.getDB().deleteFavorite(this.id!!)
                this.favorite = !this.favorite!!
                Toast.makeText(this, "Remove Favorite", Toast.LENGTH_SHORT).show()
            } else {
                this.recipeViewModel.getDB().addFavorite(this.id!!)
                this.favorite = !this.favorite!!
                Toast.makeText(this, "Add Favorite", Toast.LENGTH_SHORT).show()
            }

            this.setFavorite()
        }

        this.setFavorite()
    }

    private fun setFavorite() {
        if (this.favorite!!) {
            this.bt_favorite.text = "❤ Favorite"
        } else {
            this.bt_favorite.text = "Add Favorite"
        }
    }
}