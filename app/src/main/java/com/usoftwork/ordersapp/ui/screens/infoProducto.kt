package com.usoftwork.ordersapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp



@Composable
fun DetalleProductoScreen(
    nombre: String,
    descripcion: String,
    precio: Int,
) {
    // Estado para almacenar la cantidad seleccionada por el usuario
    var cantidad by remember { mutableIntStateOf(1) }
    var showBoleta by remember { mutableStateOf(false) } // Estado para controlar la visibilidad de la boleta

    // Calcular el valor neto, IVA y total
    val iva = 19
    val valorNeto = precio / (1 + iva / 100f)  // El valor sin IVA
    val ivaCalculado = precio - valorNeto  // El valor del IVA
    val total = precio * cantidad  // El total por la cantidad seleccionada

    // Mostrar la pantalla de detalles
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = nombre, style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Precio: $precio CLP", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = descripcion, style = MaterialTheme.typography.bodySmall)
        Spacer(modifier = Modifier.height(16.dp))

        // Input para la cantidad
        OutlinedTextField(
            value = cantidad.toString(),
            onValueChange = {
                cantidad = it.toIntOrNull() ?: 1  // Asegura que la cantidad sea un número válido
            },
            label = { Text("Cantidad") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Si showBoleta es true, mostrar el diálogo de la boleta
        if (showBoleta) {
            BoletaCompraDialog(
                nombre = nombre,
                cantidad = cantidad,
                precioUnitario = precio,
                valorNeto = valorNeto,
                iva = ivaCalculado,
                total = total,
                onDismiss = { showBoleta = false } // Cerrar el diálogo
            )
        }
    }
}


// Ventana de diálogo en la cual se genera la boleta de compra
@Composable
fun BoletaCompraDialog(
    nombre: String,
    cantidad: Int,
    precioUnitario: Int,
    valorNeto: Float,
    iva: Float,
    total: Int,
    onDismiss: () -> Unit
) {
    // Crear el diálogo
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Factura") },
        text = {
            Column(modifier = Modifier.padding(16.dp)) {
                // Mostrar la información de la boleta
                Text(text = "Descripción: $nombre")
                Spacer(modifier = Modifier.height(8.dp))

                // Mostrar la cantidad x precio y el subtotal
                Text(text = "Cantidad x Precio Subtotal: $cantidad x $precioUnitario = ${cantidad * precioUnitario}")
                Spacer(modifier = Modifier.height(8.dp))

                // Líneas separadoras
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                // Mostrar valor neto, IVA y total
                Text(text = "Valor Neto: $valorNeto CLP")
                Text(text = "IVA: $iva CLP")
                Text(text = "Total: $total CLP")

                // Línea separadora al final
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            }
        },
        confirmButton = {
            Button(
                // Cierra el diálogo cuando se presione "Aceptar"
                onClick = onDismiss
            ) {
                Text("Aceptar")
            }
        },
        modifier = Modifier.fillMaxWidth(0.9f) // Puedes ajustar el tamaño del diálogo
    )
}



