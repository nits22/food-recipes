package com.example.foodrecipes.Repository

import androidx.lifecycle.MutableLiveData
import com.example.foodrecipes.api.IFoodRecipesAPI
import com.example.foodrecipes.vo.Recipe
import io.reactivex.disposables.CompositeDisposable

class RecipeSearchDataSourceFactory(
    private val getRecipeApi: IFoodRecipesAPI,
    private val compositeDisposable: CompositeDisposable,
    private val query : String

) : androidx.paging.DataSource.Factory<Int, Recipe>(){

    val recipeListLiveDataSource = MutableLiveData<RecipeSearchDataSource>()



    override fun create(): androidx.paging.DataSource<Int, Recipe> {
        var movieDataSource = RecipeSearchDataSource(getRecipeApi, compositeDisposable, query)

        recipeListLiveDataSource.postValue(movieDataSource)
        return movieDataSource
    }
}