package com.example.db_local_menu._Producto // Paquete correcto

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

// Importaciones de tus pantallas y paquetes
import com.example.db_local_menu._Almacen.ComponenteScreen
import com.example.db_local_menu._Producto.MainMenuScreen
import com.example.db_local_menu._Producto.ProductListScreen

// Importación para la pantalla de Info (¡del paquete Info directo!)
import com.example.db_local_menu.Info.i4InfoScreen


// Define tus rutas de navegación como constantes
object Routes {
    const val MAIN_MENU_SCREEN = "main_menu_screen"
    const val PRODUCT_LIST_SCREEN = "product_list_screen"
    const val COMPONENT_LIST_SCREEN = "component_list_screen"
    const val INFO_SCREEN = "info_screen" // Ruta para la pantalla de Info
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = Routes.MAIN_MENU_SCREEN) {

                        composable(Routes.MAIN_MENU_SCREEN) {
                            MainMenuScreen(
                                onNavigateToProducts = { navController.navigate(Routes.PRODUCT_LIST_SCREEN) },
                                onNavigateToComponents = { navController.navigate(Routes.COMPONENT_LIST_SCREEN) },
                                onNavigateToInfo = { navController.navigate(Routes.INFO_SCREEN) } // Navegación a InfoScreen
                            )
                        }

                        composable(Routes.PRODUCT_LIST_SCREEN) {
                            ProductListScreen(
                                onBackClick = { navController.popBackStack() }
                            )
                        }

                        composable(Routes.COMPONENT_LIST_SCREEN) {
                            ComponenteScreen(
                                onBackClick = { navController.popBackStack() }
                            )
                        }

                        composable(Routes.INFO_SCREEN) {
                            Log.d("MainActivity", "Navegando a INFO_SCREEN")
                            i4InfoScreen(onBackClick = { navController.popBackStack() })
                        }
                    }
                }
            }
        }
    }
}
