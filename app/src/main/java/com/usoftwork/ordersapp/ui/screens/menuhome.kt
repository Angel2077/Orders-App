@file:Suppress("UNREACHABLE_CODE")

package com.usoftwork.ordersapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.usoftwork.ordersapp.R
import com.usoftwork.ordersapp.ui.theme.Dustwhite
import com.usoftwork.ordersapp.ui.theme.Salmon

@Composable
fun MenuButton(
    onClick: () -> Unit,
    text: String,
    icon: Any
) {
    Button(
        onClick = onClick,
        modifier = Modifier.padding(16.dp),
        shape = RoundedCornerShape(6.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Salmon,
            contentColor = Dustwhite
        )
    ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = text,
                    fontSize = 20.sp,
                )
                if (icon is ImageVector) {
                    Icon(
                        imageVector = icon,
                        contentDescription = text,
                        modifier = Modifier
                            .size(60.dp)
                    )
                } else if (icon is Int) {
                    Icon(
                        painter = androidx.compose.ui.res.painterResource(id = icon),
                        contentDescription = text,
                        modifier = Modifier
                            .size(60.dp)
                    )
                }
            }
        }
    }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuBar(navController: NavHostController) {
    Column(modifier = Modifier.fillMaxSize()) {
        // Barra superior con título
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = "Menú Principal",
                    fontSize = 45.sp,  // Tamaño de la fuente
                    fontWeight = FontWeight.Bold  // tipo de fuente
                )
            },            actions = { {
                }
            }
        )

        // Opciones del menú
        Column(modifier = Modifier.fillMaxWidth()) {
            MenuButton(
                onClick = {navController.navigate("Armar")},
                text = "Crear pedido",
                icon = Icons.Default.PlayArrow
            )
            MenuButton(
                onClick = {navController.navigate("Listar")},
                text = "Listado de pedidos",
                icon = Icons.Default.List
            )
            MenuButton(
                onClick = {navController.navigate("Inventario")},
                text = "Mi Inventario",
                icon = Icons.Default.DateRange
            )
            MenuButton(
                onClick = {navController.navigate("Producto")},
                text = "Productos",
                icon = R.drawable.cubiertos

            )
            MenuButton(
                onClick = {navController.navigate("Analisis")},
                text = "Analisis de ventas",
                icon = R.drawable.analitic
            )
        }
    }
}



