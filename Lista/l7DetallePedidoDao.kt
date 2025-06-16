package com.example.db_local_menu.Lista

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface l7DetallePedidoDao {
    @Insert
    suspend fun insertarDetallePedido(detalle: l4DetallePedido)

    @Update
    suspend fun actualizarDetallePedido(detalle: l4DetallePedido)

    @Delete
    suspend fun eliminarDetallePedido(detalle: l4DetallePedido)

    // Obtener todos los detalles para un pedido específico
    @Query("SELECT * FROM detalle_pedido WHERE id_pedido = :pedidoId")
    fun obtenerDetallesPorPedido(pedidoId: Int): Flow<List<l4DetallePedido>>
}