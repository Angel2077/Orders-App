package com.example.creacinappui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ConfiguracionScreen() {
    var isDarkTheme by remember { mutableStateOf(true) }
    var notifyViaWhatsapp by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2C2C2C))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = android.R.drawable.ic_menu_manage),
            contentDescription = "Configuración",
            tint = Color.LightGray,
            modifier = Modifier
                .size(48.dp)
                .padding(bottom = 8.dp)
        )

        Text(
            text = "Configuración",
            fontSize = 28.sp,
            color = Color.LightGray,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Tema
        Box(
            modifier = Modifier
                .background(Color(0xFFFF8A8A), RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Tema", color = Color.White, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .background(Color.Black, CircleShape)
                            .clickable { isDarkTheme = true }
                    )
                    Spacer(modifier = Modifier.width(24.dp))
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .background(Color.White, CircleShape)
                            .clickable { isDarkTheme = false }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Notificar pedidos via Whatsapp
        Box(
            modifier = Modifier
                .background(Color(0xFFFF8A8A), RoundedCornerShape(16.dp))
                .padding(16.dp)
                .fillMaxWidth()
                .clickable { notifyViaWhatsapp = !notifyViaWhatsapp }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Notificar pedidos\nvia Whatsapp", color = Color.White, fontWeight = FontWeight.Bold)
                if (notifyViaWhatsapp) {
                    Icon(
                        painter = painterResource(id = android.R.drawable.checkbox_on_background),
                        contentDescription = "Activado",
                        tint = Color.Black
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Conexion con impresora
        Box(
            modifier = Modifier
                .background(Color(0xFFFF8A8A), RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Conexión con impresora", color = Color.White, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))
                Box(
                    modifier = Modifier
                        .background(Color.White, RoundedCornerShape(50))
                        .padding(horizontal = 24.dp, vertical = 8.dp)
                ) {
                    Text("Detección de impresora", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Bottom Navigation Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFF8A8A)),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(5) {
                Icon(
                    painter = painterResource(id = android.R.drawable.ic_menu_agenda),
                    contentDescription = "Menu Icon",
                    tint = Color.Black,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ConfiguracionScreenPreview() {
    ConfiguracionScreen()
}
