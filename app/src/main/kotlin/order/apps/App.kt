package order.apps

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.server.http.content.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import io.ktor.http.*
import java.sql.Connection
import java.sql.DriverManager
import java.sql.Statement
import java.util.*
import java.security.MessageDigest
import java.sql.SQLException


// --- Modelos de datos ---
@Serializable
data class Usuario(
    val id: Int? = null,
    val name: String,
    val lastName: String,
    val email: String,
    val password: String,
    val role: Int = 1, // 0 = admin, 1 = operador, 2 = cliente (ejemplo)
)

@Serializable
data class Operador(
    val userId: Int,
    val adminId: Int
)

@Serializable
data class Sesion(
    val token: String,
    val userId: Int,
    val role: Int
)

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

@Serializable
data class LoginResponse(
    val token: String,
    val user: Usuario // Asegúrate de que Usuario no expone la contraseña real
)

// Clase para mensajes de respuesta simples
@Serializable
data class MessageResponse(val message: String)


// --- Almacenamiento en memoria de sesiones (considera JWT para producción) ---
val sesiones = mutableMapOf<String, Sesion>()

// --- Configuración de la base de datos ---
val DB_URL = "jdbc:mysql://34.176.136.79:3306/USER"
val DB_USER = "order"
val DB_PASSWORD = "8H|AG>GGH5iJ]7f["

// --- Funciones auxiliares de base de datos y utilidades ---

// Función para conectar a la base de datos MySQL
fun conectarDB(): Connection? {
    println("DEBUG_DB – Intentando conectar a DB: $DB_URL con usuario $DB_USER")
    return try {
        val connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)
        println("DEBUG_DB – Conexión a DB exitosa.")
        connection
    } catch (e: SQLException) {
        System.err.println("ERROR_DB – Falló la conexión a la base de datos: ${e.message}")
        e.printStackTrace()
        null
    }
}

// Función para hashear contraseñas usando SHA-256
fun hashPassword(password: String): String {
    val md = MessageDigest.getInstance("SHA-256")
    val hashedBytes = md.digest(password.toByteArray())
    return hashedBytes.joinToString("") { "%02x".format(it) }
}

// Función para verificar si un email ya existe en la base de datos
fun emailExiste(email: String, connection: Connection): Boolean {
    println("DEBUG_DB – Verificando si email '$email' existe.")
    val stmt = connection.prepareStatement("SELECT ID FROM USERS WHERE EMAIL = ?")
    stmt.setString(1, email)
    val rs = stmt.executeQuery()
    val existe = rs.next()
    println("DEBUG_DB – emailExiste('$email') -> $existe")
    return existe
}

// Función para registrar un nuevo usuario en la base de datos
fun register(usuario: Usuario): Boolean {
    println("DEBUG_REGISTER – Intentando registrar usuario: ${usuario.email}")
    var connection: Connection? = null
    return try {
        connection = conectarDB() ?: return false
        if (emailExiste(usuario.email, connection)) {
            println("DEBUG_REGISTER – Registro detenido: el email ya existe")
            return false
        }
        val stmt = connection.prepareStatement("""
            INSERT INTO USERS (NAME, LAST_NAME, EMAIL, _PASSWORD, ROLE)
            VALUES (?, ?, ?, ?, ?)
        """.trimIndent(), Statement.RETURN_GENERATED_KEYS)

        stmt.setString(1, usuario.name)
        stmt.setString(2, usuario.lastName)
        stmt.setString(3, usuario.email)
        stmt.setString(4, usuario.password) // Contraseña ya hasheada
        stmt.setInt(5, usuario.role)

        val affectedRows = stmt.executeUpdate()
        if (affectedRows == 0) {
            println("DEBUG_REGISTER – No se pudo insertar el usuario: filas afectadas 0")
            return false
        }

        val generatedKeys = stmt.generatedKeys
        if (generatedKeys.next()) {
            val nuevoId = generatedKeys.getInt(1)
            println("DEBUG_REGISTER – Usuario insertado con ID = $nuevoId")
            true
        } else {
            println("DEBUG_REGISTER – No se pudo obtener el ID generado después de la inserción")
            false
        }
    } catch (e: SQLException) {
        System.err.println("ERROR_REGISTER_DB – Falló el registro en DB: ${e.message}")
        e.printStackTrace()
        false
    } finally {
        connection?.close()
    }
}

