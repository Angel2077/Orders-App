package com.university.orderManegment

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.clases.MainActivity2

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 86.dp),
                horizontalArrangement = Arrangement.Center
            ) {

                Image(
                    painter = painterResource(id = R.drawable.mcdonalds_logo),
                    contentDescription = "logo",
                    modifier = Modifier
                        .padding(16.dp)
                        .size(240.dp)
                )
            }
            Row {

            }
            Login()
        }
    }
}


@Preview (showBackground = true)
@Composable
fun Login() {
    val contextMain = LocalContext.current
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var ruts by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var error by remember { mutableStateOf("") }
        var nombre by remember { mutableStateOf("") }

        HorizontalDivider()

        OutlinedTextField(
            value = ruts,
            onValueChange = { ruts = it },
            label = { Text("Ingrese el rut") },
            modifier = Modifier.onFocusChanged {
                ruts = autocompeltar(ruts)
                error = if (validarRut(ruts) || ruts.isEmpty()) {
                    ""
                } else {
                    "Rut no valido"
                }
            },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = androidx.compose.ui.text.input.ImeAction.Next),
            maxLines = 1
        )


        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre del usuario") },
            maxLines = 1,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = androidx.compose.ui.text.input.ImeAction.Next),
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Ingrese la contraseña") },
            maxLines = 1,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = androidx.compose.ui.text.input.ImeAction.Done),
        )

        Button(onClick = {
            if (nombre.isEmpty()) {
                error = "ingrese un nombre"
            } else if (password.isEmpty()) {
                error = "ingrese una contraseña"
            } else if ( error.isEmpty()){
                contextMain.startActivity(Intent(contextMain, MainActivity2::class.java))
            }
        }, modifier = Modifier.padding(top = 16.dp)) {
            Text(text = "Aceptar")
        }
        Text(text = error, color = androidx.compose.ui.graphics.Color.Red)
    }
}

fun autocompeltar(rut:String):String {
    if (rut.length > 4) {
        var rion: String = rut

        if (rut.contains("-")) {
            rion = rut.replace("-", "")
        }
        if (rut.contains(".")) {
            rion = rion.replace(".", "")
        }
        val dv: String ="-" + rut.last().toString()
        rion = rion.dropLast(1)
        var aux = ""

        var j = when (rion.length % 3) {
            0 -> 3
            else -> rion.length % 3
        }
        while (rion.length > 3) {
            aux += rion.take(j) + "."
            rion = rion.drop(j)
            if (j != 3) {
                j = 3
            }

        }
        aux += rion.take(j)
        return aux + dv
    } else return rut
}

fun validarRut(rut: String):Boolean {
    if (rut.length >= 11) {
        val lista = listOf(2, 3, 4, 5, 6, 7)
        var sum = 0
        var j = 0
        val ves = (rut.last()).toString()

        var lut = rut.dropLast(2)

        if (rut.contains(".")) {
            lut = lut.replace(".", "")
        }

        var id = lut.map { it.toString().toInt() }

        val index = id.size

        id = id.reversed()

        while (j < index) {
            for (i in lista) {
                if (j < index) {
                    sum += i * id[j]
                    j += 1
                }
            }
        }

        sum = 11 - (sum % 11)
        val res: String = when (sum) {
            11 -> {
                "0"
            }

            10 -> {
                "K"
            }

            else -> {
                sum.toString()
            }
        }
        return res == ves
    } else {
        return false
    }
}