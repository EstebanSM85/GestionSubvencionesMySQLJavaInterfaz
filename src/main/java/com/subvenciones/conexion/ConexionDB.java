package com.subvenciones.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Componente encargado de gestionar la conexión con la base de datos MySQL
 */
public class ConexionDB {
    
    // Parámetros de conexión
    private static final String URL = "jdbc:mysql://localhost:3306/union_europea";
    private static final String USUARIO = "root";
    private static final String PASSWORD = "";
    
    // Instancia única de conexión (patrón Singleton)
    private static Connection conexion = null;
    
    /**
     * Obtiene una conexión a la base de datos
     * @return Objeto Connection
     */
    public static Connection getConexion() {
        try {
            if (conexion == null || conexion.isClosed()) {
                // Cargar el driver de MySQL
                Class.forName("com.mysql.cj.jdbc.Driver");
                
                // Establecer la conexión
                conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
                
                System.out.println("✓ Conexión establecida con MySQL");
                System.out.println("  Base de datos: union_europea");
            }
            return conexion;
            
        } catch (ClassNotFoundException e) {
            System.err.println("❌ Error: Driver de MySQL no encontrado");
            e.printStackTrace();
            return null;
            
        } catch (SQLException e) {
            System.err.println("❌ Error al conectar con MySQL");
            System.err.println("  Verifica que MySQL esté ejecutándose");
            System.err.println("  URL: " + URL);
            System.err.println("  Usuario: " + USUARIO);
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Cierra la conexión con la base de datos
     */
    public static void cerrarConexion() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                System.out.println("✓ Conexión cerrada correctamente");
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al cerrar la conexión");
            e.printStackTrace();
        }
    }
    
    /**
     * Verifica si hay conexión activa
     * @return true si hay conexión, false en caso contrario
     */
    public static boolean estaConectado() {
        try {
            return conexion != null && !conexion.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
    
    /**
     * Método de prueba de conexión
     */
    public static boolean probarConexion() {
        Connection conn = getConexion();
        if (conn != null) {
            System.out.println("✓ Prueba de conexión exitosa");
            return true;
        } else {
            System.out.println("❌ Prueba de conexión fallida");
            return false;
        }
    }
}