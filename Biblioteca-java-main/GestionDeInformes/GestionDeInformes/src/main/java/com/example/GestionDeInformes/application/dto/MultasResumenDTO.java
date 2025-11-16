package com.example.GestionDeInformes.application.dto;

public class MultasResumenDTO {
    private long totalMultas;
    private long multasPendientes;
    private long multasPagadas;
    private long multasExentas;

    public long getTotalMultas() {
        return totalMultas;
    }

    public void setTotalMultas(long totalMultas) {
        this.totalMultas = totalMultas;
    }

    public long getMultasPendientes() {
        return multasPendientes;
    }

    public void setMultasPendientes(long multasPendientes) {
        this.multasPendientes = multasPendientes;
    }

    public long getMultasPagadas() {
        return multasPagadas;
    }

    public void setMultasPagadas(long multasPagadas) {
        this.multasPagadas = multasPagadas;
    }

    public long getMultasExentas() {
        return multasExentas;
    }

    public void setMultasExentas(long multasExentas) {
        this.multasExentas = multasExentas;
    }
}

