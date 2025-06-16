package com.usoftwork.myapplication

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat

@Composable
fun BluetoothPermissionRequesterScreen() {
    val context = LocalContext.current
    var bluetoothScanPermissionGranted by remember { mutableStateOf(false) }
    var bluetoothConnectPermissionGranted by remember { mutableStateOf(false) }
    var fineLocationPermissionGranted by remember { mutableStateOf(false) }

    // Determina los permisos necesarios basados en la versión de Android
    val permissionsToRequest = remember {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) { // Android 12+
            arrayOf(
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_CONNECT
            )
        } else { // Android 11 e inferior
            arrayOf(
                Manifest.permission.BLUETOOTH, // BLUETOOTH_ADMIN implícitamente cubierto
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        }
    }

    // Launcher para solicitar múltiples permisos
    val multiplePermissionsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                bluetoothScanPermissionGranted = permissions[Manifest.permission.BLUETOOTH_SCAN] == true
                bluetoothConnectPermissionGranted = permissions[Manifest.permission.BLUETOOTH_CONNECT] == true
            } else {
                bluetoothConnectPermissionGranted = ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED
                bluetoothScanPermissionGranted = bluetoothConnectPermissionGranted // Y también para escanear (junto con ubicación)
                fineLocationPermissionGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
            }
            updatePermissionStatus(context)
        }
    )



    // Llama a esto al inicio para comprobar el estado actual de los permisos
    LaunchedEffect(Unit) {
        updatePermissionStatus(context)
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Permisos de Bluetooth necesarios.")
        Spacer(Modifier.height(8.dp))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Text("BLUETOOTH_SCAN Concedido: $bluetoothScanPermissionGranted")
            Text("BLUETOOTH_CONNECT Concedido: $bluetoothConnectPermissionGranted")
            // Text("ACCESS_FINE_LOCATION Concedido: $fineLocationPermissionGranted") // Si es relevante
        } else {
            Text("BLUETOOTH (para conectar/escanear) Concedido: $bluetoothConnectPermissionGranted")
            Text("ACCESS_FINE_LOCATION (para escanear) Concedido: $fineLocationPermissionGranted")
        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                // Comprueba si los permisos ya están concedidos antes de solicitar
                var allPermissionsGranted = true
                for (permission in permissionsToRequest) {
                    if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                        allPermissionsGranted = false
                        break
                    }
                }

                if (!allPermissionsGranted) {
                    multiplePermissionsLauncher.launch(permissionsToRequest)
                } else {
                    // Los permisos ya están concedidos, puedes proceder
                    println("Todos los permisos necesarios ya están concedidos.")
                    // Aquí llamarías a tu lógica de Bluetooth
                }
            },
            // Habilita el botón solo si alguno de los permisos necesarios aún no está concedido
            enabled = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                !bluetoothScanPermissionGranted || !bluetoothConnectPermissionGranted // || !fineLocationPermissionGranted (si es relevante)
            } else {
                !bluetoothConnectPermissionGranted || !fineLocationPermissionGranted
            }
        ) {
            Text("Solicitar Permisos de Bluetooth")
        }

        Spacer(Modifier.height(16.dp))

        // Ejemplo de cómo podrías usar los permisos
        Button(
            onClick = { /* Lógica de Bluetooth aquí, como iniciar escaneo o conexión */
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    if (bluetoothScanPermissionGranted && bluetoothConnectPermissionGranted) {
                        println("PROCEDIENDO con Bluetooth (Android 12+)")
                        // Tu lógica para escanear/conectar aquí
                    } else {
                        println("Faltan permisos de Bluetooth en Android 12+")
                    }
                } else {
                    if (bluetoothConnectPermissionGranted && fineLocationPermissionGranted) { // Para escanear
                        println("PROCEDIENDO con escaneo Bluetooth (Android <12)")
                        // Tu lógica para escanear aquí
                    } else if (bluetoothConnectPermissionGranted) { // Para solo conectar (sin escanear)
                        println("PROCEDIENDO con conexión Bluetooth (Android <12)")
                        // Tu lógica para conectar aquí
                    }
                    else {
                        println("Faltan permisos de Bluetooth o Ubicación en Android <12")
                    }
                }
            },
            enabled = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                bluetoothScanPermissionGranted && bluetoothConnectPermissionGranted // && fineLocationPermissionGranted (si es relevante)
            } else {
                // Para escanear, necesitas ambos. Para conectar, solo BLUETOOTH.
                // Este botón se habilita si tienes lo mínimo para conectar O lo necesario para escanear.
                bluetoothConnectPermissionGranted
            }
        ) {
            Text("Usar Funcionalidad Bluetooth")
        }
    }
}

fun updatePermissionStatus(context: Context) : Boolean {
    var res = false
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val bluetoothScan = ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED
        val bluetoothConnect = ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED
        res = bluetoothScan && bluetoothConnect
    } else {
        val bluetoothConnect = ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED
        val fineLocation = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        res = bluetoothConnect && fineLocation
    }
    return res
}











