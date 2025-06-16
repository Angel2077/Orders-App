package com.example.db_local_menu.Info // Paquete correcto

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey

// Copia de l4DetallePedido pero con el nombre y FKs ajustadas para la DB de Info
@Entity(
    tableName = "detalle_pedido_info",
    indices = [
        androidx.room.Index(value = ["id_pedido"]),
        androidx.room.Index(value = ["id_producto"])
    ],
    foreignKeys = [
        ForeignKey(
            entity = i7PedidoInfo::class, // Referencia a la entidad de Pedido en esta misma DB
            parentColumns = ["id_pedido"],
            childColumns = ["id_pedido"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = i5ProductoInfo::class, // Referencia a la entidad de Producto en esta misma DB
            parentColumns = ["id"],
            childColumns = ["id_producto"],
            onDelete = ForeignKey.RESTRICT
        )
    ]
)
data class i8DetallePedidoInfo(
    @PrimaryKey(autoGenerate = true) val id_detalle: Int = 0,
    val id_pedido: Int,
    val id_producto: Int,
    val cantidad: Int,
    val precio_total: Int
)
