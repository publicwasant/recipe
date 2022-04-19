package com.example.recipeproject.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipeproject.lib.network.RetroInstanceNetwork
import com.example.recipeproject.lib.network.RetroServiceNetwork
import com.example.recipeproject.model.RecipeModel
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class RecipeViewModel: ViewModel() {
    private var recipes: MutableLiveData<ArrayList<RecipeModel>>? = null

    init {
        this.recipes = MutableLiveData()
    }

    fun getObserver(): MutableLiveData<ArrayList<RecipeModel>> {
        return this.recipes!!
    }

    fun call() {
        val retroInstance = RetroInstanceNetwork.getRetroInstance().create(RetroServiceNetwork::class.java)

        retroInstance.getRecipes()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this.getRecipeObserverRx())
    }

    private fun getRecipeObserverRx(): Observer<ArrayList<RecipeModel>> {
        return object : Observer<ArrayList<RecipeModel>> {
            override fun onComplete() {}

            override fun onError(e: Throwable) {
                recipes?.postValue(null)
            }

            override fun onNext(t: ArrayList<RecipeModel>) {
                recipes?.postValue(t)
            }

            override fun onSubscribe(d: Disposable) {}
        }
    }
}