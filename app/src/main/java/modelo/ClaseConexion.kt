package modelo

import java.sql.Connection
import java.sql.DriverManager
import java.sql.DriverManager.println

class ClaseConexion {

    fun cadenaConexion(): Connection?{
        try {
            val ip = "jdbc:oracle:thin:@192.168.1.2:1521:xe"
            val usuario = "AppBloom"
            val contrasena = "AppBloom"

            val conexion = DriverManager.getConnection(ip, usuario, contrasena)
            return conexion
        }catch (e: Exception){
            println("Este es el error: $e")
            return null
        }
    }
}