package com.example.dblocal._Almacen

import androidx.room.Database
import androidx.room.RoomDatabase
import com.usoftwork.ordersapp.data.a1Entity.a1Componente
import com.usoftwork.ordersapp.data.a2Dao.a2ComponenteDao

@Database (entities = [a1Componente::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun ComponenteDao(): a2ComponenteDao
}