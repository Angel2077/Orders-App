package com.usoftwork.ordersapp.ui.screens.basics

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.usoftwork.ordersapp.data.a1Entity.a1Componente
import com.usoftwork.ordersapp.data.classes.CustomButton
import com.usoftwork.ordersapp.data.classes.ExpandableButton
import com.usoftwork.ordersapp.ui.ViewModel.a5ComponenteViewModel

@Composable
fun CarritoScreen(viewModel: a5ComponenteViewModel = viewModel()) {
    val lista by viewModel.listaComponentes.collectAsState(initial = emptyList())
    var modoSeleccion by remember { mutableStateOf(false) }
    val seleccionados = remember { mutableStateListOf<Int>() }
    var expanded by remember { mutableStateOf(false) }

    var nombre by remember { mutableStateOf("") }
    var porcion by remember { mutableStateOf("") }
    var unidad by remember { mutableStateOf("") }
    var cantidad by remember { mutableStateOf("") }
    var idEditando by remember { mutableStateOf<Int?>(null) }
    var errorMensaje by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(8.dp)) {

        ExpandableButton(title = if (idEditando == null) "Agregar Componente" else "Editar Componente") {
            Column {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = porcion,
                    onValueChange = { porcion = it },
                    label = { Text("Porción (número)") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = unidad,
                    onValueChange = { unidad = it },
                    label = { Text("Unidad") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = cantidad,
                    onValueChange = { cantidad = it },
                    label = { Text("Cantidad (entero)") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                if (errorMensaje.isNotEmpty()) {
                    Text(text = errorMensaje, color = Color.Red)
                }
                Spacer(modifier = Modifier.height(8.dp))
                CustomButton(
                    onClick = {
                        if (nombre.isBlank() || porcion.isBlank() || unidad.isBlank() || cantidad.isBlank()) {
                            errorMensaje = "Todos los campos deben estar llenos."
                        } else {
                            val porcionDouble = porcion.toDoubleOrNull() ?: 0.0
                            val cantidadInt = cantidad.toIntOrNull() ?: 0
                            if (idEditando == null) {
                                viewModel.insertarComp(nombre, porcionDouble, unidad, cantidadInt)
                            } else {
                                viewModel.ModificarComp(
                                    a1Componente(
                                        id = idEditando!!,
                                        nombre = nombre,
                                        porcion = porcionDouble,
                                        unidad = unidad,
                                        cantidad = cantidadInt
                                    )
                                )
                            }
                            nombre = ""
                            porcion = ""
                            unidad = ""
                            cantidad = ""
                            idEditando = null
                            expanded = false
                            errorMensaje = ""
                        }
                    },
                    text = if (idEditando == null) "Agregar" else "Actualizar"
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        lista.forEach { componente ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
                    .background(
                        when {
                            modoSeleccion && seleccionados.contains(componente.id) -> Color.Gray
                            else -> Color(0xFFB8E6B8)
                        }
                    )
                    .clickable(enabled = modoSeleccion) {
                        if (seleccionados.contains(componente.id)) {
                            seleccionados.remove(componente.id)
                        } else {
                            seleccionados.add(componente.id)
                        }
                    }
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (modoSeleccion) {
                    Checkbox(
                        checked = seleccionados.contains(componente.id),
                        onCheckedChange = {
                            if (it) seleccionados.add(componente.id)
                            else seleccionados.remove(componente.id)
                        }
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = componente.nombre, fontWeight = FontWeight.Bold)
                    Text(text = "${componente.porcion} ${componente.unidad}")
                    Text(text = "Cantidad: ${componente.cantidad}")
                }
                IconButton(onClick = {
                    idEditando = componente.id
                    nombre = componente.nombre
                    porcion = componente.porcion.toString()
                    unidad = componente.unidad
                    cantidad = componente.cantidad.toString()
                    expanded = true
                }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "Editar")
                }
                IconButton(onClick = {
                    viewModel.EliminarPorId(componente.id)
                }) {
                    Icon(Icons.Default.Close, contentDescription = "Borrar")
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        CustomButton(
            onClick = { modoSeleccion = !modoSeleccion; seleccionados.clear() },
            text = if (modoSeleccion) "Cancelar Selección" else "Modo Selección Múltiple",
            containerColor = Color.Red
        )

        if (modoSeleccion && seleccionados.isNotEmpty()) {
            CustomButton(
                onClick = {
                    seleccionados.forEach { id ->
                        viewModel.EliminarPorId(id)
                    }
                    seleccionados.clear()
                    modoSeleccion = false
                },
                text = "Eliminar ${seleccionados.size} seleccionados",
                containerColor = Color.Red
            )
        }
    }
}



@Preview(showBackground = true)
@Composable
fun ComponenteScreenPreview(){
    val viewModel: a5ComponenteViewModel = viewModel()
    CarritoScreen(viewModel)
}