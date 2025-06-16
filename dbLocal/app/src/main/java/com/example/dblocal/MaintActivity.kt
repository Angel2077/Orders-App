package com.example.dblocal

// Importaciones necesarias para la navegación

// Importa tus pantallas (Composable Screens)

// ¡NUEVAS IMPORTACIONES PARA LISTADO DE PEDIDOS!

// Importa tus DAOs, Repositorios y ViewModels

// ¡NUEVAS IMPORTACIONES PARA LISTADO DE PEDIDOS!

// Para ViewModels con parámetros
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.dblocal._Almacen.ComponenteScreen
import com.example.dblocal._Almacen.a2ComponenteDao
import com.example.dblocal._Almacen.a5ComponenteViewModel
import com.example.dblocal._ListadoPedidos.ListadoPedidoScreen
import com.example.dblocal._ListadoPedidos.ListadoPedidoViewModel
import com.example.dblocal._ListadoPedidos.ListadoPedidoViewModelFactory
import com.example.dblocal._ListadoPedidos.PedidoDetalleScreen
import com.example.dblocal._Pedido.PedidoRepository
import com.example.dblocal._Pedido.a5OrderViewModel
import com.example.dblocal._Producto.AppDatabase
import com.example.dblocal._Producto.MainMenuScreen
import com.example.dblocal._Producto.ProductListScreen
import com.example.dblocal._Producto.ProductoRepository
import com.example.dblocal._Producto.ProductoViewModel
import com.example.dblocal._Producto.a2ProduCompDao
import com.example.dblocal.ui.order.OrderScreen
import com.example.dblocal.ui.theme.DblocalTheme


// *** Factories para ViewModels ***

class ProductViewModelFactory(private val productoDao: a2ProduCompDao) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductoViewModel::class.java)) {
            return ProductoViewModel(ProductoRepository(productoDao)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class a5ComponenteViewModelFactory(private val componenteDao: a2ComponenteDao) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(a5ComponenteViewModel::class.java)) {
            return a5ComponenteViewModel(componenteDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class OrderViewModelFactory(
    private val productoDao: a2ProduCompDao,
    private val pedidoRepository: PedidoRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(a5OrderViewModel::class.java)) {
            return a5OrderViewModel(productoDao, pedidoRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


// Define tus rutas de navegación como constantes (buena práctica)
// ¡ESTE ES EL OBJETO ROUTES ACTUALIZADO!
object Routes {
    const val MAIN_MENU_SCREEN = "main_menu_screen"
    const val PRODUCT_LIST_SCREEN = "product_list_screen"
    const val COMPONENT_LIST_SCREEN = "component_list_screen"
    const val ORDER_SCREEN = "order_screen"
    const val LISTADO_PEDIDO_SCREEN = "listado_pedido_screen" // <-- ¡NUEVA RUTA AÑADIDA!
    const val ORDER_DETAIL_ARG = "pedidoId"
    const val ORDER_DETAIL_SCREEN = "order_detail_screen/{${ORDER_DETAIL_ARG}}" // <-- ¡NUEVA RUTA AÑADIDA!
}


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = AppDatabase.getInstance(applicationContext)
        val productoDao = database.a2ProduCompDao()
        val componenteDao = database.componenteDao()
        val pedidoDao = database.pedidoDao()

        val orderViewModelFactory = OrderViewModelFactory(productoDao, PedidoRepository(pedidoDao))
        // Se pasa productoDao a la factory del ListadoPedidoViewModel por si ListadoPedidoRepository lo usara
        val listadoPedidoViewModelFactory = ListadoPedidoViewModelFactory(pedidoDao, componenteDao, productoDao)


        setContent {
            DblocalTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = Routes.MAIN_MENU_SCREEN) {

                        composable(Routes.MAIN_MENU_SCREEN) {
                            MainMenuScreen(
                                onNavigateToProducts = { navController.navigate(Routes.PRODUCT_LIST_SCREEN) },
                                onNavigateToComponents = { navController.navigate(Routes.COMPONENT_LIST_SCREEN) },
                                onNavigateToOrder = { navController.navigate(Routes.ORDER_SCREEN) },
                                onNavigateToListadoPedidos = { navController.navigate(Routes.LISTADO_PEDIDO_SCREEN) } // <-- ¡NUEVA NAVEGACIÓN AÑADIDA AQUÍ!
                            )
                        }

                        composable(Routes.PRODUCT_LIST_SCREEN) {
                            ProductListScreen(
                                navController = navController,
                                onBackClick = { navController.popBackStack() }
                            )
                        }

                        composable(Routes.COMPONENT_LIST_SCREEN) {
                            ComponenteScreen( // Asumo que este es el nombre correcto de tu pantalla de componentes
                                navController = navController,
                                onBackClick = { navController.popBackStack() }
                            )
                        }

                        composable(Routes.ORDER_SCREEN) {
                            val orderViewModel: a5OrderViewModel = viewModel(factory = orderViewModelFactory)
                            OrderScreen(
                                orderViewModel = orderViewModel,
                                navController = navController
                            )
                        }

                        // ¡NUEVA RUTA para Listado de Pedidos!
                        composable(Routes.LISTADO_PEDIDO_SCREEN) {
                            val listadoPedidoViewModel: ListadoPedidoViewModel = viewModel(factory = listadoPedidoViewModelFactory)
                            ListadoPedidoScreen(
                                listadoPedidoViewModel = listadoPedidoViewModel,
                                navController = navController
                            )
                        }

                        // ¡NUEVA RUTA para el Detalle de un Pedido!
                        composable(
                            route = Routes.ORDER_DETAIL_SCREEN,
                            arguments = listOf(navArgument(Routes.ORDER_DETAIL_ARG) { type = NavType.IntType })
                        ) { backStackEntry ->
                            val pedidoId = backStackEntry.arguments?.getInt(Routes.ORDER_DETAIL_ARG)
                            if (pedidoId != null) {
                                PedidoDetalleScreen(
                                    pedidoId = pedidoId,
                                    navController = navController,
                                    listadoPedidoViewModelFactory = listadoPedidoViewModelFactory
                                )
                            } else {
                                Text("Error: ID de Pedido no encontrado.")
                            }
                        }
                    }
                }
            }
        }
    }
}