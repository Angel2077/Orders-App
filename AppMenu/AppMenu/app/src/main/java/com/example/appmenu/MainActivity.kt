package com.example.appmenu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.appmenu.ui.theme.AppMenuTheme
import com.example.appmenu.pantallas.Pantalla1
import com.example.appmenu.pantallas.Pantalla2
import com.example.appmenu.pantallas.Pantalla3
import com.example.appmenu.pantallas.Pantalla4
import com.example.appmenu.pantallas.Pantalla5

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppMenuTheme {
                val navController = rememberNavController()
                Scaffold(
                    bottomBar = { BottomNavigationBar(navController) }
                ) { padding ->
                    NavHost(navController = navController, startDestination = "almacen") {
                        composable("almacen") { Pantalla1() }
                        composable("producto") { Pantalla2() }
                        composable("pedido") { Pantalla3() }
                        composable("lista") { Pantalla4() }
                        composable("info") { Pantalla5() }
                    }
                }
            }
        }
    }
}

// Estructura para cada ítem del menú
data class MenuItem(val ruta: String, val icon: ImageVector, val label: String)

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        MenuItem("almacen", Icons.Default.ShoppingCart, "Almacén"),
        MenuItem("producto", Icons.Default.Restaurant, "Producto"),
        MenuItem("pedido", Icons.Default.Notifications, "Pedido"),
        MenuItem("lista", Icons.Default.List, "Lista"),
        MenuItem("info", Icons.Default.Info, "Info")
    )

    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.ruta,
                onClick = {
                    navController.navigate(item.ruta) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                },
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) }
            )
        }
    }
}