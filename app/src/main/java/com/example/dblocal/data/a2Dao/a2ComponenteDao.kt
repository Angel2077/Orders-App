package com.usoftwork.ordersapp.data.a2Dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.usoftwork.ordersapp.data.a1Entity.a1Componente
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
    //Por ID
    @Query("DELETE FROM Componente WHERE id = :id")
    suspend fun eliminarPorId(id: Int)

        //Modificar
    @Update
    suspend fun actualizarComponente(componente: a1Componente)
}