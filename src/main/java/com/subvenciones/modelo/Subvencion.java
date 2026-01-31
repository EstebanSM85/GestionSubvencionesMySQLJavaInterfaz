package com.subvenciones.modelo;

import java.math.BigDecimal;

/**
 * Clase que representa una subvención de la Unión Europea
 */
public class Subvencion {
    
    private int idSubvencion;
    private String paisAsignado;
    private String tipoSubvencion;
    private BigDecimal importe;
    
    // Constructor vacío
    public Subvencion() {
    }
    
    // Constructor con parámetros (sin ID para nuevas subvenciones)
    public Subvencion(String paisAsignado, String tipoSubvencion, BigDecimal importe) {
        this.paisAsignado = paisAsignado;
        this.tipoSubvencion = tipoSubvencion;
        this.importe = importe;
    }
    
    // Constructor completo (con ID para subvenciones existentes)
    public Subvencion(int idSubvencion, String paisAsignado, String tipoSubvencion, BigDecimal importe) {
        this.idSubvencion = idSubvencion;
        this.paisAsignado = paisAsignado;
        this.tipoSubvencion = tipoSubvencion;
        this.importe = importe;
    }
    
    // Getters y Setters
    public int getIdSubvencion() {
        return idSubvencion;
    }
    
    public void setIdSubvencion(int idSubvencion) {
        this.idSubvencion = idSubvencion;
    }
    
    public String getPaisAsignado() {
        return paisAsignado;
    }
    
    public void setPaisAsignado(String paisAsignado) {
        this.paisAsignado = paisAsignado;
    }
    
    public String getTipoSubvencion() {
        return tipoSubvencion;
    }
    
    public void setTipoSubvencion(String tipoSubvencion) {
        this.tipoSubvencion = tipoSubvencion;
    }
    
    public BigDecimal getImporte() {
        return importe;
    }
    
    public void setImporte(BigDecimal importe) {
        this.importe = importe;
    }
    
    @Override
    public String toString() {
        return "Subvencion{" +
                "idSubvencion=" + idSubvencion +
                ", paisAsignado='" + paisAsignado + '\'' +
                ", tipoSubvencion='" + tipoSubvencion + '\'' +
                ", importe=" + importe +
                '}';
    }
}