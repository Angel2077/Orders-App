package com.usoftwork.ordersapp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun AgregarProductoScreen(navController: NavHostController) {
    // Estados para los campos del producto, utilizando rememberSaveable para preservar datos
    var nombre by rememberSaveable { mutableStateOf("") }
    var precio by rememberSaveable { mutableStateOf("") }
    var descripcion by rememberSaveable { mutableStateOf("") }
    var cantidad by rememberSaveable { mutableStateOf("") }

    // Estado para la categoría; inicializada por defecto como "Restaurant"
    val categoria = rememberSaveable { mutableStateOf("Restaurant") }
    var composicion by rememberSaveable { mutableStateOf("") }

    val imagenId = R.drawable.placeholder  // Imagen placeholder

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre del producto") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = precio,
            onValueChange = { if (it.all { char -> char.isDigit() }) precio = it },
            label = { Text("Precio") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = descripcion,
            onValueChange = { descripcion = it },
            label = { Text("Descripción") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = cantidad,
            onValueChange = { if (it.all { char -> char.isDigit() }) cantidad = it },
            label = { Text("Cantidad") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
            modifier = Modifier.fillMaxWidth()
        )

        // Campo para la categoría, editado por el cliente si lo requiere
        OutlinedTextField(
            value = categoria.value,
            onValueChange = { categoria.value = it },
            label = { Text("Categoría") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = composicion,
            onValueChange = { composicion = it },
            label = { Text("Composición") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                if (esEntradaValida(nombre, precio, descripcion, cantidad, categoria.value, composicion)) {
                    try {
                        val nuevoProducto = Producto(
                            nombre = nombre,
                            precio = precio.toInt(),
                            descripcion = "$descripcion\nComposición: $composicion\nCategoría: ${categoria.value}",
                            imagenId = imagenId,
                            cantidad = cantidad.toInt()
                        )
                        addProducto(nuevoProducto)
                        navController.popBackStack()
                    } catch (e: NumberFormatException) {
                        // Manejo de errores si no se puede convertir precio o cantidad
                        // Mostrar un mensaje de error al usuario
                    }
                }
            },
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
            enabled = esEntradaValida(nombre, precio, descripcion, cantidad, categoria.value, composicion)
        ) {
            Text("Agregar Producto")
        }
    }
}

// Función auxiliar para validar la entrada del usuario
fun esEntradaValida(nombre: String, precio: String, descripcion: String, cantidad: String, categoria: String, composicion: String): Boolean {
    return nombre.isNotBlank() &&
            precio.isNotBlank() &&
            descripcion.isNotBlank() &&
            cantidad.isNotBlank() &&
            categoria.isNotBlank() &&
            composicion.isNotBlank()
}