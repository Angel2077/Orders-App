package com.usoftwork.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class MainActivity2 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Xd()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Xd() {
    var isChecked by remember { mutableStateOf(false) }
    Switch(
        checked = isChecked,
        onCheckedChange = { newValue -> isChecked = newValue },
        modifier = Modifier.size(100.dp),
        thumbContent = {
            if (isChecked) {
                Image(
                    painter = painterResource(
                        id = R.drawable.fullmoon),
                    contentDescription = "Modo oscuro")
            } else {
                Image(
                    painter = painterResource(
                        id = R.drawable.fullsun),
                    contentDescription = "Modo claro")
            }
        },
        colors = SwitchDefaults.colors(
            checkedThumbColor = Color.Gray,
            checkedTrackColor = Color.Gray.copy(alpha = 0.5f),
            uncheckedThumbColor = Color.Red.copy(green = 0.5f),
            uncheckedTrackColor = Color.Red.copy(green = 0.5f, alpha = 0.5f)
        )
    )
}

