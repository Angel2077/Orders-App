import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.usoftwork.ordersapp.data.classes.CustomButton
import com.usoftwork.ordersapp.data.classes.ExpandableButton
import com.usoftwork.ordersapp.ui.theme.*
import com.usoftwork.ordersapp.viewmodel.SalesViewModel

@Composable
fun VentasScreen(
    onDayClick: () -> Unit,
    viewModel: SalesViewModel = viewModel()
) {
    val sales by viewModel.sales.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val top3 by viewModel.top3Sales.collectAsState()
    val leastSales by viewModel.leastSales.collectAsState()

    val categories = remember(sales) {
        sales.map { it.category }.toSet().toList()
    }

    val filteredDishes = remember(sales, selectedCategory) {
        if (selectedCategory.isNullOrEmpty() || selectedCategory == "Todos") {
            sales
        } else {
            sales.filter { it.category.equals(selectedCategory, ignoreCase = true) }
        }
    }.groupBy { it.itemName }
        .mapValues { entry -> entry.value.sumOf { it.quantity } }
        .toList()

    val sortedBottom = filteredDishes.sortedBy { it.second }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkGray)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Analisis de venta",
            fontSize = 24.sp,
            color = Dustwhite,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .background(Lightgrey),
            contentAlignment = Alignment.Center
        ) {
            Text("[Gráfico aquí]", color = DarkGray)
        }

        CustomButton(
            onClick = onDayClick,
            text = "Day",
            center = true,
            containerColor = Lightgrey,
            contentColor = DarkGray
        )

        ExpandableButton(title = "Top 3 in this Day") {
            Column(modifier = Modifier.padding(8.dp)) {
                top3.forEach { item ->
                    Text("${item.itemName} - ${item.quantity} ventas", color = Dustwhite)
                }
            }
        }

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                "Filtro por categoría:",
                color = Dustwhite,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )
            LazyRow(modifier = Modifier.fillMaxWidth()) {
                items(listOf("Todos") + categories) { category ->
                    CustomButton(
                        onClick = { viewModel.setCategoryFilter(category.takeIf { it != "Todos" }) },
                        text = category,
                        containerColor = if (category == selectedCategory || (category == "Todos" && selectedCategory == null)) Orange else Lightgrey,
                        contentColor = if (category == selectedCategory || (category == "Todos" && selectedCategory == null)) White else DarkGray,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        ExpandableButton(title = "Productos ordenados por menor venta") {
            LazyColumn(modifier = Modifier.padding(8.dp)) {
                items(sortedBottom) { (name, sales) ->
                    Text("$name - $sales ventas", color = Dustwhite)
                }
            }
        }
    }
}

@Preview
@Composable
fun VentasScreenPreview() {
    VentasScreen(onDayClick = {})
}