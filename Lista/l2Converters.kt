package com.example.db_local_menu.Lista

import androidx.room.TypeConverter
import java.time.Instant

class l2Converters {
    // Convertir de Instant a Long (timestamp) para guardar en la DB
    @TypeConverter
    fun fromTimestamp(value: Long?): Instant? {
        return value?.let { Instant.ofEpochMilli(it) }
    }

    // Convertir de Long (timestamp) a Instant para leer de la DB
    @TypeConverter
    fun dateToTimestamp(date: Instant?): Long? {
        return date?.toEpochMilli()
    }

    // Convertir de String a PedidoEstado para leer de la DB
    @TypeConverter
    fun fromPedidoEstado(value: String?): PedidoEstado? {
        return try {
            value?.let { PedidoEstado.valueOf(it) }
        } catch (e: IllegalArgumentException) {
            // Manejar caso donde el String no coincide con un enum válido (ej. dato corrupto)
            null
        }
    }

    // Convertir de PedidoEstado a String para guardar en la DB
    @TypeConverter
    fun pedidoEstadoToString(estado: PedidoEstado?): String? {
        return estado?.name
    }
}