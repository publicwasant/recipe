package com.example.recipeproject.lib.room

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavoriteDAO {
    /*
    * Recipe Data Access Object
    */

    @Query("SELECT * FROM favorites")
    fun read(): LiveData<List<FavoriteTable>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun add(favoriteTable: FavoriteTable)

    @Delete
    fun delete(favoriteTable: FavoriteTable)
}