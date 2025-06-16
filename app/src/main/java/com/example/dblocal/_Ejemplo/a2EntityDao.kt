package com.example.dblocal._Ejemplo
//Data Access Object
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface a2EntityDao {
    @Insert
    suspend fun insertar(entity: a1Entity)

    @Query("SELECT * FROM Entity")
    fun obtener(): Flow<List<a1Entity>>
}