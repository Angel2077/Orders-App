//package com.example.dblocal._Producto

package com.example.db_local_menu._Producto

// Importaciones existentes (sin cambios aquí)
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.db_local_menu._Almacen.a5ComponenteViewModel
import com.example.db_local_menu._Almacen.a5ComponenteViewModelFactory


// --- Definiciones de Categorías ---
val productCategories = listOf("Sushi", "Handroll", "Comida Rápida", "Otros")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(
    productViewModel: ProductoViewModel = viewModel(
        factory = ProductoViewModelFactory(
            ProductoRepository(AppDatabase.getInstance(LocalContext.current).a2ProduCompDao())
        )
    ),
    componentViewModel: a5ComponenteViewModel = viewModel(
        factory = a5ComponenteViewModelFactory(
            AppDatabase.getInstance(LocalContext.current)
                .componenteDao()
        )
    ),
    onBackClick: () -> Boolean
) {
    val productsWithComponents by productViewModel.productos.observeAsState(emptyList())
    val allComponents by componentViewModel.listaComponentes.observeAsState(emptyList())

    var showProductFormDialog by remember { mutableStateOf(false) }
    // currentProductToEdit ya no es necesario, el ViewModel maneja la selección.
    // var currentProductToEdit by remember { mutableStateOf<Producto?>(null) } // <-- ELIMINAR ESTA LÍNEA

    var searchText by remember { mutableStateOf("") }
    var selectedCategoryFilter by remember { mutableStateOf<String?>(null) }

    val filteredProducts = productsWithComponents.filter { pWithC ->
        val product = pWithC.producto
        (product.nombre.contains(searchText, ignoreCase = true) || searchText.isBlank()) &&
                (selectedCategoryFilter == null || product.categoria == selectedCategoryFilter)
    }

    Scaffold(
        topBar = {
            Column(modifier = Modifier.fillMaxWidth()) {
                TopAppBar(title = { Text("Productos") })
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedTextField(
                        value = searchText,
                        onValueChange = { searchText = it },
                        label = { Text("Buscador") },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.LightGray.copy(alpha = 0.3f),
                            unfocusedContainerColor = Color.LightGray.copy(alpha = 0.1f),
                            disabledContainerColor = Color.LightGray.copy(alpha = 0.1f),
                        )
                    )
                    Spacer(Modifier.width(8.dp))
                    var showFilterMenu by remember { mutableStateOf(false) }
                    Box {
                        Button(
                            onClick = { showFilterMenu = true },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.DarkGray
                            )
                        ) {
                            Text(selectedCategoryFilter ?: "Filtro")
                        }
                        DropdownMenu(
                            expanded = showFilterMenu,
                            onDismissRequest = { showFilterMenu = false }
                        ) {
                            DropdownMenuItem(text = { Text("Todas") }, onClick = {
                                selectedCategoryFilter = null
                                showFilterMenu = false
                            })
                            productCategories.forEach { category ->
                                DropdownMenuItem(text = { Text(category) }, onClick = {
                                    selectedCategoryFilter = category
                                    showFilterMenu = false
                                })
                            }
                        }
                    }
                }
            }
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    // currentProductToEdit = null // <-- ELIMINAR ESTA LÍNEA (ya no es necesaria)
                    productViewModel.limpiarSeleccion() // Limpiar y preparar para nuevo producto
                    showProductFormDialog = true
                },
                modifier = Modifier.padding(bottom = 16.dp, end = 16.dp)
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add new product")
                Text("Nuevo Producto")
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filteredProducts) { productWithComponents ->
                ProductCard(
                    productWithComponents = productWithComponents,
                    onEditClick = {
                        productViewModel.seleccionarProducto(it.producto) // Cargar el producto en el VM
                        // currentProductToEdit = it.producto // <-- ELIMINAR ESTA LÍNEA (ya no es necesaria)
                        showProductFormDialog = true
                    }
                )
            }
        }
    }

    // Product Form Dialog
    if (showProductFormDialog) {
        val selectedProductForForm by productViewModel.productoSeleccionado.observeAsState()

        // --- CAMBIO CLAVE AQUÍ: Observar las nuevas LiveData que el ViewModel manipula en el formulario ---
        val currentBaseComponentsForForm by productViewModel.currentBaseComponents.observeAsState(emptySet())
        val currentEligibleComponentsForForm by productViewModel.currentEligibleComponents.observeAsState(emptySet())
        // --- FIN CAMBIOS CLAVE ---

        ProductFormDialog(
            product = selectedProductForForm,
            // Pasamos los sets mutables (convertidos a List si ProductFormDialog lo espera así)
            initialBaseComponents = currentBaseComponentsForForm.toList(),
            initialEligibleComponents = currentEligibleComponentsForForm.toList(),
            onDismiss = {
                showProductFormDialog = false
                productViewModel.limpiarSeleccion()
            },
            // --- CAMBIO CLAVE AQUÍ: onSave ahora solo pasa el producto, el VM ya tiene las listas ---
            onSave = { productToSave -> // Eliminamos baseComps y elegibleComps de los parámetros
                Log.d("ProductListScreen", "Guardando Producto: ${productToSave.nombre} (ID: ${productToSave.id})")
                productViewModel.guardarProductoConComponentes(productToSave) // Solo pasamos el producto
                showProductFormDialog = false
                productViewModel.limpiarSeleccion()
            },
            // --- FIN CAMBIO CLAVE ---
            onDelete = { productToDelete ->
                productViewModel.eliminarProducto(productToDelete)
                showProductFormDialog = false
                productViewModel.limpiarSeleccion()
            },
            allAvailableComponents = allComponents,
            // --- NUEVOS PARÁMETROS PARA MANEJAR EL TOGGLE EN EL FORMULARIO ---
            onToggleBaseComponent = { componente -> productViewModel.toggleComponenteAsBase(componente) },
            onToggleEligibleComponent = { componente -> productViewModel.toggleComponenteAsEligible(componente) }
            // --- FIN NUEVOS PARÁMETROS ---
        )
    }
}


