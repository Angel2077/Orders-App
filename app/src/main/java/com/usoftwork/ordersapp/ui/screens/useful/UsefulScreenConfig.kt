package com.usoftwork.ordersapp.ui.screens.useful

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import com.usoftwork.ordersapp.data.classes.CustomButton
import com.usoftwork.ordersapp.data.classes.ExpandableButton

@Composable
fun ConfiguracionScreen() {
    var notifyPush by remember { mutableStateOf(true) }
    var notifySound by remember { mutableStateOf(false) }
    var notifyVibrate by remember { mutableStateOf(true) }

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

        // Notificaciones
        ExpandableButton(
            title = "Preferencias de notificaciones",
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFB8B8B), RoundedCornerShape(16.dp))
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFB8B8B)),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SwitchSetting("Notificaciones push", notifyPush) { notifyPush = it }
                SwitchSetting("Sonidos", notifySound) { notifySound = it }
                SwitchSetting("Vibración", notifyVibrate) { notifyVibrate = it }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Conexión impresora
        CustomButton(
            onClick = { /* acción para detección */ },
            text = "Detección de impresora",
            containerColor = Color(0xFFFF8A8A),
            contentColor = Color.White,
            cornerRadius = 16.dp,
            modifier = Modifier.fillMaxWidth()
        )
        }

    }


@Composable
fun SwitchSetting(text: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text, color = Color.White, fontWeight = FontWeight.Medium)
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ConfiguracionScreenPreview() {
    ConfiguracionScreen()
}
