package com.example.dblocal._Almacen

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Componente")
data class a1Componente (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val porcion: Double,
    val unidad: String,
    val cantidad: Int
)