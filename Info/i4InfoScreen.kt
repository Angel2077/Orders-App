package com.example.db_local_menu.Info // Paquete correcto

import android.graphics.Color
import android.graphics.Typeface
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel

// ¡IMPORTACIÓN CLAVE! Asegúrate de que esta línea esté presente:
import androidx.compose.ui.tooling.preview.Preview

// Importamos la nueva base de datos de Info
import com.example.db_local_menu.Info.i0InfoDatabase
import com.example.db_local_menu.Info.i1ProductoVendidos
import com.example.db_local_menu.Info.i2InfoViewModel
import com.example.db_local_menu.Info.i3InfoViewModelFactory

import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun i4InfoScreen(
    onBackClick: () -> Unit,
    viewModel: i2InfoViewModel = viewModel(
        factory = i3InfoViewModelFactory(
            pedidoInfoDao = i0InfoDatabase.getInstance(LocalContext.current).pedidoInfoDao(),
            detallePedidoDao = i0InfoDatabase.getInstance(LocalContext.current).detalleInfoDao(),
            productoInfoDao = i0InfoDatabase.getInstance(LocalContext.current).productoInfoDao()
        )
    )
) {
    val productosVendidosMes by viewModel.productosVendidosMes.observeAsState(emptyList())
    val top3ProductosVendidos by viewModel.top3ProductosVendidos.observeAsState(emptyList())
    val selectedMonth by viewModel.selectedMonth.observeAsState(YearMonth.now())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Estadísticas de Ventas") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Button(onClick = {
                    viewModel.insertarProductoInfoPrueba(1, "Pizza")
                    viewModel.insertarProductoInfoPrueba(2, "Hamburguesa")
                    viewModel.insertarProductoInfoPrueba(3, "Bebida Cola")
                    viewModel.insertarProductoInfoPrueba(4, "Papas Fritas")
                }) {
                    Text("Add Productos Info DB")
                }
                Button(onClick = {
                    viewModel.insertarPedidoDetalleInfoPrueba(1, 1, 2)
                    viewModel.insertarPedidoDetalleInfoPrueba(1, 2, 1)
                    viewModel.insertarPedidoDetalleInfoPrueba(2, 1, 3)
                    viewModel.insertarPedidoDetalleInfoPrueba(2, 3, 2)
                    viewModel.insertarPedidoDetalleInfoPrueba(3, 2, 4)
                    viewModel.insertarPedidoDetalleInfoPrueba(3, 4, 1)
                }) {
                    Text("Add Pedidos Info DB")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                IconButton(onClick = { viewModel.previousMonth() }) {
                    Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "Mes anterior")
                }
                Text(
                    text = selectedMonth.format(DateTimeFormatter.ofPattern("MMMMバシー", Locale("es", "ES"))).replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.headlineSmall
                )
                IconButton(onClick = { viewModel.nextMonth() }) {
                    Icon(Icons.Default.KeyboardArrowRight, contentDescription = "Mes siguiente")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            if (productosVendidosMes.isNotEmpty()) {
                AndroidView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .padding(bottom = 16.dp),
                    factory = { context ->
                        BarChart(context).apply {
                            description.isEnabled = false
                            setDrawGridBackground(false)
                            setDrawBarShadow(false)
                            setDrawValueAboveBar(true)

                            xAxis.apply {
                                position = XAxis.XAxisPosition.BOTTOM
                                setDrawGridLines(false)
                                setDrawAxisLine(true)
                                valueFormatter = IndexAxisValueFormatter(productosVendidosMes.map { it.nombreProducto })
                                granularity = 1f
                                labelCount = productosVendidosMes.size
                                setLabelCount(productosVendidosMes.size, false)
                                textSize = 10f
                                textColor = Color.BLACK
                                typeface = Typeface.DEFAULT_BOLD
                            }

                            axisLeft.apply {
                                setDrawGridLines(true)
                                setDrawAxisLine(true)
                                axisMinimum = 0f
                                textSize = 10f
                                textColor = Color.BLACK
                                typeface = Typeface.DEFAULT_BOLD
                            }

                            axisRight.isEnabled = false
                            legend.isEnabled = false
                            animateY(1000)
                        }
                    },
                    update = { chart ->
                        val entries = productosVendidosMes.mapIndexed { index, item ->
                            BarEntry(index.toFloat(), item.cantidadTotalVendida.toFloat())
                        }

                        val dataSet = BarDataSet(entries, "Cantidad Vendida").apply {
                            color = Color.parseColor("#42A5F5")
                            valueTextColor = Color.BLACK
                            valueTextSize = 10f
                            valueTypeface = Typeface.DEFAULT_BOLD
                        }

                        chart.data = BarData(dataSet)
                        chart.xAxis.valueFormatter = IndexAxisValueFormatter(productosVendidosMes.map { it.nombreProducto })
                        chart.xAxis.setLabelCount(productosVendidosMes.size, false)
                        chart.notifyDataSetChanged()
                        chart.invalidate()
                    }
                )
            } else {
                Text("No hay datos de ventas para este mes.", modifier = Modifier.padding(16.dp))
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("Top 3 Productos Más Vendidos", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(8.dp))
            if (top3ProductosVendidos.isNotEmpty()) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    top3ProductosVendidos.forEachIndexed { index, item ->
                        Text(
                            text = "${index + 1}. ${item.nombreProducto}: ${item.cantidadTotalVendida} unidades",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
            } else {
                Text("No hay top 3 productos para este mes.", modifier = Modifier.padding(8.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun i4InfoScreenPreview() {
    MaterialTheme {
        i4InfoScreen(onBackClick = {})
    }
}