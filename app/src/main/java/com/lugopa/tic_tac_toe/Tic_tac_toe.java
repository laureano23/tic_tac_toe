package com.lugopa.tic_tac_toe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Tic_tac_toe extends AppCompatActivity implements View.OnClickListener {

    private Button[][] botones = new Button[3][3];

    private boolean turnoJugador1 = true;

    private int contadorDeRondas;

    private int puntosJugador1;
    private int puntosJugador2;

    private TextView tv_jugador1;
    private TextView tv_jugador2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tic_tac_toe);

        inicializar();

        Button btn_reset = findViewById(R.id.Button_reset);
        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // FALTA DETALLAR
            }
        });

    }

    private void inicializar() {
        tv_jugador1 = findViewById(R.id.textView_P1);
        tv_jugador2 = findViewById(R.id.textView_P2);

        for (int i = 0; i < 3; i++) {
            for (int h = 0; h < 3; h++) {
                String botonID = "btn_" + i + h;
                int resID = getResources().getIdentifier(botonID, "id", getPackageName());
                botones[i][h] = findViewById(resID);
                botones[i][h].setOnClickListener(this);
            }
        }
    }

    @Override
    public void onClick(View v) { // Permite adminisstrar el click de c/u de los botones inicializados
        if (!((Button) v).getText().toString().equals("")) { // revisa si tiene un texto vacio el boton
            return;
        }

        if (turnoJugador1) {
            ((Button) v).setText("X");
        } else {
            ((Button) v).setText("O");
        }

        contadorDeRondas++;

        if (hayGanador()) { // si hay un ganador
            if (turnoJugador1) { // si es el jugador 1
                ganador_jugador_1();
            } else { // es el jugador 2
                ganador_jugador_2();
            }
        } else if (contadorDeRondas == 9) { // si no hubo ganador, puede ser empate
            empate();
        } else { // si no fue empate, sigue el juego, turno del contrario
            turnoJugador1 = !turnoJugador1;
        }

    }

    private boolean hayGanador() {
        String[][] campo = new String[3][3];

        for (int i = 0; i < 3; i++) {
            for (int h = 0; h < 3; h++) {
                campo[i][h] = botones[i][h].getText().toString();
            }
        }

        // lineas horizontales
        for (int i = 0; i < 3; i++) {
            if (campo[i][0].equals(campo[i][1])
                    && campo[i][0].equals(campo[i][2])
                    && !campo[i][0].equals("")) {
                return true;
            }
        }

        // lineas verticales
        for (int i = 0; i < 3; i++) {
            if (campo[0][i].equals(campo[1][i])
                    && campo[0][i].equals(campo[2][i])
                    && !campo[0][i].equals("")) {
                return true;
            }
        }

        // diagonal izq --> der
        if (campo[0][0].equals(campo[1][1])
                && campo[0][0].equals(campo[2][2])
                && !campo[0][0].equals("")) {
            return true;
        }

        // diagonal der --> izq
        if (campo[0][2].equals(campo[1][1])
                && campo[0][2].equals(campo[2][0])
                && !campo[0][2].equals("")) {
            return true;
        }
        return false;
    }

    private void ganador_jugador_1(){

    }

    private void ganador_jugador_2(){

    }

    private void empate(){

    }
}