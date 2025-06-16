package com.example.dblocal._Almacen


data class a4ComponenteRepositorio(private val dao: a2ComponenteDao){
    suspend fun insertarComponente(componente: a1Componente) = dao.insertarComponente(componente)
    fun obtenerComponentes() = dao.obtenerComponentes()
    suspend fun eliminarComponente(vararg componente: a1Componente) = dao.eliminarComponente(*componente)
    suspend fun actualizarComponente(componente: a1Componente) {
        dao.actualizarComponente(componente)
    }
}