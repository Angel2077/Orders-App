package com.usoftwork.myapplication.classes

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.IOException
import java.util.UUID

class Impresora(context: Context) {
    // Variables verificadoras
    var enable = true
    var permisos = false
    var upachalupa = false
    // Contexto de ComponentActivity
    val contexto = context

    var adaptador: BluetoothAdapter? = null
    private val MY_UUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

    var btMac = ""

    init {
        adaptador =
            (contexto.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter

        if (adaptador == null) {
            enable = false
        }
    }

    // Permisos a revisar
    val permissionsToRequest =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) { // Android 12+
            arrayOf(
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_CONNECT
            )
        } else { // Android 11 e inferior
            arrayOf(
                Manifest.permission.BLUETOOTH,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        }


    // Funcion que verifica los permisos y la activacion de Bluetooth
    fun verification():List<Boolean> {
        if (!permisos) {
            // Verificacion de permisos en base a la version del dispotitivo
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val bluetoothScan = ContextCompat.checkSelfPermission(
                    contexto,
                    Manifest.permission.BLUETOOTH_SCAN
                ) == PackageManager.PERMISSION_GRANTED
                val bluetoothConnect = ContextCompat.checkSelfPermission(
                    contexto,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) == PackageManager.PERMISSION_GRANTED
                permisos = bluetoothScan && bluetoothConnect
            } else {
                val bluetoothConnect = ContextCompat.checkSelfPermission(
                    contexto,
                    Manifest.permission.BLUETOOTH
                ) == PackageManager.PERMISSION_GRANTED
                val fineLocation = ContextCompat.checkSelfPermission(
                    contexto,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
                permisos = bluetoothConnect && fineLocation
            }
        }

        // Verificacion de hardware
        var hard = false

        if (adaptador!!.isEnabled) {
            // El dispositivo tiene Bluetooth y está habilitado.
            hard = true
        }


        upachalupa = permisos && hard

        // Entrega de informacion al usuario si hay error
        if (!permisos) {
            Toast.makeText(
                contexto,
                "Algunos permisos no estan habilitados",
                Toast.LENGTH_SHORT)
                .show()
        } else if (!hard) {
            Toast.makeText(
                contexto,
                "Verifique su Bluetooth",
                Toast.LENGTH_SHORT)
                .show()
        }

        return listOf<Boolean>(permisos,hard)
    }

    // Funcion que entrega los dispositivos cercanos
    fun getDevices(): Map<String, String>? {
        var lista: MutableMap<String, String> = mutableMapOf()
        // Verificamos si esta habilitado el permiso de bluetooth
        if (ActivityCompat.checkSelfPermission(
                contexto,
                Manifest.permission.BLUETOOTH_CONNECT
            ) == PackageManager.PERMISSION_GRANTED) {
            // Busca los dispositivos ya emparejados
            val pairedDevices: Set<BluetoothDevice>? = adaptador?.bondedDevices
            pairedDevices?.forEach { device ->
                val deviceName = device.name
                val deviceHardwareAddress = device.address // MAC address

                if (deviceName != null && deviceHardwareAddress != null) {
                    lista.put("($deviceHardwareAddress) \n$deviceName", deviceHardwareAddress)
                }
            }

            return if (lista.isEmpty()) {
                null
            } else {
                lista
            }

        } else {
            return null
        }
    }

    // Funcion que ingresa la direccion MAC escogido
    fun setMac(mac: String) {
        btMac = mac
    }

    // Funcion que envia informacion a la impresora
    fun imprimir(texto: String) {

        val device: BluetoothDevice? = adaptador?.getRemoteDevice(btMac)

        if (device != null) {
            try {
                // uuid es la firma del dispositivo
                val bluetoothSocket = device.createRfcommSocketToServiceRecord(MY_UUID)

                // nos conectamos a la impresora
                bluetoothSocket?.connect()

                val outputStream = bluetoothSocket?.outputStream
                outputStream?.write((texto + "\n\n\n").toByteArray())
                outputStream?.flush()

                // Soltamos la impresora
                bluetoothSocket?.close()

            } catch (e: IOException) {
                Toast.makeText(
                    contexto,
                    "Error al enviar los datos",
                    Toast.LENGTH_SHORT)
                    .show()
                println(e)

            } catch (e: SecurityException) {
                println(
                    "Error de seguridad (probablemente permiso faltante): ${e.message}"
                )
                println(e)
            }
        } else {
            Toast.makeText(
                contexto,
                "Dispositivo Bluetooth no encontrado",
                Toast.LENGTH_SHORT)
                .show()
        }
    }



}

