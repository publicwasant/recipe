package com.example.recipeproject.lib.network

import androidx.lifecycle.*
import com.example.recipeproject.model.RecipeModel
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlin.collections.ArrayList

class RetroUsableNetwork {
        private var retroService: RetroServiceNetwork = RetroInstanceNetwork.getRetroInstance().create(RetroServiceNetwork::class.java)
        private var items: MutableLiveData<ArrayList<RecipeModel>> = MutableLiveData()

        fun readAll(f: (MutableLiveData<ArrayList<RecipeModel>>) -> Unit) {
            this.retroService.getRecipes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<ArrayList<RecipeModel>> {
                    override fun onComplete() {}

                    override fun onError(e: Throwable) {
                        items.postValue(null)
                    }

                    override fun onNext(t: ArrayList<RecipeModel>) {
                        items.postValue(t)
                    }

                    override fun onSubscribe(d: Disposable) {}
                })
            f(this.items)
        }
    }