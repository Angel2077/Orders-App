package com.example.dblocal._Pedido


import android.util.Log // Para depuración, puedes quitarlo en producción
import kotlinx.coroutines.flow.Flow

class PedidoRepository(private val pedidoDao: PedidoDao) {

    // Función para guardar un pedido completo con sus detalles
    suspend fun guardarPedidoCompleto(
        pedido: Pedido,
        detalles: List<DetallePedido>
    ): Long {
        return try {
            // 1. Insertar el pedido (encabezado)
            val idPedido = pedidoDao.insertarPedido(pedido)
            Log.d("PedidoRepo", "Pedido insertado con ID: $idPedido")

            // 2. Asignar el ID del pedido a cada detalle y luego insertarlos
            val detallesConIdPedido = detalles.map {
                it.copy(idPedido = idPedido.toInt()) // Asigna el ID generado
            }
            pedidoDao.insertarDetallesPedido(detallesConIdPedido)
            Log.d("PedidoRepo", "${detallesConIdPedido.size} detalles insertados para Pedido ID: $idPedido")
            idPedido // Retorna el ID del pedido insertado
        } catch (e: Exception) {
            Log.e("PedidoRepo", "Error al guardar pedido completo: ${e.message}", e)
            -1L // Indica un error
        }
    }

    // Función para obtener todos los pedidos (útil para una pantalla de historial)
    fun obtenerTodosLosPedidos(): Flow<List<Pedido>> {
        return pedidoDao.obtenerTodosLosPedidos()
    }


}