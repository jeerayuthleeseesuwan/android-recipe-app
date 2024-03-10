package com.example.recipeapp.model

import com.example.recipeapp.constants.Constants

data class Recipe(
    var title: String,
    var recipeType: String?,
    var ingredients: String?,
    var steps: String?,
    var imgUrl: String?
)
