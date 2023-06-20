package com.bejussi.dailyadvice.presentation.advice

import android.content.ContentResolver
import android.content.ContentValues
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import com.bejussi.dailyadvice.R
import com.bejussi.dailyadvice.core.util.StringResourcesProvider
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import javax.inject.Singleton

class SaveToStorageManager @Inject constructor(
    val contentResolver: ContentResolver,
    val stringResourcesProvider: StringResourcesProvider
) {
    fun saveBitmap(bitmap: Bitmap): Uri? {
        val timestamp = System.currentTimeMillis()
        var uri: Uri? = null
        var path: String? = null

        val values = ContentValues().apply {
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            put(MediaStore.Images.Media.DATE_ADDED, timestamp)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.put(MediaStore.Images.Media.DATE_TAKEN, timestamp)
            values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/${stringResourcesProvider.getString(R.string.app_name)}")
            values.put(MediaStore.Images.Media.IS_PENDING, true)
            uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

            uri?.let {
                try {
                    contentResolver.openOutputStream(it)?.use { outputStream ->
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                    }
                    values.put(MediaStore.Images.Media.IS_PENDING, false)
                    contentResolver.update(it, values, null, null)
                } catch (e: Exception) {
                    return null
                }
            }
        } else {
            val imageFileFolder = File(
                Environment.getExternalStorageDirectory()
                    .toString() + '/' + stringResourcesProvider.getString(R.string.app_name)
            )
            if (!imageFileFolder.exists()) {
                imageFileFolder.mkdirs()
            }
            val mImageName = "$timestamp.png"
            val imageFile = File(imageFileFolder, mImageName)

            try {
                FileOutputStream(imageFile).use { outputStream ->
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                }
                path = imageFile.absolutePath
                values.put(MediaStore.Images.Media.DATA, imageFile.absolutePath)
                contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            } catch (e: Exception) {
                return null
            }
        }

        if (uri != null) {
            return uri
        } else {
            val image = Uri.fromFile(File(path!!))
            return image
        }
    }
}