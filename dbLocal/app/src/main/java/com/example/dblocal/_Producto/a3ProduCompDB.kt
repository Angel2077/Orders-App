// dblocal/AppDatabase.kt
package com.example.dblocal._Producto

// --- ENTIDADES ACTUALES ---

// --- NUEVAS ENTIDADES DE PEDIDO ---

// --- TU CONVERTER ---
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.dblocal._Almacen.a1Componente
import com.example.dblocal._Almacen.a2ComponenteDao
import com.example.dblocal._Pedido.DetallePedido
import com.example.dblocal._Pedido.Pedido
import com.example.dblocal._Pedido.PedidoDao
import com.example.dblocal._zConverters.LocalDateTimeConverter


// Define esta migración por encima de la clase AppDatabase o en un archivo Migrations.kt
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // SQL para crear la tabla 'pedido'
        database.execSQL("""
            CREATE TABLE IF NOT EXISTS `pedido` (
                `idPedido` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `fecha` TEXT NOT NULL,
                `estado` TEXT NOT NULL DEFAULT 'pendiente',
                `nombreCliente` TEXT NOT NULL,
                `total` INTEGER NOT NULL
            )
        """.trimIndent())

        // SQL para crear la tabla 'detalle_pedido'
        database.execSQL("""
            CREATE TABLE IF NOT EXISTS `detalle_pedido` (
                `idDetalle` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `idPedido` INTEGER NOT NULL,
                `idProducto` INTEGER NOT NULL,
                `cantidad` INTEGER NOT NULL,
                `precioTotal` INTEGER NOT NULL,
                `componentesElegidosIds` TEXT NOT NULL DEFAULT '',
                FOREIGN KEY(`idPedido`) REFERENCES `pedido`(`idPedido`) ON UPDATE NO ACTION ON DELETE CASCADE,
                FOREIGN KEY(`idProducto`) REFERENCES `producto`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE
            )
        """.trimIndent())

        // Opcional: Crear índices para mejorar el rendimiento de las consultas
        database.execSQL("CREATE INDEX IF NOT EXISTS `index_detalle_pedido_idPedido` ON `detalle_pedido` (`idPedido`)")
        database.execSQL("CREATE INDEX IF NOT EXISTS `index_detalle_pedido_idProducto` ON `detalle_pedido` (`idProducto`)")
    }
}


@Database(
    entities = [
        Producto::class,
        ProductoComponente::class,
        a1Componente::class,
        Pedido::class,         // Entidad en la v2
        DetallePedido::class   // Entidad en la v2
    ],
    version = 3, // Nueva version
    exportSchema = false
)
@TypeConverters(LocalDateTimeConverter::class) // ¡¡¡AÑADIR ESTA LÍNEA!!!
abstract class AppDatabase : RoomDatabase() {
    // Declarar todos los DAOs de tu aplicación aquí
    abstract fun a2ProduCompDao(): a2ProduCompDao
    abstract fun componenteDao(): a2ComponenteDao
    abstract fun pedidoDao(): PedidoDao // ¡¡¡AÑADIR ESTE DAO!!!

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "nombre_de_tu_base_de_datos" // Mantén el mismo nombre
                )
                    .fallbackToDestructiveMigration() // ¡¡¡ELIMINAR O COMENTAR ESTA LÍNEA!!!
                    .addMigrations(MIGRATION_1_2) // ¡¡¡AÑADIR ESTA LÍNEA!!!
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}