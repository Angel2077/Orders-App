package com.usoftwork.ordersapp.ui.screens.useful

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun VistaFunciones() {
    var opciones by remember {
        mutableStateOf(
            listOf(
                OpcionProducto("Handroll base kanikama"),
                OpcionProducto("Sushi base Salmon")
            )
        )
    }
    var resumenExpandido by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PreviewText()
            Spacer(Modifier.height(100.dp))
            CategoryBar()
            Spacer(Modifier.height(20.dp))

            opciones.forEachIndexed { index, opcion ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        TextRectangle(
                            text = opcion.nombre,
                            modifier = Modifier
                                .width(300.dp)
                                .height(80.dp)
                                .clickable {
                                    opciones = opciones.toMutableList().also {
                                        it[index] = it[index].copy(expandido = !it[index].expandido, incluido = true)
                                    }
                                },
                            backgroundColor = MaterialTheme.colorScheme.secondary,
                            textColor = MaterialTheme.colorScheme.onSecondary
                        )
                        Checkbox(
                            checked = opcion.incluido,
                            onCheckedChange = {
                                opciones = opciones.toMutableList().also {
                                    it[index] = it[index].copy(incluido = !it[index].incluido)
                                }
                            }
                        )
                    }
                    if (opcion.expandido) {
                        Spacer(Modifier.height(10.dp))
                        repeat(2) { fila ->
                            var seleccionado by remember { mutableStateOf(-1) }
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(5.dp),
                                modifier = Modifier.padding(5.dp)
                            ) {
                                (1..3).forEach { i ->
                                    val actual = i - 1
                                    val isSelected = opcion.componentesSeleccionados.contains(actual + (fila * 3))
                                    val componente = actual + (fila * 3)
                                    Button(
                                        onClick = {
                                            opciones = opciones.toMutableList().also { lista ->
                                                val nuevosComponentes = lista[index].componentesSeleccionados.toMutableList()
                                                if (isSelected) {
                                                    nuevosComponentes.remove(componente)
                                                } else {
                                                    nuevosComponentes.removeAll { it / 3 == fila}
                                                   nuevosComponentes.add(componente)


                                                }
                                                lista[index] = lista[index].copy(componentesSeleccionados = nuevosComponentes)
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = if (isSelected)
                                                MaterialTheme.colorScheme.primary
                                            else
                                                MaterialTheme.colorScheme.secondary
                                        )
                                    ) {
                                        Text("Componente $i")
                                    }
                                }
                            }
                        }
                    }
                    Spacer(Modifier.height(10.dp))
                    TextRectangle(
                        text = "+",
                        modifier = Modifier.size(width = 350.dp, height = 30.dp),
                        backgroundColor = MaterialTheme.colorScheme.secondary,
                        textColor = MaterialTheme.colorScheme.onSecondary,
                        onClick = {
                            val nuevaLista = opciones.toMutableList()
                            nuevaLista.add(index + 1, opciones[index].copy())
                            opciones = nuevaLista
                        }
                    )
                    Spacer(Modifier.height(20.dp))
                }
            }

            Spacer(Modifier.height(50.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                (1..5).forEach {
                    Boton(text = it.toString(), onClick = {})
                }
            }
            Spacer(Modifier.height(100.dp))
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primaryContainer)
                .clickable { resumenExpandido = !resumenExpandido }
                .padding(16.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Resumen del Pedido", fontWeight = FontWeight.Bold)
                // Update the summary display
                if (resumenExpandido) {
                    opciones.filter { it.incluido }.forEach { opcion ->
                        Text("- ${opcion.nombre}", fontWeight = FontWeight.Bold)
                        opcion.componentesSeleccionados.sorted().forEach { componente ->
                            Text("    • Componente ${(componente % 3) + 1} de fila ${(componente / 3) + 1}",
                                modifier = Modifier.padding(start = 16.dp))
                        }
                    }
                }
            }
        }
    }
}

data class OpcionProducto(
    val nombre: String,
    val expandido: Boolean = false,
    val incluido: Boolean = false,
    val componentesSeleccionados: List<Int> = listOf()
)

@Composable
fun TextRectangle(
    text: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.primaryContainer,
    textColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    cornerRadius: Int = 8,
    onClick: (() -> Unit)? = null
) {
    Box(
        modifier = modifier
            .background(color = backgroundColor, shape = RoundedCornerShape(cornerRadius.dp))
            .padding(5.dp)
            .let {
                if (onClick != null) it.clickable(onClick = onClick) else it
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = textColor,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun Boton(
    text: String,
    textColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    onClick: () -> Unit
) {
    Button(onClick = onClick) {
        Text(
            text = text,
            color = textColor,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun CategoryBar() {
    Text("Categorías (Placeholder)")
}

@Composable
fun PreviewText() {
    TextComponent("Tomar Pedido")
}

@Composable
fun TextComponent(text: String) {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Text(
            text = text,
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.headlineLarge
        )
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun VistaFuncionesPreview() {
    MaterialTheme {
        VistaFunciones()
    }
}

