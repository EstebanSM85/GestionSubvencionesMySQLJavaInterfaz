package com.subvenciones.ui;

import com.subvenciones.dao.SubvencionDAO;
import com.subvenciones.modelo.Subvencion;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * Ventana principal de la aplicación de gestión de subvenciones
 */
public class VentanaPrincipal extends JFrame {
    
    private SubvencionDAO subvencionDAO;
    
    // Componentes de la interfaz
    private JTable tablaSubvenciones;
    private DefaultTableModel modeloTabla;
    private JTextField txtPais;
    private JComboBox<String> cmbTipoSubvencion;
    private JTextField txtImporte;
    private JButton btnCrear;
    private JButton btnActualizar;
    private JButton btnEliminar;
    private JButton btnRefrescar;
    private JLabel lblEstadoConexion;
    private JLabel lblTotalSubvenciones;
    private JLabel lblImporteTotal;
    
    private int subvencionSeleccionadaId = -1;
    
    private NumberFormat formatoMoneda;
    
    /**
     * Constructor de la ventana
     */
    public VentanaPrincipal() {
        this.subvencionDAO = new SubvencionDAO();
        this.formatoMoneda = NumberFormat.getCurrencyInstance(new Locale("es", "ES"));
        
        inicializarComponentes();
        configurarEventos();
        cargarSubvenciones();
        actualizarEstadisticas();
    }
    
    /**
     * Inicializa todos los componentes de la interfaz
     */
    private void inicializarComponentes() {
        setTitle("Sistema de Gestión de Subvenciones - Unión Europea");
        setSize(1100, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        
        // Panel superior - Estado de conexión
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBackground(new Color(0, 51, 153)); // Azul UE
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        JLabel lblTitulo = new JLabel("🇪🇺 GESTIÓN DE SUBVENCIONES - UNIÓN EUROPEA");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        
        lblEstadoConexion = new JLabel("● Conectado a MySQL");
        lblEstadoConexion.setForeground(new Color(255, 204, 0)); // Amarillo UE
        lblEstadoConexion.setFont(new Font("Arial", Font.BOLD, 14));
        
        panelSuperior.add(lblTitulo, BorderLayout.WEST);
        panelSuperior.add(lblEstadoConexion, BorderLayout.EAST);
        
        add(panelSuperior, BorderLayout.NORTH);
        
        // Panel central - Tabla de subvenciones
        JPanel panelCentral = new JPanel(new BorderLayout(10, 10));
        panelCentral.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        String[] columnas = {"ID", "País Asignado", "Tipo de Subvención", "Importe (€)"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaSubvenciones = new JTable(modeloTabla);
        tablaSubvenciones.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaSubvenciones.getTableHeader().setReorderingAllowed(false);
        tablaSubvenciones.setRowHeight(28);
        tablaSubvenciones.setFont(new Font("Arial", Font.PLAIN, 13));
        tablaSubvenciones.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        
        // Ajustar ancho de columnas
        tablaSubvenciones.getColumnModel().getColumn(0).setPreferredWidth(50);
        tablaSubvenciones.getColumnModel().getColumn(1).setPreferredWidth(150);
        tablaSubvenciones.getColumnModel().getColumn(2).setPreferredWidth(180);
        tablaSubvenciones.getColumnModel().getColumn(3).setPreferredWidth(150);
        
        JScrollPane scrollTabla = new JScrollPane(tablaSubvenciones);
        scrollTabla.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(0, 51, 153), 2),
            "Listado de Subvenciones",
            0, 0, new Font("Arial", Font.BOLD, 14), new Color(0, 51, 153)
        ));
        
        panelCentral.add(scrollTabla, BorderLayout.CENTER);
        
