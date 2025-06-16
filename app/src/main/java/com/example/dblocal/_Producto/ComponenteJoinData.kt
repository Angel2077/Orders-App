package com.example.dblocal._Producto


import androidx.room.Embedded
import androidx.room.Relation
import com.example.dblocal._Almacen.a1Componente

data class ComponenteJoinData(
    @Embedded val productoComponente: ProductoComponente, // Esto "embebe" la fila de la tabla de unión, que contiene 'esBase'
    @Relation(
        parentColumn = "idComponente", // Desde la entidad ProductoComponente (columna idComponente)
        entityColumn = "id"            // Hacia la entidad a1Componente (columna id)
    )
    val componente: a1Componente       // Aquí se "relaciona" el componente real
)