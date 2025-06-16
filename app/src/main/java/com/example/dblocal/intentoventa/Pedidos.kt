package com.example.dblocal.intentoventa
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Pedidos")
data class Pedidos (
    @PrimaryKey(autoGenerate = true) val idPedido: Int = 0,//Esto permite generar una id
    // auto incremental partiendo desde el 0
    val nombreCliente: String,
    val nombrePlato: String,
    val precioPlato: Int,
    val cantidadVendida: Int,
    val precioTotal: Int,
    val fechaVenta: Long//Esto permite guardar la fecha de venta
)

data class VentaResumen(
    val nombrePlato: String,
    val totalVentas: Int
)
