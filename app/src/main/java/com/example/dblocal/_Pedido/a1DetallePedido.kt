// dblocal/_Pedido/DetallePedido.kt
package com.example.dblocal._Pedido

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.dblocal._Producto.Producto // Importar la entidad Producto

@Entity(
    tableName = "detalle_pedido",
    foreignKeys = [
        ForeignKey(
            entity = Pedido::class,
            parentColumns = ["idPedido"],
            childColumns = ["idPedido"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Producto::class,
            parentColumns = ["id"], // id de Producto
            childColumns = ["idProducto"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["idPedido"]),
        Index(value = ["idProducto"])
    ]
)
data class DetallePedido(
    @PrimaryKey(autoGenerate = true) val idDetalle: Int = 0, // id_detalle auto_increment
    val idPedido: Int? = null,
    val idProducto: Int,
    val cantidad: Int,
    val precioTotal: Int, // Precio total de este detalle (cantidad * precio_unitario_producto)
    val componentesElegidosIds: String = "" // String con IDs de componentes elegidos (ej. "1,5,7")
)