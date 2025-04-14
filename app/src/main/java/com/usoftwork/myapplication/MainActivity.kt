package com.usoftwork.myapplication

import android.R
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.collections.arrayListOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.LinearGradient
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.layout.VerticalAlignmentLine
import conexiondb.DatabaseConnector
import java.sql.PreparedStatement
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


// Clases para los datos
data class DatosCaja(val id: Int,var nombre: String, var cantidad: Int, val color: Color)
data class Data(val id: Int, var databox: MutableList<DatosCaja>)

// Clase principal
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainPreview()
        }
    }
}


@Preview(showBackground = true, showSystemUi = true, backgroundColor = 0xFF5A5A5A)
@Composable
// Funcion principal
fun MainPreview() {
    val cantidad = 40
    val ubicaciones = arrayListOf<String>("Casa", "FoodTruck")
    val dimencion = sacarTamano()
    val dimencionSpacer = tamanoSpacer(dimencion)
    val dimencionCaja = tamanoCaja()
    val cantBox = cajasPorBloque(dimencion, dimencionSpacer, dimencionCaja)
    val info1 = recargarDatos(cantidad, cantBox[0],Color.Cyan)
    val info2 = recargarDatos(cantidad, cantBox[0],Color.Gray)


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray)
        , horizontalAlignment = Alignment.CenterHorizontally

    ) {
// agregar funcion Bloque
        Spacer(modifier = Modifier.height((dimencionSpacer[1]).dp))
        bloque(
            dimencion[1],
            dimencionCaja[0],
            dimencionSpacer,
            ubicaciones[0],
            info1
        )
        // botones
        Column {
            Spacer(modifier = Modifier.height((dimencionSpacer[1] / 4).dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height((dimencionSpacer[0] * 3.5).dp)
                    .padding(0.dp, (dimencionSpacer[1] * 0.75).dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(
                    color = Color.Gray,
                    thickness = (dimencionSpacer[1] / 4).dp,
                    modifier = Modifier.width((dimencionSpacer[0] * 2).dp)
                )

                OutlinedButton(
                    modifier = Modifier
                        .size((dimencionSpacer[0] * 4).dp, (dimencionSpacer[1] * 1.5).dp)
                        .background(Color.Red, shape = RoundedCornerShape(11.dp))
                        .border(2.dp, Color.Black, shape = RoundedCornerShape(10.dp))
                    ,onClick = {}
                ) {
                    Text(text = "^", color = Color.Black ,fontSize = (dimencionSpacer[1] * 1.5).sp)
                }

                HorizontalDivider(
                    color = Color.Gray,
                    thickness = (dimencionSpacer[1] / 4).dp,
                    modifier = Modifier.width((dimencionSpacer[0] * 2).dp)
                )

                OutlinedButton(
                    modifier = Modifier
                        .size((dimencionSpacer[0] * 4.5).dp)
                        .background(Color.White, shape = CircleShape)
                        .border(2.dp, Color.Black, shape = CircleShape)
                    ,onClick = {}
                ) {
                    Text(text = "+", fontSize = (dimencionSpacer[1] * 4).sp)
                }

                HorizontalDivider(
                    color = Color.Gray,
                    thickness = (dimencionSpacer[1] / 4).dp,
                    modifier = Modifier.width((dimencionSpacer[0] * 2).dp)
                )

                OutlinedButton(
                    modifier = Modifier
                        .size((dimencionSpacer[0] * 4).dp, (dimencionSpacer[1] * 1.5).dp)
                        .background(Color.Green, shape = RoundedCornerShape(11.dp))
                        .border(2.dp, Color.Black, shape = RoundedCornerShape(10.dp))
                    ,onClick = {}
                ) {
                    Text(text = "v", fontSize = (dimencionSpacer[1] * 1.5).sp)
                }

                HorizontalDivider(
                    color = Color.Gray,
                    thickness = (dimencionSpacer[1] / 4).dp,
                    modifier = Modifier.width((dimencionSpacer[0] * 2).dp)
                )
            }
        }

        bloque(
            dimencion[1],
            dimencionCaja[0],
            dimencionSpacer,
            ubicaciones[1],
            info2
        )
    }
}


@Composable
// Funcion que genera las cajas
fun caja(nombre:String, cantidad:Int, color: Color){

    Box(modifier = Modifier
        .background(color = color, shape = RoundedCornerShape(11.dp))
        .border(width = 2.dp, color = Color.Black, shape = RoundedCornerShape(10.dp))
        .size(144.dp, 90.dp) //.size(ancho.dp, alto.dp)
        ,contentAlignment = Alignment.Center){

        Text(text = nombre
            ,modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(0.dp, (18).dp) // .padding(0.dp, (alto / 5).dp)
            ,fontSize = (22.5).sp) // ,fontSize = (alto / 4).sp)

        Text(text = cantidad.toString()
            ,modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(0.dp, (18).dp) // .padding(0.dp, (alto / 5).dp)
            ,fontSize = (15).sp) // ,fontSize = (alto / 6).sp)
    }
}

@Composable
// Funcion que saca el tamaño de la pantalla
fun sacarTamano(): ArrayList<Int> {
    val configuracion = LocalConfiguration.current
    val ancho = configuracion.screenWidthDp.dp
    val alto = configuracion.screenHeightDp.dp
    val anchoOut = ancho.value.toInt()
    val altoOut = alto.value.toInt()
    val lista:ArrayList<Int> = arrayListOf(anchoOut, altoOut)
    return lista
}

@Composable
fun bloque(tamY:Int, boxX:Int ,spacer: ArrayList<Int>, name:String ,datos:ArrayList<Data>) {
    Spacer(modifier = Modifier.height((spacer[1] / 2).dp))
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size((boxX * 2).dp, (spacer[1]).dp)
                .background(Color.Cyan, shape = RoundedCornerShape(12.dp))
                .border(
                    width = 2.dp,
                    color = Color.Black,
                    shape = RoundedCornerShape(10.dp)
                ), contentAlignment = Alignment.Center
        )
        {
            Text(text = name, fontSize = (spacer[1] / 1.5).sp)
        }
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .height((tamY / 2 - (spacer[1] * 3.5)).dp)
            .padding(0.dp, (spacer[1] / 2).dp)
        ,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(datos, key = { it.id }) { elem ->
            Row {
                for (item in elem.databox) {
                    caja(nombre = item.nombre, cantidad = item.cantidad, color = item.color)

                    if (item != elem.databox.last()) {
                        Spacer(modifier = Modifier.width(spacer[0].dp))
                    }
                }
            }
            Spacer(modifier = Modifier.height((spacer[1]/2).dp))
        }
    }
}

