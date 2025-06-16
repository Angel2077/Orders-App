package com.example.db_local_menu.Info // Paquete correcto

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope

// Ahora importamos las entidades y DAOs de nuestra propia DB de Info
import com.example.db_local_menu.Info.i5ProductoInfo
import com.example.db_local_menu.Info.i6ProductoInfoDao
import com.example.db_local_menu.Info.i7PedidoInfo
import com.example.db_local_menu.Info.i8DetallePedidoInfo
import com.example.db_local_menu.Info.i9PedidoInfoDao
import com.example.db_local_menu.Info.i10DetallePedidoInfoDao


import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.YearMonth
import java.time.ZoneId
import java.time.temporal.ChronoUnit

class i2InfoViewModel(
    private val pedidoInfoDao: i9PedidoInfoDao,          // DAO de Pedido para Info
    private val detallePedidoInfoDao: i10DetallePedidoInfoDao, // DAO de DetallePedido para Info
    private val productoInfoDao: i6ProductoInfoDao       // DAO de Producto para Info
) : ViewModel() {

    private val _productosVendidosMes = MutableLiveData<List<i1ProductoVendidos>>()
    val productosVendidosMes: LiveData<List<i1ProductoVendidos>> = _productosVendidosMes

    private val _top3ProductosVendidos = MutableLiveData<List<i1ProductoVendidos>>()
    val top3ProductosVendidos: LiveData<List<i1ProductoVendidos>> = _top3ProductosVendidos

    private val _selectedMonth = MutableLiveData(YearMonth.now())
    val selectedMonth: LiveData<YearMonth> = _selectedMonth

    init {
        combine(
            detallePedidoInfoDao.obtenerTodosLosDetallesInfo(), // Obtener detalles de la DB de Info
            pedidoInfoDao.obtenerTodosLosPedidosInfo(),         // Obtener pedidos de la DB de Info
            selectedMonth.asFlow()
        ) { detalles, pedidos, month ->
            processSalesData(detalles, pedidos, month)
        }.launchIn(viewModelScope)
    }

    private suspend fun processSalesData(
        detalles: List<i8DetallePedidoInfo>, // Entidad de DetallePedido de Info
        pedidos: List<i7PedidoInfo>,         // Entidad de Pedido de Info
        month: YearMonth
    ) {
        val filteredDetalles = detalles.filter { detalle ->
            val pedido = pedidos.find { it.id_pedido == detalle.id_pedido }
            pedido?.fecha?.atZone(ZoneId.systemDefault())?.let {
                YearMonth.from(it) == month
            } ?: false
        }

        val salesMap = mutableMapOf<Int, Int>()
        filteredDetalles.forEach { detalle ->
            salesMap[detalle.id_producto] = (salesMap[detalle.id_producto] ?: 0) + detalle.cantidad
        }

        val aggregatedSales = salesMap.map { (productId, totalQuantity) ->
            val productName = getProductoInfoNameById(productId) ?: "Producto Desconocido"
            i1ProductoVendidos(productId, productName, totalQuantity)
        }

        _productosVendidosMes.postValue(aggregatedSales)
        _top3ProductosVendidos.postValue(
            aggregatedSales.sortedByDescending { it.cantidadTotalVendida }.take(3)
        )
    }

    private suspend fun getProductoInfoNameById(productId: Int): String? {
        // Ahora usamos el DAO de Producto de Info DB
        return productoInfoDao.getProductoInfoById(productId)?.nombre
    }

    fun setMonthFilter(yearMonth: YearMonth) { _selectedMonth.value = yearMonth }
    fun previousMonth() { _selectedMonth.value = _selectedMonth.value?.minus(1, ChronoUnit.MONTHS) }
    fun nextMonth() { _selectedMonth.value = _selectedMonth.value?.plus(1, ChronoUnit.MONTHS) }

    // --- Métodos para insertar datos de prueba en la DB de Info (temporal) ---
    fun insertarProductoInfoPrueba(id: Int, nombre: String) {
        viewModelScope.launch {
            productoInfoDao.insertarProductoInfo(i5ProductoInfo(id = id, nombre = nombre))
        }
    }

    fun insertarPedidoDetalleInfoPrueba(pedidoId: Int, productoId: Int, cantidad: Int) {
        viewModelScope.launch {
            // Inserta un pedido simple si no existe
            val pedido = i7PedidoInfo(id_pedido = pedidoId, nombre_cliente = "Cliente Info $pedidoId", estado = "COMPLETADO", total = 0, fecha = Instant.now())
            pedidoInfoDao.insertarPedidoInfo(pedido) // Esto puede dar error si PK ya existe, considera OnConflictStrategy.IGNORE

            val detalle = i8DetallePedidoInfo(id_pedido = pedidoId, id_producto = productoId, cantidad = cantidad, precio_total = cantidad * 100)
            detallePedidoInfoDao.insertarDetallePedidoInfo(detalle)
        }
    }
}