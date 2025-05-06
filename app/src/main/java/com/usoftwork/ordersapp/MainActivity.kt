package com.usoftwork.ordersapp
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.usoftwork.ordersapp.data.classes.CreatePedido
import com.usoftwork.ordersapp.data.classes.CustomButton
import com.usoftwork.ordersapp.data.classes.ListadoPedido
import com.usoftwork.ordersapp.data.functions.SalesAnalysis
import com.usoftwork.ordersapp.ui.screens.*
import com.usoftwork.ordersapp.ui.theme.*
import com.usoftwork.ordersapp.ui.theme.OrdersAppTheme
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.time.format.TextStyle


@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OrdersAppTheme {
                val navController = rememberNavController()
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { },
                            actions = {
                                DarkModeButton()
                            }
                        )
                    }
                ) { innerPadding ->  // <-- Necesitas capturar el padding
                    NavHost(
                        navController = navController,
                        startDestination = Routes.LOGIN,
                        modifier = Modifier.padding(innerPadding) // <-- Usar el padding para evitar solapamiento
                    ) {
                        composable(Routes.LOGIN) { Login(navController) }
                        composable(Routes.REGISTER) { Register(navController) }
                        composable(Routes.HOME) { MenuBar(navController) }
                        composable(Routes.ARMAR) { CreatePedido() }
                        composable(Routes.LISTAR) { ListadoPedido() }
                        composable(Routes.ANALYSIS) { SalesAnalysis(navController) }

                    }
                }
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
    fun Login(navController: NavHostController) {
    val focusManager = LocalFocusManager.current
    var correo by remember { mutableStateOf("") }
    var contrasenna by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.size(200.dp),
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo de la aplicación"
        )
        OutlinedTextField(
            value = correo,
            onValueChange = { correo = it },
            label = { Text("Ingrese su correo") },
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
                    if (correo.isEmpty() || contrasenna.isEmpty()) {
                        error = "Por favor, complete todos los campos."
                    } else {
                        validarCredencialesAsync(correo, contrasenna) { isValid ->
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

        CustomButton(
            text = "Ingresar",
            contentColor = White,
            modifier = Modifier.width(310.dp),
            center = true,
            onClick = {
                if (correo.isEmpty() || contrasenna.isEmpty()) {
                    error = "Por favor, complete todos los campos."
                } else {
                    validarCredencialesAsync(correo, contrasenna) { isValid ->
                        if (isValid) {
                            navController.navigate(Routes.HOME)
                        } else {
                            error = "Credenciales incorrectas."
                        }
                    }
                }
            },
            containerColor = DarkRed // si quieres mantener ese color
        )
        Text(
            text = "O",
            color = grey,
            fontSize  = 10.sp)
        CustomButton(
            text = "Registrarse",
            contentColor = White,
            modifier = Modifier.width(310.dp),
            center = true,
            onClick = { navController.navigate(Routes.REGISTER) },
            containerColor = DarkNavyBlue
        )
        Text(
            text = "¿Olvidó su contraseña?",
            color = grey,
            fontSize = 16.sp,
            modifier = Modifier.clickable {

                navController.navigate(Routes.REGISTER)
            },
        )

    }
}


    @Composable
    fun Register(navController: NavHostController) {
        val focusManager = LocalFocusManager.current
        var correo by remember { mutableStateOf("") }
        var contrasenna by remember { mutableStateOf("") }
        var confirmarContrasenna by remember { mutableStateOf("") }
        var error by remember { mutableStateOf("") }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier.size(200.dp),
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo de la aplicación"
            )


            OutlinedTextField(
                value = correo,
                onValueChange = { correo = it },
                label = { Text("Ingrese su Correo") },
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
                        if (correo.isEmpty() || contrasenna.isEmpty() || confirmarContrasenna.isEmpty()) {
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
                        navController.navigate(Routes.HOME)
                    }
                },
                containerColor = DarkRed
            )
            CustomButton(
                text = "Volver a Iniciar Sesión",
                contentColor = White,
                modifier = Modifier.width(310.dp),
                onClick = { navController.navigate(Routes.LOGIN) },
                containerColor = DarkNavyBlue
            )
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
        var connection: Connection? = null
        return try {
            connection = MySQLConnector.getConnection()
            val query = "SELECT * FROM usuarios WHERE correo = ? AND contrasenna = ?"
            val preparedStatement = connection.prepareStatement(query)
            preparedStatement.setString(1, correo)
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
        const val ARMAR = "armar"
        const val LISTAR = "listar"
        const val ANALYSIS = "analizar"
    }

