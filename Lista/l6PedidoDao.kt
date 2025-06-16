package com.example.db_local_menu.Lista

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface l6PedidoDao {
    @Insert
    suspend fun insertarPedido(pedido: l3Pedido): Long // Retorna el id del pedido insertado

    @Update
    suspend fun actualizarPedido(pedido: l3Pedido)

    @Delete
    suspend fun eliminarPedido(pedido: l3Pedido)

    // Obtener todos los pedidos (solo la entidad Pedido)
    @Query("SELECT * FROM pedido ORDER BY fecha DESC")
    fun obtenerTodosLosPedidos(): Flow<List<l3Pedido>>

    // Obtener un Pedido con sus Detalles (la relación)
    @Transaction
    @Query("SELECT * FROM pedido WHERE id_pedido = :pedidoId")
    fun obtenerPedidoConDetalles(pedidoId: Int): Flow<l5PedidoConDetalles>

    // Obtener todos los pedidos con sus detalles
    @Transaction
    @Query("SELECT * FROM pedido ORDER BY fecha DESC")
    fun obtenerTodosLosPedidosConDetalles(): Flow<List<l5PedidoConDetalles>>
}