package com.example.db_local_menu.Lista

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(tableName = "pedido")
data class l3Pedido(
    @PrimaryKey(autoGenerate = true) val id_pedido: Int = 0,
    val fecha: Instant = Instant.now(), // Por defecto, la fecha actual
    val estado: PedidoEstado,
    val nombre_cliente: String,
    val total: Int
)