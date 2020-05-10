package com.example.foodrecipes.vo

data class RecipeSearchResponse(
    val count: Int,
    val recipes: List<Recipe>
)