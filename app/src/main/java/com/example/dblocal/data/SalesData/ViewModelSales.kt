package com.usoftwork.ordersapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.usoftwork.ordersapp.data.SalesData.FakeFoodDatabase
import com.usoftwork.ordersapp.data.SalesData.FoodSale
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
// Eliminamos java.time y usamos parsing manual

class SalesViewModel : ViewModel() {

    private val _sales = MutableStateFlow<List<FoodSale>>(emptyList())
    val sales: StateFlow<List<FoodSale>> = _sales.asStateFlow()

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()

    private val _top3Sales = MutableStateFlow<List<FoodSale>>(emptyList())
    val top3Sales: StateFlow<List<FoodSale>> = _top3Sales.asStateFlow()

    private val _leastSales = MutableStateFlow<List<FoodSale>>(emptyList())
    val leastSales: StateFlow<List<FoodSale>> = _leastSales.asStateFlow()

    init {
        loadSales()
    }

    private fun loadSales() {
        viewModelScope.launch {
            val data = FakeFoodDatabase.generateFoodSales(150)
            _sales.value = data
            updateTop3AndLeastSales()
        }
    }

    fun setCategoryFilter(category: String?) {
        _selectedCategory.value = category
        updateTop3AndLeastSales()
    }

    private fun updateTop3AndLeastSales() {
        val filtered = _sales.value.filterByCategory(_selectedCategory.value)
            .groupBy { it.itemName }
            .mapValues { it.value.sumOf { sale -> sale.quantity } }

        val sorted = filtered.entries.sortedByDescending { it.value }

        _top3Sales.value = sorted.take(3).mapNotNull { top ->
            _sales.value.firstOrNull { it.itemName == top.key }
        }

        val leastSorted = filtered.entries.sortedBy { it.value }
        _leastSales.value = leastSorted.take(3).mapNotNull { low ->
            _sales.value.firstOrNull { it.itemName == low.key }
        }
    }

    fun getDatesForChart(): List<Pair<String, Int>> {
        return _sales.value.groupBy { it.date }
            .map { entry ->
                val parts = entry.key.split("-")
                val day = parts.getOrNull(2)?.padStart(2, '0') ?: "01"
                val quantity = entry.value.sumOf { it.quantity }
                day to quantity
            }.sortedBy { it.first.toInt() }
    }

    private fun List<FoodSale>.filterByCategory(category: String?): List<FoodSale> {
        return if (category.isNullOrEmpty()) this else filter { it.category == category }
    }
}