//--------------------Creacion de producto-----------------------------------------------------------
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProductCard(
    productWithComponents: ProductoWithComponents, // Esto ahora contendrá List<ComponenteJoinData>
    onEditClick: (ProductoWithComponents) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    val product = productWithComponents.producto
    val allComponentsWithJoinData = productWithComponents.componentsWithJoinData // <-- Obtén la nueva lista de ComponenteJoinData

    // FILTRAR LOS COMPONENTES USANDO LA PROPIEDAD 'esBase' DE productoComponente
    val baseComponents = allComponentsWithJoinData
        .filter { it.productoComponente.esBase } // true para obligatorio (base)
        .map { it.componente } // Mapea de vuelta a List<a1Componente> para la visualización

    val eligibleComponents = allComponentsWithJoinData
        .filter { !it.productoComponente.esBase } // false para elegible
        .map { it.componente } // Mapea de vuelta a List<a1Componente> para la visualización

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded },
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFA07A)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = product.nombre, style = MaterialTheme.typography.titleLarge)
                    Text(text = product.categoria ?: "N/A", style = MaterialTheme.typography.bodyMedium)
                    Text(text = "$${product.precio}", style = MaterialTheme.typography.bodyMedium)
                    product.tamano?.let {
                        Text(text = "Tamaño: $it", style = MaterialTheme.typography.bodySmall)
                    }
                }
                IconButton(onClick = { onEditClick(productWithComponents) }) {
                    Icon(Icons.Filled.Settings, contentDescription = "Edit product")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Column {
                    // --- SECCIÓN PARA COMPONENTES BASE ---
                    if (baseComponents.isNotEmpty()) {
                        Text("Ingredientes Base:", style = MaterialTheme.typography.titleSmall)
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            baseComponents.forEach { comp -> // Esto ahora es List<a1Componente>
                                AssistChip(
                                    onClick = {},
                                    label = { Text(comp.nombre) },
                                    colors = AssistChipDefaults.assistChipColors(
                                        containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                                        labelColor = MaterialTheme.colorScheme.onPrimary
                                    )
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    // --- SECCIÓN PARA COMPONENTES ELEGIBLES ---
                    Text("Ingredientes Elegibles:", style = MaterialTheme.typography.titleSmall)
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        eligibleComponents.forEach { comp -> // Esto ahora es List<a1Componente>
                            AssistChip(
                                onClick = {},
                                label = { Text(comp.nombre) },
                                colors = AssistChipDefaults.assistChipColors(
                                    containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.8f),
                                    labelColor = MaterialTheme.colorScheme.onSecondary
                                )
                            )
                        }
                    }
                    if (eligibleComponents.size > product.maximoIngredientes) {
                        Text("Limitado a ${product.maximoIngredientes} para selección futura.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error)
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    tint = Color.Gray
                )
            }
        }
    }
}