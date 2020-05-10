package com.example.foodrecipes.ui.recipeDetails

import androidx.lifecycle.LiveData
import com.example.foodrecipes.Repository.FoodRecipeNetworkDataSource
import com.example.foodrecipes.Repository.NetworkState
import com.example.foodrecipes.api.IFoodRecipesAPI
import com.example.foodrecipes.vo.RecipeResponse
import io.reactivex.disposables.CompositeDisposable

class RecipeDetailRepository(private val apiService: IFoodRecipesAPI) {

    lateinit var foodRecipeSearchNetworkDataSource: FoodRecipeNetworkDataSource

    fun fetchSingleRecipeDetails(
        compositeDisposable: CompositeDisposable,
        recipeId: String
    ): LiveData<RecipeResponse> {

        foodRecipeSearchNetworkDataSource =
            FoodRecipeNetworkDataSource(apiService, compositeDisposable)
        foodRecipeSearchNetworkDataSource.fetchRecipeDatails(recipeId)

        return foodRecipeSearchNetworkDataSource.recipeResponse

    }

    fun fetchRecipeDetailsNetworkState() : LiveData<NetworkState>{
        return foodRecipeSearchNetworkDataSource.networkState
    }

}