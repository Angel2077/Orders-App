package com.example.clases

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity3 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val xd = intent.getStringArrayExtra("res")
            if (xd != null) {
                Boleta(xd)
            }
        }
    }
}

@Composable
fun Boleta(wa: Array<String>) {
    Column(
        modifier = Modifier
            .width(350.dp)
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Producto", fontSize = 25.sp)
            Text("Cantidad", fontSize = 25.sp)
            Text("Subtotal", fontSize = 25.sp)
        }
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxWidth()
        ) {
            // nombre, neto, cantidad, iva, total
            Text(text = wa[0], textAlign = TextAlign.Start, fontSize = 20.sp)
            Text(text = wa[2], textAlign = TextAlign.End, fontSize = 20.sp)
            Text(text = wa[4], fontSize = 20.sp)
        }

        HorizontalDivider(modifier = Modifier.fillMaxWidth())
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.width(310.dp)
        ) {
            Text(text = "Neto: ", fontSize = 25.sp)
            Text(text = wa[1], fontSize = 25.sp)
        }
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.width(310.dp)
        ) {
            Text(text = "Iva: ", fontSize = 25.sp)
            Text(text = wa[3], fontSize = 25.sp)
        }
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.width(310.dp)
        ) {
            Text(text = "Total: ", fontSize = 25.sp)
            Text(text = wa[4], fontSize = 25.sp)
        }
    }
}
