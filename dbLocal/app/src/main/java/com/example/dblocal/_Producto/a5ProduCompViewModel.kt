package com.example.dblocal._Producto

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.dblocal._Almacen.a1Componente
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ProductoViewModel(private val repository: ProductoRepository) : ViewModel() {

    val productos: LiveData<List<ProductoWithComponents>> =
        repository.obtenerProductosConComponentesElegibles().asLiveData()

    private val _productoSeleccionado = MutableLiveData<Producto?>()
    val productoSeleccionado: LiveData<Producto?> = _productoSeleccionado

    private val _componentesBase = MutableLiveData<List<a1Componente>>()
    val componentesBase: LiveData<List<a1Componente>> = _componentesBase

    private val _componentesElegibles = MutableLiveData<List<a1Componente>>()
    val componentesElegibles: LiveData<List<a1Componente>> = _componentesElegibles

    // Estas LiveData contendrán las selecciones temporales del usuario en el formulario.
    private val _currentBaseComponents = MutableLiveData<MutableSet<a1Componente>>(mutableSetOf())
    // CAMBIO AQUÍ: Usamos MediatorLiveData para transformar MutableSet a Set
    val currentBaseComponents: LiveData<Set<a1Componente>> = MediatorLiveData<Set<a1Componente>>().apply {
        addSource(_currentBaseComponents) { mutableSet ->
            value = mutableSet.toSet() // Convertir a Set inmutable y postear
        }
    }

    private val _currentEligibleComponents = MutableLiveData<MutableSet<a1Componente>>(mutableSetOf())
    // CAMBIO AQUÍ: Usamos MediatorLiveData para transformar MutableSet a Set
    val currentEligibleComponents: LiveData<Set<a1Componente>> = MediatorLiveData<Set<a1Componente>>().apply {
        addSource(_currentEligibleComponents) { mutableSet ->
            value = mutableSet.toSet() // Convertir a Set inmutable y postear
        }
    }

    fun seleccionarProducto(producto: Producto?) {
        _productoSeleccionado.value = producto
        if (producto != null) {
            viewModelScope.launch {
                // Asegurarse de que el producto tiene un ID válido antes de cargar
                if (producto.id != 0) {
                    val baseComps = repository.obtenerIngredientesBase(producto.id).first()
                    _currentBaseComponents.value = baseComps.toMutableSet()

                    val eligibleComps = repository.obtenerIngredientesElegibles(producto.id).first()
                    _currentEligibleComponents.value = eligibleComps.toMutableSet()
                } else {
                    // Si es un producto nuevo (id=0), inicializar con conjuntos vacíos
                    _currentBaseComponents.value = mutableSetOf()
                    _currentEligibleComponents.value = mutableSetOf()
                }
            }
        } else {
            // Cuando no hay producto seleccionado (ej. al añadir uno nuevo)
            limpiarSeleccion()
        }
    }

    fun limpiarSeleccion() {
        _productoSeleccionado.value = null
        _currentBaseComponents.value = mutableSetOf()
        _currentEligibleComponents.value = mutableSetOf()
    }

    // Funciones para añadir/eliminar componentes de los sets temporales
    fun toggleComponenteAsBase(componente: a1Componente) {
        val current = _currentBaseComponents.value ?: mutableSetOf()
        val updated = current.toMutableSet() // Trabajar con una copia mutable

        if (updated.contains(componente)) {
            updated.remove(componente)
        } else {
            updated.add(componente)
            // Asegurarse de que no esté en los elegibles si es base
            val currentEligible = _currentEligibleComponents.value ?: mutableSetOf()
            val updatedEligible = currentEligible.toMutableSet()
            updatedEligible.remove(componente)
            _currentEligibleComponents.value = updatedEligible
        }
        _currentBaseComponents.value = updated
    }

    fun toggleComponenteAsEligible(componente: a1Componente) {
        val current = _currentEligibleComponents.value ?: mutableSetOf()
        val updated = current.toMutableSet() // Trabajar con una copia mutable

        if (updated.contains(componente)) {
            updated.remove(componente)
        } else {
            updated.add(componente)
            // Asegurarse de que no esté en los base si es elegible
            val currentBase = _currentBaseComponents.value ?: mutableSetOf()
            val updatedBase = currentBase.toMutableSet()
            updatedBase.remove(componente)
            _currentBaseComponents.value = updatedBase
        }
        _currentEligibleComponents.value = updated
    }


    /**
     * Guarda un producto y sus componentes asociados (base y elegibles).
     * Si el producto.id es 0, se inserta; de lo contrario, se actualiza.
     * Las relaciones de componentes se manejan de forma transaccional.
     * @param producto La entidad Producto a guardar/actualizar.
     * Ahora, las listas de componentes se obtienen de _currentBaseComponents y _currentEligibleComponents
     */
    fun guardarProductoConComponentes(producto: Producto) {
        viewModelScope.launch {
            val baseComps = _currentBaseComponents.value?.toList() ?: emptyList()
            val elegibleComps = _currentEligibleComponents.value?.toList() ?: emptyList()
            repository.guardarProductoConComponentes(producto, baseComps, elegibleComps)
            limpiarSeleccion() // Limpiar selección después de guardar
        }
    }

    fun eliminarProducto(producto: Producto) {
        viewModelScope.launch {
            repository.eliminarProducto(producto)
            limpiarSeleccion() // Limpiar selección después de eliminar
        }
    }
}