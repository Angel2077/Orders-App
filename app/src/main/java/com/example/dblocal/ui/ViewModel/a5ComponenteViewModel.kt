package com.example.dblocal.ui.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.dblocal._Almacen.AppDatabase
import com.usoftwork.ordersapp.data.a1Entity.a1Componente
import com.usoftwork.ordersapp.data.a4Repositories.a4ComponenteRepositorio
import kotlinx.coroutines.launch

class a5ComponenteViewModel (application: Application): AndroidViewModel(application){
    private val db = Room.databaseBuilder(
        application,
        AppDatabase::class.java,
        "Componentes"
    ).build()

    //Repositorio
    private val repository = a4ComponenteRepositorio(db.ComponenteDao())

    val listaComponentes = repository.obtenerComponentes()

    fun insertarComp(nombre: String,porcion: Double,unidad: String,cantidad: Int){
        viewModelScope.launch {
            repository.insertarComponente(a1Componente(nombre = nombre,porcion = porcion,unidad = unidad,cantidad = cantidad))
        }
    }
    fun EliminarComp(vararg componente: a1Componente){
        viewModelScope.launch {
            repository.eliminarComponente(*componente)
        }
    }
    fun EliminarPorId(id: Int){
        viewModelScope.launch {
            repository.eliminarPorId(id)
        }
    }
    fun ModificarComp(componente: a1Componente) {
        viewModelScope.launch {
            repository.actualizarComponente(componente)
        }
    }


}