// Función para verificar credenciales de login
fun loginVerified(email: String, hashedPassword: String): Usuario? {
    var connection: Connection? = null
    return try {
        connection = conectarDB()
        if (connection == null) return null

        val stmt = connection.prepareStatement("SELECT ID, NAME, LAST_NAME, EMAIL, _PASSWORD, ROLE FROM USERS WHERE EMAIL= ? AND _PASSWORD = ?")
        stmt.setString(1, email)
        stmt.setString(2, hashedPassword)
        val rs = stmt.executeQuery()
        if (rs.next()) {
            Usuario(
                id = rs.getInt("ID"),
                name = rs.getString("NAME"),
                lastName = rs.getString("LAST_NAME"),
                email = rs.getString("EMAIL"),
                password = "", // ¡Importante! No devolver la contraseña real al cliente
                role = rs.getInt("ROLE"),
            )
        } else {
            null
        }
    } catch (e: SQLException) {
        System.err.println("ERROR_LOGIN_VERIFIED_DB – Falló la verificación de login en DB: ${e.message}")
        e.printStackTrace()
        null
    } finally {
        connection?.close()
    }
}

// Función para listar operadores asociados a un administrador
fun listarOperadores(adminId: Int): List<Usuario> {
    val operadores = mutableListOf<Usuario>()
    var connection: Connection? = null
    try {
        connection = conectarDB()
        if (connection == null) {
            println("ERROR_LISTAR_OPERADORES_DB: Conexión a DB es nula.")
            return emptyList()
        }

        val sql = """
            SELECT u.ID, u.NAME, u.LAST_NAME, u.EMAIL, u.ROLE, u._PASSWORD
            FROM USERS u
            JOIN OPERATOR o ON u.ID = o.USER_ID
            WHERE o.ADMIN_ID = ?
        """.trimIndent()
        val statement = connection.prepareStatement(sql)
        statement.setInt(1, adminId)
        println("DEBUG_LISTAR_OPERADORES_DB: Ejecutando consulta para adminId: $adminId")
        val resultSet = statement.executeQuery()

        while (resultSet.next()) {
            val operador = Usuario(
                id = resultSet.getInt("ID"),
                name = resultSet.getString("NAME"), // ¡Verifica que estos nombres de columna sean correctos en tu DB!
                lastName = resultSet.getString("LAST_NAME"), // ej. "name", "last_name"
                email = resultSet.getString("EMAIL"),
                password = "", // No enviar la contraseña real al cliente
                role = resultSet.getInt("ROLE")
            )
            operadores.add(operador)
            println("DEBUG_LISTAR_OPERADORES_DB: Agregado operador: ID=${operador.id}, Name=${operador.name}, Email=${operador.email}")
        }
        println("DEBUG_LISTAR_OPERADORES_DB: Total de operadores encontrados: ${operadores.size}")
    } catch (e: SQLException) {
        System.err.println("ERROR_LISTAR_OPERADORES_DB: ${e.message}")
        e.printStackTrace()
    } finally {
        connection?.close()
    }
    return operadores
}

