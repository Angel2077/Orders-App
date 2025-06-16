//package com.example.dblocal._Almacen

package com.example.db_local_menu._Almacen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.db_local_menu._Producto.AppDatabase


@OptIn(ExperimentalMaterial3Api::class) // Anotación para TopAppBar y ExposedDropdownMenuBox
@Composable
fun ComponenteScreen(
    onBackClick: () -> Unit,
    viewModel: a5ComponenteViewModel = viewModel(
        factory = a5ComponenteViewModelFactory(
            AppDatabase.getInstance(LocalContext.current).componenteDao()
        )
    )
) {
    val allComponents by viewModel.listaComponentes.observeAsState(emptyList())

    var selectedComponent by remember { mutableStateOf<a1Componente?>(null) }
    var nombre by remember { mutableStateOf("") }
    var porcion by remember { mutableStateOf("") }

    // --- CAMBIOS AQUÍ para el campo Unidad ---
    val unitOptions = listOf("unidad", "kg", "Lts", "gr", "ml") // Opciones de unidades
    var unidad by remember { mutableStateOf(unitOptions[0]) } // Valor por defecto 'unidad'
    var isExpanded by remember { mutableStateOf(false) } // Estado para controlar si el desplegable está abierto
    // -----------------------------------------

    var cantidad by remember { mutableStateOf("") }

    var mostrarDialogoConfirmacion by remember { mutableStateOf(false) }
    var componenteAEliminar by remember { mutableStateOf<a1Componente?>(null) }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gestión de Componentes") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Formulario de entrada/edición de componentes
            TextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = porcion,
                onValueChange = { newValue ->
                    if (newValue.matches(Regex("^\\d*\\.?\\d*\$"))) {
                        porcion = newValue
                    }
                },
                label = { Text("Porción") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))

            // --- REEMPLAZO DEL TEXTFIELD DE UNIDAD POR EXPOSED DROPDOWN MENU ---
            ExposedDropdownMenuBox(
                expanded = isExpanded,
                onExpandedChange = { isExpanded = !isExpanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                TextField(
                    value = unidad,
                    onValueChange = {}, // El valor se cambia seleccionando del menú
                    readOnly = true, // No se puede escribir directamente en el TextField
                    label = { Text("Unidad") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) },
                    modifier = Modifier
                        .menuAnchor() // Importante para que el TextField actúe como ancla del menú
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = isExpanded,
                    onDismissRequest = { isExpanded = false }
                ) {
                    unitOptions.forEach { unit ->
                        DropdownMenuItem(
                            text = { Text(unit) },
                            onClick = {
                                unidad = unit
                                isExpanded = false
                            }
                        )
                    }
                }
            }
            // -------------------------------------------------------------------

            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = cantidad,
                onValueChange = { newValue ->
                    if (newValue.all { char -> char.isDigit() }) {
                        cantidad = newValue
                    }
                },
                label = { Text("Cantidad") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Botones de acción (Agregar/Modificar)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Button(
                    onClick = {
                        val parsedPorcion = porcion.toDoubleOrNull()
                        val parsedCantidad = cantidad.toIntOrNull()

                        if (nombre.isNotBlank() && parsedPorcion != null && unidad.isNotBlank() && parsedCantidad != null) {
                            if (selectedComponent == null) {
                                viewModel.insertarComp(
                                    nombre,
                                    parsedPorcion,
                                    unidad,
                                    parsedCantidad
                                )
                            } else {
                                val updatedComponent = selectedComponent!!.copy(
                                    nombre = nombre,
                                    porcion = parsedPorcion,
                                    unidad = unidad,
                                    cantidad = parsedCantidad
                                )
                                viewModel.ModificarComp(updatedComponent)
                            }
                            // Limpiar formulario y deseleccionar después de guardar
                            nombre = ""
                            porcion = ""
                            unidad = unitOptions[0] // Restablecer a la unidad por defecto
                            cantidad = ""
                            selectedComponent = null
                        } else {
                            // Mostrar mensaje de error si faltan datos
                        }
                    }
                ) {
                    Text(if (selectedComponent == null) "Agregar Componente" else "Modificar Componente")
                }

                if (selectedComponent != null) {
                    Button(onClick = {
                        nombre = ""
                        porcion = ""
                        unidad = unitOptions[0] // Restablecer a la unidad por defecto
                        cantidad = ""
                        selectedComponent = null
                    }) {
                        Text("Cancelar Edición")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Divider()
            Spacer(modifier = Modifier.height(16.dp))

            Text("Lista de Componentes", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(8.dp))

            // Lista de componentes
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(allComponents) { componente ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "Nombre: ${componente.nombre}")
                            Text(text = "Porción: ${componente.porcion}")
                            Text(text = "Unidad: ${componente.unidad}")
                            Text(text = "Cantidad: ${componente.cantidad}")
                        }
                        IconButton(onClick = {
                            selectedComponent = componente
                            nombre = componente.nombre
                            porcion = componente.porcion.toString()
                            unidad = componente.unidad // Cargar la unidad existente
                            cantidad = componente.cantidad.toString()
                        }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "Editar")
                        }

                        IconButton(onClick = {
                            mostrarDialogoConfirmacion = true
                            componenteAEliminar = componente
                        }) {
                            Icon(Icons.Default.Close, contentDescription = "Eliminar")
                        }
                    }
                    Divider()
                }
            }

            // Diálogo de confirmación para eliminar
            if (mostrarDialogoConfirmacion) {
                AlertDialog(
                    onDismissRequest = { mostrarDialogoConfirmacion = false },
                    title = { Text("Confirmar Eliminación") },
                    text = { Text("¿Estás seguro de eliminar el componente '${componenteAEliminar?.nombre}'?") },
                    confirmButton = {
                        TextButton(onClick = {
                            componenteAEliminar?.let { viewModel.EliminarComp(it) }
                            mostrarDialogoConfirmacion = false
                            componenteAEliminar = null
                        }) {
                            Text("Sí")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = {
                            mostrarDialogoConfirmacion = false
                            componenteAEliminar = null
                        }) {
                            Text("No")
                        }
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ComponenteScreenPreview() {
    MaterialTheme {
        ComponenteScreen(onBackClick = {})
    }
}
