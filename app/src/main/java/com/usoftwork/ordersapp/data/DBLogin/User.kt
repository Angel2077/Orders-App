package com.example.usoftwork.data.DBLogin

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey val id: Int = 1, // ID fijo para un solo usuario
    val username: String,
    val password: String
)
