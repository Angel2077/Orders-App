package com.example.db_local_menu.Lista

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

// Factory para instanciar el l9PedidoViewModel, recibiendo el repositorio como dependencia
class l10PedidoViewModelFactory(private val repository: l8PedidoRepositorio) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(l9PedidoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return l9PedidoViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}