package com.example.dblocal._Ejemplo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun EntidadScreen(viewModel: a5ViewModel = viewModel()) {
    val nombre = remember { mutableStateOf("") }
    val edad = remember { mutableStateOf("") }
    val entidades by viewModel.listaEntidades.collectAsStateWithLifecycle(initialValue = emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Agregar nueva entidad", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = nombre.value,
            onValueChange = { nombre.value = it },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(4.dp))

        TextField(
            value = edad.value,
            onValueChange = { edad.value = it },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = { Text("Edad") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                val edadInt = edad.value.toIntOrNull()
                if (nombre.value.isNotBlank() && edadInt != null) {
                    viewModel.agregarEntidad(nombre.value, edadInt)
                    nombre.value = ""
                    edad.value = ""
                } else {
                    // Aquí podrías mostrar un mensaje de error (ej. Snackbar o Toast)
                }
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Agregar")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Lista de entidades:", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        Divider()

        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(entidades) { entidad ->
                Text("ID: ${entidad.id} - ${entidad.nombre}, Edad: ${entidad.edad}",
                    modifier = Modifier.padding(vertical = 4.dp))
                Divider()
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun EntidadScreenPreview() {
    EntidadScreen()
}