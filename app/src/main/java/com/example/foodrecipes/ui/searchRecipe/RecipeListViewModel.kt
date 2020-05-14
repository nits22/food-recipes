package com.example.foodrecipes.ui.searchRecipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.example.foodrecipes.Repository.NetworkState
import com.example.foodrecipes.vo.Recipe
import io.reactivex.disposables.CompositeDisposable

class RecipeListViewModel(private val recipeListRepostory: RecipePagedListRepository, private val query: String) :
    ViewModel() {

    private val compositeDisposable  = CompositeDisposable()
    private var isViewingRecipe: Boolean = false

    val recipePagedList : LiveData<PagedList<Recipe>> by lazy {
        recipeListRepostory.fetchLiveRecipePagedList(compositeDisposable)
    }

    val searchPagedList : LiveData<PagedList<Recipe>> by lazy {
        recipeListRepostory.fetchLiveSearchPagedList(compositeDisposable)
    }

    val categoryPagedList : LiveData<PagedList<Recipe>> by lazy {
        //isViewingRecipe = true
        recipeListRepostory.fetchCategoryPagedList(compositeDisposable)
    }

    val networkState : LiveData<NetworkState> by lazy {
        recipeListRepostory.fetchLiveNetworkState()
    }

    val networkStateForCategory : LiveData<NetworkState> by lazy {
        recipeListRepostory.fetchLiveNetworkStateCategory()
    }
    val networkStateForSearch : LiveData<NetworkState> by lazy {
        recipeListRepostory.fetchLiveNetworkStateForSearch()
    }

    fun listIsEmpty() : Boolean{
        return recipePagedList.value?.isEmpty() ?: true
    }

    fun listIsEmptyCateogry() : Boolean{
        return categoryPagedList.value?.isEmpty() ?: true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun setIsViewingRecipe(isViewingRecipe : Boolean) {
        this.isViewingRecipe = isViewingRecipe
    }

    fun isViewingRecipes() : Boolean {
        return isViewingRecipe
    }


}