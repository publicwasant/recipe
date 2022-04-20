package com.example.recipeproject.lib.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteTable (
    @PrimaryKey
    val id: String
)