package com.example.dblocal.ui.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.usoftwork.ordersapp.data.a1Entity.User
import com.usoftwork.ordersapp.data.a3DataBase.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val db = Room.databaseBuilder(
        application,
        AppDatabase::class.java,
        "login-db"
    ).build()

    init {
        insertarUsuarioDePrueba()
    }

    private fun insertarUsuarioDePrueba() {
        viewModelScope.launch(Dispatchers.IO) {
            val existing = db.userDao().getUserByUsername("admin")
            if (existing == null) {
                val user = User(username = "admin", password = "1234")
                db.userDao().insert(user)
            }
        }
    }

    fun validateLogin(username: String, password: String, callback: (Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val storedUser = db.userDao().getUserByUsername(username)
            val isValid = storedUser?.password == password
            withContext(Dispatchers.Main) {
                callback(isValid)
            }
        }
    }
}