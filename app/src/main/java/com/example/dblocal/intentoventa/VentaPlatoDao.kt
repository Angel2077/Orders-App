package com.example.dblocal.intentoventa
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface VentaPlatoDao {
    @Insert
    suspend fun insertarPedido(pedido: Pedidos)

    @Insert
    suspend fun insertarPlato(plato: Plato)

    @Query("SELECT nombrePlato,SUM(cantidadVendida) as totalVentas FROM pedidos GROUP BY nombrePlato")
    //esto servirá para hacer un resumen de lo vendido y dividirá por grupos
    fun obtenerVentas(): Flow<List<VentaResumen>>

    @Query("SELECT * FROM plato")
    suspend fun obtenerplato(): List<Plato>
}