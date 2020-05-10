package com.example.foodrecipes.api

import com.example.foodrecipes.vo.RecipeResponse
import com.example.foodrecipes.vo.RecipeSearchResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface IFoodRecipesAPI {

    // SEARCH
    @GET("api/search")
    fun searchRecipe(
        @Query("q") query: String,
        @Query("page") page: String
    ): Observable<RecipeSearchResponse>

    // GET RECIPE REQUEST
    @GET("api/get")
    fun getRecipe(
        @Query("rId") recipe_id: String
    ): Observable<RecipeResponse>
}