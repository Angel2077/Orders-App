package com.usoftwork.ordersapp.ui.screens.basics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.usoftwork.ordersapp.ui.navi_bar.NavBar

@Composable
fun SalesAnalysisScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2B2B2B))
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Settings, // Puedes cambiar por un ícono más adecuado
                contentDescription = "Settings",
                tint = Color.LightGray
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Analisis de venta",
                color = Color.LightGray,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFD3D3D3))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .background(Color.White)
                ) {
                    // Aquí colocarías un gráfico real, usando por ejemplo un Canvas o una librería de gráficos.
                    Text(
                        text = "[Gráfico]",
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {},
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD3D3D3))
                ) {
                    Text("Day", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFD3D3D3))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Top 3 in this",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF8080))
                ) {
                    Text("Day", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFD3D3D3))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Dishes filter",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {},
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF8080))
                ) {
                    Text("Dishes", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}



@Preview(showBackground = true, showSystemUi = true)
@Composable
    fun PaginaSales(){
        Column {
            val naviItems = listOf("create_dish","sales","storage","orders" ,"take_order")
            Scaffold(
                bottomBar = {
                    NavBar(
                        isVisible = true,
                        selectedItem = naviItems[1],
                        onItemClick = {} // Use the correct index for "take_order"
                    )
                }
            ){padding ->
                SalesAnalysisScreen()
            }
        }
    }



