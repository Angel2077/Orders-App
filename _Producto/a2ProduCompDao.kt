// dblocal/_Producto/a2ProduCompDao.kt (MODIFICADO)
//package com.example.dblocal._Producto

package com.example.db_local_menu._Producto

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.db_local_menu._Almacen.a1Componente
import kotlinx.coroutines.flow.Flow

@Dao
interface a2ProduCompDao {

    // ... (Mantén todos tus métodos existentes de Producto y ProductoComponente) ...

    @Transaction
    @Query("""
        SELECT * FROM producto
    """)
    fun obtenerProductosConComponentesElegibles(): Flow<List<ProductoWithComponents>>

    @Transaction
    @Query("""
        SELECT * FROM producto WHERE id = :idProducto
    """)
    fun getProductoWithComponentsById(idProducto: Int): Flow<ProductoWithComponents>


    // Producto
    @Insert
    suspend fun insertarProducto(producto: Producto): Long // <<-- ¡CAMBIO AQUÍ! Ahora devuelve el ID de la fila

    @Update
    suspend fun actualizarProducto(producto: Producto)

    @Query("SELECT * FROM producto")
    fun obtenerProductos(): Flow<List<Producto>> // Aunque ahora usaremos el de ProductoWithComponents para la lista

    @Query("SELECT * FROM producto WHERE id = :idProducto")
    fun getProductoById(idProducto: Int): Flow<Producto>

    @Delete
    suspend fun eliminarProducto(producto: Producto)

    // ProductoComponente
    @Insert
    suspend fun insertarRelacion(relacion: ProductoComponente)

    @Insert
    suspend fun insertarRelaciones(relaciones: List<ProductoComponente>)

    @Query("DELETE FROM ProductoComponente WHERE idProducto = :idProducto")
    suspend fun eliminarRelacionesProducto(idProducto: Int)

    // Consultas de componentes relacionados
    @Query("""
        SELECT C.* FROM Componente C
        INNER JOIN ProductoComponente PC ON C.id = PC.idComponente
        WHERE PC.idProducto = :idProducto AND PC.esBase = 1
    """)
    fun obtenerIngredientesBase(idProducto: Int): Flow<List<a1Componente>>

    @Query("""
        SELECT C.* FROM Componente C
        INNER JOIN ProductoComponente PC ON C.id = PC.idComponente
        WHERE PC.idProducto = :idProducto AND PC.esBase = 0 -- esBase = 0 para elegibles
    """)
    fun obtenerIngredientesElegibles(idProducto: Int): Flow<List<a1Componente>>
}
