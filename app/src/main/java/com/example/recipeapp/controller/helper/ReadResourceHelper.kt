package com.example.recipeapp.controller.helper

import android.app.Activity
import com.example.recipeapp.R

interface ReadResourceHelper {
    fun extractResource(resourceId: Int, activity: Activity) : Array<String>
}