package com.example.db_local_menu.Lista

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

// Anotación @Database:
// - entities: Lista todas las entidades (tablas) que pertenecerán a esta base de datos.
// - version: La versión de tu esquema de base de datos. Increméntala si cambias la estructura de las tablas.
// - exportSchema: Desactívalo en desarrollo para evitar advertencias de KSP.
@Database(
    entities = [l3Pedido::class, l4DetallePedido::class], // Las nuevas entidades para Pedidos
    version = 1, // Versión inicial para esta nueva base de datos
    exportSchema = false // Desactívalo para el desarrollo
)
// Anotación @TypeConverters:
// - Registra la clase que contiene tus TypeConverters para que Room sepa cómo manejar tipos personalizados.
@TypeConverters(l2Converters::class)
abstract class l0Database : RoomDatabase() {

    // Declarar los DAOs (Data Access Objects) que pertenecen a esta base de datos.
    // Room generará las implementaciones de estos DAOs.
    abstract fun pedidoDao(): l6PedidoDao
    abstract fun detallePedidoDao(): l7DetallePedidoDao

    companion object {
        // La instancia Singleton de la base de datos para evitar múltiples instancias.
        @Volatile
        private var INSTANCE: l0Database? = null

        // Método para obtener la instancia de la base de datos de forma segura (Singleton).
        fun getInstance(context: Context): l0Database {
            // Si la instancia ya existe, la retorna.
            return INSTANCE ?: synchronized(this) {
                // Si no existe, crea una nueva instancia de la base de datos.
                val instance = Room.databaseBuilder(
                    context.applicationContext, // Usa el contexto de la aplicación para evitar fugas de memoria.
                    l0Database::class.java, // La clase de la base de datos que estamos construyendo.
                    "pedidos_db" // **Nombre ÚNICO para esta base de datos de pedidos.**
                    // Asegúrate de que no sea el mismo que el de tu AppDatabase principal.
                )
                    // .addMigrations(MIGRATION_X_Y) // Si en el futuro cambias el esquema, las migraciones irían aquí.
                    .build()
                INSTANCE = instance // Guarda la instancia recién creada.
                instance // Retorna la instancia.
            }
        }
    }
}