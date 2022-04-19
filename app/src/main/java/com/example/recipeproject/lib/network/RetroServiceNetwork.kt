package com.example.recipeproject.lib.network

import com.example.recipeproject.model.RecipeModel
import io.reactivex.Observable
import retrofit2.http.GET

interface RetroServiceNetwork {
    @GET("recipes.json")
    fun getRecipes(): Observable<ArrayList<RecipeModel>>
}