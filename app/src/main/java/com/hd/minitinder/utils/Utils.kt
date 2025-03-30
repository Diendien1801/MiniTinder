package com.hd.minitinder.utils
import android.net.Uri
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Utils {
    fun toTimeString(timestamp: Long): String {
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val date = Date(timestamp)
        return dateFormat.format(date)
    }

    fun toDateString(timestamp: Long): String {
        val sdf = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
        return sdf.format(java.util.Date(timestamp))
    }

    fun formatDate(day: String, month: String, year: String): String {
        // ghép chuỗi ngày, tháng, năm thành chuỗi "dd/MM/yyyy"
        return "$day/$month/$year"
    }
    private fun uriToFile(uri: Uri, context: Context): File {
        val inputStream = context.contentResolver.openInputStream(uri)
        val file = File(context.cacheDir, "temp_image.jpg")
        file.outputStream().use { outputStream ->
            inputStream?.copyTo(outputStream)
        }
        return file
    }
    fun DobToAge(dob: String): String
    {
        val sdf = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
        val date = sdf.parse(dob)
        val currentDate = Date()

        val diff = currentDate.time - date.time
        val age = diff / (1000L * 60 * 60 * 24 * 365)
        return age.toString()

    }
}