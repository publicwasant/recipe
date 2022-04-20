package com.example.recipeproject.lib.room

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RecipeDAO {
    /*
    * Recipe Data Access Object
    */

    @Query("SELECT * FROM recipes")
    fun read(): LiveData<List<RecipeTable>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun add(recipeTable: RecipeTable)

    @Update
    fun update(recipeTable: RecipeTable)

    @Query("DELETE FROM recipes")
    fun deleteAll()
}