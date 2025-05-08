package com.usoftwork.ordersapp.data.classes

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


/**
 * ExpandableButton: Un botón que se expande sobre sí mismo para mostrar contenido.
 *
 * @param title Texto mostrado en la parte superior del botón.
 * @param modifier Opcional: permite personalizar el modificador del contenedor.
 * @param content Contenido a mostrar al expandirse.
 */
@Composable
fun ExpandableButton(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFFFB8B8B))
            .clickable { expanded = !expanded }
            .padding(12.dp)
    ) {
        Text(
            text = if (expanded) "Ocultar    $title" else title,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onSecondary,
        )

        AnimatedVisibility(
            visible = expanded,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                content()
            }
        }
    }
}