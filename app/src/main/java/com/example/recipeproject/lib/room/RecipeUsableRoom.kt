package com.example.recipeproject.lib.room

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.recipeproject.model.FavoriteModel
import com.example.recipeproject.model.RecipeModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RecipeUsableRoom(application: Application, private val parentViewModel: AndroidViewModel) {
    private var recipeRepository: RecipeRepository = RecipeRepository(RecipeDatabase.getDatabase(application).recipeDao())
    private var favoriteRepository: FavoriteRepository = FavoriteRepository(FavoriteDatabase.getDatabase(application).favoriteDao())

    fun readFavorites(f: (LiveData<List<FavoriteTable>>) -> Unit) {
        f(this.favoriteRepository.read)
    }

    fun addFavorite(id: String) {
        this.parentViewModel.viewModelScope.launch(Dispatchers.IO) {
            favoriteRepository.add(FavoriteTable(id))
        }
    }

    fun deleteFavorite(id: String) {
        this.parentViewModel.viewModelScope.launch(Dispatchers.IO) {
            favoriteRepository.delete(FavoriteTable(id))
        }
    }

    fun read(f: (LiveData<List<RecipeTable>>) -> Unit) {
        f(this.recipeRepository.read)
    }

    fun backup(items: ArrayList<RecipeModel>) {
        this.parentViewModel.viewModelScope.launch(Dispatchers.IO) {
            items.forEach {
                recipeRepository.add(RecipeTable(
                    it.id,
                    it.calories,
                    it.carbos,
                    it.description,
                    it.difficulty,
                    it.fats,
                    it.headline,
                    it.image,
                    it.name,
                    it.proteins,
                    it.thumb,
                    it.time,
                ))
            }
        }
    }

    fun backupAndReset(items: ArrayList<RecipeModel>) {
        this.parentViewModel.viewModelScope.launch(Dispatchers.IO) {
            recipeRepository.deleteAll()
            items.forEach {
                recipeRepository.add(RecipeTable(
                    it.id,
                    it.calories,
                    it.carbos,
                    it.description,
                    it.difficulty,
                    it.fats,
                    it.headline,
                    it.image,
                    it.name,
                    it.proteins,
                    it.thumb,
                    it.time,
                ))
            }
        }
    }

    fun toFavoriteModel(items: List<FavoriteTable>): ArrayList<FavoriteModel> {
        val result: ArrayList<FavoriteModel> = arrayListOf()

        items.forEach {
            result.add(FavoriteModel(it.id))
        }

        return result
    }

    fun toRecipeModel(items: List<RecipeTable>): ArrayList<RecipeModel> {
        val result: ArrayList<RecipeModel> = arrayListOf()

        items.forEach {
            result.add(RecipeModel(
                it.calories,
                it.carbos,
                it.description,
                it.difficulty,
                it.fats,
                it.headline,
                it.id,
                it.image,
                it.name,
                it.proteins,
                it.thumb,
                it.time
            ))
        }

        return result
    }
}