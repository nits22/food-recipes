package com.example.foodrecipes.Repository

import androidx.lifecycle.MutableLiveData
import com.example.foodrecipes.vo.Recipe
import io.reactivex.disposables.CompositeDisposable

class CategoryDataSourceFactory (
private val compositeDisposable: CompositeDisposable,
private val provider : StringListProvider

) : androidx.paging.DataSource.Factory<Int, Recipe>(){

    val categoryDataSource = MutableLiveData<CategoryDataSource>()

    override fun create(): androidx.paging.DataSource<Int, Recipe> {
        var movieDataSource = CategoryDataSource(compositeDisposable, provider)
        categoryDataSource.postValue(movieDataSource)
        return movieDataSource
    }
}