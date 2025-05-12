package com.usoftwork.ordersapp.ui.screens

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.usoftwork.ordersapp.R
import com.usoftwork.ordersapp.data.classes.CustomButton

@Composable
fun MenuBar(navController: NavHostController, scrollState: ScrollState) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(top = 32.dp),

        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Menú Principal",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        CustomButton(
            onClick = { navController.navigate("armar") },
            text = "Crear pedido",
            icon = Icons.Default.PlayArrow,
            modifier = Modifier.padding(vertical = 4.dp)
        )
        CustomButton(
            onClick = { navController.navigate("listado") },
            text = "Listado de pedidos",
            icon = Icons.AutoMirrored.Filled.List,
            modifier = Modifier.padding(vertical = 4.dp)
        )
        CustomButton(
            onClick = { navController.navigate("inventario") },
            text = "Mi Inventario",
            icon = Icons.Default.DateRange,
            modifier = Modifier.padding(vertical = 4.dp)
        )
        CustomButton(
            onClick = { navController.navigate("productos") },
            text = "Productos",
            icon = R.drawable.cubiertos,
            modifier = Modifier.padding(vertical = 4.dp)
        )
        CustomButton(
            onClick = { navController.navigate("analisis") },
            text = "Análisis de ventas",
            icon = R.drawable.analitic,
            modifier = Modifier.padding(vertical = 4.dp)
        )
    }
}
