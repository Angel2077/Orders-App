package com.example.dblocal._Producto
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.dblocal._Almacen.a1Componente


@Entity(tableName = "producto")
data class Producto (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val precio: Int,
    val categoria: String?,
    val tamano: String?= null,
    val maximoIngredientes: Int
)

@Entity(
    tableName = "ProductoComponente",
    primaryKeys = ["idProducto", "idComponente"],
    foreignKeys = [
        ForeignKey(entity = Producto::class, parentColumns = ["id"], childColumns = ["idProducto"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = a1Componente::class, parentColumns = ["id"], childColumns = ["idComponente"], onDelete = ForeignKey.CASCADE)
    ],
    indices = [Index("idProducto"), Index("idComponente")]
)
data class ProductoComponente(
    val idProducto: Int,
    val idComponente: Int,
    val esBase: Boolean,    // true: obligatorio / false: elegible
)
