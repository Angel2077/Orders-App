package com.example.dblocal._ListadoPedidos

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PedidoDetalleScreen(
    pedidoId: Int,
    navController: NavController,
    listadoPedidoViewModelFactory: ListadoPedidoViewModelFactory
) {
    val listadoPedidoViewModel: ListadoPedidoViewModel = viewModel(factory = listadoPedidoViewModelFactory)

    val pedidoDetalle by listadoPedidoViewModel.listadoPedidoRepository.getPedidoCompletoById(pedidoId)
        .collectAsState(initial = null)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del Pedido") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (pedidoDetalle == null) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                Text(text = "Cliente: ${pedidoDetalle?.pedido?.nombreCliente}", fontWeight = FontWeight.Bold, fontSize = 24.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Fecha: ${pedidoDetalle?.pedido?.fecha?.toLocalDate()}", fontSize = 16.sp)
                Text(text = "Estado: ${pedidoDetalle?.pedido?.estado}", fontSize = 16.sp)
                Text(text = "Total del Pedido: $ ${pedidoDetalle?.pedido?.total}", fontWeight = FontWeight.Bold, fontSize = 20.sp)

                Spacer(modifier = Modifier.height(16.dp))
                Divider()
                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Productos en el Pedido:", fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(pedidoDetalle?.detalles ?: emptyList()) { detalle ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(text = "${detalle.producto.nombre} (x${detalle.detallePedido.cantidad})", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                Text(text = "Precio Unitario: $ ${detalle.producto.precio}", fontSize = 14.sp)
                                Text(text = "Precio de la Cantidad: $ ${detalle.detallePedido.precioTotal}", fontSize = 14.sp)

                                if (detalle.componentesElegidos.isNotEmpty()) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(text = "Componentes Elegidos:", fontWeight = FontWeight.Medium, fontSize = 14.sp)
                                    Column(modifier = Modifier.padding(start = 8.dp)) {
                                        detalle.componentesElegidos.forEach { componente ->
                                            Text(text = "- ${componente.nombre}", fontSize = 12.sp)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
