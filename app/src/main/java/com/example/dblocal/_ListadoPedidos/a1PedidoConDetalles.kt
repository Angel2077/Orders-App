package com.example.dblocal._ListadoPedidos

import androidx.room.Embedded
import androidx.room.Relation
import com.example.dblocal._Pedido.DetallePedido
import com.example.dblocal._Pedido.Pedido
import com.example.dblocal._Producto.Producto // Asegúrate de que Producto sea una @Entity

// Esta clase representa un Pedido completo con sus detalles directos (DetallePedido).
// Room puede mapear esto directamente porque DetallePedido es una @Entity.
data class PedidoConDetalles(
    @Embedded val pedido: Pedido, // El objeto Pedido principal (debe ser una @Entity)

    @Relation(
        parentColumn = "idPedido",    // Columna en la entidad Pedido (la "padre")
        entityColumn = "idPedido"     // Columna en la entidad DetallePedido (la "hija")
    )
    val detalles: List<DetallePedido> // Lista de DetallePedido, que debe ser una @Entity.
    // NO incluimos Producto o Componentes aquí directamente vía @Relation anidada.
)

// Esta clase data class combina un DetallePedido con su Producto asociado.
// Puedes usarla en consultas de DAO si necesitas obtener un DetallePedido directamente con su Producto.
// Es una estructura que Room sí puede entender para una relación directa.
data class DetallePedidoConProducto(
    @Embedded val detallePedido: DetallePedido, // El objeto DetallePedido principal (debe ser una @Entity)

    @Relation(
        parentColumn = "idProducto", // Columna en DetallePedido (la "padre" de esta relación)
        entityColumn = "id"          // Columna en Producto (la "hija" de esta relación)
    )
    val producto: Producto // El Producto asociado a este detalle (debe ser una @Entity)
)

