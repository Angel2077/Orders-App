package com.usoftwork.ordersapp.data.a4Repositories

import com.usoftwork.ordersapp.data.a1Entity.a1Componente
import com.usoftwork.ordersapp.data.a2Dao.a2ComponenteDao

data class a4ComponenteRepositorio(private val dao: a2ComponenteDao){
    suspend fun insertarComponente(componente: a1Componente) = dao.insertarComponente(componente)
    fun obtenerComponentes() = dao.obtenerComponentes()
    suspend fun eliminarComponente(vararg componente: a1Componente) = dao.eliminarComponente(*componente)
    suspend fun eliminarPorId(id: Int) = dao.eliminarPorId(id)
    suspend fun actualizarComponente(componente: a1Componente) {
        dao.actualizarComponente(componente)
    }
}