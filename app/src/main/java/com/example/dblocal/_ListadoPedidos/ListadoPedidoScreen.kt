package com.example.dblocal._ListadoPedidos

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.example.dblocal.Routes


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListadoPedidoScreen(
    listadoPedidoViewModel: ListadoPedidoViewModel,
    navController: NavController
) {
    val pendientePedidos = listadoPedidoViewModel.pendientePedidos.collectAsLazyPagingItems()
    val showConfirmDialog by listadoPedidoViewModel.showConfirmDialog.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Listado Pedido") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Button(onClick = { /* Lógica para mostrar Entregados */ }) {
                    Text("Entregado")
                }
                Button(onClick = { /* Lógica para mostrar Pendientes */ }) {
                    Text("Pendiente")
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(
                    count = pendientePedidos.itemCount,
                    key = pendientePedidos.itemKey { it.pedido.idPedido }
                ) { index ->
                    val pedido = pendientePedidos[index]
                    if (pedido != null) {
                        PedidoCard(
                            pedidoConDetalles = pedido,
                            listadoPedidoViewModel = listadoPedidoViewModel,
                            onCardClick = { clickedPedidoId ->
                                navController.navigate("${Routes.ORDER_DETAIL_SCREEN}/${clickedPedidoId}")
                            }
                        )
                    }
                }
            }

            if (showConfirmDialog) {
                AlertDialog(
                    onDismissRequest = { listadoPedidoViewModel.onDismissConfirmDialog() },
                    title = { Text("Confirmar acción") },
                    text = { Text("¿Seguro deseas cambiar el estado de este pedido?") },
                    confirmButton = {
                        Button(onClick = { listadoPedidoViewModel.onConfirmProcessPedido() }) {
                            Text("Confirmar")
                        }
                    },
                    dismissButton = {
                        Button(onClick = { listadoPedidoViewModel.onDismissConfirmDialog() }) {
                            Text("Cancelar")
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun PedidoCard(
    pedidoConDetalles: ListadoPedidoRepository.PedidoConDetallesProcesados,
    listadoPedidoViewModel: ListadoPedidoViewModel,
    onCardClick: (Int) -> Unit
) {
    val backgroundColor = Color(listadoPedidoViewModel.getBackgroundColorForPedido(pedidoConDetalles.pedido.fecha))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCardClick(pedidoConDetalles.pedido.idPedido) },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(backgroundColor)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = pedidoConDetalles.pedido.nombreCliente,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color.White
                )
                Text(
                    text = listadoPedidoViewModel.calculateTimeLeft(pedidoConDetalles.pedido.fecha),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Productos: ${pedidoConDetalles.detalles.joinToString { it.producto.nombre }}",
                fontSize = 14.sp,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$ ${pedidoConDetalles.pedido.total}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.White
                )
                Button(
                    onClick = { listadoPedidoViewModel.onFinishPedidoClick(pedidoConDetalles.pedido) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (pedidoConDetalles.pedido.estado == "entregado") MaterialTheme.colorScheme.primary else Color(0xFF6200EE)
                    )
                ) {
                    Text(if (pedidoConDetalles.pedido.estado == "entregado") "Devolver" else "Finalizar", color = Color.White)
                }
            }
        }
    }
}

