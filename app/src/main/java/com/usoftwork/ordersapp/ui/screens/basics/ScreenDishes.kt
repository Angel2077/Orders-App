package com.usoftwork.ordersapp.ui.screens.basics

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.usoftwork.ordersapp.ui.navi_bar.NavBar
import com.usoftwork.ordersapp.ui.theme.*

@Composable
fun ProductosScreen(padding: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1C1C1C))
            .padding(padding) // usa padding del Scaffold
            .padding(16.dp)   // padding interno adicional
    ){
        Text(
            text = "Productos",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.LightGray
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            TextField(
                value = "",
                onValueChange = {},
                placeholder = { Text("Buscador") },
                modifier = Modifier.weight(1f),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.LightGray,
                    focusedContainerColor = Color.LightGray,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = MaterialTheme.shapes.medium,
                maxLines = 1,
                singleLine = true
            )
            Button(onClick = {}, colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)) {
                Icon(Icons.Filled.Info, contentDescription = "Filtro")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        ProductoCard(title = "Sushi base Salmon", isFirst = true)
        Spacer(modifier = Modifier.height(8.dp))
        ProductoCard(title = "Handroll kanikama")

        Spacer(modifier = Modifier.height(16.dp))

        Slider(value = 0.5f, onValueChange = {}, modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {},
            colors = ButtonDefaults.buttonColors(containerColor = Salmon),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Delete, contentDescription = "Eliminar")
        }
    }
}

@Composable
fun ProductoCard(title: String, isFirst: Boolean = false) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Salmon, shape = MaterialTheme.shapes.medium)
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Icon(Icons.Default.Settings, contentDescription = "Config", tint = Color.Black)
        }

        if (isFirst) {
            Spacer(modifier = Modifier.height(8.dp))
            Text("Ingrediente elegible 1", color = Color.White, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                repeat(3) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(Color.LightGray, shape = MaterialTheme.shapes.small)
                            .padding(4.dp)
                            .padding(end = 8.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text("↑", fontSize = 18.sp, color = Color.White, modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            Spacer(modifier = Modifier.height(8.dp))
            Text("↓", fontSize = 18.sp, color = Color.White, modifier = Modifier.align(Alignment.CenterHorizontally))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewProductos() {
    Column {
        val naviItems = listOf("create_dish","sales","storage","orders" ,"take_order")
        Scaffold(
            bottomBar = {
                NavBar(
                    isVisible = true,
                    selectedItem = naviItems[0],
                    onItemClick = {} // Use the correct index for "take_order"
                )
            }
        ){ padding ->
            ProductosScreen(padding = padding) // pasa el padding aquí
        }
    }
}
