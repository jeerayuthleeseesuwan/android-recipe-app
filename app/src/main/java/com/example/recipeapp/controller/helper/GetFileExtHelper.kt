package com.example.recipeapp.controller.helper

import android.content.ContentResolver
import android.net.Uri
import android.webkit.MimeTypeMap

class GetFileExtHelper {
    fun getFileExtension(fileUri: Uri?, contentResolver: ContentResolver) : String? {
        if (fileUri != null) {
            var mime = MimeTypeMap.getSingleton()
            return mime.getExtensionFromMimeType(contentResolver.getType(fileUri))
        }

        return null
    }
}