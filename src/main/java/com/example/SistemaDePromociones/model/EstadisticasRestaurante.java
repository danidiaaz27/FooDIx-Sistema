package com.example.SistemaDePromociones.model;

import jakarta.persistence.*;

@Entity
@Table(name = "estadisticas_restaurante")
public class EstadisticasRestaurante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double ingresosTotales;
    private int pedidosTotales;
    private int vistasTotales;
    private double tasaConversion;
    private double ticketPromedio;
    private double ctrPromociones;
    private double satisfaccionCliente;

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public double getIngresosTotales() { return ingresosTotales; }
    public void setIngresosTotales(double ingresosTotales) { this.ingresosTotales = ingresosTotales; }
    public int getPedidosTotales() { return pedidosTotales; }
    public void setPedidosTotales(int pedidosTotales) { this.pedidosTotales = pedidosTotales; }
    public int getVistasTotales() { return vistasTotales; }
    public void setVistasTotales(int vistasTotales) { this.vistasTotales = vistasTotales; }
    public double getTasaConversion() { return tasaConversion; }
    public void setTasaConversion(double tasaConversion) { this.tasaConversion = tasaConversion; }
    public double getTicketPromedio() { return ticketPromedio; }
    public void setTicketPromedio(double ticketPromedio) { this.ticketPromedio = ticketPromedio; }
    public double getCtrPromociones() { return ctrPromociones; }
    public void setCtrPromociones(double ctrPromociones) { this.ctrPromociones = ctrPromociones; }
    public double getSatisfaccionCliente() { return satisfaccionCliente; }
    public void setSatisfaccionCliente(double satisfaccionCliente) { this.satisfaccionCliente = satisfaccionCliente; }
}
