package com.example.db_local_menu.Lista

import androidx.room.Embedded
import androidx.room.Relation

data class l5PedidoConDetalles(
    @Embedded val pedido: l3Pedido,
    @Relation(
        parentColumn = "id_pedido",
        entityColumn = "id_pedido"
    )
    val detalles: List<l4DetallePedido>
)