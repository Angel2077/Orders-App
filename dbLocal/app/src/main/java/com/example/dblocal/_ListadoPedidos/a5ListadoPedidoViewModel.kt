package com.example.dblocal._ListadoPedidos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.dblocal._Almacen.a2ComponenteDao
import com.example.dblocal._Pedido.Pedido
import com.example.dblocal._Pedido.PedidoDao
import com.example.dblocal._Producto.a2ProduCompDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit


class ListadoPedidoViewModel(
    val listadoPedidoRepository: ListadoPedidoRepository
) : ViewModel() {

    val pendientePedidos: Flow<PagingData<ListadoPedidoRepository.PedidoConDetallesProcesados>> =
        listadoPedidoRepository.getPendientePedidosPaging().cachedIn(viewModelScope)

    private val _showConfirmDialog = MutableStateFlow(false)
    val showConfirmDialog: StateFlow<Boolean> = _showConfirmDialog

    private var _pedidoToProcess: Pedido? = null

    fun onFinishPedidoClick(pedido: Pedido) {
        _pedidoToProcess = pedido
        _showConfirmDialog.value = true
    }

    fun onConfirmProcessPedido() {
        _pedidoToProcess?.let { pedido ->
            viewModelScope.launch {
                val newState = if (pedido.estado == "entregado") "en proceso" else "entregado"
                listadoPedidoRepository.updatePedido(pedido.copy(estado = newState))
            }
        }
        _showConfirmDialog.value = false
        _pedidoToProcess = null
    }

    fun onDismissConfirmDialog() {
        _showConfirmDialog.value = false
        _pedidoToProcess = null
    }

    fun calculateTimeLeft(fechaPedido: LocalDateTime): String {
        val now = LocalDateTime.now()
        val minutes = ChronoUnit.MINUTES.between(fechaPedido, now)
        val hours = minutes / 60
        val remainingMinutes = minutes % 60
        return String.format("%02d:%02d", hours, remainingMinutes)
    }

    fun getBackgroundColorForPedido(fechaPedido: LocalDateTime): Int {
        val now = LocalDateTime.now()
        val minutesElapsed = ChronoUnit.MINUTES.between(fechaPedido, now)

        return when {
            minutesElapsed < 10 -> 0xFF8BC34A.toInt() // Verde
            minutesElapsed < 20 -> 0xFFFFEB3B.toInt() // Amarillo
            else -> 0xFFF44336.toInt() // Rojo
        }
    }
}

class ListadoPedidoViewModelFactory(
    private val pedidoDao: PedidoDao,
    private val componenteDao: a2ComponenteDao,
    private val productoDao: a2ProduCompDao
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListadoPedidoViewModel::class.java)) {
            val listadoPedidoRepository = ListadoPedidoRepository(pedidoDao, componenteDao,productoDao)
            return ListadoPedidoViewModel(listadoPedidoRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}