// Funciones para sacar el tamaño de las cajas
fun tamanoCaja(): ArrayList<Int> {
    val anchoCaja = 144
    val altoCaja = 90
    val lista2 = arrayListOf(anchoCaja, altoCaja)
    return lista2
}

// Funciones para sacar el tamaño de los espacios
fun tamanoSpacer(dimencion: ArrayList<Int>): ArrayList<Int> {
    val anchoSpacer = dimencion[0] / 20
    val altoSpacer = dimencion[1] / 25
    val lista2 = arrayListOf(anchoSpacer, altoSpacer)
    return lista2
}

// Funcion para sacar el numero de cajas por Bloque
fun cajasPorBloque(tamano: ArrayList<Int>, spacer: ArrayList<Int>, caja: ArrayList<Int>): ArrayList<Int> {
    val x = ((tamano[0] - (spacer[0] * 2)))
    val y = ((tamano[1] / 2) - (spacer[1] * 3.5))
    var aux = 0
    var xmax = 0
    var ymax = 0
    while (aux < x) {
        xmax++
        aux = xmax * (caja[0] + spacer[0])
        if (aux > x) {
            xmax--
        }
    }
    aux = 0
    while (aux < y) {
        ymax = ymax + 1
        aux = ymax * (caja[1] + (spacer[1]/2))
        if (aux > x) {
            ymax--
        }
    }
    val results = arrayListOf<Int>(xmax , ymax)
    return results
}

fun recargarDatos(total:Int, cantBoxH:Int, color:Color):ArrayList<Data> {
    var lista = arrayListOf<Data>()
    var listaAux = mutableListOf<DatosCaja>()

    var i = 1
    var j = 1
    while (i <= total) {
        while ((i <= total) && j <= cantBoxH ) {
            listaAux.add(DatosCaja(id = i
                ,nombre = "Producto $i"
                ,cantidad = i * 2
                ,color = color))
            j++
            i++
        }
        lista.add(Data(id = i, databox = listaAux))
        listaAux = mutableListOf<DatosCaja>()
        j = 1
    }
    return lista
}

fun xd() {

}

/*
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

 */