        // Panel de estadísticas
        JPanel panelEstadisticas = new JPanel(new GridLayout(1, 2, 20, 0));
        panelEstadisticas.setBackground(new Color(240, 240, 240));
        panelEstadisticas.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        lblTotalSubvenciones = new JLabel("Total Subvenciones: 0");
        lblTotalSubvenciones.setFont(new Font("Arial", Font.BOLD, 14));
        lblTotalSubvenciones.setForeground(new Color(0, 51, 153));
        
        lblImporteTotal = new JLabel("Importe Total: 0,00 €");
        lblImporteTotal.setFont(new Font("Arial", Font.BOLD, 14));
        lblImporteTotal.setForeground(new Color(0, 102, 51));
        
        panelEstadisticas.add(lblTotalSubvenciones);
        panelEstadisticas.add(lblImporteTotal);
        
        panelCentral.add(panelEstadisticas, BorderLayout.SOUTH);
        
        add(panelCentral, BorderLayout.CENTER);
        
        // Panel derecho - Formulario
        JPanel panelDerecho = new JPanel();
        panelDerecho.setLayout(new BoxLayout(panelDerecho, BoxLayout.Y_AXIS));
        panelDerecho.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelDerecho.setPreferredSize(new Dimension(380, 0));
        panelDerecho.setBackground(new Color(250, 250, 250));
        
        // Título del formulario
        JLabel lblFormulario = new JLabel("Gestión de Subvenciones");
        lblFormulario.setFont(new Font("Arial", Font.BOLD, 16));
        lblFormulario.setForeground(new Color(0, 51, 153));
        lblFormulario.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelDerecho.add(lblFormulario);
        panelDerecho.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Campo País
        JLabel lblPais = new JLabel("País Asignado:");
        lblPais.setFont(new Font("Arial", Font.BOLD, 12));
        lblPais.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelDerecho.add(lblPais);
        
        txtPais = new JTextField();
        txtPais.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        txtPais.setFont(new Font("Arial", Font.PLAIN, 13));
        txtPais.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelDerecho.add(txtPais);
        panelDerecho.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Campo Tipo de Subvención
        JLabel lblTipo = new JLabel("Tipo de Subvención:");
        lblTipo.setFont(new Font("Arial", Font.BOLD, 12));
        lblTipo.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelDerecho.add(lblTipo);
        
        String[] tiposSubvencion = {
            "Agrícola", 
            "Industrial", 
            "Tecnológica", 
            "Educativa", 
            "Sanitaria",
            "Infraestructura",
            "Medioambiental",
            "Cultural",
            "Investigación"
        };
        cmbTipoSubvencion = new JComboBox<>(tiposSubvencion);
        cmbTipoSubvencion.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        cmbTipoSubvencion.setFont(new Font("Arial", Font.PLAIN, 13));
        cmbTipoSubvencion.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelDerecho.add(cmbTipoSubvencion);
        panelDerecho.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Campo Importe
        JLabel lblImporte = new JLabel("Importe (€):");
        lblImporte.setFont(new Font("Arial", Font.BOLD, 12));
        lblImporte.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelDerecho.add(lblImporte);
        
        txtImporte = new JTextField();
        txtImporte.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        txtImporte.setFont(new Font("Arial", Font.PLAIN, 13));
        txtImporte.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelDerecho.add(txtImporte);
        
        JLabel lblAyudaImporte = new JLabel("Ejemplo: 40000000 o 40000000.50");
        lblAyudaImporte.setFont(new Font("Arial", Font.ITALIC, 11));
        lblAyudaImporte.setForeground(Color.GRAY);
        lblAyudaImporte.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelDerecho.add(lblAyudaImporte);
        panelDerecho.add(Box.createRigidArea(new Dimension(0, 25)));
        
