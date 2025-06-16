//package com.example.dblocal._Ejemplo

package com.example.db_local_menu._Ejemplo
//Viewmodel necesario para mantener la información inmutable a cambios como giro de pantalla,
//para ello guar y administra la información de la base de datos
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room.databaseBuilder
import kotlinx.coroutines.launch


class a5ViewModel (application: Application) : AndroidViewModel(application) {
    //Inicialización de la base de datos
    private val db = databaseBuilder(//Creamos o abrimos un archivo de DB
        application,// Contexto igual que en la class anterior
        AppDatabase::class.java,//Clase que define la base de datos //preguntar
        "database-name"//Nombre del archivo donde se guardarán los datos
    ).build()

        //Repositorio
    //El repositorio fue creado como un intermediario para acceder a la base de datos y
    //proveer una abstracción de datos.
    //Asi para que en futuros cambios solo se deba editar este "Reposiorio"
    private val repository = a4Repository(db.EntityDao())

        //Llamada a funciones del repositorio

    //llama a una fución flow que mantiene los datos actualizados en la base de datos y en la vista
    val listaEntidades = repository.obtener()

    //Función para agregar una entidad
    //Nombre de funcion      Datos de entrada
    fun agregarEntidad(nombre: String, edad: Int) {
        viewModelScope.launch {//Ejecucíon de agregado en segundo plano
            repository.insertar(a1Entity(nombre = nombre, edad = edad))//Guardado de elemenetos bajo
            // el formato de la entidad
        }
    }
}


