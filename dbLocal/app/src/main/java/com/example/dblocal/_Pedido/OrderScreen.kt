// OrderScreen.kt
package com.example.dblocal.ui.order // Ajusta el paquete según tu estructura

// Para mostrar mensajes con Snackbar
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dblocal._Pedido.OrderItem
import com.example.dblocal._Pedido.a5OrderViewModel
import com.example.dblocal._Producto.ComponenteJoinData
import com.example.dblocal._Producto.ProductoWithComponents


@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class) // Para usar Scaffold y SnackbarHost, FlowRow
@Composable
fun OrderScreen(
    orderViewModel: a5OrderViewModel,
    navController: NavController
) {
    val allProducts by orderViewModel.allProducts.collectAsState()
    val currentOrderItems by orderViewModel.currentOrderItems.collectAsState()
    val totalOrderPrice by orderViewModel.totalOrderPrice.collectAsState()
    val showOrderSummary by orderViewModel.showOrderSummary.collectAsState()
    val clientName by orderViewModel.clientName.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    // Colectar mensajes del ViewModel para mostrar en Snackbar
    LaunchedEffect(Unit) {
        orderViewModel.userMessage.collect { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(title = { Text("Tomar Pedido") })
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                // --- Sección de Selección de Productos ---
                Text("Productos Disponibles", style = MaterialTheme.typography.headlineSmall)
                Spacer(Modifier.height(8.dp))
                LazyColumn(
                    modifier = Modifier
                        .weight(1f) // Ocupa espacio disponible
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    items(allProducts) { product ->
                        ProductItem(product = product) {
                            orderViewModel.addProductToOrder(product)
                        }
                    }
                }

                Divider(modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(16.dp))

                // --- Sección de Items del Pedido Actual ---
                Text("Pedido Actual", style = MaterialTheme.typography.headlineSmall)
                Spacer(Modifier.height(8.dp))
                LazyColumn(
                    modifier = Modifier
                        .weight(1.5f) // Ocupa más espacio
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    if (currentOrderItems.isEmpty()) {
                        item {
                            Text("No hay productos en el pedido.", modifier = Modifier.fillMaxWidth().padding(8.dp))
                        }
                    } else {
                        items(currentOrderItems, key = { it.uniqueId }) { orderItem ->
                            CurrentOrderItem(
                                orderItem = orderItem,
                                onQuantityChange = { newQty ->
                                    orderViewModel.updateOrderItemQuantity(orderItem.uniqueId, newQty)
                                },
                                onToggleEligibleComponent = { componentJoinData -> // Pasa ComponenteJoinData
                                    orderViewModel.toggleEligibleComponent(orderItem.uniqueId, componentJoinData)
                                },
                                onRemove = { orderViewModel.removeOrderItem(orderItem.uniqueId) },
                                onDuplicate = { orderViewModel.duplicateOrderItem(orderItem) }
                            )
                        }
                    }
                }

                Divider(modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(16.dp))

                // --- Resumen del Pedido y Botón Finalizar ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Total: $${totalOrderPrice}", style = MaterialTheme.typography.headlineMedium)
                    Button(
                        onClick = { orderViewModel.toggleOrderSummary(true) }, // Muestra el resumen al hacer click
                        enabled = currentOrderItems.isNotEmpty() // Deshabilita si no hay ítems
                    ) {
                        Text("Finalizar Pedido")
                    }
                }
            }

            // --- Diálogo de Resumen del Pedido (Condicional) ---
            if (showOrderSummary) {
                OrderSummaryDialog(
                    orderItems = currentOrderItems,
                    totalPrice = totalOrderPrice,
                    clientName = clientName,
                    onClientNameChange = { orderViewModel.updateClientName(it) },
                    onConfirm = { orderViewModel.saveOrder() }, // Llama a saveOrder
                    onDismiss = { orderViewModel.toggleOrderSummary(false) }
                )
            }
        }
    )
}

// --- Composable para un Item de Producto Disponible ---
@Composable
fun ProductItem(
    product: ProductoWithComponents, // Este 'product' ya tiene componentsWithJoinData
    onAddProduct: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onAddProduct() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = product.producto.nombre, style = MaterialTheme.typography.titleMedium)
                Text(text = "$${product.producto.precio}", style = MaterialTheme.typography.bodyLarge)

                // ¡¡¡CAMBIO AQUÍ!!! Filtra y muestra componentes base
                val baseComponents = product.componentsWithJoinData.filter { it.productoComponente.esBase }.map { it.componente }
                if (baseComponents.isNotEmpty()) {
                    Text(text = "Base: ${baseComponents.joinToString { it.nombre }}", fontSize = 12.sp)
                }

                // ¡¡¡CAMBIO AQUÍ!!! Filtra y muestra componentes elegibles
                val eligibleComponents = product.componentsWithJoinData.filter { !it.productoComponente.esBase }.map { it.componente }
                if (eligibleComponents.isNotEmpty()) {
                    Text(text = "Elegibles (${product.producto.maximoIngredientes}): ${eligibleComponents.joinToString { it.nombre }}", fontSize = 12.sp)
                }
            }
            IconButton(onClick = onAddProduct) {
                Icon(Icons.Default.Add, contentDescription = "Añadir al pedido")
            }
        }
    }
}