// Función para eliminar un operador de la base de datos
fun eliminarOperador(userId: Int, adminId: Int): Boolean {
    var connection: Connection? = null // Declara la conexión fuera del try-catch para un manejo adecuado
    var committed = false // Para rastrear si la transacción se ha confirmado

    return try {
        connection = conectarDB()
        if (connection == null) return false

        connection.autoCommit = false // Iniciar transacción explícitamente

        // Verificar que el administrador tiene permiso para eliminar a este operador
        val checkOwnershipSql = "SELECT COUNT(*) FROM OPERATOR WHERE USER_ID = ? AND ADMIN_ID = ?"
        val checkStmt = connection.prepareStatement(checkOwnershipSql) // Usar 'connection' aquí
        checkStmt.setInt(1, userId)
        checkStmt.setInt(2, adminId)
        val rs = checkStmt.executeQuery()
        if (rs.next() && rs.getInt(1) == 0) {
            println("DEBUG_ELIMINAR_OPERADOR_DB: Admin $adminId no tiene permiso para eliminar a User $userId o no es su operador.")
            connection.rollback() // Rollback si no hay permiso
            return false
        }
        println("DEBUG_ELIMINAR_OPERADOR_DB: Admin $adminId tiene permiso para eliminar a User $userId.")


        // Eliminar de la tabla OPERATOR
        val deleteOperatorSql = "DELETE FROM OPERATOR WHERE USER_ID = ?"
        val deleteOperatorStmt = connection.prepareStatement(deleteOperatorSql) // Usar 'connection' aquí
        deleteOperatorStmt.setInt(1, userId)
        val affectedRowsOperator = deleteOperatorStmt.executeUpdate()
        println("DEBUG_ELIMINAR_OPERADOR_DB: Filas afectadas en OPERATOR: $affectedRowsOperator")

        // Eliminar de la tabla USERS (solo si es un operador, rol = 1)
        val deleteUserSql = "DELETE FROM USERS WHERE ID = ? AND ROLE = 1"
        val deleteUserStmt = connection.prepareStatement(deleteUserSql) // Usar 'connection' aquí
        deleteUserStmt.setInt(1, userId)
        val affectedRowsUser = deleteUserStmt.executeUpdate()
        println("DEBUG_ELIMINAR_OPERADOR_DB: Filas afectadas en USERS: $affectedRowsUser")

        if (affectedRowsOperator > 0 && affectedRowsUser > 0) {
            connection.commit() // Confirmar transacción
            committed = true // Marcar como confirmado
            println("DEBUG_ELIMINAR_OPERADOR_DB: Operador $userId eliminado exitosamente.")
            true
        } else {
            connection.rollback() // Revertir transacción
            println("DEBUG_ELIMINAR_OPERADOR_DB: Falló la eliminación del operador $userId. Filas afectadas OPERATOR: $affectedRowsOperator, USERS: $affectedRowsUser.")
            false
        }
    } catch (e: SQLException) {
        System.err.println("ERROR_ELIMINAR_OPERADOR_DB: ${e.message}")
        e.printStackTrace()
        // Si la transacción no se ha confirmado, intentar un rollback
        if (connection != null && !committed) {
            try {
                connection.rollback()
                println("DEBUG_ELIMINAR_OPERADOR_DB: Rollback de transacción debido a error.")
            } catch (rbEx: SQLException) {
                System.err.println("ERROR_ELIMINAR_OPERADOR_DB: Falló el rollback: ${rbEx.message}")
            }
        }
        false
    } finally {
        // Asegurarse de que la conexión se cierra y el autoCommit se restaura
        if (connection != null) {
            try {
                connection.autoCommit = true // Restaurar autoCommit
                connection.close() // Cerrar la conexión
            } catch (closeEx: SQLException) {
                System.err.println("ERROR_ELIMINAR_OPERADOR_DB: Falló el cierre de conexión o restauración de autoCommit: ${closeEx.message}")
            }
        }
    }
}


// --- Validación de sesión/token (NO es JWT real, es un mapa en memoria) ---
fun validarSesion(token: String?, soloAdmin: Boolean = false): Sesion? {
    if (token == null) {
        println("DEBUG_VALIDAR_SESION: Token es nulo.")
        return null
    }
    val sesion = sesiones[token]
    if (sesion == null) {
        println("DEBUG_VALIDAR_SESION: Sesión no encontrada para el token: $token")
        return null
    }
    if (soloAdmin && sesion.role != 0) {
        println("DEBUG_VALIDAR_SESION: Acceso denegado. Rol de usuario ${sesion.role} no es administrador para el token: $token")
        return null
    }
    println("DEBUG_VALIDAR_SESION: Sesión válida para UserID: ${sesion.userId}, Role: ${sesion.role}")
    return sesion
}

