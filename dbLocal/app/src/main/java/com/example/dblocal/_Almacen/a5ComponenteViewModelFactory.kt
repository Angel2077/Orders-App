package com.example.dblocal._Almacen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class a5ComponenteViewModelFactory(private val componenteDao: a2ComponenteDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(a5ComponenteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return a5ComponenteViewModel(componenteDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}