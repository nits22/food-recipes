package com.example.foodrecipes.Repository

import android.util.Log
import androidx.lifecycle.MutableLiveData

import androidx.paging.PageKeyedDataSource
import com.example.foodrecipes.api.FIRST_PAGE
import com.example.foodrecipes.api.IFoodRecipesAPI
import com.example.foodrecipes.api.POST_PER_PAGE
import com.example.foodrecipes.vo.Recipe
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class RecipeSearchDataSource(
    private val getRecipeApi: IFoodRecipesAPI,
    private val compositeDisposable: CompositeDisposable,
    private val query: String
) : PageKeyedDataSource<Int, Recipe>() {

    private var page = FIRST_PAGE

    val networkState: MutableLiveData<NetworkState> = MutableLiveData()

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Recipe>
    ) {
        networkState.postValue(NetworkState.LOADING)

        try {
            compositeDisposable.add(
                getRecipeApi.searchRecipe(query, page.toString())
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                        {
                            callback.onResult(it.recipes, null, page + 1)
                            networkState.postValue(NetworkState.LOADED)
                        },
                        {
                            networkState.postValue(NetworkState.ERROR)
                            Log.e("RecipeSearchDataSource", it.message)
                        }
                    )
            )
        } catch (e: Exception) {
            Log.e("RecipeSearchDataSource", e.message)
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Recipe>) {

        networkState.postValue(NetworkState.LOADING)

        try {
            compositeDisposable.add(
                getRecipeApi.searchRecipe("Chicken", params.key.toString())
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                        {
                            if (it.count == POST_PER_PAGE)
                                callback.onResult(it.recipes, params.key + 1)
                            networkState.postValue(NetworkState.LOADED)
                        },
                        {
                            networkState.postValue(NetworkState.ENDOFLIST)
                        }
                    )
            )
        } catch (e: Exception) {
            Log.e("RecipeSearchDataSource", e.message)
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Recipe>) {

    }

}