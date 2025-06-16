package com.example.db_local_menu.Info // Paquete correcto

import androidx.room.Entity
import androidx.room.PrimaryKey

// Entidad 'Producto' específica para la base de datos de Info.
// Contiene la información mínima necesaria para la gráfica (id y nombre).
@Entity(tableName = "producto_info") // Nombre de tabla diferente para evitar conflictos
data class i5ProductoInfo(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String
)
