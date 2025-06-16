//package com.example.dblocal._Ejemplo

package com.example.db_local_menu._Ejemplo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Entity")
data class a1Entity (//Parentesis no corchetessss
    @PrimaryKey(autoGenerate = true) val id: Int = 0, //clave primaria auto incremental
    val nombre: String,
    val edad: Int
)