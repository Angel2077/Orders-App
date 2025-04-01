@file:Suppress("UNREACHABLE_CODE")

package com.usoftwork.ordersapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.usoftwork.ordersapp.ui.theme.DarkNavyBlue

// Clase para productos
data class Producto(
    val nombre: String,
    val precio: Int,
    val descripcion: String,
    val imagenId: Int,
    val cantidad: Int
)

// Clase para guardar las compras con sus productos
data class Compra(
    val nombre: String,
    val cantidad: Int,
    val precioUnitario: Int,
    val total: Int,
    val fecha: String,
    val id: Int
)


// Contador para generar IDs únicos de compra
var compraIdCounter = 1

// Lista para almacenar todas las compras realizadas
val listaDeCompras = mutableListOf<Compra>()


// Funcion para agregar productos de lista
fun getProductos(): List<Producto> {
    return listOf(
        Producto("Laptop Gamer", 1200000, "Laptop gaming con una rtx 3070 y tun ryzen 7 5700h.", R.drawable.designer, 1),
        Producto("Samsung A25 5G", 249000, "El mejor compañero para tu día a día.", R.drawable.designer, 1),
        Producto("Tablet", 500000, "Tablet para todas tus necesidades", R.drawable.designer, 1),
        Producto("Auriculares lenovo Bluetooth", 25000, "Sonido de alta calidad, bluetooth 5.3, diseño ergonómico", R.drawable.designer, 1),
        Producto("Monitor 144hz", 239000, "Pantalla de 27 pulgadas con resolución FHD a 144hz con display port", R.drawable.designer, 1)
    )
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuBar(navController: NavHostController) {
    var Buscador by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    Column(modifier = Modifier.fillMaxSize()) {
        CenterAlignedTopAppBar(
            title = { Text("Mi App") },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = DarkNavyBlue
            ),
            actions = {
                IconButton(
                    onClick = {
                        navController.navigate("misCompras")
                    }
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.designer),
                        contentDescription = "Mis Compras",
                        modifier = Modifier.size(24.dp)
                    )
                }

                OutlinedTextField(
                    value = Buscador,
                    onValueChange = { Buscador = it },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "buscador",
                            tint = Color.White
                        )
                    },
                    placeholder = { Text("Buscar...", color = Color.LightGray) },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = {
                        focusManager.clearFocus()
                    }),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
            }
        )


        Button(
            onClick = { navController.navigate("misCompras") },
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Mis Compras")
        }
    }
}

@Composable
fun Navigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "menu") {
        composable("menu") {
            MenuBar(navController = navController)
        }
        composable("detalle/{nombre}/{precio}/{descripcion}") { backStackEntry ->
            val nombre = backStackEntry.arguments?.getString("nombre") ?: ""
            val precio = backStackEntry.arguments?.getString("precio")?.toInt() ?: 0
            val descripcion = backStackEntry.arguments?.getString("descripcion") ?: ""
            DetalleProductoScreen(
                nombre = nombre, precio = precio, descripcion = descripcion,
                onRegistrarCompra = TODO()
            )
        }

    }
}








