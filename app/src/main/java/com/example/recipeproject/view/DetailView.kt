package com.example.recipeproject.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.recipeproject.R
import kotlinx.android.synthetic.main.activity_detail_view.*

class DetailView : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_detail_view)

        val calories = intent.getStringExtra("calories").toString()
        val carbos = intent.getStringExtra("carbos").toString()
        val description = intent.getStringExtra("description").toString()
        val difficulty = intent.getIntExtra("difficulty", 0).toInt()
        val fats = intent.getStringExtra("fats").toString()
        val headline = intent.getStringExtra("headline").toString()
        val id = intent.getStringExtra("id").toString()
        val name = intent.getStringExtra("name").toString()
        val proteins = intent.getStringExtra("proteins").toString()
        val time = intent.getStringExtra("time").toString()
        val image = intent.getStringExtra("image").toString()

        Glide.with(this)
            .load(image)
            .fitCenter()
            .into(iv_detailImage)

        tv_detailNameAndHeadline.text = "${name} ${headline}"
        tv_detailDescription.text = "${description}"
        tv_detailValues1.text = "◦ Difficulty: ${difficulty} | Time: ${time}"
        tv_detailValues2.text = "◦ Calories: ${calories} | Carbos: ${carbos}"
        tv_detailValues3.text = "◦ Fats: ${fats} | Proteins: ${proteins}"
    }
}