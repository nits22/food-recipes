package com.example.foodrecipes.ui.recipeDetails

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.foodrecipes.BaseActivity
import com.example.foodrecipes.R
import com.example.foodrecipes.Repository.NetworkState
import com.example.foodrecipes.api.IFoodRecipesAPI
import com.example.foodrecipes.api.ServiceGenerator
import com.example.foodrecipes.vo.Recipe
import com.example.foodrecipes.vo.RecipeResponse
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.activity_recipe.*

class RecipeActivity : BaseActivity() {

    private lateinit var viewModel: RecipeDetailsViewModel
    private lateinit var recipeDetailsRepository : RecipeDetailRepository

    // UI components
    private lateinit var mRecipeImage: ImageView
    private var mRecipeTitle: TextView? = null
    private var mRecipeRank:TextView? = null
    private var mRecipeIngredientsContainer: LinearLayout? = null
    private var mScrollView: ScrollView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe)

        val recipeId: String = intent.getStringExtra("RECIPE_ID")

        mRecipeImage = findViewById(R.id.recipe_image)
        mRecipeTitle = findViewById(R.id.recipe_title)
        mRecipeRank = findViewById(R.id.recipe_social_score)
        mRecipeIngredientsContainer = findViewById(R.id.ingredients_container)
        mScrollView = findViewById(R.id.parent)

        val apiService = ServiceGenerator.buildService(IFoodRecipesAPI::class.java)
        recipeDetailsRepository = RecipeDetailRepository(apiService)

        viewModel = getViewModel(recipeId)

        viewModel.recipeDetails.observe(this, Observer {
            setRecipeProperties(it)
        })

        viewModel.networkState.observe(this, Observer {
            txt_error.visibility = if (it == NetworkState.ERROR) View.VISIBLE else View.GONE
            showProgressBar(if (it == NetworkState.ERROR) true else false)

        })
    }

    private fun getViewModel(recipeId : String) : RecipeDetailsViewModel{

        return ViewModelProviders.of(this, object : ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return RecipeDetailsViewModel(recipeDetailsRepository, recipeId) as T
            }

        })[RecipeDetailsViewModel::class.java]
    }


    private fun setRecipeProperties(recipeResponse: RecipeResponse?) {
        var recipe = recipeResponse?.recipe
        if (recipe != null) {
            val requestOptions = RequestOptions()
                .placeholder(R.drawable.ic_launcher_background)

            Glide.with(this)
                .setDefaultRequestOptions(requestOptions)
                .load(recipe!!.image_url)
                .into(mRecipeImage)

            mRecipeTitle?.setText(recipe!!.title)
            mRecipeRank?.setText(recipe!!.social_rank.toInt().toString())

            mRecipeIngredientsContainer?.removeAllViews()
            for (ingredient in recipe!!.ingredients) {
                val textView = TextView(this)
                textView.setText(ingredient)
                textView.textSize = 15f
                textView.layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
                )
                mRecipeIngredientsContainer?.addView(textView)
            }
        }

        showParent()
    }

    private fun showParent() {
        mScrollView?.setVisibility(View.VISIBLE)
    }
}
