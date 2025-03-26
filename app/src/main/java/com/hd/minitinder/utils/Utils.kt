package com.hd.minitinder.utils

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
}