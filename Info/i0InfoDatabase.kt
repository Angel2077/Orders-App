package com.example.db_local_menu.Info // Paquete correcto

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters // Necesario para la anotación @TypeConverters
// Importa la clase de Type Converters desde su propio archivo
import com.example.db_local_menu.Info.InfoTypeConverters

// Entidades de la base de datos de Info
import com.example.db_local_menu.Info.i5ProductoInfo
import com.example.db_local_menu.Info.i7PedidoInfo
import com.example.db_local_menu.Info.i8DetallePedidoInfo

// DAOs de la base de datos de Info
import com.example.db_local_menu.Info.i6ProductoInfoDao
import com.example.db_local_menu.Info.i9PedidoInfoDao
import com.example.db_local_menu.Info.i10DetallePedidoInfoDao


@Database(
    entities = [
        i5ProductoInfo::class,
        i7PedidoInfo::class,
        i8DetallePedidoInfo::class
    ],
    version = 1,
    exportSchema = false
)
// ¡Referencia la clase InfoTypeConverters que ahora está en su propio archivo!
@TypeConverters(InfoTypeConverters::class)
abstract class i0InfoDatabase : RoomDatabase() {

    abstract fun productoInfoDao(): i6ProductoInfoDao
    abstract fun pedidoInfoDao(): i9PedidoInfoDao
    abstract fun detallePedidoInfoDao(): i10DetallePedidoInfoDao

    companion object {
        @Volatile
        private var INSTANCE: i0InfoDatabase? = null

        fun getInstance(context: Context): i0InfoDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    i0InfoDatabase::class.java,
                    "info_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}