//package com.example.dblocal._Ejemplo

package com.example.db_local_menu._Ejemplo

class a4Repository(private val dao: a2EntityDao) {
    suspend fun insertar(entity: a1Entity) = dao.insertar(entity)
    fun obtener() = dao.obtener()
}
