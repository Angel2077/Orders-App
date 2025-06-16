package com.example.appmenu.pantallas

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Producto2(
    val nombre: String,
    val precio: Int,
    val categoria: String,
    val porcion: String? = null,
    val unidad: String? = null
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Pantalla2() {
    // Lista de productos
    var productos by remember {
        mutableStateOf(
            listOf(
                Producto2("Sushi Salmón", 3500, "Sushi"),
                Producto2("Handroll Atún", 2800, "Handroll"),
                Producto2("Pizza", 5000, "Comida Rápida"),
                Producto2("Cola", 1500, "Bebida", porcion = "1", unidad = "1L"),
                Producto2("Helado Chocolate", 1800, "Helado", porcion = "1", unidad = "100ml"),
                Producto2("Empanada", 2000, "Otros")
            )
        )
    }

    var productoSeleccionado by remember { mutableStateOf<Producto2?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Productos",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(productos) { producto ->
                Box(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
                        .combinedClickable(
                            onClick = {
                                productoSeleccionado = producto
                            }
                        )
                        .padding(12.dp)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = producto.nombre,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }

    // Diálogo con información del producto
    productoSeleccionado?.let { producto ->
        AlertDialog(
            onDismissRequest = { productoSeleccionado = null },
            title = {
                Column {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.TopEnd
                    ) {
                        Text(
                            text = producto.categoria,
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                    Text(
                        text = producto.nombre,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .background(Color.LightGray),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Imagen", fontSize = 12.sp, color = Color.DarkGray)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Precio: \$${producto.precio}")

                    if (producto.categoria == "Helado" || producto.categoria == "Bebida") {
                        Spacer(modifier = Modifier.height(8.dp))
                        val porcionUnidad = listOfNotNull(producto.porcion, producto.unidad).joinToString(" - ")
                        Text(porcionUnidad)
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { productoSeleccionado = null }) {
                    Text("Cerrar")
                }
            }
        )
    }
}
