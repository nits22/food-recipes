package com.example.foodrecipes.ui.searchRecipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.foodrecipes.Repository.NetworkState
import com.example.foodrecipes.Repository.RecipeSearchDataSource
import com.example.foodrecipes.Repository.RecipeSearchDataSourceFactory
import com.example.foodrecipes.Repository.RecipeSearchDataSourceFactoryNew
import com.example.foodrecipes.api.IFoodRecipesAPI
import com.example.foodrecipes.api.POST_PER_PAGE
import com.example.foodrecipes.vo.Recipe
import io.reactivex.disposables.CompositeDisposable

class RecipePagedListRepository(private val getRecipeApi: IFoodRecipesAPI, private val query : String) {

    lateinit var recipePagedList : LiveData<PagedList<Recipe>>
    lateinit var recipeSearchDataSourceFactory: RecipeSearchDataSourceFactory
    lateinit var recipeSearchDataSourceFactoryNew : RecipeSearchDataSourceFactoryNew

    fun fetchLiveRecipePagedList(compositeDisposable: CompositeDisposable) : LiveData<PagedList<Recipe>>{

        recipeSearchDataSourceFactory = RecipeSearchDataSourceFactory(getRecipeApi, compositeDisposable, query)

        var config = PagedList.Config.Builder()
            .setEnablePlaceholders(true)
            .setPageSize(POST_PER_PAGE)
            .build()

        recipePagedList = LivePagedListBuilder(recipeSearchDataSourceFactory, config).build()

        return recipePagedList
    }

    fun fetchLiveSearchPagedList(compositeDisposable: CompositeDisposable) : LiveData<PagedList<Recipe>>{

        recipeSearchDataSourceFactoryNew = RecipeSearchDataSourceFactoryNew(getRecipeApi, compositeDisposable, query)

        var config = PagedList.Config.Builder()
            .setEnablePlaceholders(true)
            .setPageSize(POST_PER_PAGE)
            .build()

        recipePagedList = LivePagedListBuilder(recipeSearchDataSourceFactoryNew, config).build()

        return recipePagedList
    }

    fun fetchLiveNetworkState() : LiveData<NetworkState>{
        return Transformations.switchMap<RecipeSearchDataSource, NetworkState>(
            recipeSearchDataSourceFactory.recipeListLiveDataSource, RecipeSearchDataSource::networkState)
    }

    fun fetchLiveNetworkStateForSearch() : LiveData<NetworkState>{
        return Transformations.switchMap<RecipeSearchDataSource, NetworkState>(
            recipeSearchDataSourceFactoryNew.recipeListLiveDataSource, RecipeSearchDataSource::networkState)
    }


}