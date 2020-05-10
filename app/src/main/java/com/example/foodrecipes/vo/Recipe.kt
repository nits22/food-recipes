package com.example.foodrecipes.vo

import android.os.Parcel
import android.os.Parcelable

data class Recipe(
    val __v: Int,
    val _id: String,
    val image_url: String,
    val ingredients: List<String>,
    val publisher: String,
    val publisher_url: String,
    val recipe_id: String,
    val social_rank: Double,
    val source_url: String,
    val title: String
) {
}