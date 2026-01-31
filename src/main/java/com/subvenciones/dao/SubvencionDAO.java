package com.subvenciones.dao;

import com.subvenciones.conexion.ConexionDB;
import com.subvenciones.modelo.Subvencion;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Componente DAO para gestionar las operaciones CRUD sobre la tabla subvenciones
 */
public class SubvencionDAO {
    
    /**
     * Inserta una nueva subvención en la base de datos
     * @param subvencion Objeto Subvencion a insertar
     * @return true si se insertó correctamente, false en caso contrario
     */
    public boolean insertar(Subvencion subvencion) {
        String sql = "INSERT INTO subvenciones (pais_asignado, tipo_subvencion, importe) VALUES (?, ?, ?)";
        
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, subvencion.getPaisAsignado());
            pstmt.setString(2, subvencion.getTipoSubvencion());
            pstmt.setBigDecimal(3, subvencion.getImporte());
            
            int filasAfectadas = pstmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                // Obtener el ID generado
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    subvencion.setIdSubvencion(rs.getInt(1));
                }
                System.out.println("✓ Subvención insertada: ID = " + subvencion.getIdSubvencion());
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error al insertar subvención");
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Actualiza una subvención existente en la base de datos
     * @param subvencion Objeto Subvencion con los datos actualizados
     * @return true si se actualizó correctamente, false en caso contrario
     */
    public boolean actualizar(Subvencion subvencion) {
        String sql = "UPDATE subvenciones SET pais_asignado = ?, tipo_subvencion = ?, importe = ? WHERE id_subvencion = ?";
        
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, subvencion.getPaisAsignado());
            pstmt.setString(2, subvencion.getTipoSubvencion());
            pstmt.setBigDecimal(3, subvencion.getImporte());
            pstmt.setInt(4, subvencion.getIdSubvencion());
            
            int filasAfectadas = pstmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                System.out.println("✓ Subvención actualizada: ID = " + subvencion.getIdSubvencion());
                return true;
            } else {
                System.out.println("⚠ No se encontró la subvención con ID = " + subvencion.getIdSubvencion());
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error al actualizar subvención");
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Elimina una subvención de la base de datos
     * @param idSubvencion ID de la subvención a eliminar
     * @return true si se eliminó correctamente, false en caso contrario
     */
    public boolean eliminar(int idSubvencion) {
        String sql = "DELETE FROM subvenciones WHERE id_subvencion = ?";
        
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idSubvencion);
            
            int filasAfectadas = pstmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                System.out.println("✓ Subvención eliminada: ID = " + idSubvencion);
                return true;
            } else {
                System.out.println("⚠ No se encontró la subvención con ID = " + idSubvencion);
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error al eliminar subvención");
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Consulta todas las subvenciones almacenadas en la base de datos
     * @return Lista de todas las subvenciones
     */
    public List<Subvencion> obtenerTodas() {
        List<Subvencion> subvenciones = new ArrayList<>();
        String sql = "SELECT * FROM subvenciones ORDER BY id_subvencion";
        
        try (Connection conn = ConexionDB.getConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Subvencion subvencion = new Subvencion();
                subvencion.setIdSubvencion(rs.getInt("id_subvencion"));
                subvencion.setPaisAsignado(rs.getString("pais_asignado"));
                subvencion.setTipoSubvencion(rs.getString("tipo_subvencion"));
                subvencion.setImporte(rs.getBigDecimal("importe"));
                
                subvenciones.add(subvencion);
            }
            
            System.out.println("✓ Subvenciones recuperadas: " + subvenciones.size());
            
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener subvenciones");
            e.printStackTrace();
        }
        
        return subvenciones;
    }
    
    /**
     * Consulta una subvención específica por su ID
     * @param idSubvencion ID de la subvención a buscar
     * @return Objeto Subvencion si se encuentra, null en caso contrario
     */
    public Subvencion obtenerPorId(int idSubvencion) {
        String sql = "SELECT * FROM subvenciones WHERE id_subvencion = ?";
        
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idSubvencion);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Subvencion subvencion = new Subvencion();
                    subvencion.setIdSubvencion(rs.getInt("id_subvencion"));
                    subvencion.setPaisAsignado(rs.getString("pais_asignado"));
                    subvencion.setTipoSubvencion(rs.getString("tipo_subvencion"));
                    subvencion.setImporte(rs.getBigDecimal("importe"));
                    
                    System.out.println("✓ Subvención encontrada: ID = " + idSubvencion);
                    return subvencion;
                } else {
                    System.out.println("⚠ No se encontró la subvención con ID = " + idSubvencion);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error al buscar subvención");
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Obtiene el total de subvenciones almacenadas
     * @return Número total de subvenciones
     */
    public int contarSubvenciones() {
        String sql = "SELECT COUNT(*) as total FROM subvenciones";
        
        try (Connection conn = ConexionDB.getConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt("total");
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error al contar subvenciones");
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Obtiene la suma total de importes de todas las subvenciones
     * @return Suma total de importes
     */
    public BigDecimal calcularImporteTotal() {
        String sql = "SELECT SUM(importe) as total FROM subvenciones";
        
        try (Connection conn = ConexionDB.getConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                BigDecimal total = rs.getBigDecimal("total");
                return total != null ? total : BigDecimal.ZERO;
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error al calcular importe total");
            e.printStackTrace();
        }
        
        return BigDecimal.ZERO;
    }
}