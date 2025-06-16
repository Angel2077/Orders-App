package com.example.db_local_menu.Info // Paquete correcto

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface i10DetallePedidoInfoDao {
    @Insert
    suspend fun insertarDetallePedidoInfo(detalle: i8DetallePedidoInfo)

    @Update
    suspend fun actualizarDetallePedidoInfo(detalle: i8DetallePedidoInfo)

    @Query("SELECT * FROM detalle_pedido_info")
    fun obtenerTodosLosDetallesInfo(): Flow<List<i8DetallePedidoInfo>>
}