// --- Función principal de la aplicación Ktor ---
fun main() {
    embeddedServer(Netty, port = 80, host = "0.0.0.0") {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                encodeDefaults = true
            })
        }

        routing {
            // Sirve archivos estáticos desde el directorio 'static'
            staticResources("/", "static") {
                default("index.html")
            }

            // Endpoint para el login de usuarios
            post("/login") {
                println("DEBUG_ROUTE – Petición POST /login recibida.")
                try {
                    val loginRequest = call.receive<LoginRequest>()
                    println("DEBUG_LOGIN – LoginRequest recibido: email=${loginRequest.email}")

                    val email = loginRequest.email
                    val hashedPassword = hashPassword(loginRequest.password)

                    if (email.isBlank() || hashedPassword.isBlank()) {
                        call.respond(HttpStatusCode.BadRequest, MessageResponse("Email y contraseña vacíos"))
                        return@post
                    }

                    val usuario = loginVerified(email, hashedPassword)
                    if (usuario != null) {
                        val token = UUID.randomUUID().toString()
                        sesiones[token] = Sesion(token, usuario.id!!, usuario.role)
                        call.respond(HttpStatusCode.OK, LoginResponse(token = token, user = usuario))
                        println("DEBUG_LOGIN: Login exitoso para ${usuario.email}, token generado.")
                    } else {
                        call.respond(HttpStatusCode.Unauthorized, MessageResponse("Credenciales incorrectas"))
                        println("DEBUG_LOGIN: Fallo de login para ${email}: credenciales incorrectas.")
                    }
                } catch (e: Exception) {
                    System.err.println("ERROR_LOGIN – Error al deserializar LoginRequest o en lógica de login: ${e.message}")
                    e.printStackTrace()
                    call.respond(HttpStatusCode.BadRequest, MessageResponse("Formato de credenciales incorrecto o error interno."))
                }
            }

            // Endpoint para el registro de nuevos usuarios
            post("/register") {
                println("DEBUG_ROUTE – Petición POST /register recibida.")
                try {
                    val nuevo = call.receive<Usuario>()
                    println("DEBUG_REGISTER – Usuario recibido para registro: ${nuevo.email}")

                    // Solo permite registrar usuarios con un rol específico si es necesario, aquí 2 (cliente)
                    // Puedes ajustar esto según la lógica de tu aplicación
                    val usuarioParaRegistro = nuevo.copy(
                        password = hashPassword(nuevo.password),
                        role = 2 // Asume que el registro público es solo para clientes
                    )

                    val ok = register(usuarioParaRegistro)
                    if (ok) {
                        call.respond(HttpStatusCode.Created, MessageResponse("Usuario registrado correctamente"))
                        println("DEBUG_REGISTER: Usuario ${usuarioParaRegistro.email} registrado exitosamente.")
                    } else {
                        call.respond(HttpStatusCode.BadRequest, MessageResponse("Error al registrar o email ya existe"))
                        println("DEBUG_REGISTER: Fallo al registrar usuario ${usuarioParaRegistro.email}.")
                    }
                } catch (e: Exception) {
                    System.err.println("ERROR_REGISTER – Error al deserializar Usuario o en lógica de registro: ${e.message}")
                    e.printStackTrace()
                    call.respond(HttpStatusCode.BadRequest, MessageResponse("Formato de datos de usuario incorrecto o error interno."))
                }
            }

            // Rutas protegidas para el panel de administración
            route("/admin") {
                // Endpoint para crear un nuevo operador (solo accesible por administradores)
                post("/crear-operador") {
                    println("DEBUG_ROUTE – Petición POST /admin/crear-operador recibida.")
                    val token = call.request.headers["Authorization"]
                    val sesion = validarSesion(token, soloAdmin = true)
                    if (sesion == null) {
                        call.respond(HttpStatusCode.Unauthorized, MessageResponse("Token inválido o no autorizado."))
                        return@post
                    }
                    val adminId = sesion.userId // Obtiene el ID del admin de la sesión validada
                    println("DEBUG_CREAR_OPERADOR_ENDPOINT: Admin ID de sesión: $adminId")

                    try {
                        val nuevoOperadorRequest = call.receive<Usuario>()
                        // Ignoramos 'id' y 'role' de la desestructuración ya que 'id' se genera en DB y 'role' es fijo a 1.
                        val (_, name, lastName, email, password, _) = nuevoOperadorRequest

                        if (email.isBlank() || password.isBlank()) {
                            call.respond(HttpStatusCode.BadRequest, MessageResponse("Email y contraseña son obligatorios."))
                            return@post
                        }

                        // Uso de la función crearOperadorBD encapsulada
                        val success = crearOperadorBD(name, lastName, email, password, 1, adminId) // Rol 1 para operador
                        if (success) {
                            call.respond(HttpStatusCode.Created, MessageResponse("Operador creado exitosamente."))
                            println("DEBUG_CREAR_OPERADOR_ENDPOINT: Operador $email creado por Admin $adminId.")
                        } else {
                            // La función crearOperadorBD ya maneja emailExiste internamente,
                            // pero aquí puedes dar un mensaje más genérico si no se sabe la causa exacta.
                            call.respond(HttpStatusCode.InternalServerError, MessageResponse("Error al crear operador (posiblemente email ya registrado o error de DB)."))
                            println("DEBUG_CREAR_OPERADOR_ENDPOINT: Fallo al crear operador $email por Admin $adminId.")
                        }
                    } catch (e: Exception) {
                        System.err.println("ERROR_CREAR_OPERADOR_ENDPOINT: ${e.message}")
                        e.printStackTrace()
                        call.respond(HttpStatusCode.BadRequest, MessageResponse("Formato de datos de operador incorrecto o error interno: ${e.message}"))
                    }
                }

                // Endpoint para listar operadores (solo accesible por administradores)
                get("/listar-operadores") {
                    println("DEBUG_ROUTE – Petición GET /admin/listar-operadores recibida.")
                    val token = call.request.headers["Authorization"]
                    val sesion = validarSesion(token, soloAdmin = true)
                    if (sesion == null) {
                        call.respond(HttpStatusCode.Unauthorized, MessageResponse("Token inválido o no autorizado."))
                        return@get
                    }
                    val adminId = sesion.userId
                    println("DEBUG_LISTAR_OPERADORES_ENDPOINT: Admin ID de sesión: $adminId")

                    val operadores = listarOperadores(adminId)
                    println("DEBUG_LISTAR_OPERADORES_ENDPOINT: Operadores obtenidos de DB: ${operadores.size} elementos.")
                    call.respond(HttpStatusCode.OK, operadores)
                }

                // Endpoint para eliminar un operador (solo accesible por administradores)
                delete("/eliminar-operador/{userId}") {
                    println("DEBUG_ROUTE – Petición DELETE /admin/eliminar-operador recibida.")
                    val token = call.request.headers["Authorization"]
                    val sesion = validarSesion(token, soloAdmin = true)
                    if (sesion == null) {
                        call.respond(HttpStatusCode.Unauthorized, MessageResponse("Token inválido o no autorizado."))
                        return@delete
                    }

                    val userIdToDelete = call.parameters["userId"]?.toIntOrNull()
                    if (userIdToDelete == null) {
                        call.respond(HttpStatusCode.BadRequest, MessageResponse("ID de usuario inválido."))
                        return@delete
                    }
                    val adminId = sesion.userId
                    println("DEBUG_ELIMINAR_OPERADOR_ENDPOINT: Admin ID: $adminId, Intentando eliminar operador con User ID: $userIdToDelete")

                    try {
                        val success = eliminarOperador(userIdToDelete, adminId)
                        if (success) {
                            call.respond(HttpStatusCode.OK, MessageResponse("Operador eliminado correctamente."))
                            println("DEBUG_ELIMINAR_OPERADOR_ENDPOINT: Operador $userIdToDelete eliminado por Admin $adminId.")
                        } else {
                            call.respond(HttpStatusCode.InternalServerError, MessageResponse("Error al eliminar operador (no se encontró, no es un operador válido, o no tienes permiso)."))
                            println("DEBUG_ELIMINAR_OPERADOR_ENDPOINT: Fallo al eliminar operador $userIdToDelete por Admin $adminId.")
                        }
                    } catch (e: Exception) {
                        System.err.println("ERROR_ELIMINAR_OPERADOR_ENDPOINT: ${e.message}")
                        e.printStackTrace()
                        call.respond(HttpStatusCode.InternalServerError, MessageResponse("Error interno al eliminar operador: ${e.message}"))
                    }
                }
            }
        }
    }.start(wait = true)
}

