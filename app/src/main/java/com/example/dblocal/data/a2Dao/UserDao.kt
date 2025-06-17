package com.usoftwork.ordersapp.data.a2Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.usoftwork.ordersapp.data.a1Entity.User

@Dao
interface UserDao {
    @Insert
    suspend fun insert(user: User)

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    suspend fun getUserByUsername(username: String): User?
}