package com.iman.story_akhir1.com

import android.content.Context
import android.os.Environment
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

object CamUtility {

    private const val DATE_FORMAT = "yyyy-dd-MM"

    private val timeStamp: String = SimpleDateFormat(
        DATE_FORMAT,
        Locale.US
    ).format(System.currentTimeMillis())

    fun createCustomTempFile(context: Context): File {
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(timeStamp, ".jpg", storageDir)
    }

}