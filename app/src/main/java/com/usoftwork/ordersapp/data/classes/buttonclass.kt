package com.usoftwork.ordersapp.data.classes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.Dp.Companion.Unspecified
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.usoftwork.ordersapp.ui.theme.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomButton(
    onClick: () -> Unit,
    text: String,
    icon: Any? = null,
    modifier: Modifier = Modifier,
    height: Dp = Unspecified,
    weight: Float = 1f,
    fontSize: TextUnit = 20.sp,
    fontWeight: FontWeight = FontWeight.Normal,
    cornerRadius: Dp = 6.dp,
    containerColor: Color = Salmon,
    contentColor: Color = Dustwhite,
    iconSize: Dp = 60.dp,
    center: Boolean = false
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .then(if (height != Unspecified) Modifier.height(height) else Modifier)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(cornerRadius),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = when {
                center && icon == null -> Arrangement.Center
                icon != null -> Arrangement.SpaceBetween
                else -> Arrangement.Start
            },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                fontSize = fontSize,
                fontWeight = fontWeight,
            )
            //en caso de que exista un icono
            icon?.let {
                when (it) {
                    is ImageVector -> Icon(
                        imageVector = it,
                        contentDescription = text,
                        modifier = Modifier.size(iconSize)
                    )
                    is Int -> Icon(
                        painter = painterResource(id = it),
                        contentDescription = text,
                        modifier = Modifier.size(iconSize)
                    )
                }
            }
        }
    }
}

