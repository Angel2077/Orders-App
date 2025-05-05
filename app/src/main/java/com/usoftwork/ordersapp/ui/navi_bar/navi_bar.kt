package com.usoftwork.ordersapp.ui.navi_bar

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier

val navItems = listOf(
    Triple(Icons.Default.Favorite,"Crear Platillo","create_dish"),
    Triple(Icons.Default.Info,"Analisis de venta","sales"),
    Triple(Icons.Default.DateRange,"Almacen","storage"),
    Triple(Icons.Default.List,"Lista Pedidos","orders"),
    Triple(Icons.Default.Create,"Tomar Pedido","take_order"),
)


@Composable
fun NavBar(
    isVisible: Boolean= true,
    selectedItem: String = "take_order",
    onItemClick: (String)-> Unit
) {
    if (isVisible) {
        NavigationBar(
            modifier = Modifier.fillMaxWidth(),
            containerColor = Color(0XFFFB8B8B)
        ) {
            navItems.forEach { (icon, text, id) ->
                NavigationBarItem(
                    icon = { Icon(icon, contentDescription = text) },
                    label = { Text(text) },
                    selected = selectedItem == id,
                    onClick = { onItemClick(id) }
                )
            }
        }
    }
}