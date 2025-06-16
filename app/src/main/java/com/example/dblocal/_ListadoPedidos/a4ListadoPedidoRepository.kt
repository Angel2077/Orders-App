package com.example.dblocal._ListadoPedidos


import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import androidx.room.Embedded
import com.example.dblocal._Almacen.a1Componente
import com.example.dblocal._Almacen.a2ComponenteDao
import com.example.dblocal._Pedido.DetallePedido
import com.example.dblocal._Pedido.Pedido
import com.example.dblocal._Pedido.PedidoDao
import com.example.dblocal._Producto.Producto
import com.example.dblocal._Producto.a2ProduCompDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext


class ListadoPedidoRepository(
    private val pedidoDao: PedidoDao,
    private val componenteDao: a2ComponenteDao,
    private val productoDao: a2ProduCompDao
) {
    fun getPendientePedidosPaging(): Flow<PagingData<PedidoConDetallesProcesados>> {
        return Pager(
            config = PagingConfig(
                pageSize = 15,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { pedidoDao.getPendientePedidosPagingSource() }
        ).flow.map { pagingData ->
            pagingData.map { pedidoConDetalles ->
                procesarPedidoConDetalles(pedidoConDetalles)
            }
        }
    }

    fun getPedidoCompletoById(idPedido: Int): Flow<PedidoConDetallesProcesados?> {
        return pedidoDao.getSinglePedidoConDetalles(idPedido).map { pedidoConDetalles ->
            pedidoConDetalles?.let { procesarPedidoConDetalles(it) }
        }
    }

    private suspend fun procesarPedidoConDetalles(pedidoConDetalles: PedidoConDetalles): PedidoConDetallesProcesados {
        return withContext(Dispatchers.IO) {
            val detallesProcesados = pedidoConDetalles.detalles.map { detalleActual -> // Renombré 'detalle' a 'detalleActual' para mayor claridad
                // 1. Obtener el Producto asociado a este DetallePedido
                // Asumo que DetallePedido tiene una propiedad 'idProducto'
                // y que 'productoDao' tiene un metodo 'getProductoByIdSync'
                val productoAsociado = productoDao.getProductoByIdSync(detalleActual.idProducto)
                    ?: Producto(
                        nombre = "Producto Desconocido",
                        precio = 0, // Int
                        categoria = "Sin Categoría", // String (o null si prefieres)
                        tamano = "Sin Tamaño",     // String (o null si prefieres)
                        maximoIngredientes = 0     // Int
                    ) // Manejar caso donde el producto no se encuentra (o lanza error si prefieres)


                // 2. Obtener los IDs de componentes directamente de detalleActual
                val componentesIds = detalleActual.componentesElegidosIds // ¡Acceso directo a la propiedad de DetallePedido!
                    .split(",")
                    .filter { it.isNotBlank() }
                    .mapNotNull { it.toIntOrNull() }

                val componentesElegidos = componentesIds.mapNotNull { id ->
                    componenteDao.getComponenteByIdSync(id)
                }

                DetallePedidoConProductoYComponentesProcesados(
                    detallePedido = detalleActual, // ¡Aquí pasas el objeto DetallePedido directamente!
                    producto = productoAsociado, // ¡Aquí pasas el Producto que acabamos de obtener!
                    componentesElegidos = componentesElegidos
                )
            }
            PedidoConDetallesProcesados(
                pedido = pedidoConDetalles.pedido,
                detalles = detallesProcesados
            )
        }
    }

    suspend fun updatePedido(pedido: Pedido) {
        pedidoDao.updatePedido(pedido)
    }

    data class PedidoConDetallesProcesados(
        @Embedded val pedido: Pedido,
        val detalles: List<DetallePedidoConProductoYComponentesProcesados>
    )

    data class DetallePedidoConProductoYComponentesProcesados(
        @Embedded val detallePedido: DetallePedido,
        val producto: Producto,
        val componentesElegidos: List<a1Componente>
    )
}