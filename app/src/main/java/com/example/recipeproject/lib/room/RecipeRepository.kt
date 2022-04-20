package com.example.recipeproject.lib.room

import androidx.lifecycle.LiveData

class RecipeRepository(private val recipeDAO: RecipeDAO) {

    val read: LiveData<List<RecipeTable>> = recipeDAO.read()

    suspend fun add(recipeTable: RecipeTable){
        recipeDAO.add(recipeTable)
    }

    suspend fun edit(recipeTable: RecipeTable){
        recipeDAO.update(recipeTable)
    }

    suspend fun deleteAll() {
        recipeDAO.deleteAll()
    }
}