        // Botones de acción
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new GridLayout(2, 2, 10, 10));
        panelBotones.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        panelBotones.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelBotones.setOpaque(false);
        
        btnCrear = new JButton("Crear");
        btnCrear.setBackground(new Color(76, 175, 80));
        btnCrear.setForeground(Color.BLACK);
        btnCrear.setFont(new Font("Arial", Font.BOLD, 13));
        btnCrear.setFocusPainted(false);
        
        btnActualizar = new JButton("Actualizar");
        btnActualizar.setBackground(new Color(33, 150, 243));
        btnActualizar.setForeground(Color.BLACK);
        btnActualizar.setFont(new Font("Arial", Font.BOLD, 13));
        btnActualizar.setFocusPainted(false);
        btnActualizar.setEnabled(false);
        
        btnEliminar = new JButton("Eliminar");
        btnEliminar.setBackground(new Color(244, 67, 54));
        btnEliminar.setForeground(Color.BLACK);
        btnEliminar.setFont(new Font("Arial", Font.BOLD, 13));
        btnEliminar.setFocusPainted(false);
        btnEliminar.setEnabled(false);
        
        btnRefrescar = new JButton("Refrescar");
        btnRefrescar.setBackground(new Color(158, 158, 158));
        btnRefrescar.setForeground(Color.BLACK);
        btnRefrescar.setFont(new Font("Arial", Font.BOLD, 13));
        btnRefrescar.setFocusPainted(false);
        
        panelBotones.add(btnCrear);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnRefrescar);
        
        panelDerecho.add(panelBotones);
        
        add(panelDerecho, BorderLayout.EAST);
    }
    
    /**
     * Configura los eventos de los componentes
     */
    private void configurarEventos() {
        // Selección de fila en la tabla
        tablaSubvenciones.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int filaSeleccionada = tablaSubvenciones.getSelectedRow();
                if (filaSeleccionada >= 0) {
                    cargarSubvencionEnFormulario(filaSeleccionada);
                    btnActualizar.setEnabled(true);
                    btnEliminar.setEnabled(true);
                } else {
                    limpiarFormulario();
                    btnActualizar.setEnabled(false);
                    btnEliminar.setEnabled(false);
                }
            }
        });
        
        // Botón Crear
        btnCrear.addActionListener(e -> crearSubvencion());
        
        // Botón Actualizar
        btnActualizar.addActionListener(e -> actualizarSubvencion());
        
        // Botón Eliminar
        btnEliminar.addActionListener(e -> eliminarSubvencion());
        
        // Botón Refrescar
        btnRefrescar.addActionListener(e -> {
            cargarSubvenciones();
            actualizarEstadisticas();
        });
    }
    
    /**
     * Carga todas las subvenciones en la tabla
     */
    private void cargarSubvenciones() {
        modeloTabla.setRowCount(0);
        List<Subvencion> subvenciones = subvencionDAO.obtenerTodas();
        
        for (Subvencion sub : subvenciones) {
            Object[] fila = {
                sub.getIdSubvencion(),
                sub.getPaisAsignado(),
                sub.getTipoSubvencion(),
                formatoMoneda.format(sub.getImporte())
            };
            modeloTabla.addRow(fila);
        }
        
        lblEstadoConexion.setText("● Conectado - " + subvenciones.size() + " subvenciones cargadas");
    }
    
    /**
     * Carga los datos de una subvención en el formulario
     */
    private void cargarSubvencionEnFormulario(int fila) {
        subvencionSeleccionadaId = (int) modeloTabla.getValueAt(fila, 0);
        
        Subvencion subvencion = subvencionDAO.obtenerPorId(subvencionSeleccionadaId);
        
        if (subvencion != null) {
            txtPais.setText(subvencion.getPaisAsignado());
            cmbTipoSubvencion.setSelectedItem(subvencion.getTipoSubvencion());
            txtImporte.setText(subvencion.getImporte().toString());
        }
    }
    
    /**
     * Crea una nueva subvención
     */
    private void crearSubvencion() {
        if (!validarFormulario()) {
            return;
        }
        
        try {
            String pais = txtPais.getText().trim();
            String tipo = (String) cmbTipoSubvencion.getSelectedItem();
            BigDecimal importe = new BigDecimal(txtImporte.getText().trim());
            
            if (importe.compareTo(BigDecimal.ZERO) <= 0) {
                JOptionPane.showMessageDialog(this, 
                    "El importe debe ser mayor que 0", 
                    "Error de validación", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Subvencion nuevaSubvencion = new Subvencion(pais, tipo, importe);
            
            if (subvencionDAO.insertar(nuevaSubvencion)) {
                JOptionPane.showMessageDialog(this, 
                    "Subvención creada exitosamente\nID: " + nuevaSubvencion.getIdSubvencion());
                limpiarFormulario();
                cargarSubvenciones();
                actualizarEstadisticas();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Error al crear la subvención", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "El importe debe ser un número válido", 
                "Error de formato", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Actualiza la subvención seleccionada
     */
    private void actualizarSubvencion() {
        if (subvencionSeleccionadaId == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una subvención primero");
            return;
        }
        
        if (!validarFormulario()) {
            return;
        }
        
        try {
            String pais = txtPais.getText().trim();
            String tipo = (String) cmbTipoSubvencion.getSelectedItem();
            BigDecimal importe = new BigDecimal(txtImporte.getText().trim());
            
            if (importe.compareTo(BigDecimal.ZERO) <= 0) {
                JOptionPane.showMessageDialog(this, 
                    "El importe debe ser mayor que 0", 
                    "Error de validación", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Subvencion subvencionActualizada = new Subvencion(subvencionSeleccionadaId, pais, tipo, importe);
            
            if (subvencionDAO.actualizar(subvencionActualizada)) {
                JOptionPane.showMessageDialog(this, "Subvención actualizada exitosamente");
                limpiarFormulario();
                cargarSubvenciones();
                actualizarEstadisticas();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Error al actualizar la subvención", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "El importe debe ser un número válido", 
                "Error de formato", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Elimina la subvención seleccionada
     */
    private void eliminarSubvencion() {
        if (subvencionSeleccionadaId == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una subvención primero");
            return;
        }
        
        int confirmacion = JOptionPane.showConfirmDialog(this, 
            "¿Está seguro de eliminar esta subvención?\nID: " + subvencionSeleccionadaId, 
            "Confirmar eliminación", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            if (subvencionDAO.eliminar(subvencionSeleccionadaId)) {
                JOptionPane.showMessageDialog(this, "Subvención eliminada exitosamente");
                limpiarFormulario();
                cargarSubvenciones();
                actualizarEstadisticas();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Error al eliminar la subvención", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Actualiza las estadísticas mostradas
     */
    private void actualizarEstadisticas() {
        int total = subvencionDAO.contarSubvenciones();
        BigDecimal importeTotal = subvencionDAO.calcularImporteTotal();
        
        lblTotalSubvenciones.setText("Total Subvenciones: " + total);
        lblImporteTotal.setText("Importe Total: " + formatoMoneda.format(importeTotal));
    }
    
    /**
     * Valida los campos del formulario
     */
    private boolean validarFormulario() {
        if (txtPais.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "El país es obligatorio", 
                "Error de validación", 
                JOptionPane.ERROR_MESSAGE);
            txtPais.requestFocus();
            return false;
        }
        
        if (txtImporte.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "El importe es obligatorio", 
                "Error de validación", 
                JOptionPane.ERROR_MESSAGE);
            txtImporte.requestFocus();
            return false;
        }
        
        return true;
    }
    
    /**
     * Limpia todos los campos del formulario
     */
    private void limpiarFormulario() {
        txtPais.setText("");
        cmbTipoSubvencion.setSelectedIndex(0);
        txtImporte.setText("");
        subvencionSeleccionadaId = -1;
        tablaSubvenciones.clearSelection();
        btnActualizar.setEnabled(false);
        btnEliminar.setEnabled(false);
    }
}