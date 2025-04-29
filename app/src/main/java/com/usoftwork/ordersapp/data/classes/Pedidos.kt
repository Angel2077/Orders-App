package com.usoftwork.ordersapp.data.classes

// Clase para productos
/*
data class Producto(
    val nombre: String,
    val precio: Int,
    val descripcion: String,
    val imagenId: Int,
    val cantidad: Int
)

// Clase para guardar las compras con sus productos
data class Compra(
    val nombre: String,
    val cantidad: Int,
    val precioUnitario: Int,
    val total: Int,
    val fecha: String,
    val id: Int
)


// Clase para productos
data class Producto(
    val nombre: String,
    val precio: Int,
    val descripcion: String,
    val imagenId: Int,
    val cantidad: Int
)

// Clase para guardar las compras con sus productos
data class Compra(
    val nombre: String,
    val cantidad: Int,
    val precioUnitario: Int,
    val total: Int,
    val fecha: String,
    val id: Int
)


// Contador para generar IDs únicos de compra
var compraIdCounter = 1

// Lista para almacenar todas las compras realizadas
val listaDeCompras = mutableListOf<Compra>()
// Lista mutable de productos

val listaDeProductos = mutableListOf<Producto>()

// Función para agregar un producto
fun addProducto(producto: Producto) {
    listaDeProductos.add(producto)
}
*/




import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.usoftwork.ordersapp.ui.theme.Cian
import com.usoftwork.ordersapp.ui.theme.DuckYellow
import com.usoftwork.ordersapp.ui.theme.Dustwhite
import com.usoftwork.ordersapp.ui.theme.LightGray
import com.usoftwork.ordersapp.ui.theme.Salmon
import com.usoftwork.ordersapp.ui.theme.Softred
import com.usoftwork.ordersapp.ui.theme.White
import com.usoftwork.ordersapp.ui.theme.Yellow
import com.usoftwork.ordersapp.ui.theme.grey


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextComponent(text: String) {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Titulo(text, MaterialTheme.colorScheme.secondary)
    }
}

@Composable
fun Titulo(text: String, color: Color) {
    Text(
        text = text,
        color = color,
        style = MaterialTheme.typography.headlineLarge
    )
}

@Composable
fun Sub(text: String,color: Color){
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Text(text = text, color = color, style = MaterialTheme.typography.headlineMedium)
    }
}

@Composable
fun CategoryBar(){
    Scaffold (topBar = {Sub("Categorias", MaterialTheme.colorScheme.onBackground)}){
            innerPadding ->
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(innerPadding)
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(5.dp)

        )
        {
            MenuCategoryCard(title = "Sushi")
            MenuCategoryCard(title = "Handroll")
            MenuCategoryCard(title = "Bebidas")
            MenuCategoryCard(title = "Otros")
        }
    }
}

@Composable
fun MenuCategoryCard(title: String) {
    Card(
        modifier = Modifier
            .width(85.dp)
            .fillMaxHeight(0.1f),
        colors = CardDefaults.cardColors(
            containerColor = Salmon
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(2.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onTertiary
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun PreviewText() {
    MaterialTheme {
        TextComponent("Tomar Pedido")
    }
}

@Composable
fun TextRectangle(
    text: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.primaryContainer,
    textColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    cornerRadius: Int = 8
) {
    Box(
        modifier = modifier
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(cornerRadius.dp)
            )
            .padding(5.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = textColor,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}


@Composable
fun Boton(text:String,
          textColor:Color = MaterialTheme.colorScheme.onPrimaryContainer,
          onClick: () -> Unit){
    Button(onClick = onClick ) {
        Text (
            text = text,
            color = textColor,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun Create_Pedido(navController: NavHostController) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                PreviewText()
                Spacer(Modifier.height(100.dp))
                CategoryBar()
            }
            Column(modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(Modifier.height(300.dp))
                TextRectangle(
                    text = "Handroll base kanikama",
                    modifier = Modifier.size(width = 350.dp, height = 80.dp)
                    ,backgroundColor = MaterialTheme.colorScheme.secondary
                    ,textColor = MaterialTheme.colorScheme.onSecondary
                )
                Spacer(Modifier.height(20.dp))
                TextRectangle(
                    text = "+",
                    modifier = Modifier.size(width = 350.dp, height = 25.dp)
                    ,backgroundColor = MaterialTheme.colorScheme.secondary
                    ,textColor = MaterialTheme.colorScheme.onSecondary
                )
                Spacer(Modifier.height(50.dp))
                TextRectangle(
                    text = "Sushi base Salmon",
                    modifier = Modifier.size(width = 350.dp, height = 80.dp)
                    ,backgroundColor = MaterialTheme.colorScheme.secondary
                    ,textColor = MaterialTheme.colorScheme.onSecondary
                )
                Spacer(Modifier.height(20.dp))
                TextRectangle(
                    text = "+",
                    modifier = Modifier.size(width = 350.dp, height = 25.dp)
                    ,backgroundColor = MaterialTheme.colorScheme.secondary
                    ,textColor = MaterialTheme.colorScheme.onSecondary
                )
                Spacer(Modifier.height(10.dp))
                Row {
                    Boton(text = "1",
                        textColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        onClick = {})
                    Boton(text = "2",
                        textColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        onClick = {})
                    Boton(text = "3",
                        textColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        onClick = {})
                    Boton(text = "4",
                        textColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        onClick = {})
                    Boton(text = "5",
                        textColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        onClick = {})
                }
            }
        }
}

// Listado de pedidos
@Composable
fun ListadoPedido(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2C2C2C))
            .padding(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(Color.Gray, shape = RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Listado Pedido",
                fontSize = 24.sp,
                color = White,
                fontWeight = FontWeight.Bold
            )
        }

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .background(Dustwhite)
        ) {
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(containerColor = Cian),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Entregado", color = White)
            }
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(containerColor = Salmon),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Pendiente", color = White)
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Salmon)
        ) {
            Column(
                Modifier.padding(8.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Juan Perez", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = White)
                    Text("Time Left", color = White)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Divider(color = grey, thickness = 1.dp)
                Divider(color = grey, thickness = 1.dp, modifier = Modifier.padding(top = 4.dp))
//No se
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF815959)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("$", color = White)
                    }
                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(containerColor = Softred),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Finish", color = White)
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(Yellow),
            contentAlignment = Alignment.TopEnd
        ) {
            Text(
                text = "00:12:00",
                color = Color.White,
                modifier = Modifier
                    .background(DuckYellow)
                    .padding(4.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(Color(0xFFA7D7AC))
        ) {}

        Spacer(modifier = Modifier.height(260.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Salmon),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(5) { _ ->
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings",
                    tint = White,
                    modifier = Modifier
                        .size(50.dp)
                        .background(grey, shape = RoundedCornerShape(8.dp)).padding(8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
                .background(LightGray)
        )
    }
}