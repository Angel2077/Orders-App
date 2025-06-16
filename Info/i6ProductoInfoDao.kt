package com.example.db_local_menu.Info // Paquete correcto

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface i6ProductoInfoDao {
    // Inserta un producto. Si ya existe (por PK), lo reemplaza. Útil para "sincronizar" productos.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarProductoInfo(producto: i5ProductoInfo)

    // Obtiene un producto por su ID. Necesario para obtener el nombre en el ViewModel.
    @Query("SELECT * FROM producto_info WHERE id = :id")
    suspend fun getProductoInfoById(id: Int): i5ProductoInfo?

    // Obtiene todos los productos (útil para pruebas o inicialización)
    @Query("SELECT * FROM producto_info")
    fun obtenerTodosLosProductosInfo(): Flow<List<i5ProductoInfo>>
}
