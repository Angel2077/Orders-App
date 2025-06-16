package com.example.db_local_menu.Info // Paquete correcto

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
// Ahora importamos los DAOs de nuestra propia DB de Info
import com.example.db_local_menu.Info.i6ProductoInfoDao
import com.example.db_local_menu.Info.i9PedidoInfoDao
import com.example.db_local_menu.Info.i10DetallePedidoInfoDao

class i3InfoViewModelFactory(
    private val pedidoInfoDao: i9PedidoInfoDao,
    private val detallePedidoInfoDao: i10DetallePedidoInfoDao,
    private val productoInfoDao: i6ProductoInfoDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(i2InfoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return i2InfoViewModel(pedidoInfoDao, detallePedidoInfoDao, productoInfoDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
