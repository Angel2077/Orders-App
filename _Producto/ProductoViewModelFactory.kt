//package com.example.dblocal._Producto

package com.example.db_local_menu._Producto

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

// La Factory recibe el repositorio que el ViewModel necesita
class ProductoViewModelFactory(private val repository: ProductoRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Aseguramos que estamos creando una instancia de ProductoViewModel
        if (modelClass.isAssignableFrom(ProductoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            // Retornamos una instancia de ProductoViewModel pasándole el repositorio
            return ProductoViewModel(repository) as T
        }
        // Si la clase del ViewModel no coincide, lanzamos una excepción
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}