package com.example.db_local_menu.Info // Paquete correcto

import androidx.room.TypeConverter
import java.time.Instant // Para los TypeConverters

// ¡Clase de Type Converters extraída a su propio archivo!
class InfoTypeConverters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Instant? {
        return value?.let { Instant.ofEpochMilli(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Instant?): Long? {
        return date?.toEpochMilli()
    }
    // No necesitamos convertir PedidoEstado a String aquí porque la entidad i7PedidoInfo ya usa String
    // Si tuvieras un enum en i7PedidoInfo, necesitarías añadir la lógica aquí.
}
