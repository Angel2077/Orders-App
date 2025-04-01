package com.usoftwork.ordersapp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun AgregarProductoScreen(navController: NavHostController) {
    // Estados para los campos del producto
    var nombre by rememberSaveable { mutableStateOf("") }
    var precio by rememberSaveable { mutableStateOf("") }
    var descripcion by rememberSaveable { mutableStateOf("") }
    var cantidad by rememberSaveable { mutableStateOf("") }
    var categoria by rememberSaveable { mutableStateOf("") }
    var composicion by rememberSaveable { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    val categorias = listOf("Alimentos", "Electrónica", "Ropa", "Salud", "Hogar", "Otro")

    val imagenId = R.drawable.placeholder  // Placeholder de imagen por defecto

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

        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = categoria,
                onValueChange = {},
                readOnly = true,
                label = { Text("Categoría") },
                modifier = Modifier.fillMaxWidth().clickable { expanded = true }
            )
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                categorias.forEach { opcion ->
                    DropdownMenuItem(
                        text = { Text(opcion) },
                        onClick = {
                            categoria = opcion
                            expanded = false
                        }
                    )
                }
            }
        }

        OutlinedTextField(
            value = composicion,
            onValueChange = { composicion = it },
            label = { Text("Composición") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                if (esEntradaValida(nombre, precio, descripcion, cantidad, categoria, composicion)) {
                    val nuevoProducto = Producto(
                        nombre = nombre,
                        precio = precio.toInt(),
                        descripcion = "$descripcion\nComposición: $composicion\nCategoría: $categoria",
                        imagenId = imagenId,
                        cantidad = cantidad.toInt()
                    )
                    addProducto(nuevoProducto)
                    navController.popBackStack()
                }
            },
            modifier = Modifier.padding(top = 16.dp).fillMaxWidth(),
            enabled = esEntradaValida(nombre, precio, descripcion, cantidad, categoria, composicion)
        ) {
            Text("Agregar Producto")
        }
    }
}

fun addProducto(nuevoProducto: Producto) {

}

// Función auxiliar para validar la entrada del usuario
fun esEntradaValida(nombre: String, precio: String, descripcion: String, cantidad: String, categoria: String, composicion: String): Boolean {
    return nombre.isNotBlank() && precio.isNotBlank() && cantidad.isNotBlank() &&
            descripcion.isNotBlank() && categoria.isNotBlank() && composicion.isNotBlank()
}

