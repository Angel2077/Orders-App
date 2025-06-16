// dblocal/_Producto/a4ProduCompRepository.kt (MODIFICADO)
package com.example.dblocal._Producto

import androidx.room.Transaction
import com.example.dblocal._Almacen.a1Componente
import kotlinx.coroutines.flow.Flow // Asegúrate de tener este import

class ProductoRepository(private val dao: a2ProduCompDao) {

    // Cambiado para usar la nueva relación con componentes
    fun obtenerProductosConComponentesElegibles(): Flow<List<ProductoWithComponents>> {
        return dao.obtenerProductosConComponentesElegibles()
    }

    // Mantén el resto de tus métodos existentes
    fun obtenerProductos() = dao.obtenerProductos() // Mantener si todavía lo usas sin componentes
    fun obtenerIngredientesBase(idProducto: Int) = dao.obtenerIngredientesBase(idProducto)
    fun obtenerIngredientesElegibles(idProducto: Int) = dao.obtenerIngredientesElegibles(idProducto)
    fun getProductoById(idProducto: Int) = dao.getProductoById(idProducto)
    fun getProductoWithComponentsById(idProducto: Int) = dao.getProductoWithComponentsById(idProducto)


    suspend fun insertarProducto(producto: Producto) = dao.insertarProducto(producto)
    suspend fun actualizarProducto(producto: Producto) = dao.actualizarProducto(producto)
    suspend fun eliminarProducto(producto: Producto) = dao.eliminarProducto(producto)
    suspend fun insertarRelaciones(relaciones: List<ProductoComponente>) = dao.insertarRelaciones(relaciones)
    suspend fun eliminarRelacionesProducto(idProducto: Int) = dao.eliminarRelacionesProducto(idProducto)
    suspend fun insertarRelacion(productoComponente: ProductoComponente) = dao.insertarRelacion(productoComponente)

    @Transaction
    suspend fun guardarProductoConComponentes(
        producto: Producto,
        componentesBase: List<a1Componente>,
        componentesElegibles: List<a1Componente>
    ) {
        val productoId = if (producto.id == 0) {
            dao.insertarProducto(producto).toInt() // Asumiendo que insertarProducto devuelve el ID
        } else {
            dao.actualizarProducto(producto)
            dao.eliminarRelacionesProducto(producto.id)
            producto.id
        }

        val relaciones = mutableListOf<ProductoComponente>()
        relaciones += componentesBase.map {
            ProductoComponente(productoId, it.id, esBase = true)
        }
        relaciones += componentesElegibles.map {
            ProductoComponente(productoId, it.id, esBase = false)
        }
        if (relaciones.isNotEmpty()) {
            dao.insertarRelaciones(relaciones)
        }
    }
}

