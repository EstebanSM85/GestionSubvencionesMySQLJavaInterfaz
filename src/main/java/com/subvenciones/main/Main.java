package com.subvenciones.main;

import com.subvenciones.conexion.ConexionDB;
import com.subvenciones.ui.VentanaPrincipal;

import javax.swing.*;

/**
 * Clase principal de la aplicación de gestión de subvenciones de la Unión Europea
 * Aplicación basada en componentes DAO que utiliza MySQL
 * 
 * @author Esteban Sanchez
 * @version 1.0.0
 */
public class Main {
    
    public static void main(String[] args) {
        System.out.println("==========================================================");
        System.out.println("  SISTEMA DE GESTIÓN DE SUBVENCIONES - UNIÓN EUROPEA");
        System.out.println("  Versión 1.0.0");
        System.out.println("  Autor: Esteban Sanchez - 2º DAM");
        System.out.println("==========================================================\n");
        
        // Configurar el Look and Feel del sistema
        configurarLookAndFeel();
        
        // Iniciar la aplicación en el hilo de eventos de Swing
        SwingUtilities.invokeLater(() -> {
            try {
                iniciarAplicacion();
            } catch (Exception e) {
                System.err.println("❌ Error al iniciar la aplicación: " + e.getMessage());
                e.printStackTrace();
                mostrarErrorInicio(e.getMessage());
            }
        });
    }
    
    /**
     * Inicializa la aplicación y sus componentes
     */
    private static void iniciarAplicacion() {
        System.out.println("Iniciando aplicación...\n");
        
        // PASO 1: Probar la conexión con MySQL
        System.out.println("PASO 1: Verificando conexión con MySQL...");
        boolean conectado = ConexionDB.probarConexion();
        
        if (!conectado) {
            String mensaje = "No se pudo conectar con MySQL.\n\n" +
                           "Verifique que:\n" +
                           "1. MySQL esté instalado y en ejecución\n" +
                           "2. La base de datos 'union_europea' exista\n" +
                           "3. El usuario 'root' tenga permisos de acceso\n" +
                           "4. La contraseña sea correcta (vacía por defecto)\n\n" +
                           "Puede crear la base de datos ejecutando:\n" +
                           "CREATE DATABASE union_europea;";
            mostrarErrorInicio(mensaje);
            return;
        }
        
        System.out.println("✓ Conexión con MySQL establecida correctamente\n");
        
        // PASO 2: Crear y mostrar la interfaz gráfica
        System.out.println("PASO 2: Inicializando interfaz gráfica...");
        VentanaPrincipal ventana = new VentanaPrincipal();
        
        // Agregar hook para cerrar la conexión al salir
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\n==========================================================");
            System.out.println("Cerrando aplicación...");
            ConexionDB.cerrarConexion();
            System.out.println("Aplicación cerrada correctamente");
            System.out.println("==========================================================");
        }));
        
        ventana.setVisible(true);
        System.out.println("✓ Interfaz gráfica lista\n");
        System.out.println("==========================================================");
        System.out.println("  APLICACIÓN INICIADA CORRECTAMENTE");
        System.out.println("  Base de datos: union_europea");
        System.out.println("  Tabla: subvenciones");
        System.out.println("==========================================================\n");
    }
    
    /**
     * Configura el Look and Feel de la interfaz
     */
    private static void configurarLookAndFeel() {
        try {
            // Intentar usar el Look and Feel del sistema
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("⚠ No se pudo configurar el Look and Feel del sistema");
            // Continuar con el Look and Feel por defecto
        }
    }
    
    /**
     * Muestra un mensaje de error al iniciar
     */
    private static void mostrarErrorInicio(String mensaje) {
        System.err.println("\n❌ ERROR CRÍTICO:\n" + mensaje + "\n");
        
        JOptionPane.showMessageDialog(null, 
            mensaje, 
            "Error al iniciar la aplicación", 
            JOptionPane.ERROR_MESSAGE);
        
        System.exit(1);
    }
}
