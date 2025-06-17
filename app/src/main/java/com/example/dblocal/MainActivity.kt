package com.example.dblocal

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
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

import com.example.dblocal.data.classes.CustomButton
import com.example.dblocal.ui.order.OrderScreen
import com.example.dblocal.ui.screens.*
import com.example.dblocal.ui.screens.useful.LoginScreen
import com.example.dblocal.ui.theme.*
import com.example.dblocal.ui.theme.OrdersAppTheme

import conexiondb.DatabaseConnector


import kotlin.text.isEmpty
import kotlin.text.isNotEmpty

// *** Factories para ViewModels ***


@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OrdersAppTheme {
                val database = AppDatabase.getInstance(applicationContext)
                val productoDao = database.a2ProduCompDao()
                val componenteDao = database.componenteDao()
                val pedidoDao = database.pedidoDao()
                val orderViewModelFactory = OrderViewModelFactory(productoDao, PedidoRepository(pedidoDao))
                // Se pasa productoDao a la factory del ListadoPedidoViewModel por si ListadoPedidoRepository lo usara
                val listadoPedidoViewModelFactory = ListadoPedidoViewModelFactory(pedidoDao, componenteDao, productoDao)


                val navController = rememberNavController()
                val scrollState = rememberScrollState()
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {

                    NavHost(navController = navController, startDestination = Routes.LOGIN) {
                        composable(Routes.LOGIN) { LoginScreen(navController, scrollState) }
                        composable(Routes.REGISTER) { Register(navController, scrollState) }
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



//@RequiresApi(Build.VERSION_CODES.O)
//@Composable
//
//fun Login(navController: NavHostController, scrollState: ScrollState) {
//    val focusManager = LocalFocusManager.current
//    var correo by remember { mutableStateOf("") }
//    var contrasenna by remember { mutableStateOf("") }
//    var error by remember { mutableStateOf("") }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .verticalScroll(scrollState),
//                verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//
//    ) {
//        BoxWithConstraints {
//            val imageHeight = when {
//                maxWidth <= 200.dp -> 100.dp
//                maxWidth <= 350.dp -> 150.dp
//                maxWidth <= 500.dp -> 200.dp
//                else -> 300.dp
//            }
//            Image(
//                painter = painterResource(id = R.drawable.logo),
//                contentDescription = "Logo de la aplicación",
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(imageHeight)
//            )
//        }
//        OutlinedTextField(
//            value = correo,
//            onValueChange = { correo = it },
//            label = { Text("Ingrese su correo") },
//            singleLine = true,
//            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
//            keyboardActions = KeyboardActions(
//                onNext = { focusManager.moveFocus(FocusDirection.Down) }
//            )
//        )
//
//        OutlinedTextField(
//            value = contrasenna,
//            onValueChange = { contrasenna = it },
//            label = { Text("Ingrese Contraseña") },
//            singleLine = true,
//            visualTransformation = PasswordVisualTransformation(),
//            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
//            keyboardActions = KeyboardActions(
//                onDone = {
//                    focusManager.clearFocus()
//                    if (correo.isEmpty() || contrasenna.isEmpty()) {
//                        error = "Por favor, complete todos los campos."
//                    } else {
//                        validarCredencialesAsync(correo, contrasenna) { isValid ->
//                            if (isValid) {
//                                navController.navigate(Routes.HOME)
//                            } else {
//                                error = "Credenciales incorrectas."
//                            }
//                        }
//                    }
//                }
//            )
//        )
//
//        CustomButton(
//            text = "Ingresar",
//            contentColor = White,
//            modifier = Modifier.width(310.dp),
//            center = true,
//            onClick = {
//                if (correo.isEmpty() || contrasenna.isEmpty()) {
//                    error = "Por favor, complete todos los campos."
//                } else {
//                    validarCredencialesAsync(correo, contrasenna) { isValid ->
//                        if (isValid) {
//                            navController.navigate(Routes.HOME)
//                        } else {
//                            error = "Credenciales incorrectas."
//                        }
//                    }
//                }
//            },
//            containerColor = DarkRed // si quieres mantener ese color
//        )
//
//        Text(
//            text = "¿Eres nuevo? \n"  +
//                    "¡Registrate con nosotros!",
//            textAlign = TextAlign.Center,
//            color = if (LocalDarkTheme.current.value) Dustwhite else grey, // Cambia el color según el tema
//            fontSize  = 16.sp)
//        CustomButton(
//            text = "Registrarse",
//            contentColor = White,
//            modifier = Modifier.width(310.dp),
//            center = true,
//            onClick = { navController.navigate(Routes.REGISTER) },
//            containerColor = DarkNavyBlue
//        )
//        Text(
//            text = "¿Olvidó su contraseña?",
//            color = if (LocalDarkTheme.current.value) Dustwhite else grey, // Cambia el color según el tema
//            fontSize = 16.sp,
//            modifier = Modifier.clickable {
//                navController.navigate(Routes.REGISTER)
//            },
//        )
//
//    }
//}


@Composable
fun Register(navController: NavHostController, scrollState: ScrollState) {
    val focusManager = LocalFocusManager.current
    var currentSlide by remember { mutableIntStateOf(0) } // 0: planes, 1: formulario
    var correo by remember { mutableStateOf("") }
    var contrasenna by remember { mutableStateOf("") }
    var confirmarContrasenna by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(12.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (currentSlide == 0) {
            Text("Elige tu Plan", style = MaterialTheme.typography.headlineSmall)

            Card(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Plan de Prueba", style = MaterialTheme.typography.titleMedium)
                    Text("Ideal para comenzar a usar la app sin costo.")
                    Button(onClick = { currentSlide = 1 }) {
                        Text("Seleccionar Gratis")
                    }
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Plan Pro (Próximamente)", style = MaterialTheme.typography.titleMedium)
                    Text("Funciones avanzadas para empresas y equipos.")
                    Button(onClick = {}, enabled = false) {
                        Text("Próximamente")
                    }
                }
            }
            Text(
                text = "¿Ya tienes cuenta?",
                textAlign = TextAlign.Center,
                color = if (LocalDarkTheme.current.value) Dustwhite else grey, // Cambia el color según el tema
                fontSize = 16.sp)

            CustomButton(
                text = "Volver a Ingresar",
                contentColor = White,
                modifier = Modifier.width(310.dp),
                center = true,
                onClick = {
                    navController.navigate(Routes.LOGIN)
                          },
                containerColor = DarkNavyBlue // si quieres mantener ese color
            )


        } else {
            // FORMULARIO DE REGISTRO
            Image(
                modifier = Modifier.size(150.dp),
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo"
            )

            OutlinedTextField(
                value = correo,
                onValueChange = { correo = it },
                label = { Text("Correo") },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                )
            )

            OutlinedTextField(
                value = contrasenna,
                onValueChange = { contrasenna = it },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                )
            )

            OutlinedTextField(
                value = confirmarContrasenna,
                onValueChange = { confirmarContrasenna = it },
                label = { Text("Confirmar Contraseña") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        if (correo.isEmpty() || contrasenna.isEmpty() || confirmarContrasenna.isEmpty()) {
                            error = "Por favor, complete todos los campos."
                        } else if (contrasenna != confirmarContrasenna) {
                            error = "Las contraseñas no coinciden."
                        } else {
                            navController.navigate(Routes.MAIN_MENU_SCREEN)
                        }
                    }
                )
            )

            if (error.isNotEmpty()) {
                Text(error, color = Red)
            }

            CustomButton(
                text = "Registrar",
                contentColor = White,
                modifier = Modifier.width(310.dp),
                onClick = {
                    if (correo.isEmpty() || contrasenna.isEmpty() || confirmarContrasenna.isEmpty()) {
                        error = "Por favor, complete todos los campos."
                    } else if (contrasenna != confirmarContrasenna) {
                        error = "Las contraseñas no coinciden."
                    } else {
                        navController.navigate(Routes.MAIN_MENU_SCREEN)
                    }
                },
                containerColor = DarkRed
            )

            CustomButton(
                text = "Volver a Elegir Plan",
                contentColor = White,
                modifier = Modifier.width(310.dp),
                onClick = { currentSlide = 0 },
                containerColor = DarkNavyBlue
            )
        }
    }
}



