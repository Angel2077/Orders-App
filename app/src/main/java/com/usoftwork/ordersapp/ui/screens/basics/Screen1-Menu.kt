package com.usoftwork.ordersapp.ui.screens.basics

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun TextComponent(text: String) {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Titulo(text, MaterialTheme.colorScheme.secondary)
    }
}

@Composable
fun Titulo(text: String, color: Color) {
    Text(
        text = text,
        color = color,
        style = MaterialTheme.typography.headlineLarge
    )
}

@Composable
fun Sub(text: String,color: Color){
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Text(text = text, color = color, style = MaterialTheme.typography.headlineMedium)
    }
}
@Composable
fun PreviewText() {
    MaterialTheme {
        TextComponent("INICIO")
    }
}

@Composable
fun Rectangulo(width: Dp = 200.dp, height: Dp = 50.dp) {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Box(
            modifier = Modifier
                .size(width, height)

                .background(color = Color(0xFFFB8B8B), RoundedCornerShape(20.dp))
        )
    }
}

@Preview(showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ScreenMenu() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                PreviewText()
                Spacer(Modifier.height(100.dp))
                Sub(
                    text = "Correo",
                    color = MaterialTheme.colorScheme.onBackground
                )
                Rectangulo()
                Spacer(Modifier.height(30.dp))
                Sub(
                    text = "Contraseña",
                    color = MaterialTheme.colorScheme.onBackground
                )
                Rectangulo()
                Spacer(Modifier.height(100.dp))
                Sub(
                    text = "¿Olvidaste tu contraseña?",
                    color = MaterialTheme.colorScheme.onBackground
                )
                Rectangulo()
                Spacer(Modifier.height(10.dp))
                Sub(
                    text = "¿No tienes una cuenta?",
                    color = MaterialTheme.colorScheme.onBackground
                )
                Rectangulo()
            }
        }
    }


