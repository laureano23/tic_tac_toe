package com.lugopa.tic_tac_toe;

public class Jugador {
    private int dni;
    private String nombre;
    private int victorias;

    public Jugador(int dni, String nombre, int victorias) {
        this.dni = dni;
        this.nombre = nombre;
        this.victorias = victorias;
    }

    public int getDni() {
        return dni;
    }

    public void setDni(int dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getVictorias() {
        return victorias;
    }

    public void setVictorias(int victorias) {
        this.victorias = victorias;
    }
}


