package com.example.dblocal._Producto

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.dblocal._Almacen.a1Componente


@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ProductFormDialog(
    product: Producto?,
    // Estas listas ahora reflejan los estados OBSERVADOS del ViewModel (currentBaseComponents / currentEligibleComponents)
    initialBaseComponents: List<a1Componente>,
    initialEligibleComponents: List<a1Componente>,
    onDismiss: () -> Unit,
    // CAMBIO CLAVE: onSave ahora solo recibe el Producto
    onSave: (Producto) -> Unit,
    onDelete: (Producto) -> Unit,
    allAvailableComponents: List<a1Componente>,
    // NUEVOS CALLBACKS: El diálogo los usará para notificar al ViewModel sobre los toggles
    onToggleBaseComponent: (a1Componente) -> Unit,
    onToggleEligibleComponent: (a1Componente) -> Unit
) {
    // Estados internos del diálogo para los campos de texto
    var productName by remember(product) { mutableStateOf(product?.nombre ?: "") }
    var productPrice by remember(product) { mutableStateOf(product?.precio?.toString() ?: "") }
    var productCategory by remember(product) { mutableStateOf(product?.categoria ?: productCategories.first()) }
    var productSize by remember(product) { mutableStateOf(product?.tamano ?: "") }
    var productMaxIngredients by remember(product) { mutableStateOf(product?.maximoIngredientes?.toString() ?: "0") }

    var showCategoryMenu by remember { mutableStateOf(false) }
    val productCategories = listOf("Sushi", "Handroll", "Comida Rápida", "Otros")
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (product == null) "Nuevo Producto" else "Editar Producto") },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) { // Añadir fillMaxWidth al Column para que los TextFields se expandan
                OutlinedTextField(
                    value = productName,
                    onValueChange = { productName = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = productPrice,
                    onValueChange = { productPrice = it },
                    label = { Text("Precio") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = productMaxIngredients,
                    onValueChange = { productMaxIngredients = it },
                    label = { Text("Máximo Ingredientes Elegibles") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = productSize,
                    onValueChange = { productSize = it },
                    label = { Text("Tamaño (Opcional)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Selector de categoría
                ExposedDropdownMenuBox(
                    expanded = showCategoryMenu,
                    onExpandedChange = { showCategoryMenu = !showCategoryMenu },
                    modifier = Modifier.fillMaxWidth() // Asegura que ocupe todo el ancho disponible
                ) {
                    OutlinedTextField(
                        value = productCategory,
                        onValueChange = { /* Este campo es solo de lectura, se cambia con la selección del menú */ },
                        readOnly = true, // Hace que el campo de texto sea solo de lectura
                        label = { Text("Categoría") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = showCategoryMenu) }, // Icono de flecha
                        modifier = Modifier
                            .menuAnchor() // Esto es CRUCIAL para que el menú se ancle al TextField
                            .fillMaxWidth()
                    )

                    // El menú desplegable real
                    ExposedDropdownMenu(
                        expanded = showCategoryMenu,
                        onDismissRequest = { showCategoryMenu = false }
                        // El modificador de ancho no suele ser necesario aquí si el TextField ya tiene fillMaxWidth
                    ) {
                        productCategories.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category) },
                                onClick = {
                                    productCategory = category
                                    showCategoryMenu = false // Cierra el menú después de seleccionar
                                }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                // --- SECCIÓN DE COMPONENTES BASE ---
                Text("Componentes Base:")
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    allAvailableComponents.forEach { componente ->
                        val isSelected = initialBaseComponents.contains(componente)
                        AssistChip(
                            // CAMBIO CLAVE: Llama al callback del ViewModel
                            onClick = { onToggleBaseComponent(componente) },
                            label = { Text(componente.nombre) },
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                labelColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                // --- SECCIÓN DE COMPONENTES ELEGIBLES ---
                Text("Componentes Elegibles:")
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    allAvailableComponents.forEach { componente ->
                        val isSelected = initialEligibleComponents.contains(componente)
                        AssistChip(
                            // CAMBIO CLAVE: Llama al callback del ViewModel
                            onClick = { onToggleEligibleComponent(componente) },
                            label = { Text(componente.nombre) },
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = if (isSelected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.surfaceVariant,
                                labelColor = if (isSelected) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                val price = productPrice.toIntOrNull() ?: 0
                val maxIng = productMaxIngredients.toIntOrNull() ?: 0
                val productToSave = product?.copy(
                    nombre = productName,
                    precio = price,
                    categoria = productCategory,
                    tamano = productSize.ifBlank { null },
                    maximoIngredientes = maxIng
                ) ?: Producto(
                    nombre = productName,
                    precio = price,
                    categoria = productCategory,
                    tamano = productSize.ifBlank { null },
                    maximoIngredientes = maxIng
                )
                // CAMBIO CLAVE: onSave ahora solo recibe el producto. El ViewModel obtiene los componentes.
                onSave(productToSave)
            }) {
                Text("Guardar")
            }
        },
        dismissButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Botón de eliminar (solo si es un producto existente)
                if (product != null) {
                    IconButton(onClick = { onDelete(product) }) {
                        Icon(Icons.Default.Delete, contentDescription = "Eliminar Producto")
                    }
                }
                TextButton(onClick = onDismiss) {
                    Text("Cancelar")
                }
            }
        }
    )
}