package com.example.recipeapp.controller.helper

import android.app.Activity

class ReadResourceHelperImpl : ReadResourceHelper {
    override fun extractResource(resourceId: Int, activity: Activity) : Array<String>{
        return activity.resources.getStringArray(resourceId)
    }
}