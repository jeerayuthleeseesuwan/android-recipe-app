package com.example.recipeapp.controller

import android.app.Activity
import android.content.ContentResolver
import android.net.Uri
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.core.content.contentValuesOf
import com.example.recipeapp.R
import com.example.recipeapp.controller.helper.GetFileExtHelper
import com.example.recipeapp.controller.helper.ReadResourceHelper
import com.example.recipeapp.controller.helper.ReadResourceHelperImpl
import com.example.recipeapp.model.Recipe
import com.example.recipeapp.repository.RecipeDataRepository
import com.example.recipeapp.repository.RecipeDataRepositoryFirebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class AddUpdateRecipeController(private var activity: Activity) {
    var recipeTypes = arrayOf<String>()
    var editMode = false
    var selectedImg : Uri? = null
    private var repository: RecipeDataRepository = RecipeDataRepositoryFirebase(FirebaseAuth.getInstance().currentUser?.uid ?: "no-uid")

    fun extractRecipeTypes() {
        //Dependency injection concept
        val helper : ReadResourceHelper = ReadResourceHelperImpl()
        recipeTypes = helper.extractResource(R.array.recipetypes, activity)
    }

    fun createRecipe(recipe: Recipe, onDone: () -> Unit) {
        if (recipe.title.isNotBlank()) {
            var fileExtension = GetFileExtHelper().getFileExtension(selectedImg, activity.contentResolver)
            repository.uploadImg(
                selectedImg,
                fileExtension,
                {
                    recipe.imgUrl = it // set uploaded img url link

                    repository.createRecipe(
                        recipe, {
                            //Done
                            onDone()
                            activity.finish()
                        },
                        {
                            //Error
                            Toast.makeText(activity, it, Toast.LENGTH_LONG).show()
                            onDone()
                        }
                    )
                },
                {
                    //Error
                    Toast.makeText(activity, it, Toast.LENGTH_LONG).show()
                    onDone()
                }
            )

        } else {
            Toast.makeText(activity, "Please enter your recipe title.", Toast.LENGTH_SHORT).show()
            onDone()
        }
    }

    fun updateRecipe(recipe: Recipe, onDone: () -> Unit) {
        if (recipe.title.isNotBlank()) {
            var fileExtension = GetFileExtHelper().getFileExtension(selectedImg, activity.contentResolver)
            // Upload Image
            repository.uploadImg(
                selectedImg,
                fileExtension,
                {
                    recipe.imgUrl = it // set uploaded img url link

                    // Update Recipe
                    repository.updateRecipe(
                        recipe, {
                            //Done
                            onDone()
                            activity.finish()
                        },
                        {
                            //Error
                            Toast.makeText(activity, it, Toast.LENGTH_LONG).show()
                            onDone()
                        }
                    )
                },
                {
                    //Error
                    Toast.makeText(activity, it, Toast.LENGTH_LONG).show()
                    onDone()
                }
            )
        } else {
            Toast.makeText(activity, "Please enter your recipe title.", Toast.LENGTH_SHORT).show()
            onDone()
        }
    }
}