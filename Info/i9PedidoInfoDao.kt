package com.example.db_local_menu.Info // Paquete correcto

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface i9PedidoInfoDao {
    @Insert
    suspend fun insertarPedidoInfo(pedido: i7PedidoInfo): Long

    @Update
    suspend fun actualizarPedidoInfo(pedido: i7PedidoInfo)

    @Delete
    suspend fun eliminarPedidoInfo(pedido: i7PedidoInfo)

    @Query("SELECT * FROM pedido_info ORDER BY fecha DESC")
    fun obtenerTodosLosPedidosInfo(): Flow<List<i7PedidoInfo>>

    // Necesitamos una clase para la relación PedidoInfo con DetallePedidoInfo
    // Pero para la gráfica, solo necesitamos los detalles y el pedido en general
}