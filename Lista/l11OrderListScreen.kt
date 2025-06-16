package com.example.db_local_menu.Lista

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

// Importamos las nuevas entidades, POJOs y bases de datos desde el paquete 'Lista'
import com.example.db_local_menu.Lista.l0Database
import com.example.db_local_menu.Lista.l3Pedido
import com.example.db_local_menu.Lista.l4DetallePedido
import com.example.db_local_menu.Lista.l5PedidoConDetalles
import com.example.db_local_menu.Lista.l8PedidoRepositorio
import com.example.db_local_menu.Lista.l9PedidoViewModel
import com.example.db_local_menu.Lista.l10PedidoViewModelFactory
import com.example.db_local_menu.Lista.PedidoEstado // Importa el enum PedidoEstado

// ¡IMPORTACIÓN CLAVE! Asegúrate de que esta línea esté presente y correcta, con el nombre real de tu DB principal
import com.example.db_local_menu._Producto.a // <--- CAMBIO AQUÍ: Usar a3ProduCompDB en lugar de AppDatabase

import java.time.format.DateTimeFormatter
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderListScreen(
    onBackClick: () -> Unit,
    // El ViewModel ahora obtiene el repositorio de l0Database
    viewModel: l9PedidoViewModel = viewModel(
        factory = l10PedidoViewModelFactory(
            l8PedidoRepositorio(
                l0Database.getInstance(LocalContext.current).pedidoDao(), // Usa l0Database
                l0Database.getInstance(LocalContext.current).detallePedidoDao() // Usa l0Database
            )
        )
    )
) {
    // Observa la lista de todos los pedidos con sus detalles desde el ViewModel
    val allPedidosWithDetails by viewModel.allPedidosWithDetails.observeAsState(emptyList())
    // Observa el pedido seleccionado para ver sus detalles en el diálogo
    val selectedPedidoDetails by viewModel.selectedPedidoDetails.observeAsState(null)

    // Estado para controlar la visibilidad del diálogo de detalles del pedido
    var showDetailsDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lista de Pedidos") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally // Centra el contenido horizontalmente
        ) {
            // Muestra un mensaje si no hay pedidos o la lista de pedidos
            if (allPedidosWithDetails.isEmpty()) {
                Text("No hay pedidos registrados.", modifier = Modifier.padding(16.dp))
                // Botón de ejemplo para añadir un pedido de prueba (útil para empezar a ver datos)
                Button(onClick = {
                    viewModel.insertarPedido("Cliente de Prueba", PedidoEstado.PENDIENTE, 5000)
                }, modifier = Modifier.padding(top = 16.dp)) {
                    Text("Añadir Pedido de Prueba")
                }
            } else {
                // Lista desplazable de pedidos
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(allPedidosWithDetails) { pedidoConDetalles ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                // Al hacer clic en una tarjeta de pedido, carga sus detalles y muestra el diálogo
                                .clickable {
                                    viewModel.loadPedidoDetails(pedidoConDetalles.pedido.id_pedido)
                                    showDetailsDialog = true
                                },
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth()
                            ) {
                                // Muestra el nombre del cliente
                                Text(
                                    text = "Cliente: ${pedidoConDetalles.pedido.nombre_cliente}",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                // Muestra el estado del pedido
                                Text(text = "Estado: ${pedidoConDetalles.pedido.estado.name}")
                                // Muestra la fecha del pedido con formato específico
                                Text(
                                    text = "Fecha: ${
                                        pedidoConDetalles.pedido.fecha
                                            .atZone(ZoneId.systemDefault())
                                            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                                    }"
                                )
                                // Muestra el precio total del pedido
                                Text(
                                    text = "Total: \$${pedidoConDetalles.pedido.total}",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            // Diálogo para mostrar los detalles de un pedido seleccionado
            if (showDetailsDialog && selectedPedidoDetails != null) {
                AlertDialog(
                    onDismissRequest = {
                        showDetailsDialog = false
                        viewModel.clearSelectedPedidoDetails() // Limpia los detalles al cerrar el diálogo
                    },
                    title = { Text("Detalles del Pedido #${selectedPedidoDetails!!.pedido.id_pedido}") },
                    text = {
                        Column {
                            Text("Cliente: ${selectedPedidoDetails!!.pedido.nombre_cliente}", fontWeight = FontWeight.Bold)
                            Text("Fecha: ${selectedPedidoDetails!!.pedido.fecha.atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))}")
                            Text("Estado: ${selectedPedidoDetails!!.pedido.estado.name}")
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Items:", fontWeight = FontWeight.Bold)
                            if (selectedPedidoDetails!!.detalles.isEmpty()) {
                                Text("No hay detalles para este pedido.")
                            } else {
                                selectedPedidoDetails!!.detalles.forEach { detalle ->
                                    Text("${detalle.cantidad} x Producto ID: ${detalle.id_producto} - Total: \$${detalle.precio_total}")
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Total del Pedido: \$${selectedPedidoDetails!!.pedido.total}", fontWeight = FontWeight.Bold)
                        }
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            showDetailsDialog = false
                            viewModel.clearSelectedPedidoDetails()
                        }) {
                            Text("Cerrar")
                        }
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OrderListScreenPreview() {
    MaterialTheme {
        OrderListScreen(onBackClick = {})
    }
}

