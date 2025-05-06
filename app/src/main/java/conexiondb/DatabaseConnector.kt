package conexiondb

import android.os.StrictMode
import android.util.Log
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

class DatabaseConnector {
    // Cargamos las variables
    private var url = ""
    private var user = ""
    private var pass = ""
    private var connectionEstablished: Boolean = false

    init {
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
    }

    // funcion que conecta a la base de datos
    private fun getConnection(): Connection? {
        return try {
            Class.forName("com.mysql.jdbc.Driver")
            val connection = DriverManager.getConnection(url, user, pass)
            Log.d("DatabaseConnector", "Conexión exitosa a la base de datos")
            connection

        } catch (e: ClassNotFoundException) {
            Log.e("DatabaseConnector", "Error con la clase", e)
            e.printStackTrace()
            null

        } catch (e: SQLException) {
            Log.e("DatabaseConnector", "Error con la conexion", e)
            e.printStackTrace()
            null


        } catch (e: Exception) {
            Log.e("DatabaseConnector", "Error sin relacion", e)
            e.printStackTrace()
            null
        }
    }

    // Funcion que establece los datos para la conexion
    fun setConnection(
        direccion: String,
        empresa: String,
        usuario: String,
        contrasena: String
    ): Boolean {
        url = "jdbc:mysql://$direccion:3306/$empresa"
        user = usuario
        pass = contrasena
        connectionEstablished = false

        if (url.contains(" ")) {
            url = url.replace(" ", "_")
        }
        if (user.contains(" ")) {
            user.replace(" ", "_")
        }

        val coneccion = this.getConnection()
        if (coneccion != null) {
            connectionEstablished = true
            coneccion.close()
        }
        return connectionEstablished
    }

    // Funcion que inserta ocurrencias a la base de datos
    fun submit(
        tabla: String,
        columnas: String,
        valores:
        ArrayList<String>
    ): Boolean {
        val conection = this.getConnection()
        if (conection != null) {
            var query = "INSERT INTO $tabla($columnas) VALUES ("
            repeat(valores.size) {
                query = query.plus("?,")
            }
            query = query.dropLast(1)
            query = query.plus(")")

            return try {
                conection.prepareStatement(query).use { preparedStatement: PreparedStatement ->
                    var aux = 0
                    while (aux < valores.size) {
                        if (valores[aux].contains(".") && valores[aux].toFloatOrNull() != null) {
                            preparedStatement.setFloat(aux + 1, valores[aux].toFloat())

                        } else if (valores[aux].toIntOrNull() != null) {
                            preparedStatement.setInt(aux + 1, valores[aux].toInt())

                        } else {
                            preparedStatement.setString(aux + 1, valores[aux])

                        }
                        aux++
                    }

                    preparedStatement.executeUpdate() > 0
                }
            } catch (e: Exception) {
                e.printStackTrace()
                false
            } finally {
                conection.close()
                true
            }
        }
        return true

    }


    // Funcion que recibe ocurrencias de la base de datos
    // investigar en una forma de cambiar el where y el having de forma automatica para mayor facilidad en el manejo del codigo
    fun receive(
        tabla: String,
        columnas: String,
        condicion: String? = null,
        valores: ArrayList<String>? = null
    ): ArrayList<Map<String, String>>? {
        var res = arrayListOf<Map<String, String>>()
        val conection = this.getConnection()
        if (conection != null) {
            var query = "SELECT $columnas FROM $tabla"
            if (condicion != null && valores != null) {
                query.plus("WHERE $condicion")
            }
            try {
                conection.prepareStatement(query).use { preparedStatement: PreparedStatement ->
                    if (valores != null) {
                        var aux = 0
                        while (aux < valores.size) {
                            if (valores[aux].contains(".") && valores[aux].toFloatOrNull() != null) {
                                preparedStatement.setFloat(aux + 1, valores[aux].toFloat())

                            } else if (valores[aux].toIntOrNull() != null) {
                                preparedStatement.setInt(aux + 1, valores[aux].toInt())

                            } else {
                                preparedStatement.setString(aux + 1, valores[aux])

                            }
                            aux++
                        }
                    }

                    preparedStatement.executeQuery().use { resultSet: ResultSet ->
                        val metaData = resultSet.metaData
                        val columnCount = metaData.columnCount

                        while (resultSet.next()) {
                            var mapa: MutableMap<String, String> = mutableMapOf()
                            for (i in 1..columnCount) {
                                val tipo = metaData.getColumnTypeName(i)
                                val stri: String? = if (tipo == "INT") {
                                    resultSet.getInt(i).toString()
                                } else if (tipo == "DECIMAL") {
                                    resultSet.getFloat(i).toString()
                                } else if (tipo == "VARCHAR" || tipo == "CHAR" || tipo == "DATETIME") {
                                    resultSet.getString(i)
                                } else {
                                    null
                                }
                                if (stri != null) {
                                    mapa.put(metaData.getColumnName(i), stri)
                                }
                            }
                            res.add(mapa)
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            } finally {
                conection.close()
            }
        }
         return if (res.isEmpty()) {
             null
         } else {
             res
        }
    }
}

