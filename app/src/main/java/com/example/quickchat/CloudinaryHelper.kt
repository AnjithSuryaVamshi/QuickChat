package com.example.quickchat

import com.cloudinary.Cloudinary
import android.content.Context
import android.net.Uri
import com.cloudinary.utils.ObjectUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.*

object CloudinaryHelper {

    private val cloudinary = Cloudinary(
        ObjectUtils.asMap(
            "cloud_name", "da1ubi0r7",
            "api_key", "131225859314861",
            "api_secret", "vvNw_WVsyInk4hMXECDwUGoc3Mo"
        )
    )

    suspend fun uploadImage(context: Context, uri: Uri): String? = withContext(Dispatchers.IO) {
        try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val file = File(context.cacheDir, UUID.randomUUID().toString() + ".jpg")
            inputStream?.use { input ->
                file.outputStream().use { output -> input.copyTo(output) }
            }

            val result = cloudinary.uploader().upload(file, ObjectUtils.emptyMap())
            return@withContext result["secure_url"] as? String
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext null
        }
    }
}
