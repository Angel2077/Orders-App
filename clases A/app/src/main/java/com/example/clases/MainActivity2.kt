package com.example.clases

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.clases.classes.Json
import com.google.gson.Gson
import conexiondb.DatabaseConnector
import java.sql.PreparedStatement
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MainActivity2 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DatabaseConnector.getConnection()
            Selector()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Selector() {
    val contextMain = LocalContext.current

    var cantidadPapas by remember { mutableStateOf("") }
    var cantidadHamb by remember { mutableStateOf("") }
    var cantidadHand by remember { mutableStateOf("") }
    var cantidadChorr by remember { mutableStateOf("") }

    val precioPapas by remember { mutableStateOf("2500") }
    val precioHamb by remember { mutableStateOf("4000") }
    val precioHand by remember { mutableStateOf("2500") }
    val precioChorr by remember { mutableStateOf("7000") }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 30.dp)
            .border(
                2.dp,
                color = Color.Black,
                shape = RoundedCornerShape(30.dp)
            ),
    ) {
        Row (
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 10.dp)
        ){
            Text("Productos:", fontSize = 30.sp)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                Image(
                    painter = painterResource(id = R.drawable.hamburguesa_clasica),
                    contentDescription = "Hamburgesa",
                    modifier = Modifier
                        .size(140.dp)
                        .padding(10.dp)
                        .clip(RoundedCornerShape(25.dp))
                        .border(
                            2.dp,
                            color = Color.Black,
                            shape = RoundedCornerShape(25.dp)
                        )
                )

                Text(text = "Hamburgesa")
                Text(text = "$$precioHamb")

                OutlinedTextField(
                    value = cantidadHamb,
                    onValueChange = {
                        if ( it != "") {
                            cantidadPapas = ""
                            cantidadHamb = it
                            cantidadHand = ""
                            cantidadChorr = ""
                        }
                    },
                    label = { Text("Cantidad") },
                    textStyle = TextStyle(textAlign = TextAlign.Center),
                    modifier = Modifier
                        .width(100.dp)
                        .padding(top = 7.dp)
                        .onFocusChanged {


                        },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Number),
                    maxLines = 1
                )
            }


            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.papas_fritas),
                    contentDescription = "Papas Fritas",
                    modifier = Modifier
                        .size(140.dp)
                        .padding(10.dp)
                        .clip(RoundedCornerShape(25.dp))
                        .border(
                            2.dp,
                            color = Color.Black,
                            shape = RoundedCornerShape(25.dp)
                        )
                )
                Text(text = "Papas Fritas")
                Text(text = "$$precioPapas")

                OutlinedTextField(
                    value = cantidadPapas,
                    onValueChange = {
                        if ( it != "") {
                            cantidadPapas = it
                            cantidadHamb = ""
                            cantidadHand = ""
                            cantidadChorr = ""
                        }
                    },
                    label = { Text("Cantidad") },
                    textStyle = TextStyle(textAlign = TextAlign.Center),
                    modifier = Modifier
                        .padding(top = 7.dp)
                        .width(100.dp)
                        .onFocusChanged {

                        },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Number),
                    maxLines = 1
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                Image(
                    painter = painterResource(id = R.drawable.handrollcongelado),
                    contentDescription = "HandRoll",
                    modifier = Modifier
                        .size(140.dp)
                        .padding(10.dp)
                        .clip(RoundedCornerShape(25.dp))
                        .border(
                            2.dp,
                            color = Color.Black,
                            shape = RoundedCornerShape(25.dp)
                        )
                )
                Text(text = "HandRoll")
                Text(text = "$$precioHand")

                OutlinedTextField(
                    value = cantidadHand,
                    onValueChange = {
                        if ( it != "") {
                            cantidadPapas = ""
                            cantidadHamb = ""
                            cantidadHand = it
                            cantidadChorr = ""
                        }
                    },
                    label = { Text("Cantidad") },
                    textStyle = TextStyle(textAlign = TextAlign.Center),
                    modifier = Modifier
                        .padding(top = 7.dp)
                        .width(100.dp)
                        .onFocusChanged {

                        },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Number),
                    maxLines = 1
                )
            }




            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.chorrillana),
                    contentDescription = "Chorrillana",
                    modifier = Modifier
                        .size(140.dp)
                        .padding(10.dp)
                        .clip(RoundedCornerShape(25.dp))
                        .border(
                            2.dp,
                            color = Color.Black,
                            shape = RoundedCornerShape(25.dp)
                        )
                )
                Text(text = "Chorrillana")
                Text(text = "$$precioChorr")

                OutlinedTextField(
                    value = cantidadChorr,
                    onValueChange = {
                        if ( it != "") {
                            cantidadPapas = ""
                            cantidadHamb = ""
                            cantidadHand = ""
                            cantidadChorr = it
                        }
                    },
                    label = { Text("Cantidad") },
                    textStyle = TextStyle(textAlign = TextAlign.Center),
                    modifier = Modifier
                        .padding(top = 7.dp)
                        .width(100.dp)
                        .onFocusChanged { },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Number),
                    maxLines = 1
                )
            }
        }

    }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 32.dp)
        ) {
            Button(
                onClick = {
                    cantidadHamb = ""
                    cantidadPapas = ""
                    cantidadHand = ""
                    cantidadChorr = ""
                },
                colors = buttonColors(containerColor = Color.Red),
                modifier = Modifier.padding(top = 16.dp, bottom = 14.dp)
            ) {
                Text(text = "Borrar")
            }
            Button(
                onClick = {
                    val produc:Array<String> = when {
                        cantidadHamb != "" -> {
                            arrayOf("Hamburgesa", precioHamb, cantidadHamb)
                        }

                        cantidadPapas != "" -> {
                            arrayOf("Papas Fritas", precioPapas, cantidadPapas)
                        }

                        cantidadHand != "" -> {
                            arrayOf("Handroll", precioHand, cantidadHand)
                        }

                        cantidadChorr != "" -> {
                            arrayOf("Chorrillana", precioChorr, cantidadChorr)
                        }

                        else -> {
                            arrayOf("None", "0", "0")
                        }
                    }
                    val res = calculos(produc)
                    val json = transform(res)
                    val xd = submitt(res,json)

                    val intent = Intent(contextMain, MainActivity3::class.java)
                    intent.putExtra("res", res)
                    contextMain.startActivity(intent)
                    

                },
                colors = buttonColors(containerColor = Color.Green),
                modifier = Modifier.padding(top = 16.dp, bottom = 14.dp)
            ) {
                Text(text = "Aceptar")
            }
        }
}



