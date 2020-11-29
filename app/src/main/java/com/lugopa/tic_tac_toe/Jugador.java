package com.lugopa.tic_tac_toe;

public class Jugador {
    private int dni;
    private String nombre;
    private int victorias;
    private String key;

    public Jugador(){

    }

    public Jugador(int dni, String nombre, int victorias, String key) {
        this.dni = dni;
        this.nombre = nombre;
        this.victorias = victorias;
        this.key = key;
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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}


