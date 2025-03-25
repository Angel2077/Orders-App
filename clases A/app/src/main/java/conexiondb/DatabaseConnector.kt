package conexiondb

import android.os.StrictMode
import android.util.Log
import java.sql.Connection
import java.sql.DriverManager

object DatabaseConnector {
    private const val URL = "jdbc:mysql://dbucm.cl:3306/cdb105948_proyecto"
    private const val USER = "cdb105948_estudiante"
    private const val PASS = "aprueba2024"

    init {
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
    }
    fun getConnection(): Connection? {
        return try {
            Class.forName("com.mysql.jdbc.Driver")
            val connection = DriverManager.getConnection(URL, USER, PASS)
            Log.d("DatabaseConnector", "Conexión exitosa a la base de datos")
            connection
        } catch (e: Exception) {
            Log.e("DatabaseConnector", "Error al conectar a la base de datos", e)

            e.printStackTrace()
            null

        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
