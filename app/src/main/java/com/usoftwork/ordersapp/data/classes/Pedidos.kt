package com.usoftwork.ordersapp.data.classes

// Clase para productos
/*
data class Producto(
    val nombre: String,
    val precio: Int,
    val descripcion: String,
    val imagenId: Int,
    val cantidad: Int
)

// Clase para guardar las compras con sus productos
data class Compra(
    val nombre: String,
    val cantidad: Int,
    val precioUnitario: Int,
    val total: Int,
    val fecha: String,
    val id: Int
)


// Clase para productos
data class Producto(
    val nombre: String,
    val precio: Int,
    val descripcion: String,
    val imagenId: Int,
    val cantidad: Int
)
*/
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.usoftwork.ordersapp.ui.screens.useful.OpcionProducto
import com.usoftwork.ordersapp.ui.theme.*

data class OpcionProducto(
    val nombre: String,
    val expandido: Boolean = false,
    val incluido: Boolean = false,
    val componentesSeleccionados: List<Int> = listOf()
)

@Composable
fun CreatePedido() {
    var opciones by remember {
        mutableStateOf(
            listOf(
                OpcionProducto("Handroll base kanikama"),
                OpcionProducto("Sushi base Salmon")
            )
        )
    }
    var resumenExpandido by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Título principal
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(
                    text = "Tomar Pedido",
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.headlineLarge
                )
            }

            Spacer(Modifier.height(100.dp))

            // Categorías
            Text("Categorías (Placeholder)")

            Spacer(Modifier.height(20.dp))

            opciones.forEachIndexed { index, opcion ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .width(300.dp)
                                .height(80.dp)
                                .background(
                                    MaterialTheme.colorScheme.secondary,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .clickable {
                                    opciones = opciones.toMutableList().also {
                                        it[index] = it[index].copy(
                                            expandido = !it[index].expandido,
                                            incluido = true
                                        )
                                    }
                                }
                                .padding(5.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = opcion.nombre,
                                color = MaterialTheme.colorScheme.onSecondary,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }

                        Checkbox(
                            checked = opcion.incluido,
                            onCheckedChange = {
                                opciones = opciones.toMutableList().also {
                                    it[index] = it[index].copy(incluido = !it[index].incluido)
                                }
                            }
                        )
                    }

                    if (opcion.expandido) {
                        Spacer(Modifier.height(10.dp))
                        repeat(2) { fila ->
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(5.dp),
                                modifier = Modifier.padding(5.dp)
                            ) {
                                (1..3).forEach { i ->
                                    val componente = (i - 1) + (fila * 3)
                                    val isSelected =
                                        opcion.componentesSeleccionados.contains(componente)

                                    Button(
                                        onClick = {
                                            opciones = opciones.toMutableList().also { lista ->
                                                val nuevosComponentes =
                                                    lista[index].componentesSeleccionados.toMutableList()
                                                if (isSelected) {
                                                    nuevosComponentes.remove(componente)
                                                } else {
                                                    nuevosComponentes.removeAll { it / 3 == fila }
                                                    nuevosComponentes.add(componente)
                                                }
                                                lista[index] =
                                                    lista[index].copy(componentesSeleccionados = nuevosComponentes)
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = if (isSelected)
                                                MaterialTheme.colorScheme.primary
                                            else
                                                MaterialTheme.colorScheme.secondary
                                        )
                                    ) {
                                        Text("Componente $i")
                                    }
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(10.dp))

                    // Botón "+"
                    Box(
                        modifier = Modifier
                            .size(width = 350.dp, height = 30.dp)
                            .background(
                                MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable {
                                val nuevaLista = opciones.toMutableList()
                                nuevaLista.add(index + 1, opciones[index].copy())
                                opciones = nuevaLista
                            }
                            .padding(5.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "+",
                            color = MaterialTheme.colorScheme.onSecondary,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }

                    Spacer(Modifier.height(20.dp))
                }
            }

            Spacer(Modifier.height(50.dp))

        }

        // Resumen del pedido
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .heightIn(min = 80.dp, max = 300.dp) // Altura máxima del resumen
                .background(MaterialTheme.colorScheme.primaryContainer)
                .clickable { resumenExpandido = !resumenExpandido }
                .padding(16.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                Text("Resumen del Pedido", fontWeight = FontWeight.Bold)

                if (resumenExpandido) {
                    opciones.filter { it.incluido }.forEach { opcion ->
                        Text("- ${opcion.nombre}", fontWeight = FontWeight.Bold)
                        opcion.componentesSeleccionados.sorted().forEach { componente ->
                            Text(
                                "    • Componente ${(componente % 3) + 1} de fila ${(componente / 3) + 1}",
                                modifier = Modifier.padding(start = 16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}




        // Listado de pedidos
        @Composable
        fun ListadoPedido() {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF2C2C2C))
                    .padding(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(Color.Gray, shape = RoundedCornerShape(8.dp))
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Listado Pedido",
                        fontSize = 24.sp,
                        color = White,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .background(Dustwhite)
                ) {
                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(containerColor = Cian),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Entregado", color = White)
                    }
                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(containerColor = Salmon),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Pendiente", color = White)
                    }
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Salmon)
                ) {
                    Column(
                        Modifier.padding(8.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                "Juan Perez",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = White
                            )
                            Text("Time Left", color = White)
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        HorizontalDivider(thickness = 1.dp, color = grey)
                        HorizontalDivider(
                            color = grey,
                            thickness = 1.dp,
                            modifier = Modifier.padding(top = 4.dp)
                        )
//No se
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Button(
                                onClick = {},
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(
                                        0xFF815959
                                    )
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("$", color = White)
                            }
                            Button(
                                onClick = {},
                                colors = ButtonDefaults.buttonColors(containerColor = Softred),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Finish", color = White)
                            }
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .background(Yellow),
                    contentAlignment = Alignment.TopEnd
                ) {
                    Text(
                        text = "00:12:00",
                        color = Color.White,
                        modifier = Modifier
                            .background(DuckYellow)
                            .padding(4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .background(Color(0xFFA7D7AC))
                ) {}

                Spacer(modifier = Modifier.height(260.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Salmon),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(5) { _ ->
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = White,
                            modifier = Modifier
                                .size(50.dp)
                                .background(grey, shape = RoundedCornerShape(8.dp)).padding(8.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp)
                        .background(LightGray)
                )
            }
        }
