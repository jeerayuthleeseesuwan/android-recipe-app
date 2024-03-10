package com.example.recipeapp.view.main

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.recipeapp.R
import com.example.recipeapp.constants.Constants
import com.example.recipeapp.controller.RecipeDetailsController
import com.example.recipeapp.databinding.ActivityMainBinding
import com.example.recipeapp.databinding.ActivityRecipeDetailsBinding
import com.example.recipeapp.model.Recipe

class RecipeDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecipeDetailsBinding
    private lateinit var _controller: RecipeDetailsController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipeDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        _controller = RecipeDetailsController(this)

        // get from intent
        val title : String? = intent.getStringExtra(Constants.KEY_TITLE)
        val recipe = Recipe(
            title ?: "",
            intent.getStringExtra(Constants.KEY_RECIPE_TYPE),
            intent.getStringExtra(Constants.KEY_INGREDIENTS),
            intent.getStringExtra(Constants.KEY_STEPS),
            intent.getStringExtra(Constants.KEY_IMG_URL)
        )

        binding.title.text = recipe.title
        binding.recipeType.text = recipe.recipeType
        binding.ingredients.text = recipe.ingredients
        binding.steps.text = recipe.steps
        try {
            Glide.with(this)
                .load(recipe.imgUrl)
                .centerCrop() // Scale type for the image
                .into(binding.image)
        } catch (e : Exception){
            //Error
            Log.d("MyTag", e.toString())
        }

        binding.editBtn.setOnClickListener{
            _controller.goToEditPage(recipe)
        }

        binding.deleteBtn.setOnClickListener {
            binding.deleteBtn.isEnabled = false
            _controller.deleteRecipe(recipe.title) {
                binding.deleteBtn.isEnabled = true
            }
        }
    }
}