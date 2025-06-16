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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Producto(
    val nombre: String,
    val porcion: Int,
    val unidad: String,
    val cantidad: Int
)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Pantalla1() {
    var productos by remember {
        mutableStateOf(
            listOf(
                Producto("Arroz", 300, "gr", 23),
                Producto("Leche", 1, "lt", 10),
                Producto("Carne", 500, "gr", 5),
                Producto("Pan", 1, "unidad", 15),
                Producto("Huevos", 12, "unidad", 8),
                Producto("Yogur", 200, "ml", 7)
            )
        )
    }

    var productoSeleccionado by remember { mutableStateOf<Producto?>(null) }
    var mostrarDialogoAgregar by remember { mutableStateOf(false) }
    var modoEliminar by remember { mutableStateOf(false) }
    var productoAEliminar by remember { mutableStateOf<Producto?>(null) }
    var productoEditar by remember { mutableStateOf<Producto?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Almacén",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Button(onClick = { mostrarDialogoAgregar = true }) {
                Text("Agregar producto")
            }

            Button(onClick = { modoEliminar = true }) {
                Text("Eliminar producto")
            }
        }

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
                                if (modoEliminar) {
                                    productoAEliminar = producto
                                } else {
                                    productoSeleccionado = producto
                                }
                            },
                            onLongClick = {
                                productoEditar = producto
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

    // Diálogo informativo del producto
    productoSeleccionado?.let { producto ->
        AlertDialog(
            onDismissRequest = { productoSeleccionado = null },
            title = { Text(producto.nombre, fontWeight = FontWeight.Bold) },
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
                    Text("${producto.porcion} - ${producto.unidad}")
                    Text("Cantidad: ${producto.cantidad}")
                }
            },
            confirmButton = {
                TextButton(onClick = { productoSeleccionado = null }) {
                    Text("Cerrar")
                }
            }
        )
    }

    // Diálogo para agregar producto
    if (mostrarDialogoAgregar) {
        var nombre by remember { mutableStateOf("") }
        var porcion by remember { mutableStateOf("") }
        var unidad by remember { mutableStateOf("") }
        var cantidad by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { mostrarDialogoAgregar = false },
            title = { Text("Nuevo producto") },
            text = {
                Column {
                    OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") })
                    OutlinedTextField(value = porcion, onValueChange = { porcion = it }, label = { Text("Porción") })
                    OutlinedTextField(value = unidad, onValueChange = { unidad = it }, label = { Text("Unidad") })
                    OutlinedTextField(value = cantidad, onValueChange = { cantidad = it }, label = { Text("Cantidad") })
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    val nuevo = Producto(
                        nombre.trim(),
                        porcion.toIntOrNull() ?: 0,
                        unidad.trim(),
                        cantidad.toIntOrNull() ?: 0
                    )
                    productos = productos + nuevo
                    mostrarDialogoAgregar = false
                }) {
                    Text("Agregar")
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogoAgregar = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    // Diálogo de confirmación para eliminar
    productoAEliminar?.let { producto ->
        AlertDialog(
            onDismissRequest = {
                productoAEliminar = null
                modoEliminar = false
            },
            title = { Text("¿Eliminar ${producto.nombre}?") },
            text = { Text("Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(onClick = {
                    productos = productos.filter { it != producto }
                    productoAEliminar = null
                    modoEliminar = false
                }) {
                    Text("Sí, eliminar", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    productoAEliminar = null
                    modoEliminar = false
                }) {
                    Text("Cancelar")
                }
            }
        )
    }

    // Diálogo para editar producto
    productoEditar?.let { producto ->
        var nombre by remember { mutableStateOf(producto.nombre) }
        var porcion by remember { mutableStateOf(producto.porcion.toString()) }
        var unidad by remember { mutableStateOf(producto.unidad) }
        var cantidad by remember { mutableStateOf(producto.cantidad.toString()) }

        AlertDialog(
            onDismissRequest = { productoEditar = null },
            title = { Text("Editar producto") },
            text = {
                Column {
                    OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") })
                    OutlinedTextField(value = porcion, onValueChange = { porcion = it }, label = { Text("Porción") })
                    OutlinedTextField(value = unidad, onValueChange = { unidad = it }, label = { Text("Unidad") })
                    OutlinedTextField(value = cantidad, onValueChange = { cantidad = it }, label = { Text("Cantidad") })
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    val actualizado = Producto(
                        nombre.trim(),
                        porcion.toIntOrNull() ?: 0,
                        unidad.trim(),
                        cantidad.toIntOrNull() ?: 0
                    )
                    productos = productos.map {
                        if (it == producto) actualizado else it
                    }
                    productoEditar = null
                }) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                TextButton(onClick = { productoEditar = null }) {
                    Text("Cancelar")
                }
            }
        )
    }
}