// Función para crear un usuario y un operador - Encapsula la lógica de DB
fun crearOperadorBD(name: String, lastName: String, email: String, password: String, role: Int, adminId: Int): Boolean {
    var connection: Connection? = null
    try {
        connection = conectarDB()
        if (connection == null) {
            println("DEBUG_CREAR_OPERADOR_DB: Conexión a DB es nula, no se puede crear operador.")
            return false
        }

        // Iniciar transacción
        connection.autoCommit = false

        if (emailExiste(email, connection)) {
            connection.rollback()
            println("DEBUG_CREAR_OPERADOR_DB: Email '$email' ya existe, rollback.")
            return false
        }

        val hashedPassword = hashPassword(password)

        // 1. Insertar en la tabla USERS
        val insertUserSql = "INSERT INTO USERS (NAME, LAST_NAME, EMAIL, _PASSWORD, ROLE) VALUES (?, ?, ?, ?, ?)"
        val userStatement = connection.prepareStatement(insertUserSql, Statement.RETURN_GENERATED_KEYS)
        userStatement.setString(1, name)
        userStatement.setString(2, lastName)
        userStatement.setString(3, email)
        userStatement.setString(4, hashedPassword)
        userStatement.setInt(5, role)
        val affectedRows = userStatement.executeUpdate()

        if (affectedRows == 0) {
            connection.rollback()
            println("DEBUG_CREAR_OPERADOR_DB: No se insertó el usuario en USERS, filas afectadas 0, rollback.")
            return false
        }

        val generatedKeys = userStatement.generatedKeys
        val newUserId: Int
        if (generatedKeys.next()) {
            newUserId = generatedKeys.getInt(1)
            println("DEBUG_CREAR_OPERADOR_DB: Nuevo usuario ID generado: $newUserId")
        } else {
            connection.rollback()
            println("DEBUG_CREAR_OPERADOR_DB: No se pudo obtener el ID generado del usuario, rollback.")
            return false
        }

        // 2. Insertar en la tabla OPERATOR
        val insertOperatorSql = "INSERT INTO OPERATOR (USER_ID, ADMIN_ID) VALUES (?, ?)"
        val operatorStatement = connection.prepareStatement(insertOperatorSql)
        operatorStatement.setInt(1, newUserId)
        operatorStatement.setInt(2, adminId)
        val affectedOperatorRows = operatorStatement.executeUpdate()

        if (affectedOperatorRows == 0) {
            connection.rollback()
            println("DEBUG_CREAR_OPERADOR_DB: No se insertó la relación en OPERATOR, filas afectadas 0, rollback.")
            return false
        }

        connection.commit() // Confirmar transacción
        println("DEBUG_CREAR_OPERADOR_DB: Operador creado y asociado exitosamente para User ID: $newUserId.")
        return true
    } catch (e: SQLException) {
        System.err.println("ERROR_CREAR_OPERADOR_DB: Error de SQL al crear operador: ${e.message}")
        e.printStackTrace()
        connection?.rollback() // Revertir en caso de error
        return false
    } finally {
        connection?.autoCommit = true // Restaurar auto-commit
        connection?.close()
    }
}
