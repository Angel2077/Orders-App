package com.example.dblocal.intentoventa
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "plato")
data class Plato (
    @PrimaryKey(autoGenerate = true) val idPlato: Int = 0,
    val nombre: String,
    val precio: Int,
    val categoria: String
)