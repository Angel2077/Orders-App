package com.example.db_local_menu._Producto // Paquete correcto

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun MainMenuScreen(
    onNavigateToProducts: () -> Unit,
    onNavigateToComponents: () -> Unit,
    onNavigateToInfo: () -> Unit // Solo la navegación a la pantalla de Info
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Menú Principal",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onNavigateToProducts,
            modifier = Modifier.fillMaxWidth(0.6f)
        ) {
            Text("Gestionar Productos")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onNavigateToComponents,
            modifier = Modifier.fillMaxWidth(0.6f)
        ) {
            Text("Gestionar Componentes")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onNavigateToInfo,
            modifier = Modifier.fillMaxWidth(0.6f)
        ) {
            Text("Estadísticas de Ventas")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainMenuScreenPreview() {
    MaterialTheme {
        MainMenuScreen(
            onNavigateToProducts = {},
            onNavigateToComponents = {},
            onNavigateToInfo = {}
        )
    }
}

