package com.laureano.ta_te_ti;

public class ValidadorDeDatos {

    public ValidadorDeDatos() {
    }

    public boolean validar_dni(String dni) {
        return (Integer.parseInt(dni) >= 10000000 && Integer.parseInt(dni) <= 99999999);
    }

}
