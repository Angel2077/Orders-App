package com.example.dblocal.intentoventa

import androidx.room.Database
import androidx.room.RoomDatabase

@Database (entities = [Pedidos::class,Plato::class], version = 1)
abstract class PedidoDatabase: RoomDatabase() {
 abstract  fun ventaPlatoDao(): VentaPlatoDao

}
