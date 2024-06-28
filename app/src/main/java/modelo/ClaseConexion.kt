package modelo

import java.sql.Connection
import java.sql.DriverManager

class ClaseConexion {

    fun cadenaConexion(): Connection?{
        try {
            val ip = "jdbc:oracle:thin:@10.10.1.115:1521:xe"
            val usuario = "DANIEL_DEVELOPER"
            val contrasena = "20200008"

            val conexion = DriverManager.getConnection(ip, usuario, contrasena)
            return conexion
        }catch (e: Exception){
            println("Este es el error: $e")
            return null
        }
    }
}