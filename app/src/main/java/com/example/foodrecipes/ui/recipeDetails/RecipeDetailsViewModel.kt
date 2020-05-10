package com.example.foodrecipes.ui.recipeDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.foodrecipes.Repository.NetworkState
import com.example.foodrecipes.vo.RecipeResponse
import io.reactivex.disposables.CompositeDisposable

class RecipeDetailsViewModel(private val recipeDetailRepository: RecipeDetailRepository, recipeId : String) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val recipeDetails : LiveData<RecipeResponse> by lazy {
        recipeDetailRepository.fetchSingleRecipeDetails(compositeDisposable, recipeId)
    }

    val networkState : LiveData<NetworkState> by lazy {
        recipeDetailRepository.fetchRecipeDetailsNetworkState()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}