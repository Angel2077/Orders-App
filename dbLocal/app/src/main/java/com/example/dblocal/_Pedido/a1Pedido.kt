// dblocal/_Pedido/Pedido.kt
package com.example.dblocal._Pedido

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime // Para manejar la fecha y hora

@Entity(tableName = "pedido")
data class Pedido(
    @PrimaryKey(autoGenerate = true) val idPedido: Int = 0,
    val fecha: LocalDateTime = LocalDateTime.now(), // Se registrará automáticamente al crear
    val estado: String = "pendiente", // Podría ser un enum en el futuro, por ahora String
    val nombreCliente: String,
    val total: Int // El total del pedido
)
