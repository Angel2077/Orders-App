package com.example.db_local_menu.Lista

import kotlinx.coroutines.flow.Flow

// El Repositorio recibe ambos DAOs para manejar las operaciones de Pedido y DetallePedido
class l8PedidoRepositorio(
    private val pedidoDao: l6PedidoDao,
    private val detallePedidoDao: l7DetallePedidoDao
) {
    // --- Operaciones para Pedido ---
    suspend fun insertarPedido(pedido: l3Pedido): Long {
        return pedidoDao.insertarPedido(pedido)
    }

    suspend fun actualizarPedido(pedido: l3Pedido) {
        pedidoDao.actualizarPedido(pedido)
    }

    suspend fun eliminarPedido(pedido: l3Pedido) {
        pedidoDao.eliminarPedido(pedido)
    }

    fun obtenerTodosLosPedidosConDetalles(): Flow<List<l5PedidoConDetalles>> {
        return pedidoDao.obtenerTodosLosPedidosConDetalles()
    }

    fun obtenerPedidoConDetalles(pedidoId: Int): Flow<l5PedidoConDetalles> {
        return pedidoDao.obtenerPedidoConDetalles(pedidoId)
    }

    // --- Operaciones para DetallePedido ---
    suspend fun insertarDetallePedido(detalle: l4DetallePedido) {
        detallePedidoDao.insertarDetallePedido(detalle)
    }

    suspend fun actualizarDetallePedido(detalle: l4DetallePedido) {
        detallePedidoDao.actualizarDetallePedido(detalle)
    }

    suspend fun eliminarDetallePedido(detalle: l4DetallePedido) {
        detallePedidoDao.eliminarDetallePedido(detalle)
    }

    fun obtenerDetallesPorPedido(pedidoId: Int): Flow<List<l4DetallePedido>> {
        return detallePedidoDao.obtenerDetallesPorPedido(pedidoId)
    }
}