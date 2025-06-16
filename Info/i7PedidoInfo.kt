package com.example.db_local_menu.Info // Paquete correcto

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

// Copia de l3Pedido pero con el nombre ajustado para la DB de Info
@Entity(tableName = "pedido_info")
data class i7PedidoInfo(
    @PrimaryKey(autoGenerate = true) val id_pedido: Int = 0,
    val fecha: Instant = Instant.now(),
    val estado: String, // Usamos String para el estado ya que PedidoEstado es de Lista
    val nombre_cliente: String,
    val total: Int
)
