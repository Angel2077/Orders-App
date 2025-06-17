@file:Suppress("UNCHECKED_CAST")

package com.example.dblocal.ui.screens.useful

import android.app.Application
import android.os.Build
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.dblocal.ui.ViewModel.LoginViewModel
import com.example.dblocal.R
import com.example.dblocal.Routes
import com.example.dblocal.data.classes.CustomButton
import com.example.dblocal.ui.theme.DarkNavyBlue
import com.example.dblocal.ui.theme.DarkRed
import com.example.dblocal.ui.theme.Dustwhite
import com.example.dblocal.ui.theme.LocalDarkTheme
import com.example.dblocal.ui.theme.White
import com.example.dblocal.ui.theme.grey

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LoginScreen(navController: NavHostController, scrollState: ScrollState) {
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val loginViewModel: LoginViewModel = viewModel (
        factory = LoginViewModelFactory(context.applicationContext as Application)
    )

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
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

class LoginViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LoginViewModel(application) as T
    }
}
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
        viewModel.validateLogin(username, password) { success ->
            if (success) {
                navController.navigate(Routes.MAIN_MENU_SCREEN)
            } else {
                onError("Credenciales incorrectas.")
            }
        }
    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    LoginScreen(navController = NavHostController(LocalContext.current), scrollState = rememberScrollState())
}


