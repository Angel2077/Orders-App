package com.example.db_local_menu.Lista

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "detalle_pedido",
    // 1. Añadimos 'indices' para optimizar el rendimiento y satisfacer a KSP
    indices = [
        Index(value = ["id_pedido"]),
        Index(value = ["id_producto"])
    ],
    foreignKeys = [
        ForeignKey(
            entity = l3Pedido::class,
            parentColumns = ["id_pedido"],
            childColumns = ["id_pedido"],
            onDelete = ForeignKey.Companion.CASCADE // Si se elimina un pedido, se eliminan sus detalles
        )
        // 2. Eliminamos la clave foránea a Producto, ya que Producto no está en esta DB (l0Database).
        // La relación se manejará a nivel de la lógica de la aplicación.
        /*
        ForeignKey(
            entity = Producto::class, // ¡ADAPTA ESTO SI TU ENTIDAD PRODUCTO ES DIFERENTE!
            parentColumns = ["id"],
            childColumns = ["id_producto"],
            onDelete = ForeignKey.RESTRICT
        )
        */
    ]
)
data class l4DetallePedido(
    @PrimaryKey(autoGenerate = true) val id_detalle: Int = 0,
    val id_pedido: Int,
    val id_producto: Int, // Este ID ahora es una referencia lógica, no una FK impuesta por Room en esta DB.
    val cantidad: Int,
    val precio_total: Int
)