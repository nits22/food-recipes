package com.example.foodrecipes.Repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.example.foodrecipes.api.FIRST_PAGE
import com.example.foodrecipes.vo.Recipe
import io.reactivex.disposables.CompositeDisposable


class CategoryDataSource(
    private val compositeDisposable: CompositeDisposable,
    val provider: StringListProvider
) : PageKeyedDataSource<Int, Recipe>() {

    private var page = FIRST_PAGE

    val networkState: MutableLiveData<NetworkState> = MutableLiveData()

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Recipe>
    ) {
        networkState.postValue(NetworkState.LOADING)

        try {
            val list: MutableList<Recipe> = provider.getStringList(0, params.requestedLoadSize)
            callback.onResult(list, null, page + 1)
            networkState.postValue(NetworkState.LOADED)
        } catch (e: Exception) {
            Log.e("CategoryDataSource", e.message)
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Recipe>) {

        networkState.postValue(NetworkState.LOADING)

        try {
            val list = provider.getStringList(params.key, params.requestedLoadSize)
            callback.onResult(list, params.key + 1)
            networkState.postValue(NetworkState.LOADED)
        } catch (e: Exception) {
            Log.e("RecipeSearchDataSource", e.message)
            networkState.postValue(NetworkState.ENDOFLIST)
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Recipe>) {

    }

}

class StringListProvider(val list: MutableList<Recipe>) {


    fun getStringList(page: Int, pageSize: Int): MutableList<Recipe> {

        val initialIndex = page * pageSize
        val finalIndex =
            if ((initialIndex + pageSize) > list.size && initialIndex < list.size - 1) list.size else (initialIndex + pageSize)
        //TODO manage index out of range
        return list.subList(initialIndex, finalIndex)
    }
}