fun transform(lista: Array<String>):String {
    val elem = Json(
        nombre= lista[0],
        cantidad= lista[2].toInt(),
        iva= lista[1].toFloat().toInt(),
        neto= lista[3].toFloat().toInt(),
        total= lista[4].toFloat().toInt()
    )
    val gson = Gson()
    val json = gson.toJson(elem)
    return json
}

fun submitt(res: Array<String>, json: String): Boolean {
    val conection = DatabaseConnector.getConnection()
    if (conection != null) {
        val currentDate: Date = Calendar.getInstance().time
        val formattedDate: String =
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(currentDate)
        val query =
            "INSERT INTO venta(nombrep, neto, cantidad, iva, total, Fecha, pdf_json, codigo_user) VALUES (?,?,?,?,?,?,?,?)"
        return try {
            val preparedStatement: PreparedStatement =
                conection.prepareStatement(query)
                    ?: return false
            preparedStatement.setString(1, res[0])
            preparedStatement.setInt(2, res[1].toFloat().toInt())
            preparedStatement.setInt(3, res[2].toInt())
            preparedStatement.setInt(4, res[3].toFloat().toInt())
            preparedStatement.setInt(5, res[4].toFloat().toInt())
            preparedStatement.setString(6, formattedDate)
            preparedStatement.setString(7, json)
            preparedStatement.setInt(8, 11)

            preparedStatement.executeUpdate() > 0
        } catch (e: Exception) {
            e.printStackTrace()
            false
        } finally {
            conection.close()
        }
    }
    return true
}

fun calculos(productData: Array<String>) : Array<String>{
    val total = productData[1].toInt() * productData[2].toInt()
    val iva = total * 0.19
    val neto = total - iva
    // nombre, neto, cantidad, iva, total
    val totalList = arrayOf(productData[0],neto.toString(),productData[2],iva.toString(),total.toString())
    return totalList
}



