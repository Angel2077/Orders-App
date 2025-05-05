
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material.icons.filled.Info
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.usoftwork.ordersapp.ui.navi_bar.NavBar

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
            containerColor = Color(0xFFFB8B8B)
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
fun VistaFunciones() {

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
                Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
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

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Pagina(){
    val naviItems = listOf("create_dish","sales","storage","orders" ,"take_order")
    Scaffold(
        bottomBar = {
        NavBar(
            isVisible = true,
            selectedItem = naviItems[4],
            onItemClick = {} // Use the correct index for "take_order"
        )
}
    ){padding ->
        VistaFunciones()
    }
}
