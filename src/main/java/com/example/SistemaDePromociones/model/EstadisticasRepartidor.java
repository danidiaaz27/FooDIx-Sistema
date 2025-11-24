package com.example.SistemaDePromociones.model;


public class EstadisticasRepartidor {
    // Ganancias hoy
    private Double gananciasHoy = 0.0;
    // Entregas hoy
    private Integer entregasHoy = 0;
    // Calificación promedio
    private Double calificacionPromedio = 0.0;
    // Tasa de éxito
    private Double tasaExito = 0.0;
    // Ganancias esta semana
    private Double gananciasSemana = 0.0;
    // Progreso semanal (porcentaje)
    private Double progresoSemanal = 0.0;
    // Meta semanal
    private Double metaSemanal = 0.0;
    // Total de entregas
    private Integer totalEntregas = 0;
    // Promedio por entrega
    private Double promedioEntrega = 0.0;
    // Tiempo promedio por entrega
    private Double tiempoPromedio = 0.0;

    public EstadisticasRepartidor() {
        // Inicializar todos los campos con valores por defecto
        this.gananciasHoy = 0.0;
        this.entregasHoy = 0;
        this.calificacionPromedio = 0.0;
        this.tasaExito = 0.0;
        this.gananciasSemana = 0.0;
        this.progresoSemanal = 0.0;
        this.metaSemanal = 0.0;
        this.totalEntregas = 0;
        this.promedioEntrega = 0.0;
        this.tiempoPromedio = 0.0;
    }

    public Double getGananciasHoy() { return gananciasHoy; }
    public void setGananciasHoy(Double gananciasHoy) { this.gananciasHoy = gananciasHoy; }

    public Integer getEntregasHoy() { return entregasHoy; }
    public void setEntregasHoy(Integer entregasHoy) { this.entregasHoy = entregasHoy; }

    public Double getCalificacionPromedio() { return calificacionPromedio; }
    public void setCalificacionPromedio(Double calificacionPromedio) { this.calificacionPromedio = calificacionPromedio; }

    public Double getTasaExito() { return tasaExito; }
    public void setTasaExito(Double tasaExito) { this.tasaExito = tasaExito; }

    public Double getGananciasSemana() { return gananciasSemana; }
    public void setGananciasSemana(Double gananciasSemana) { this.gananciasSemana = gananciasSemana; }

    public Double getProgresoSemanal() { return progresoSemanal; }
    public void setProgresoSemanal(Double progresoSemanal) { this.progresoSemanal = progresoSemanal; }

    public Double getMetaSemanal() { return metaSemanal; }
    public void setMetaSemanal(Double metaSemanal) { this.metaSemanal = metaSemanal; }

    public Integer getTotalEntregas() { return totalEntregas; }
    public void setTotalEntregas(Integer totalEntregas) { this.totalEntregas = totalEntregas; }

    public Double getPromedioEntrega() { return promedioEntrega; }
    public void setPromedioEntrega(Double promedioEntrega) { this.promedioEntrega = promedioEntrega; }

    public Double getTiempoPromedio() { return tiempoPromedio; }
    public void setTiempoPromedio(Double tiempoPromedio) { this.tiempoPromedio = tiempoPromedio; }
}
