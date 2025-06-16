package com.example.dblocal._Ejemplo

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [a1Entity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun EntityDao(): a2EntityDao
}
