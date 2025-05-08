package com.usoftwork.ordersapp.ui.screens.useful

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.usoftwork.ordersapp.data.classes.CustomButton
import com.usoftwork.ordersapp.ui.theme.Dustwhite
import com.usoftwork.ordersapp.ui.theme.Salmon

@Composable
fun UsefulChangePassword() {
    var email by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2C2C2C))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Coffee Of Lizz",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Dustwhite
        )

        Text(
            text = "Recuperar Contraseña",
            fontSize = 18.sp,
            color = Dustwhite.copy(alpha = 0.7f)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Ingrese su Correo",
            fontSize = 16.sp,
            color = Dustwhite.copy(alpha = 0.9f)
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = email,
            onValueChange = { email = it },
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .background(Color.Transparent),
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Salmon,
                focusedContainerColor = Salmon,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                cursorColor = Color.Black,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        CustomButton(
            onClick = {
                Toast.makeText(
                    context,
                    "Se enviará un correo a $email para recuperar su contraseña",
                    Toast.LENGTH_LONG
                ).show()
            },
            text = "Recuperar Contraseña",
            fontWeight = FontWeight.Bold,
            containerColor = Salmon,
            contentColor = Color.White,
            cornerRadius = 12.dp
        )

        Spacer(modifier = Modifier.height(24.dp))

    }
}

@Preview(showBackground = true)
@Composable
fun PreviewUsefulChangePassword() {
    UsefulChangePassword()
}
