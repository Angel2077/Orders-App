package com.usoftwork.ordersapp

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.usoftwork.ordersapp.ui.theme.DarkNavyBlue
import com.usoftwork.ordersapp.ui.theme.DarkRed
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainApp()
        }
    }
}

@Composable
fun MainApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.LOGIN) {
        composable(Routes.LOGIN) { Login(navController) }
        composable(Routes.REGISTER) { Register(navController) }
        composable(Routes.HOME) { MenuBar(navController) }
        composable(Routes.DETALLE) { backStackEntry ->
            val nombre = backStackEntry.arguments?.getString("nombre") ?: ""
            val precio = backStackEntry.arguments?.getString("precio")?.toIntOrNull() ?: 0
            val descripcion = backStackEntry.arguments?.getString("descripcion") ?: ""
            DetalleProductoScreen(
                nombre = nombre,
                precio = precio,
                descripcion = descripcion,
                onRegistrarCompra = { /* Lógica para registrar compra */ }
            )
        }
        composable(Routes.AGREGAR_PRODUCTO) { AgregarProductoScreen(navController = navController) }
    }
}

@Composable
fun Login(navController: NavHostController) {
    val focusManager = LocalFocusManager.current
    var Correo by remember { mutableStateOf("") }
    var contrasenna by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.designer),
            contentDescription = "Logo de la aplicación"
        )


        OutlinedTextField(
            value = Correo,
            onValueChange = { Correo = it },
            label = { Text("Ingrese correo") },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )
        )

        OutlinedTextField(
            value = contrasenna,
            onValueChange = { contrasenna = it },
            label = { Text("Ingrese Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    if (Correo.isEmpty() || contrasenna.isEmpty()) {
                        error = "Por favor, complete todos los campos."
                    } else {
                        validarCredencialesAsync(Correo, contrasenna) { isValid ->
                            if (isValid) {
                                navController.navigate(Routes.HOME)
                            } else {
                                error = "Credenciales incorrectas."
                            }
                        }
                    }
                }
            )
        )

        Button(
            colors = ButtonDefaults.buttonColors(DarkRed),
            onClick = {
                if (Correo.isEmpty() || contrasenna.isEmpty()) {
                    error = "Por favor, complete todos los campos."
                } else {
                    validarCredencialesAsync( Correo, contrasenna) { isValid ->
                        if (isValid) {
                            navController.navigate(Routes.HOME)
                        } else {
                            error = "Credenciales incorrectas."
                        }
                    }
                }
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = "Ingresar")
        }

        Button(
            onClick = { navController.navigate(Routes.REGISTER) },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Registrarse")
        }

        if (error.isNotEmpty()) {
            Text(text = error, color = Color.Red, modifier = Modifier.padding(top = 8.dp))
        }
    }
}

@Composable
fun Register(navController: NavHostController) {
    val focusManager = LocalFocusManager.current
    var Correo by remember { mutableStateOf("") }
    var contrasenna by remember { mutableStateOf("") }
    var confirmarContrasenna by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.designer),
            contentDescription = "Logo de la aplicación"
        )


        OutlinedTextField(
            value = Correo,
            onValueChange = { Correo = it },
            label = { Text("Ingrese Nombre") },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )
        )

        OutlinedTextField(
            value = contrasenna,
            onValueChange = { contrasenna = it },
            label = { Text("Ingrese Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )
        )

        OutlinedTextField(
            value = confirmarContrasenna,
            onValueChange = { confirmarContrasenna = it },
            label = { Text("Confirme Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    if ( Correo.isEmpty() || contrasenna.isEmpty() || confirmarContrasenna.isEmpty()) {
                        error = "Por favor, complete todos los campos."
                    } else if (contrasenna != confirmarContrasenna) {
                        error = "Las contraseñas no coinciden."
                    } else {
                        // Aquí puedes agregar la lógica para registrar al usuario
                        navController.navigate(Routes.HOME) // Cambiar ruta según sea necesario
                    }
                }
            )
        )

        Button(
            colors = ButtonDefaults.buttonColors(DarkNavyBlue),
            onClick = {
                if ( Correo.isEmpty() || contrasenna.isEmpty() || confirmarContrasenna.isEmpty()) {
                    error = "Por favor, complete todos los campos."
                } else if (contrasenna != confirmarContrasenna) {
                    error = "Las contraseñas no coinciden."
                } else {
                    // Aquí puedes agregar la lógica para registrar al usuario
                    navController.navigate(Routes.HOME) // Cambiar ruta según sea necesario
                }
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = "Registrar")
        }

        Button(
            onClick = { navController.navigate(Routes.LOGIN) },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Volver a Iniciar Sesión")
        }

        if (error.isNotEmpty()) {
            Text(text = error, color = Color.Red, modifier = Modifier.padding(top = 8.dp))
        }
    }
}





fun validarCredencialesAsync(
    Correo: String,
    contrasenna: String,
    onResult: (Boolean) -> Unit
) {
    Thread {
        val isValid = validarCredenciales(Correo, contrasenna)
        Handler(Looper.getMainLooper()).post {
            onResult(isValid)
        }
    }.start()
}


fun validarCredenciales(Correo: String, contrasenna: String): Boolean {
    var connection: Connection? = null
    return try {
        connection = MySQLConnector.getConnection()
        val query = "SELECT * FROM usuarios WHERE rut = ? AND nombre = ? AND contrasenna = ?"
        val preparedStatement = connection.prepareStatement(query)
        preparedStatement.setString(1, Correo)
        preparedStatement.setString(2, contrasenna)

        val resultSet = preparedStatement.executeQuery()
        resultSet.next()
    } catch (e: SQLException) {
        e.printStackTrace()
        false
    } finally {
        connection?.close()
    }
}

object MySQLConnector {
    private const val JDBC_URL = "jdbc:mysql://dbucm.cl:3306/cdb105948/proyecto"
    private const val USER = "cdb105948_sebastian"
    private const val ALT_USER = "cdb105948_estudiantes"
    private const val PASS = "contrasena2024"

    init {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver")
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
    }

    @Throws(SQLException::class)
    fun getConnection(useAltUser: Boolean = false): Connection {
        val user = if (useAltUser) ALT_USER else USER
        return DriverManager.getConnection(JDBC_URL, user, PASS)
    }
}

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
    const val DETALLE = "detalle/{nombre}/{precio}/{descripcion}"
    const val AGREGAR_PRODUCTO = "Agrega"
}
