package com.usoftwork.ordersapp.ui.screens.useful

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

class AlmacenActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            var productos by remember { mutableStateOf(listOf<Product>()) }
            var showCreateDialog by remember { mutableStateOf(false) }
            var productoSeleccionado by remember { mutableStateOf<Product?>(null) }
            var showEliminarDialog by remember { mutableStateOf(false) }
            var showDetalleDialog by remember { mutableStateOf(false) }
            var showEditarDialog by remember { mutableStateOf(false) }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black),
                contentAlignment = Alignment.TopCenter
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(top = 50.dp)
                ) {
                    // Título
                    Text(
                        text = "Almacén",
                        color = Color.Yellow,
                        fontSize = 32.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Botones Crear y Eliminar
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = { showCreateDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)) // Verde
                        ) {
                            Text("Crear", color = Color.White)
                        }
                        Button(
                            onClick = {
                                if (productoSeleccionado != null) {
                                    productos = productos - productoSeleccionado!!
                                    productoSeleccionado = null
                                } else {
                                    showEliminarDialog = true
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336)) // Rojo
                        ) {
                            Text("Eliminar", color = Color.White)
                        }
                    }

                    Spacer(modifier = Modifier.height(30.dp))

                    // Productos
                    val chunkedProductos = productos.chunked(3)
                    Column(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(horizontal = 10.dp)
                    ) {
                        chunkedProductos.forEach { fila ->
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                fila.forEach { producto ->
                                    InfoBox(
                                        producto = producto,
                                        modifier = Modifier.weight(1f),
                                        onClick = {
                                            productoSeleccionado = producto
                                            showDetalleDialog = true
                                        }
                                    )
                                }
                                if (fila.size < 3) {
                                    repeat(3 - fila.size) {
                                        Spacer(modifier = Modifier.weight(1f))
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (showCreateDialog) {
                CreateProductDialog(
                    onDismiss = { showCreateDialog = false },
                    onCreate = { nuevoProducto ->
                        productos = productos + nuevoProducto
                        showCreateDialog = false
                    }
                )
            }

            if (showEliminarDialog) {
                AlertDialog(
                    onDismissRequest = { showEliminarDialog = false },
                    title = { Text("Eliminar Producto") },
                    text = { Text("Toca un producto primero para poder eliminarlo.") },
                    confirmButton = {
                        Button(onClick = { showEliminarDialog = false }) {
                            Text("Ok")
                        }
                    }
                )
            }

            if (showDetalleDialog && productoSeleccionado != null) {
                DetalleProductoDialog(
                    producto = productoSeleccionado!!,
                    onDismiss = { showDetalleDialog = false },
                    onEditarClicked = {
                        showDetalleDialog = false
                        showEditarDialog = true
                    }
                )
            }

            if (showEditarDialog && productoSeleccionado != null) {
                EditarProductoDialog(
                    producto = productoSeleccionado!!,
                    onDismiss = { showEditarDialog = false },
                    onEdit = { productoEditado ->
                        productos = productos.map {
                            if (it == productoSeleccionado) productoEditado else it
                        }
                        showEditarDialog = false
                    }
                )
            }
        }
    }
}

// ------ Data Class ------
data class Product(val nombre: String, val cantidad: String, val unidad: String)

// ------ Composable Info Box ------
@Composable
fun InfoBox(producto: Product, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .height(100.dp)
            .background(Color.White, shape = RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(10.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = producto.nombre,
                color = Color.Black,
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${producto.cantidad} ${producto.unidad}",
                color = Color.Gray,
                fontSize = 16.sp
            )
        }
    }
}

// ------ Dialog Crear Producto ------
@Composable
fun CreateProductDialog(onDismiss: () -> Unit, onCreate: (Product) -> Unit) {
    var nombre by remember { mutableStateOf(TextFieldValue("")) }
    var cantidad by remember { mutableStateOf(TextFieldValue("")) }
    var unidad by remember { mutableStateOf(TextFieldValue("")) }

    Dialog(onDismissRequest = { onDismiss() }) {
        Box(
            modifier = Modifier
                .background(Color.White, shape = RoundedCornerShape(12.dp))
                .padding(16.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Botón de Cerrar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(onClick = { onDismiss() }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cerrar",
                            tint = Color.Black
                        )
                    }
                }

                Text("Crear Producto", fontSize = 20.sp, color = Color.Black)
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") }
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = cantidad,
                    onValueChange = { cantidad = it },
                    label = { Text("Cantidad (ej: 500)") }
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = unidad,
                    onValueChange = { unidad = it },
                    label = { Text("Unidad (ej: ml, kg)") }
                )
                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = {
                    if (nombre.text.isNotBlank() && cantidad.text.isNotBlank() && unidad.text.isNotBlank()) {
                        onCreate(Product(nombre.text, cantidad.text, unidad.text))
                    }
                }) {
                    Text("Crear")
                }
            }
        }
    }
}

// ------ Dialog Ver Detalle Producto ------
@Composable
fun DetalleProductoDialog(producto: Product, onDismiss: () -> Unit, onEditarClicked: () -> Unit) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Box(
            modifier = Modifier
                .background(Color.White, shape = RoundedCornerShape(12.dp))
                .padding(16.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(onClick = { onDismiss() }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cerrar",
                            tint = Color.Black
                        )
                    }
                }
                Text(text = "Detalles del Producto", fontSize = 20.sp, color = Color.Black)
                Spacer(modifier = Modifier.height(8.dp))

                Text("Nombre: ${producto.nombre}", fontSize = 16.sp)
                Text("Cantidad: ${producto.cantidad}", fontSize = 16.sp)
                Text("Unidad: ${producto.unidad}", fontSize = 16.sp)

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = { onEditarClicked() }) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Editar")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Editar")
                }
            }
        }
    }
}

// ------ Dialog Editar Producto ------
@Composable
fun EditarProductoDialog(producto: Product, onDismiss: () -> Unit, onEdit: (Product) -> Unit) {
    var nombre by remember { mutableStateOf(TextFieldValue(producto.nombre)) }
    var cantidad by remember { mutableStateOf(TextFieldValue(producto.cantidad)) }
    var unidad by remember { mutableStateOf(TextFieldValue(producto.unidad)) }

    Dialog(onDismissRequest = { onDismiss() }) {
        Box(
            modifier = Modifier
                .background(Color.White, shape = RoundedCornerShape(12.dp))
                .padding(16.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(onClick = { onDismiss() }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cerrar",
                            tint = Color.Black
                        )
                    }
                }
                Text(text = "Editar Producto", fontSize = 20.sp, color = Color.Black)
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") }
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = cantidad,
                    onValueChange = { cantidad = it },
                    label = { Text("Cantidad") }
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = unidad,
                    onValueChange = { unidad = it },
                    label = { Text("Unidad") }
                )
                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = {
                    if (nombre.text.isNotBlank() && cantidad.text.isNotBlank() && unidad.text.isNotBlank()) {
                        onEdit(Product(nombre.text, cantidad.text, unidad.text))
                    }
                }) {
                    Text("Guardar Cambios")
                }
            }
        }
    }
}
