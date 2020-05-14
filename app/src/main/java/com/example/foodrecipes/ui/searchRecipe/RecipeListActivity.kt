package com.example.foodrecipes.ui.searchRecipe

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
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
import kotlinx.android.synthetic.main.layout_category_list_item.*
import kotlinx.android.synthetic.main.layout_category_list_item.view.*
import kotlinx.android.synthetic.main.layout_category_list_item.view.category_linear
import kotlinx.android.synthetic.main.layout_recipe_list_item.*
import kotlinx.android.synthetic.main.network_state_item.*

class RecipeListActivity : BaseActivity() {

    private lateinit var viewModel: RecipeListViewModel

    private lateinit var searchViewModel: RecipeListViewModel

    lateinit var recipeRepository: RecipePagedListRepository

    private lateinit var linearLayoutManager: LinearLayoutManager

    private lateinit var recipeAdapter: RecipeSearchPagedListAdapter

    private lateinit var context: LifecycleOwner

    private lateinit var apiService: IFoodRecipesAPI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_list)

        apiService = ServiceGenerator.buildService(IFoodRecipesAPI::class.java)

        val recipeType: String = intent.getStringExtra("CATEGORY_ID") ?: ""
        recipeRepository = RecipePagedListRepository(apiService, recipeType)

        viewModel = getViewModel()
        context = this
        recipeAdapter = RecipeSearchPagedListAdapter(this)


        linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        rv_movie_list.layoutManager = linearLayoutManager
        rv_movie_list.setHasFixedSize(true)
        rv_movie_list.adapter = recipeAdapter
        if (!recipeAdapter.categoryVisible() && recipeType.equals("")) {
            viewModel.categoryPagedList.observe(this, Observer {
                if (!viewModel.isViewingRecipes()) {
                    viewModel.setIsViewingRecipe(true)
                    recipeAdapter.notifyDataSetChanged()
                    recipeAdapter.submitList(it)

                } else
                    recipeAdapter.submitList(it)
            })

            viewModel.networkStateForCategory.observe(this, Observer {
                progress_bar_popular.visibility =
                    if (viewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
                //txt_error_popular.visibility = if (viewModel.listIsEmpty() && it == NetworkState.ERROR) View.VISIBLE else View.GONE
                showProgressBar(if (it == NetworkState.ERROR) true else false)
                showErrorMessage(if (it == NetworkState.ERROR) true else false, null)
                if (!viewModel.listIsEmpty()) {
                    recipeAdapter.setNetworkState(it)
                }
            })
        } else {
            viewModel = getViewModel(recipeType)
            viewModel.recipePagedList.observe(this, Observer {
                if (!viewModel.isViewingRecipes()) {
                    recipeAdapter.notifyDataSetChanged()
                    recipeAdapter.submitList(it)

                }
            })
            viewModel.networkState.observe(this, Observer {
                progress_bar_popular.visibility =
                    if (viewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
                //txt_error_popular.visibility = if (viewModel.listIsEmpty() && it == NetworkState.ERROR) View.VISIBLE else View.GONE
                showProgressBar(if (it == NetworkState.ERROR) true else false)
                showErrorMessage(if (it == NetworkState.ERROR) true else false, null)
                if (!viewModel.listIsEmpty()) {
                    recipeAdapter.setNetworkState(it)
                }
            })
        }

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

    private fun getViewModel(query: String): RecipeListViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return RecipeListViewModel(recipeRepository, query) as T
            }
        })[RecipeListViewModel::class.java]
    }

    fun initSearchView() {
        search_view.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                recipeRepository = RecipePagedListRepository(apiService, query!!)
                searchViewModel = RecipeListViewModel(recipeRepository, "")
                searchViewModel.searchPagedList.observe(context, Observer {
                    recipeAdapter.clear()
                    recipeAdapter.submitList(it)
                })
                Log.i("RecipeListActivity", "Llego al querytextchange")

                searchViewModel.networkStateForSearch.observe(context, Observer {
                    progress_bar_popular.visibility =
                        if (searchViewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
                    //txt_error_popular.visibility = if (searchViewModel.listIsEmpty() && it == NetworkState.ERROR) View.VISIBLE else View.GONE

                    showProgressBar(if (it == NetworkState.ERROR) true else false)
                    showErrorMessage(if (it == NetworkState.ERROR) true else false, null)

                    if (!searchViewModel.listIsEmpty()) {
                        recipeAdapter.setNetworkState(it)
                    }
                })
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

}
