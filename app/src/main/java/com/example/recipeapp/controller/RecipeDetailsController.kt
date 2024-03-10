package com.example.recipeapp.controller

import android.app.Activity
import android.content.Intent
import com.example.recipeapp.constants.Constants
import com.example.recipeapp.model.Recipe
import com.example.recipeapp.repository.RecipeDataRepository
import com.example.recipeapp.repository.RecipeDataRepositoryFirebase
import com.example.recipeapp.view.main.AddUpdateRecipeActivity
import com.google.firebase.auth.FirebaseAuth

class RecipeDetailsController(private var activity: Activity) {

    private var repository: RecipeDataRepository = RecipeDataRepositoryFirebase(FirebaseAuth.getInstance().currentUser?.uid ?: "no-uid")

    fun goToEditPage(recipe: Recipe){
        // on click, go to details page
        var intent = Intent(activity, AddUpdateRecipeActivity::class.java)
        intent.putExtra(Constants.KEY_TITLE, recipe.title)
        intent.putExtra(Constants.KEY_RECIPE_TYPE, recipe.recipeType)
        intent.putExtra(Constants.KEY_INGREDIENTS, recipe.ingredients)
        intent.putExtra(Constants.KEY_STEPS, recipe.steps)
        intent.putExtra(Constants.KEY_IMG_URL, recipe.imgUrl)
        activity.startActivity(intent)
        activity.finish()
    }

    fun deleteRecipe(toDelete: String, onDone: () -> Unit){
        repository.deleteRecipe(toDelete, {
            onDone()
            activity.finish()
        }, {
            onDone()
        })
    }
}