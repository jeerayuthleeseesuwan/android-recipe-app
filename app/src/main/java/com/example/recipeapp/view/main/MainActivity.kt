package com.example.recipeapp.view.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipeapp.controller.MainController
import com.example.recipeapp.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var _controller : MainController

    override fun onStart() {
        super.onStart()

        if(FirebaseAuth.getInstance().currentUser == null) {
            _controller.goToLoginPage()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        _controller = MainController(this)

        _controller.extractRecipeTypes()

        // setup the drop down menu
        var dropdownAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, _controller.recipeTypes)
        dropdownAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.dropdownMenu.adapter = dropdownAdapter
        binding.dropdownMenu.onItemSelectedListener = (object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                _controller.filterRecipes(p2)
            }
            override fun onNothingSelected(p0: AdapterView<*>?) { }
        })

        // List View
        binding.recipeListView.setHasFixedSize(true)
        binding.recipeListView.layoutManager = LinearLayoutManager(this)
        _controller.getRecipes(){
            _controller.adapter = it
            _controller.filterRecipes(0) // default to filter the first value
            binding.recipeListView.adapter = _controller.adapter
        }

        // Add Button
        binding.addBtn.setOnClickListener{
            // go to Add Recipe Page
            var intent = Intent(this, AddUpdateRecipeActivity::class.java)
            startActivity(intent)
        }

        // log out button
        binding.logoutBtn.setOnClickListener{
            _controller.logout()
        }
    }
}