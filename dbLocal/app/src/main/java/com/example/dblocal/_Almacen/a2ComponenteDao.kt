package com.example.dblocal._Almacen
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface a2ComponenteDao {
    @Insert
    suspend fun insertarComponente(componente: a1Componente)

    @Query("SELECT * FROM Componente")
    fun obtenerComponentes(): Flow<List<a1Componente>>


        //Eliminacion
    //Completo
    @Delete
    suspend fun eliminarComponente(vararg componente : a1Componente)

        //Modificar
    @Update
    suspend fun actualizarComponente(componente: a1Componente)

    //obtener detalle de componentes
    @Query("SELECT * FROM Componente WHERE id = :idComponente")
    suspend fun getComponenteByIdSync(idComponente: Int): a1Componente?
}