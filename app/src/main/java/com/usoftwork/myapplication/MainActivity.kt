package com.usoftwork.myapplication

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.usoftwork.myapplication.classes.Impresora

class MainActivity : ComponentActivity() {

    /*
    var check = false
    val requestPermissionLauncher =
        registerForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions(),
            callback = { permissions ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    val bluetoothScan = permissions[Manifest.permission.BLUETOOTH_SCAN] == true
                    val bluetoothConnect =
                        permissions[Manifest.permission.BLUETOOTH_CONNECT] == true
                    check = bluetoothScan && bluetoothConnect
                } else {
                    // En versiones < S, BLUETOOTH es suficiente para conectar si está concedido
                    // (asumiendo que BLUETOOTH_ADMIN también está en el manifest).
                    // El chequeo principal aquí es para la ubicación.
                    val bluetoothConnect = ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.BLUETOOTH
                    ) == PackageManager.PERMISSION_GRANTED
                    val bluetoothScan =
                        bluetoothConnect // Y también para escanear (junto con ubicación)
                    val fineLocation = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
                }
            }
        )
*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Prueba()

        }
    }
}


@Preview(showBackground = true)
@Composable
fun Prueba() {

    val contexto = LocalContext.current
    var check by remember { mutableStateOf(false) }
    val requestPermissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions(),
            onResult = { permissions ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    val bluetoothScan = permissions[Manifest.permission.BLUETOOTH_SCAN] == true
                    val bluetoothConnect =
                        permissions[Manifest.permission.BLUETOOTH_CONNECT] == true
                    check = bluetoothScan && bluetoothConnect

                } else {
                    val bluetoothConnect = ContextCompat.checkSelfPermission(
                        contexto,
                        Manifest.permission.BLUETOOTH
                    ) == PackageManager.PERMISSION_GRANTED
                    val fineLocation = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
                    check = bluetoothConnect && fineLocation
                }
            }
        )

    var active by remember { mutableStateOf(false) }


    var texto by remember { mutableStateOf("Seleccione el \ndispositivo") }
    var index by remember { mutableIntStateOf(0) }
    var devText by remember { mutableStateOf("") }


    var color = Color.Gray

    var select by remember { mutableStateOf("") }

    var impres = Impresora(contexto)
    val habilitado = impres.enable



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .background(
                        color = color,
                        shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)
                    )
                    .size(100.dp, 50.dp), contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Impresora", textAlign = TextAlign.Center, fontSize = 18.sp
                )
            }
            Box(
                modifier = Modifier
                    .background(
                        color = color,
                        shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)
                    )
                    .size(80.dp, 50.dp), contentAlignment = Alignment.Center
            ) {
                Switch(
                    checked = active,
                    enabled = habilitado,
                    onCheckedChange = {
                        val verificacion = impres.verification()

                        if (active) {
                            active = false
                        } else {
                            if (impres.upachalupa) {
                                active = true
                            } else if (!verificacion[0]) {
                                requestPermissionLauncher.launch(impres.permissionsToRequest)
                                active = check && verificacion[1]
                            } else if (!verificacion[1]) {
                                active = false
                            }
                        }
                    }
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = color,
                    shape = RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                verticalArrangement = Arrangement.Center, modifier = Modifier.padding(10.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            val macmap = impres.getDevices()
                            val lista = listar(macmap)

                            if (index - 1 < 0) {
                                index = lista.size - 1
                            } else {
                                index--
                            }
                            texto = lista[index]
                            select = macmap?.get(lista[index]).toString()
                        },
                        enabled = active
                    ) {
                        Text(text = "<")
                    }

                    Text(
                        text = texto,
                        maxLines = 2,
                        modifier = Modifier
                            .background(color = Color.White, shape = RectangleShape)
                            .padding(10.dp),
                        textAlign = TextAlign.Center
                    )

                    Button(
                        onClick = {
                            val macmap = impres.getDevices()
                            val lista = listar(macmap)

                            if (index + 1 == lista.size) {
                                index = 0
                            } else {
                                index++
                            }
                            texto = lista[index]
                            select = macmap?.get(lista[index]).toString()
                        },
                        enabled = active
                    ) {
                        Text(text = ">")
                    }
                }
                OutlinedTextField(
                    value = devText,
                    onValueChange = { devText = it },
                    label = { Text("Texto a imprimir") },
                    enabled = active
                )

                Button(
                    enabled = active,
                    onClick = {
                        if (select != "" && select != "null") {
                            impres.setMac(select)
                            impres.imprimir(devText)
                        }
                    },
                ) {
                    Text(
                        text = "Enviar"
                    )
                }
            }
        }
    }
}



fun listar(xd: Map<String, String>?): MutableList<String> {
    var lista = mutableListOf<String>("(00:00:00:00:00:00) \nNo device")
    xd?.forEach { key, value ->
        lista.add(key)
    }
    return lista
}

