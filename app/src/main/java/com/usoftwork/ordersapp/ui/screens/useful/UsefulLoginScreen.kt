package com.usoftwork.ordersapp.ui.screens.useful
import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController

import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.*
import com.usoftwork.ordersapp.R
import com.usoftwork.ordersapp.Routes
import com.usoftwork.ordersapp.data.classes.CustomButton
import com.usoftwork.ordersapp.ui.theme.*
import io.ktor.client.call.body
import kotlinx.serialization.json.Json

// --- 1. Modelos de Datos ---

@Serializable
data class Usuario(
    val id: Int? = null,
    val name: String,
    val lastName: String,
    val email: String,
    val password: String = "",
    val role: Int = 1
)

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

@Serializable
data class LoginResponse(
    val token: String,
    val user: Usuario
)

@Serializable
data class MessageResponse(
    val message: String
)

// --- 2. Servicio API (AuthService) ---

// Reemplaza con la IP real de tu VM
const val BASE_URL = "http://34.176.244.119:80"

class AuthService {

    private val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    android.util.Log.d("KtorClient", message)
                }
            }
            level = LogLevel.ALL
        }
    }

    suspend fun login(request: LoginRequest): Result<LoginResponse> {
        return try {
            val response = client.post("$BASE_URL/login-mobile") {
                contentType(ContentType.Application.Json)
                setBody(request) }
            if (response.status.isSuccess()) {
                Result.success(response.body<LoginResponse>())
            } else {
                val errorBody = response.body<MessageResponse>()
                Result.failure(Exception(errorBody.message ?: "Error condescension en el login"))
            }
        } catch (e: Exception) {
            android.util.Log.e("AuthService", "Error en la petición de login: ${e.message}", e)
            Result.failure(Exception("Error de conexión o de red: ${e.message}"))
        }
    }
}

// --- 3. ViewModel (LoginViewModel) ---

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val authService = AuthService()

    fun validateLogin(email: String, password: String, onResult: (Boolean) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                Log.d("AuthService", "Enviando login con email: $email, password: $password")
                authService.login(LoginRequest(email, password))
            }

            result.onSuccess { loginResponse ->
                Log.d("LoginViewModel", "Login exitoso. Token: ${loginResponse.token}, User: ${loginResponse.user.email}")
                val sharedPrefs = getApplication<Application>().getSharedPreferences("auth_prefs", 0)
                sharedPrefs.edit().putString("auth_token", loginResponse.token).apply()
                sharedPrefs.edit().putInt("user_id", loginResponse.user.id ?: -1).apply()
                sharedPrefs.edit().putInt("user_role", loginResponse.user.role).apply()
                onResult(true)
            }.onFailure { exception ->
                Log.e("LoginViewModel", "Fallo en el login: ${exception.message}", exception)
                onError(exception.message ?: "Error desconocido")
                onResult(false)
            }
        }
    }

    fun getAuthToken(): String? {
        val sharedPrefs = getApplication<Application>().getSharedPreferences("auth_prefs", 0)
        return sharedPrefs.getString("auth_token", null)
    }

    fun getUserRole(): Int? {
        val sharedPrefs = getApplication<Application>().getSharedPreferences("auth_prefs", 0)
        val role = sharedPrefs.getInt("user_role", -1)
        return if (role != -1) role else null
    }

    fun getUserId(): Int? {
        val sharedPrefs = getApplication<Application>().getSharedPreferences("auth_prefs", 0)
        val id = sharedPrefs.getInt("user_id", -1)
        return if (id != -1) id else null
    }

    fun logout() {
        val sharedPrefs = getApplication<Application>().getSharedPreferences("auth_prefs", 0)
        sharedPrefs.edit().clear().apply()
        Log.d("LoginViewModel", "Sesión cerrada, token eliminado.")
    }
}

// Factory para el ViewModel (se sigue necesitando si lo creas manualmente)
class LoginViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


// --- 4. Composable LoginScreen y Funciones Auxiliares ---

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LoginScreen(navController: NavHostController, scrollState: ScrollState) {
    val focusManager = LocalFocusManager.current
    val currentScrollState = rememberScrollState() // Renombré para evitar conflicto con el parámetro
    val context = LocalContext.current
    val loginViewModel: LoginViewModel = androidx.lifecycle.viewmodel.compose.viewModel ( // Uso el ViewModel Compose helper
        factory = LoginViewModelFactory(context.applicationContext as Application)
    )

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(currentScrollState), // Usar el scrollState localmente creado
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo de la aplicación",
            modifier = Modifier.size(200.dp)
        )

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Ingrese su usuario") },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Ingrese contraseña") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    attemptLogin(username, password, loginViewModel, navController, onError = {
                        errorMessage = it
                    })
                }
            )
        )

        if (errorMessage.isNotEmpty()) {
            Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(16.dp))

        CustomButton(
            text = "Ingresar",
            contentColor = White,
            modifier = Modifier.width(310.dp),
            center = true,
            onClick = {
                attemptLogin(username, password, loginViewModel, navController, onError = {
                    errorMessage = it
                })
            },
            containerColor = DarkRed
        )

        Text(
            text = "¿Eres nuevo?\n¡Registrate con nosotros!",
            textAlign = TextAlign.Center,
            color = if (LocalDarkTheme.current.value) Dustwhite else grey,
            fontSize = 16.sp
        )

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
            color = if (LocalDarkTheme.current.value) Dustwhite else grey,
            fontSize = 16.sp,
            modifier = Modifier.clickable {
                navController.navigate(Routes.REGISTER)
            },
        )
    }
}

// Función auxiliar para el intento de login (mantenerla privada si no se usa fuera)
private fun attemptLogin(
    username: String,
    password: String,
    viewModel: LoginViewModel,
    navController: NavHostController,
    onError: (String) -> Unit
) {
    if (username.isBlank() || password.isBlank()) {
        onError("Por favor, complete todos los campos.")
    } else {
        viewModel.validateLogin(username, password, onResult = { success ->
            if (success) {
                navController.navigate(Routes.HOME) {
                    popUpTo(Routes.LOGIN) { inclusive = true } // Limpia el back stack
                }
            } else {
                // El onError ya se propaga desde el ViewModel, así que no es necesario manejarlo aquí de nuevo
            }
        }, onError = onError) // Pasa el callback onError al ViewModel
    }
}