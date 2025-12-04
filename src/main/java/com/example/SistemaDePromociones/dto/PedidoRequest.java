package com.example.SistemaDePromociones.dto;

public class PedidoRequest {
    private String direccionEntrega;
    private String telefono;
    private String metodoPago;
    private String referencia;
    private String notas;

    // Constructors
    public PedidoRequest() {}

    public PedidoRequest(String direccionEntrega, String telefono, String metodoPago, String referencia, String notas) {
        this.direccionEntrega = direccionEntrega;
        this.telefono = telefono;
        this.metodoPago = metodoPago;
        this.referencia = referencia;
        this.notas = notas;
    }

    // Getters and Setters
    public String getDireccionEntrega() {
        return direccionEntrega;
    }

    public void setDireccionEntrega(String direccionEntrega) {
        this.direccionEntrega = direccionEntrega;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }
}
