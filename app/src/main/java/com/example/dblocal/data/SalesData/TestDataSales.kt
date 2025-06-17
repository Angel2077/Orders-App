package com.usoftwork.ordersapp.data.SalesData

object FakeFoodDatabase {
    private val foodCategories = listOf(
        "Entradas", "Platos Fuertes", "Postres", "Bebidas", "Ensaladas"
    )

    private val regions = listOf("Norte", "Sur", "Este", "Oeste", "Centro")

    private val menuItems = listOf(
        FoodItem(1, "Ceviche", "Entradas", 12.99),
        FoodItem(2, "Lomo Saltado", "Platos Fuertes", 18.50),
        FoodItem(3, "Pollo a la Brasa", "Platos Fuertes", 15.75),
        FoodItem(4, "Suspiro Limeño", "Postres", 8.25),
        FoodItem(5, "Chicha Morada", "Bebidas", 4.50),
        FoodItem(6, "Causa Limeña", "Entradas", 10.25),
        FoodItem(7, "Ají de Gallina", "Platos Fuertes", 16.80),
        FoodItem(8, "Mazamorra Morada", "Postres", 7.50),
        FoodItem(9, "Inca Kola", "Bebidas", 3.75),
        FoodItem(10, "Ensalada César", "Ensaladas", 11.25)
    )

    fun generateFoodSales(count: Int): List<FoodSale> {
        return List(count) { i ->
            val randomItem = menuItems.random()
            FoodSale(
                id = i + 1,
                itemId = randomItem.id,
                itemName = randomItem.name,
                category = randomItem.category,
                date = generateRandomDate(),
                amount = randomItem.price,
                quantity = (1..3).random(),
                region = regions.random()
            )
        }
    }

    private fun generateRandomDate(): String {
        val year = 2023
        val month = (1..12).random().toString().padStart(2, '0')
        val day = (1..28).random().toString().padStart(2, '0')
        return "$year-$month-$day"
    }
}

data class FoodItem(
    val id: Int,
    val name: String,
    val category: String,
    val price: Double
)

data class FoodSale(
    val id: Int,
    val itemId: Int,
    val itemName: String,
    val category: String,
    val date: String,
    val amount: Double,
    val quantity: Int,
    val region: String
)