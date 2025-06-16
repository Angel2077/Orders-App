package com.example.dblocal._Producto

// Ya no necesitas 'import androidx.room.Junction' si la tenías
import androidx.room.Embedded
import androidx.room.Relation

data class ProductoWithComponents(
    @Embedded val producto: Producto,

    @Relation(
        parentColumn = "id", // ID del Producto
        entityColumn = "idProducto", // Columna en ProductoComponente (la entidad que está embebida en ComponenteJoinData)
        entity = ProductoComponente::class // La entidad base de esta relación es ProductoComponente
    )
    val componentsWithJoinData: List<ComponenteJoinData> // ¡Ahora es una lista de ComponenteJoinData!
)