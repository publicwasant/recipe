package com.example.recipeproject.viewModel

import android.app.Application
import androidx.lifecycle.*
import com.example.recipeproject.lib.thread.BackgroundFetchThread
import com.example.recipeproject.lib.network.RetroUsableNetwork
import com.example.recipeproject.lib.room.*

class RecipeViewModel(application: Application): AndroidViewModel(application) {
    private var backgroundFetchThread: BackgroundFetchThread = BackgroundFetchThread()
    private var db: RecipeUsableRoom = RecipeUsableRoom(application, this)
    private var api: RetroUsableNetwork = RetroUsableNetwork()

    fun getDB(): RecipeUsableRoom {
        return this.db
    }

    fun getAPI(): RetroUsableNetwork {
        return this.api
    }

    fun setBackgroundTask(t: (Int) -> Unit) {
        this.backgroundFetchThread.setTask(t)
    }

    fun startBackgroundTask() {
        this.backgroundFetchThread.start()
    }
}