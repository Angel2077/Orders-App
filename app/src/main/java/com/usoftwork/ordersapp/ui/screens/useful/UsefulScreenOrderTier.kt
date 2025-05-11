package com.usoftwork.ordersapp.ui.screens.useful

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.material.icons.Icons
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.usoftwork.ordersapp.ui.navi_bar.NavBar
import com.usoftwork.ordersapp.ui.theme.Salmon

@Composable
fun ListadoPedidoScreen(pedidos: List<Pedido>, onFinalizarPedido: (Pedido) -> Unit, onReanudarPedido: (Pedido) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2C2C2C))
            .padding(8.dp)
    ) {
        // Encabezado
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            Icon(Icons.Default.Settings, contentDescription = null, tint = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Listado Pedido",
                fontSize = 24.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }

        // Filtros
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .background(Color(0xFF545454))
        ) {
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7BB6FF)),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Entregado", color = Color.White)
            }
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(containerColor = Salmon),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Pendiente", color = Color.White)
            }
        }

        // Lista de pedidos
        pedidos.forEach { pedido ->
            PedidoCard(pedido, onFinalizarPedido, onReanudarPedido)
        }
    }
}

@Composable
fun PedidoCard(pedido: Pedido, onFinalizarPedido: (Pedido) -> Unit, onReanudarPedido: (Pedido) -> Unit) {
    ExpandableButton(title = pedido.cliente) {
        Column {
            Text("Tiempo: ${pedido.tiempo}", color = Color.White)
            Text("Pedido:", fontWeight = FontWeight.Bold, color = Color.White)
            pedido.items.forEach {
                Text("- $it", color = Color.White)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Precio: $${pedido.precio}", color = Color.White, fontWeight = FontWeight.Bold)
                Button(
                    onClick = {
                        if (pedido.entregado) onReanudarPedido(pedido) else onFinalizarPedido(pedido)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = if (pedido.entregado) Color.Yellow else Color.Green),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(if (pedido.entregado) "Reanudar" else "Finalizar", color = Color.White)
                }
            }
        }
    }
}

@Composable
fun ExpandableButton(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Salmon)
            .clickable { expanded = !expanded }
            .padding(12.dp)
    ) {
        Text(
            text = if (expanded) "Ocultar    $title" else title,
            fontSize = 18.sp,
            color = Color.White,
        )

        AnimatedVisibility(
            visible = expanded,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                content()
            }
        }
    }
}

// Datos de ejemplo
data class Pedido(
    val cliente: String,
    val tiempo: String,
    val items: List<String>,
    val precio: Double,
    val entregado: Boolean
)

@Preview(showSystemUi = true)
@Composable
fun PaginaOrderTierPreview() {
    val samplePedidos = listOf(
        Pedido("Juan Perez", "00:12:00", listOf("Hamburguesa", "Papas", "Bebida"), 8.99, false),
        Pedido("Maria Lopez", "00:20:00", listOf("Pizza", "Refresco"), 12.50, true)
    )

    Scaffold(
        bottomBar = {
            NavBar(
                isVisible = true,
                selectedItem = "orders",
                onItemClick = {}
            )
        }
    ) { padding ->
        ListadoPedidoScreen(
            pedidos = samplePedidos,
            onFinalizarPedido = { /* lógica finalizar */ },
            onReanudarPedido = { /* lógica reanudar */ }
        )
    }
}
