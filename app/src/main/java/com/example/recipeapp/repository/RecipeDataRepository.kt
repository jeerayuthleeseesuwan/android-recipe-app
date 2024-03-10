package com.example.recipeapp.repository

import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.example.recipeapp.model.Recipe
import com.google.firebase.storage.FirebaseStorage

interface RecipeDataRepository {
    fun createRecipe(recipe: Recipe, onSuccess: () -> Unit, onFailure: (String) -> Unit)
    fun getRecipes(onRecipeListChange: (recipeList: List<Recipe>) -> Unit)
    fun updateRecipe(recipe: Recipe, onSuccess: () -> Unit, onFailure: (String) -> Unit)
    fun deleteRecipe(titleToDelete: String, onSuccess: () -> Unit, onFailure: (String) -> Unit)
    fun uploadImg(selectedImgUri: Uri?, fileExt: String?, onSuccess: (String) -> Unit, onFailure:(String) -> Unit)
}