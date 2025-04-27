@file:Suppress("UNREACHABLE_CODE")

package com.usoftwork.ordersapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuBar(navController: NavHostController) {
    Column(modifier = Modifier.fillMaxSize()) {
        // Barra superior con título
        CenterAlignedTopAppBar(
            title = { Text("Menú Principal") },
            actions = {
                // Icono del carrito de compras
                IconButton(
                    onClick = {
                        navController.navigate("carritoCompras") // Navegar al carrito de compras
                    }
                ) {
                }
            }
        )

        // Opciones del menú
        Column(modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = { navController.navigate("home") },
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Inicio")
            }
            Button(
                onClick = { navController.navigate("productos") }, // Cambio de `Navigation` a `navigate`
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Productos")
            }
            Button(
                onClick = { navController.navigate("perfil") }, // Cambio de `Navigation` a `navigate`
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Perfil")
            }
        }
    }
}



