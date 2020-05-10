package com.example.foodrecipes.ui.searchRecipe

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodrecipes.BaseActivity
import com.example.foodrecipes.R
import com.example.foodrecipes.Repository.NetworkState
import com.example.foodrecipes.api.IFoodRecipesAPI
import com.example.foodrecipes.api.ServiceGenerator
import kotlinx.android.synthetic.main.activity_recipe_list.*

class RecipeListActivity : AppCompatActivity() {

    private lateinit var viewModel: RecipeListViewModel

    private lateinit var searchViewModel: RecipeListViewModel

    lateinit var recipeRepository: RecipePagedListRepository

    private lateinit var linearLayoutManager: LinearLayoutManager

    private lateinit var recipeAdapter : RecipeSearchPagedListAdapter

    private lateinit var context : LifecycleOwner

    private lateinit var apiService : IFoodRecipesAPI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_list)

        apiService = ServiceGenerator.buildService(IFoodRecipesAPI::class.java)

        recipeRepository = RecipePagedListRepository(apiService, "Chicken")

        viewModel = getViewModel()
        context = this
        recipeAdapter = RecipeSearchPagedListAdapter(this)

        linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        rv_movie_list.layoutManager = linearLayoutManager
        rv_movie_list.setHasFixedSize(true)
        rv_movie_list.adapter = recipeAdapter

        viewModel.recipePagedList.observe(this, Observer {
            recipeAdapter.submitList(it)
        })

        viewModel.networkState.observe(this, Observer {
            progress_bar_popular.visibility = if (viewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
            txt_error_popular.visibility = if (viewModel.listIsEmpty() && it == NetworkState.ERROR) View.VISIBLE else View.GONE

            if (!viewModel.listIsEmpty()) {
                recipeAdapter.setNetworkState(it)
            }
        })
        println("TESTING APP")
        initSearchView()


    }

    private fun getViewModel(): RecipeListViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return RecipeListViewModel(recipeRepository, "Chicken") as T
            }
        })[RecipeListViewModel::class.java]
    }

    private fun getViewModel(query : String): RecipeListViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return RecipeListViewModel(recipeRepository, query) as T
            }
        })[RecipeListViewModel::class.java]
    }


    fun testRetrofit() {
        var recipeApi = ServiceGenerator.buildService(IFoodRecipesAPI::class.java)
        var myCall = recipeApi.searchRecipe("chicken breast", "1")

    }

    fun initSearchView(){
        search_view.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                recipeRepository = RecipePagedListRepository(apiService, query!!)
                searchViewModel =  RecipeListViewModel(recipeRepository, "")
                searchViewModel.searchPagedList.observe(context, Observer {
                    recipeAdapter.clear()
                    recipeAdapter.submitList(it)
                })
                Log.i("RecipeListActivity","Llego al querytextchange")
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
    }
}