// --- Composable para un Item del Pedido Actual ---
@Composable
fun CurrentOrderItem(
    orderItem: OrderItem,
    onQuantityChange: (Int) -> Unit,
    onToggleEligibleComponent: (ComponenteJoinData) -> Unit, // Recibe ComponenteJoinData
    onRemove: () -> Unit,
    onDuplicate: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Título del producto
                Text(
                    text = "${orderItem.productWithComponents.producto.nombre} - $${orderItem.calculateItemPrice()}",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )

                // Botones de acción
                Row {
                    IconButton(onClick = onDuplicate) {
                        Icon(Icons.Default.Add, contentDescription = "Duplicar item", tint = MaterialTheme.colorScheme.primary)
                    }
                    IconButton(onClick = onRemove) {
                        Icon(Icons.Default.Delete, contentDescription = "Eliminar item", tint = MaterialTheme.colorScheme.error)
                    }
                }
            }
            Spacer(Modifier.height(4.dp))

            // Control de Cantidad
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Cantidad: ")
                IconButton(onClick = { onQuantityChange(orderItem.quantity - 1) }) {
                    Icon(Icons.Default.Delete, contentDescription = "Disminuir cantidad")
                }
                Text("${orderItem.quantity}", style = MaterialTheme.typography.bodyLarge)
                IconButton(onClick = { onQuantityChange(orderItem.quantity + 1) }) {
                    Icon(Icons.Default.Add, contentDescription = "Aumentar cantidad")
                }
            }

            // Componentes Base (Si los hay)
            // ¡¡¡CAMBIO AQUÍ!!! Filtra la lista componentsWithJoinData
            val baseComponents = orderItem.productWithComponents.componentsWithJoinData
                .filter { it.productoComponente.esBase }
                .map { it.componente } // Mapea a a1Componente
            if (baseComponents.isNotEmpty()) {
                Text(
                    text = "Base: ${baseComponents.joinToString { it.nombre }}", // Accede a .nombre de a1Componente
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            // Componentes Elegibles (Si los hay)
            // ¡¡¡CAMBIO AQUÍ!!! Filtra la lista componentsWithJoinData y pasa ComponenteJoinData al toggle
            val eligibleComponentsFromProduct = orderItem.productWithComponents.componentsWithJoinData
                .filter { !it.productoComponente.esBase } // Filtra solo los elegibles (esBase = false)
            if (eligibleComponentsFromProduct.isNotEmpty()) {
                Text(
                    text = "Elegibles (max ${orderItem.productWithComponents.producto.maximoIngredientes}):",
                    fontSize = 12.sp,
                    color = Color.DarkGray
                )
                FlowRow( // Usamos FlowRow para que los chips se ajusten al ancho
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    eligibleComponentsFromProduct.forEach { componentJoinData ->
                        // Aquí 'componentJoinData' es de tipo ComponenteJoinData
                        val isSelected = orderItem.selectedEligibleComponentsJoinData.contains(componentJoinData)
                        FilterChip(
                            selected = isSelected,
                            onClick = { onToggleEligibleComponent(componentJoinData) }, // Pasa el ComponenteJoinData completo
                            label = { Text(componentJoinData.componente.nombre) }, // Accede al nombre desde componente.nombre
                            leadingIcon = if (isSelected) { { Icon(Icons.Default.Check, contentDescription = "Selected", modifier = Modifier.size(FilterChipDefaults.IconSize)) } } else null
                        )
                    }
                }
            }
            Spacer(Modifier.height(4.dp))
            // Muestra los componentes elegidos en el resumen del item
            if (orderItem.selectedEligibleComponentsJoinData.isNotEmpty()) {
                Text(
                    text = "Seleccionados: ${orderItem.selectedEligibleComponentsJoinData.joinToString { it.componente.nombre }}", // Accede al nombre
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

// --- Diálogo de Resumen del Pedido ---
@Composable
fun OrderSummaryDialog(
    orderItems: List<OrderItem>,
    totalPrice: Int,
    clientName: String,
    onClientNameChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Resumen del Pedido") },
        text = {
            Column {
                OutlinedTextField(
                    value = clientName,
                    onValueChange = onClientNameChange,
                    label = { Text("Nombre del Cliente") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                LazyColumn(modifier = Modifier.fillMaxHeight(0.6f)) { // Limita la altura de la lista
                    items(orderItems) { item ->
                        Text(
                            text = "${item.quantity}x ${item.productWithComponents.producto.nombre} - $${item.calculateItemPrice()}",
                            fontSize = 14.sp
                        )
                        if (item.selectedEligibleComponentsJoinData.isNotEmpty()) {
                            Text(
                                text = "  (${item.selectedEligibleComponentsJoinData.joinToString { it.componente.nombre }})", // Accede al nombre del componente real
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }
                Spacer(Modifier.height(8.dp))
                Text("Total: $${totalPrice}", style = MaterialTheme.typography.headlineSmall)
            }
        },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text("Guardar Pedido")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}