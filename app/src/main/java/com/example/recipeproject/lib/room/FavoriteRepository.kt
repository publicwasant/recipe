package com.example.recipeproject.lib.room

import androidx.lifecycle.LiveData

class FavoriteRepository(private val favoriteDAO: FavoriteDAO) {

    val read: LiveData<List<FavoriteTable>> = favoriteDAO.read()

    suspend fun add(favoriteTable: FavoriteTable){
        favoriteDAO.add(favoriteTable)
    }

    suspend fun delete(favoriteTable: FavoriteTable) {
        favoriteDAO.delete(favoriteTable)
    }
}