fun validarCredencialesAsync(
    correo: String,
    contrasenna: String,
    onResult: (Boolean) -> Unit
) {
    Thread {
        val isValid = validarCredenciales(correo, contrasenna)
        Handler(Looper.getMainLooper()).post {
            onResult(isValid)
        }
    }.start()
}

fun validarCredenciales(correo: String, contrasenna: String): Boolean {
    val database = DatabaseConnector()
    /*
            integrar 2 nuevos campos:
            1) la ip/dns de la base de datos (si es que este puede cambiar al paso del tiempo)
            2) el nombre de la base de datos
         */
    val res = database.setConnection("", "test", correo, contrasenna)
    return res
}


object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val MAIN_MENU_SCREEN = "main_menu_screen"
    const val PRODUCT_LIST_SCREEN = "product_list_screen"
    const val COMPONENT_LIST_SCREEN = "component_list_screen"
    const val ORDER_SCREEN = "order_screen"
    const val LISTADO_PEDIDO_SCREEN = "listado_pedido_screen" // <-- ¡NUEVA RUTA AÑADIDA!
    const val ORDER_DETAIL_ARG = "pedidoId"
    const val ORDER_DETAIL_SCREEN = "order_detail_screen/{${ORDER_DETAIL_ARG}}" // <-- ¡NUEVA RUTA AÑADIDA!
}
