package com.example.dblocal._Pedido

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update // Necesitarás este para el botón de "Finalizar"
import androidx.room.Delete // Necesitarás este si quieres implementar borrar pedidos
import kotlinx.coroutines.flow.Flow

// Para Paging3
import androidx.paging.PagingSource // Importa PagingSource
import com.example.dblocal._ListadoPedidos.PedidoConDetalles // <-- ¡Importación desde el nuevo paquete!

@Dao
interface PedidoDao {
    // --- MÉTODOS EXISTENTES (NO SE MODIFICAN SUS NOMBRES) ---
    @Insert
    suspend fun insertarPedido(pedido: Pedido): Long

    @Insert
    suspend fun insertarDetallePedido(detalle: DetallePedido): Long

    @Insert
    suspend fun insertarDetallesPedido(detalles: List<DetallePedido>)

    @Query("SELECT * FROM pedido WHERE idPedido = :idPedido")
    fun obtenerPedidoPorId(idPedido: Int): Flow<Pedido>

    @Query("SELECT * FROM detalle_pedido WHERE idPedido = :idPedido")
    fun obtenerDetallesDePedido(idPedido: Int): Flow<List<DetallePedido>>

    @Query("SELECT * FROM pedido ORDER BY fecha DESC")
    fun obtenerTodosLosPedidos(): Flow<List<Pedido>>


    // --- NUEVOS MÉTODOS PARA _ListadoPedidos (AÑADIDOS) ---

    @Update // Añadir si no lo tenías para poder cambiar el estado del pedido
    suspend fun updatePedido(pedido: Pedido)

    @Delete // Opcional: si quieres poder borrar pedidos
    suspend fun deletePedido(pedido: Pedido)

    // Consulta para Paging3: Pedidos en proceso/pendientes (no entregados), ordenados por fecha
    // Usamos @Transaction porque PedidoConDetalles implica múltiples tablas (Pedido, DetallePedido, Producto)
    @Transaction
    @Query("SELECT * FROM Pedido WHERE estado != 'entregado' ORDER BY fecha ASC")
    fun getPendientePedidosPagingSource(): PagingSource<Int, PedidoConDetalles>

    // Consulta para Paging3: Pedidos entregados, ordenados por fecha (si se necesita una paginación separada)
    @Transaction
    @Query("SELECT * FROM Pedido WHERE estado = 'entregado' ORDER BY fecha ASC")
    fun getEntregadoPedidosPagingSource(): PagingSource<Int, PedidoConDetalles>

    // Consulta para obtener UN solo PedidoConDetalles por ID (para la pantalla de detalle)
    @Transaction
    @Query("SELECT * FROM Pedido WHERE idPedido = :idPedido")
    fun getSinglePedidoConDetalles(idPedido: Int): Flow<PedidoConDetalles?>
}