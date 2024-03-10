package com.example.recipeapp.controller

import android.app.Activity
import android.content.Intent
import com.example.recipeapp.R
import com.example.recipeapp.constants.Constants
import com.example.recipeapp.controller.helper.ReadResourceHelper
import com.example.recipeapp.controller.helper.ReadResourceHelperImpl
import com.example.recipeapp.model.Recipe
import com.example.recipeapp.repository.RecipeDataRepository
import com.example.recipeapp.repository.RecipeDataRepositoryFirebase
import com.example.recipeapp.view.auth.LoginActivity
import com.example.recipeapp.view.main.RecipeDetailsActivity
import com.example.recipeapp.view.adapters.RecipeListAdapter
import com.google.firebase.auth.FirebaseAuth

class MainController(private var activity: Activity) {

    var recipeTypes = arrayOf<String>()
    var recipeList = listOf<Recipe>()
    var adapter : RecipeListAdapter? = null;
    private var repository: RecipeDataRepository = RecipeDataRepositoryFirebase(FirebaseAuth.getInstance().currentUser?.uid ?: "no-uid")

    fun extractRecipeTypes() {
        //Dependency injection concept
        val helper : ReadResourceHelper = ReadResourceHelperImpl()
        recipeTypes = helper.extractResource(R.array.recipetypes, activity)
    }

    fun getRecipes(onRecipeListChange: (RecipeListAdapter) -> Unit){
        repository.getRecipes { _recipeList ->
            recipeList = _recipeList

            var adapter = RecipeListAdapter( activity, recipeList ) {
                // on click, go to details page
                var intent = Intent(activity, RecipeDetailsActivity::class.java)
                intent.putExtra(Constants.KEY_TITLE, it.title)
                intent.putExtra(Constants.KEY_RECIPE_TYPE, it.recipeType)
                intent.putExtra(Constants.KEY_INGREDIENTS, it.ingredients)
                intent.putExtra(Constants.KEY_STEPS, it.steps)
                intent.putExtra(Constants.KEY_IMG_URL, it.imgUrl)
                activity.startActivity(intent)
            }

            this.adapter = adapter

            onRecipeListChange(adapter)
        }
    }

    fun filterRecipes (index: Int) {
        if (recipeTypes.isNotEmpty()) {
            adapter?.filter?.filter(recipeTypes[index])
        }
    }

    fun goToLoginPage(){
        val intent = Intent(activity, LoginActivity::class.java)
        activity.startActivity(intent)
        activity.finish()
    }

    fun logout() {
        FirebaseAuth.getInstance().signOut()
        goToLoginPage()
    }
}