package com.example.foodrecipes.Repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.foodrecipes.api.IFoodRecipesAPI
import com.example.foodrecipes.vo.RecipeResponse
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class FoodRecipeNetworkDataSource(
    private val getRecipeApi: IFoodRecipesAPI,
    private val compositeDisposable: CompositeDisposable
) {


    private var _recipeResponse = MutableLiveData<RecipeResponse>()
    val recipeResponse: LiveData<RecipeResponse>
        get() = _recipeResponse


    private var _networkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState>
        get() = _networkState

    fun fetchRecipeDatails(recipeId: String) {
        _networkState.postValue(NetworkState.LOADING)

        try {
            compositeDisposable.add(
                getRecipeApi.getRecipe(recipeId)
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                        {
                            _recipeResponse.postValue(it)
                            _networkState.postValue(NetworkState.LOADED)
                        },
                        {
                            _networkState.postValue(NetworkState.ERROR)
                            Log.e("FoodRecipeNetwork", it.message)
                        }
                    )
            )
        } catch (e: Exception) {
            Log.e("FoodRecipeNetwork", e.message)
        }

    }

}