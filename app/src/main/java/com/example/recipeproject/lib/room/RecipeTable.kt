package com.example.recipeproject.lib.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class RecipeTable (
    @PrimaryKey
    var id: String,
    var calories: String,
    var carbos: String,
    var description: String,
    var difficulty: Int,
    var fats: String,
    var headline: String,
    var image: String,
    var name: String,
    var proteins: String,
    var thumb: String,
    var time: String,
)