package com.lugopa.tic_tac_toe;

public class ValidadorDeDatos {

    public ValidadorDeDatos() {
    }

    public boolean validar_dni(int dni) {
        return (dni >= 10000000 && dni < 99999999);
    }

}
