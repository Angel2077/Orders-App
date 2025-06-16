package com.example.dblocal._Pedido // Ajusta el paquete según tu estructura de carpetas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dblocal._Producto.ComponenteJoinData
import com.example.dblocal._Producto.ProductoWithComponents
import com.example.dblocal._Producto.a2ProduCompDao
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

// Clase de datos para representar un item individual en el pedido actual.
// Esto es necesario para manejar múltiples instancias del mismo producto base
// pero con diferentes configuraciones (ej. 2 sushis iguales pero con distintos elegibles).
data class OrderItem(
    val uniqueId: String = UUID.randomUUID().toString(), // ID único para esta instancia específica del producto en el pedido
    val productWithComponents: ProductoWithComponents, // El producto base con sus componentes (base y elegibles)
    var quantity: Int = 1, // Cantidad de este item específico
    // ¡¡¡CAMBIO CRUCIAL AQUÍ!!! Ahora almacena ComponenteJoinData, no directamente a1Componente
    val selectedEligibleComponentsJoinData: MutableList<ComponenteJoinData> = mutableListOf()
) {
    // Función helper para calcular el precio de este item del pedido
    fun calculateItemPrice(): Int {
        val basePrice = productWithComponents.producto.precio
        // Si los componentes elegibles añaden costo, la lógica iría aquí.
        // Por ahora, asumimos que el precio del componente se incluye en el precio del producto base.
        return basePrice * quantity
    }
}


