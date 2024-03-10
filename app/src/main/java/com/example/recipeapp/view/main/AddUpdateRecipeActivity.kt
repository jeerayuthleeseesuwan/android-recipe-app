package com.example.recipeapp.view.main

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.example.recipeapp.constants.Constants
import com.example.recipeapp.controller.AddUpdateRecipeController
import com.example.recipeapp.databinding.ActivityAddUpdateRecipeBinding
import com.example.recipeapp.model.Recipe

class AddUpdateRecipeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddUpdateRecipeBinding
    private lateinit var _controller : AddUpdateRecipeController
    private lateinit var imageContract: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddUpdateRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        _controller = AddUpdateRecipeController(this)

        _controller.extractRecipeTypes()

        // setup the drop down menu
        var dropdownAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, _controller.recipeTypes)
        dropdownAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.dropdownMenu.adapter = dropdownAdapter

        // get from intent and Fill Text Field
        val title : String? = intent.getStringExtra(Constants.KEY_TITLE)
        _controller.editMode = (title != null)

        if (_controller.editMode) {
            val recipe = Recipe(
                title ?: "",
                intent.getStringExtra(Constants.KEY_RECIPE_TYPE),
                intent.getStringExtra(Constants.KEY_INGREDIENTS),
                intent.getStringExtra(Constants.KEY_STEPS),
                intent.getStringExtra(Constants.KEY_IMG_URL)
            )

            // Assign all the data to respective fields
            binding.title.setText(recipe.title)
            binding.dropdownMenu.setSelection(dropdownAdapter.getPosition(recipe.recipeType))
            binding.ingredients.setText(recipe.ingredients)
            binding.steps.setText(recipe.steps)
            try {
                Glide.with(this)
                    .load(recipe.imgUrl)
                    .centerCrop() // Scale type for the image
                    .into(binding.image)
            } catch (e : Exception){
                //Error
                Log.d("MyTag", e.toString())
            }

            binding.submitBtn.text = "Update Recipe"
        } else {
            binding.submitBtn.text = "Add Recipe"
        }


        // Add Image button
        imageContract = registerForActivityResult(ActivityResultContracts.GetContent()) {
            if (it != null) {
                _controller.selectedImg = it
                binding.image.setImageURI(it)
            }
        }
        binding.addImgBtn.setOnClickListener{
            //Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show()
            imageContract.launch("image/*")
        }


        // Submit Button
        binding.submitBtn.setOnClickListener{
            val recipe = Recipe(
                binding.title.text.toString(),
                binding.dropdownMenu.selectedItem.toString(),
                binding.ingredients.text.toString(),
                binding.steps.text.toString(),
                _controller.selectedImg.toString(),
            )
            binding.addImgBtn.isEnabled = false
            binding.submitBtn.isEnabled = false
            // Create Recipe
            if (!_controller.editMode) {
                _controller.createRecipe(recipe) {
                    binding.addImgBtn.isEnabled = true
                    binding.submitBtn.isEnabled = true
                }
            }
            // Update Recipe
            else {
                _controller.updateRecipe(recipe) {
                    binding.addImgBtn.isEnabled = true
                    binding.submitBtn.isEnabled = true
                }
            }

        }
    }
    private fun getImagePath(uri: Uri?): String? {
        if (uri == null) return null
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(uri, projection, null, null, null)
        val columnIndex = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor?.moveToFirst()
        val path = cursor?.getString(columnIndex ?: 0)
        cursor?.close()
        return path
    }
}