package com.example.db_local_menu.Lista

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.time.Instant
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.async // ¡Asegúrate de que esta importación esté presente!

// El ViewModel recibe el Repositorio de Pedido
class l9PedidoViewModel(private val repository: l8PedidoRepositorio) : ViewModel() {

    // Expone la lista de pedidos con sus detalles para ser observada por la UI
    val allPedidosWithDetails = repository.obtenerTodosLosPedidosConDetalles().asLiveData()

    // Variable de estado para el pedido seleccionado para ver detalles
    private val _selectedPedidoDetails = MutableLiveData<l5PedidoConDetalles?>()
    val selectedPedidoDetails: LiveData<l5PedidoConDetalles?> = _selectedPedidoDetails

    // Función para insertar un nuevo pedido
    fun insertarPedido(nombreCliente: String, estado: PedidoEstado, total: Int) {
        viewModelScope.launch {
            val nuevoPedido = l3Pedido(nombre_cliente = nombreCliente, estado = estado, total = total, fecha = Instant.now())
            repository.insertarPedido(nuevoPedido)
        }
    }

    // Función para insertar un nuevo pedido y sus detalles
    // Retorna el ID del pedido insertado, útil para agregar detalles
    suspend fun insertarPedidoConDetalles(
        nombreCliente: String,
        estado: PedidoEstado,
        detalles: List<Pair<Int, Int>> // Lista de pares (id_producto, cantidad)
    ): Long { // El tipo de retorno es Long
        // CAMBIO CRUCIAL: Asegurarse de que 'async' sea llamado y su resultado 'await'eado
        // directamente para el retorno de la función suspendida.
        return viewModelScope.async { // Lanza una corrutina asíncrona dentro del viewModelScope
            // Calcular el total basado en los detalles (asumiendo que el precio unitario se obtiene en otro lugar)
            // Por simplicidad, asumiremos un precio unitario de 1000 por cada item para el cálculo del total del pedido
            // En un caso real, esto debería venir de la DB de productos
            val totalCalculado = detalles.sumOf { (idProducto, cantidad) ->
                cantidad * 1000
            }

            val nuevoPedido = l3Pedido(
                nombre_cliente = nombreCliente,
                estado = estado,
                total = totalCalculado, // Total calculado
                fecha = Instant.now()
            )
            val pedidoId = repository.insertarPedido(nuevoPedido) // Insertar pedido primero para obtener su ID (Long)

            // Insertar los detalles asociados a este pedido
            detalles.forEach { (idProducto, cantidad) ->
                val precioTotalDetalle = cantidad * 1000 // Asumir precio unitario de 1000 para el detalle
                repository.insertarDetallePedido(
                    l4DetallePedido(
                        id_pedido = pedidoId.toInt(), // Convertir Long a Int para la FK
                        id_producto = idProducto,
                        cantidad = cantidad,
                        precio_total = precioTotalDetalle
                    )
                )
            }
            pedidoId // Este es el valor Long que devuelve el bloque async
        }.await() // Llama a .await() sobre el Deferred devuelto por async
    }

    // Función para actualizar un pedido
    fun actualizarPedido(pedido: l3Pedido) {
        viewModelScope.launch {
            repository.actualizarPedido(pedido)
        }
    }

    // Función para eliminar un pedido
    fun eliminarPedido(pedido: l3Pedido) {
        viewModelScope.launch {
            repository.eliminarPedido(pedido)
        }
    }

    // Función para cargar los detalles de un pedido específico en selectedPedidoDetails
    fun loadPedidoDetails(pedidoId: Int) {
        viewModelScope.launch {
            repository.obtenerPedidoConDetalles(pedidoId).collect {
                _selectedPedidoDetails.postValue(it)
            }
        }
    }

    // Limpiar los detalles seleccionados
    fun clearSelectedPedidoDetails() {
        _selectedPedidoDetails.postValue(null)
    }
}