class a5OrderViewModel(
    private val productoDao: a2ProduCompDao, // DAO para obtener la lista de productos disponibles
    private val pedidoRepository: PedidoRepository // Repositorio para guardar los pedidos
) : ViewModel() {

    // Lista de todos los productos disponibles para elegir (con sus componentes)
    private val _allProducts = MutableStateFlow<List<ProductoWithComponents>>(emptyList())
    val allProducts: StateFlow<List<ProductoWithComponents>> = _allProducts.asStateFlow()

    // La lista de items que el usuario ha añadido al pedido actual
    private val _currentOrderItems = MutableStateFlow<List<OrderItem>>(emptyList())
    val currentOrderItems: StateFlow<List<OrderItem>> = _currentOrderItems.asStateFlow()

    // El precio total calculado del pedido actual
    private val _totalOrderPrice = MutableStateFlow(0)
    val totalOrderPrice: StateFlow<Int> = _totalOrderPrice.asStateFlow()

    // Estado para controlar la visibilidad de la sección de resumen del pedido (imagen 3)
    private val _showOrderSummary = MutableStateFlow(false)
    val showOrderSummary: StateFlow<Boolean> = _showOrderSummary.asStateFlow()

    // Estado para el nombre del cliente (obligatorio al finalizar el pedido)
    private val _clientName = MutableStateFlow("")
    val clientName: StateFlow<String> = _clientName.asStateFlow()

    // Mensajes para el usuario (ej. "Nombre de cliente es obligatorio")
    private val _userMessage = MutableSharedFlow<String>()
    val userMessage: SharedFlow<String> = _userMessage.asSharedFlow()


    init {
        // Carga todos los productos disponibles cuando el ViewModel se inicializa
        viewModelScope.launch {
            productoDao.obtenerProductosConComponentesElegibles().collect { products ->
                _allProducts.value = products
            }
        }

        // Observa los cambios en los items del pedido actual para recalcular el precio total
        viewModelScope.launch {
            _currentOrderItems.collect { items ->
                _totalOrderPrice.value = items.sumOf { it.calculateItemPrice() }
            }
        }
    }

    // --- Funciones de construcción del pedido ---

    /**
     * Añade una nueva instancia de un producto base al pedido.
     * Inicialmente, con cantidad 1 y sin componentes elegibles seleccionados.
     */
    fun addProductToOrder(product: ProductoWithComponents) {
        val newItem = OrderItem(
            productWithComponents = product,
            quantity = 1,
            selectedEligibleComponentsJoinData = mutableListOf() // Lista mutable para permitir cambios posteriores
        )
        _currentOrderItems.update { it + newItem }
    }

    /**
     * Duplica un OrderItem existente. Útil para "pedir el mismo sushi pero con otros elegibles".
     * Se crea una copia con un nuevo uniqueId y una copia profunda de los componentes seleccionados.
     */
    fun duplicateOrderItem(orderItem: OrderItem) {
        val newDuplicatedItem = orderItem.copy(
            uniqueId = UUID.randomUUID().toString(), // Genera un ID nuevo para que sea una instancia distinta
            // ¡¡¡CAMBIO AQUÍ!!! Copia profunda de la lista mutable de ComponenteJoinData
            selectedEligibleComponentsJoinData = orderItem.selectedEligibleComponentsJoinData.toMutableList()
        )
        _currentOrderItems.update { it + newDuplicatedItem }
    }

    /**
     * Actualiza la cantidad de un OrderItem específico.
     * Asegura que la cantidad sea al menos 1.
     */
    fun updateOrderItemQuantity(orderItemUniqueId: String, newQuantity: Int) {
        _currentOrderItems.update { currentList ->
            currentList.map { item ->
                if (item.uniqueId == orderItemUniqueId) {
                    item.copy(quantity = newQuantity.coerceAtLeast(1)) // Cantidad mínima de 1
                } else {
                    item
                }
            }
        }
    }

    /**
     * Alterna la selección de un componente elegible para un OrderItem específico.
     * Maneja la adición/eliminación y respeta el máximo de ingredientes elegibles.
     */
    // ¡¡¡CAMBIO CRUCIAL AQUÍ!!! Ahora recibe ComponenteJoinData
    fun toggleEligibleComponent(orderItemUniqueId: String, componentJoinData: ComponenteJoinData) {
        _currentOrderItems.update { currentList ->
            currentList.map { item ->
                if (item.uniqueId == orderItemUniqueId) {
                    // Usamos un set temporal para manejar fácilmente la adición/eliminación y evitar duplicados
                    val newSelectedComponents = item.selectedEligibleComponentsJoinData.toMutableSet()
                    if (newSelectedComponents.contains(componentJoinData)) {
                        newSelectedComponents.remove(componentJoinData)
                    } else {
                        // Filtra los componentes elegibles reales de la lista completa del producto
                        // Asumimos que ComponenteJoinData.productoComponente.esBase indica si es base (true) o elegible (false)
                        val elegiblesDelProducto = item.productWithComponents.componentsWithJoinData
                            .filter { !it.productoComponente.esBase } // Solo los que son 'elegibles' (esBase = false)
                            .toSet()

                        // Cuenta cuántos componentes elegibles *reales* están actualmente seleccionados
                        val currentlySelectedEligiblesCount = newSelectedComponents.count {
                            // Verifica que el componente seleccionado sea realmente elegible para este producto
                            elegiblesDelProducto.contains(it)
                        }

                        if (currentlySelectedEligiblesCount < item.productWithComponents.producto.maximoIngredientes) {
                            newSelectedComponents.add(componentJoinData)
                        } else {
                            // Emite un mensaje al usuario para indicar que se alcanzó el límite
                            viewModelScope.launch {
                                _userMessage.emit("Máximo de ${item.productWithComponents.producto.maximoIngredientes} ingredientes elegibles alcanzado para este producto.")
                            }
                        }
                    }
                    // Retorna una nueva copia del OrderItem con la lista de componentes actualizada
                    item.copy(selectedEligibleComponentsJoinData = newSelectedComponents.toMutableList())
                } else {
                    item
                }
            }
        }
    }

    /**
     * Elimina un OrderItem específico del pedido actual.
     */
    fun removeOrderItem(orderItemUniqueId: String) {
        _currentOrderItems.update { currentList ->
            currentList.filter { it.uniqueId != orderItemUniqueId }
        }
    }

    // --- Funciones de resumen y guardado del pedido ---

    /**
     * Actualiza el nombre del cliente.
     */
    fun updateClientName(name: String) {
        _clientName.value = name
    }

    /**
     * Alterna la visibilidad de la sección de resumen del pedido.
     */
    fun toggleOrderSummary(show: Boolean) {
        _showOrderSummary.value = show
    }

    /**
     * Guarda el pedido actual en la base de datos.
     * Realiza validaciones y limpia el pedido si es exitoso.
     */
    fun saveOrder() {
        if (_clientName.value.isBlank()) {
            viewModelScope.launch {
                _userMessage.emit("El nombre del cliente es obligatorio para el pedido.")
            }
            return // No guardar si el nombre del cliente está vacío
        }
        if (_currentOrderItems.value.isEmpty()) {
            viewModelScope.launch {
                _userMessage.emit("El pedido está vacío. Añade productos antes de guardar.")
            }
            return // No guardar si el pedido está vacío
        }


        viewModelScope.launch {
            val order = Pedido(
                nombreCliente = _clientName.value,
                total = _totalOrderPrice.value
                // 'fecha' y 'estado' tienen valores por defecto en la entidad Pedido
            )

            // Mapea los OrderItem a DetallePedido para guardarlos en la DB
            val detalles = _currentOrderItems.value.map { orderItem ->
                DetallePedido(
                    idProducto = orderItem.productWithComponents.producto.id, // Esto debería estar bien ahora.
                    cantidad = orderItem.quantity,
                    precioTotal = orderItem.calculateItemPrice(),
                    componentesElegidosIds = orderItem.selectedEligibleComponentsJoinData.joinToString(",") { it.componente.id.toString() }
                )
            }

            val orderId = pedidoRepository.guardarPedidoCompleto(order, detalles)
            if (orderId != -1L) {
                // Pedido guardado exitosamente
                viewModelScope.launch {
                    _userMessage.emit("Pedido #${orderId} guardado exitosamente.")
                }
                clearOrder() // Limpia el pedido actual para uno nuevo
                toggleOrderSummary(false) // Cierra la pantalla de resumen
            } else {
                // Fallo al guardar el pedido
                viewModelScope.launch {
                    _userMessage.emit("Error al guardar el pedido. Intente de nuevo.")
                }
            }
        }
    }

    /**
     * Reinicia el estado del pedido actual (vacía items, precio, nombre cliente).
     */
    fun clearOrder() {
        _currentOrderItems.value = emptyList()
        _totalOrderPrice.value = 0
        _clientName.value = ""
    }
}