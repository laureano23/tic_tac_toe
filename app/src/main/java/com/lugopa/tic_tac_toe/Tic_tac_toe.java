package com.lugopa.tic_tac_toe;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Tic_tac_toe extends AppCompatActivity implements View.OnClickListener {

    private Button[][] botones = new Button[3][3];

    private boolean turnoJugador1 = true;

    private int contadorDeRondas;

    private int puntosJugador1;
    private int puntosJugador2;

    private TextView tv_jugador1;
    private TextView tv_jugador2;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tic_tac_toe);

        tv_jugador1 = findViewById(R.id.textView_P1);
        tv_jugador2 = findViewById(R.id.textView_P2);

        for (int i = 0; i < 3; i++) {
            for (int h = 0; h < 3; h++) {
                String buttonID = "button_" + i + h;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                botones[i][h] = findViewById(resID);
                botones[i][h].setOnClickListener(this);
            }
        }

        Button btn_reset = findViewById(R.id.button_reset);
        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetearJuego();
            }
        });

    }

    @Override
    public void onClick(View v) { // Permite administrar el click de c/u de los botones inicializados
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
        puntosJugador1++;
        String[][] msg = new String[][]{{"P","2","-"},{"H","A","S"},{"W","O","N"}};
        Toast.makeText(this, "Player 1 wins!", Toast.LENGTH_SHORT).show();
        actualizarTextoPuntuacion();
        resetearTablero();
    }

    private void ganador_jugador_2(){
        puntosJugador2++;
        String[][] msg = new String[][]{{"P","2","-"},{"H","A","S"},{"W","O","N"}};
        Toast.makeText(this, "Player 2 wins!", Toast.LENGTH_SHORT).show();
        actualizarTextoPuntuacion();
        resetearTablero();
    }

    private void empate(){
        Toast.makeText(this, "It is a Tie!", Toast.LENGTH_SHORT).show();
        resetearTablero();
    }

    private void actualizarTextoPuntuacion(){
        tv_jugador1.setText("Player 1: " + puntosJugador1);
        tv_jugador2.setText("Player 2: " + puntosJugador2);
    }

    private void resetearTablero() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                botones[i][j].setText("");
            }
            contadorDeRondas = 0;
            turnoJugador1 = true;
        }
    }

    private void resetearJuego() {
        puntosJugador1 = 0;
        puntosJugador2 = 0;
        actualizarTextoPuntuacion();
        resetearTablero();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) { // invocado cuando se rota el dispositivo
        super.onSaveInstanceState(outState); // almacenamos cada uno de los valores en un estado externo
        outState.putInt("contadorDeRondas", contadorDeRondas);
        outState.putInt("puntosJugador1", puntosJugador1);
        outState.putInt("puntosJugador2", puntosJugador2);
        outState.putBoolean("turnoJugador1", turnoJugador1);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState); // reestablecemos cada uno de los valores salvados en el metodo anterior
        contadorDeRondas = savedInstanceState.getInt("contadorDeRondas");
        puntosJugador1 = savedInstanceState.getInt("puntosJugador1");
        puntosJugador2 = savedInstanceState.getInt("puntosJugador2");
        turnoJugador1 = savedInstanceState.getBoolean("turnoJugador1");
    }

}