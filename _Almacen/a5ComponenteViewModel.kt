//package com.example.dblocal._Almacen

package com.example.db_local_menu._Almacen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

// El ViewModel ahora recibe el DAO de componente directamente
class a5ComponenteViewModel(private val componenteDao: a2ComponenteDao) : ViewModel() {

    private val repository = a4ComponenteRepositorio(componenteDao)

    val listaComponentes = repository.obtenerComponentes().asLiveData()

    fun insertarComp(nombre: String, porcion: Double, unidad: String, cantidad: Int) {
        viewModelScope.launch {
            repository.insertarComponente(a1Componente(nombre = nombre, porcion = porcion, unidad = unidad, cantidad = cantidad))
        }
    }
    fun EliminarComp(vararg componente: a1Componente) {
        viewModelScope.launch {
            repository.eliminarComponente(*componente)
        }
    }

    fun ModificarComp(componente: a1Componente) {
        viewModelScope.launch {
            repository.actualizarComponente(componente)
        }
    }
}