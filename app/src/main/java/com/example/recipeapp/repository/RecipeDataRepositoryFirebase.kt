package com.example.recipeapp.repository

import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.example.recipeapp.constants.Constants
import com.example.recipeapp.model.Recipe
import com.example.recipeapp.view.adapters.RecipeListAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

class RecipeDataRepositoryFirebase(userId: String) : RecipeDataRepository{

    private val databaseRef = FirebaseDatabase.getInstance().getReference(userId)

    override fun createRecipe(recipe: Recipe, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        databaseRef.child(recipe.title).setValue(recipe).addOnSuccessListener {
            onSuccess()
        }.addOnFailureListener{
            onFailure(it.toString())
        }
    }

    override fun getRecipes(onRecipeListChange: (recipeList: List<Recipe>) -> Unit) {
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    var recipeList = mutableListOf<Recipe>()
                    for (data in snapshot.children) {
                        if (data.child(Constants.FIRE_KEY_TITLE).exists()) {
                            val recipe = Recipe(
                                data.child(Constants.FIRE_KEY_TITLE).value.toString(),
                                data.child(Constants.FIRE_KEY_RECIPE_TYPE).value.toString(),
                                data.child(Constants.FIRE_KEY_INGREDIENTS).value.toString(),
                                data.child(Constants.FIRE_KEY_STEPS).value.toString(),
                                data.child(Constants.FIRE_KEY_IMG_URL).value.toString(),
                            )
                            recipeList.add(recipe)
                        }
                    }
                    onRecipeListChange(recipeList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    override fun updateRecipe(recipe: Recipe, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        val map = mutableMapOf<String, Any>()
        map[Constants.FIRE_KEY_RECIPE_TYPE] = recipe.recipeType ?: ""
        map[Constants.FIRE_KEY_INGREDIENTS] = recipe.ingredients ?: ""
        map[Constants.FIRE_KEY_STEPS] = recipe.steps ?: ""
        if (!recipe.imgUrl.isNullOrBlank()) {
            map[Constants.FIRE_KEY_IMG_URL] = recipe.imgUrl ?: ""
        }
        databaseRef.child(recipe.title).updateChildren(map).addOnSuccessListener {
            onSuccess()
        }.addOnFailureListener{
            onFailure(it.toString())
        }
    }

    override fun deleteRecipe(
        titleToDelete: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        databaseRef.child(titleToDelete).removeValue().addOnSuccessListener {
            onSuccess()
        }.addOnFailureListener{
            onFailure(it.toString())
        }
    }

    override fun uploadImg(selectedImgUri: Uri?, fileExt: String?, onSuccess: (String) -> Unit, onFailure:(String) -> Unit){
        if (selectedImgUri != null && fileExt != null) {
            var storageReference = FirebaseStorage.getInstance().reference
            var imageReference = storageReference.child(System.currentTimeMillis().toString() + "." + fileExt)
            imageReference.putFile(selectedImgUri!!).addOnSuccessListener {
                imageReference.downloadUrl.addOnSuccessListener{
                    Log.d("MyTag", it.toString())
                    onSuccess(it.toString())
                }
            }.addOnFailureListener{
                Log.d("MyTag", it.toString())
                onFailure(it.toString())
            }
        } else {
            // No picture to upload
            onSuccess("")
        }
    }

}