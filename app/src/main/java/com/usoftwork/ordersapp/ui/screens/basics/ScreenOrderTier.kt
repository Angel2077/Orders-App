import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ListadoPedidoScreen() {
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
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .background(Color(0xFFD8D8D8))
        ) {
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7BB6FF)),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Entregado", color = Color.White)
            }
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFA6A6)),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Pendiente", color = Color.White)
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFF8A8A))
        ) {
            Column(
                Modifier.padding(8.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Juan Perez", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.White)
                    Text("Time Left", color = Color.White)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Divider(color = Color.Gray, thickness = 1.dp)
                Divider(color = Color.Gray, thickness = 1.dp, modifier = Modifier.padding(top = 4.dp))
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
                        Text("$", color = Color.White)
                    }
                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF815959)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Finish", color = Color.White)
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(Color(0xFFE3B64A)),
            contentAlignment = Alignment.TopEnd
        ) {
            Text(
                text = "00:12:00",
                color = Color.White,
                modifier = Modifier
                    .background(Color(0xFFB97C10))
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
                .background(Color(0xFFFF8A8A)),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(5) { _ ->
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings",
                    tint = Color.White,
                    modifier = Modifier
                        .size(50.dp)
                        .background(Color.Gray, shape = RoundedCornerShape(8.dp)).padding(8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
                .background(Color(0xFFD8D8D8))
        )
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ListadoPedidoScreenPreview() {
    ListadoPedidoScreen()
}

