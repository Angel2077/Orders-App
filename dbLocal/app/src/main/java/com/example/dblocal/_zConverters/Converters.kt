package com.example.dblocal._zConverters


import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LocalDateTimeConverter {
    // Formateador para convertir LocalDateTime a String y viceversa
    // ISO_LOCAL_DATE_TIME es un formato estándar como "2023-10-27T10:30:00"
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    @TypeConverter
    fun fromTimestamp(value: String?): LocalDateTime? {
        // Convierte un String de la DB a un objeto LocalDateTime
        return value?.let { LocalDateTime.parse(it, formatter) }
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?): String? {
        // Convierte un objeto LocalDateTime a un String para guardar en la DB
        return date?.format(formatter)
    }
}