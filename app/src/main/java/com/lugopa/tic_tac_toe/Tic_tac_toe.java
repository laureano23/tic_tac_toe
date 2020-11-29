package com.lugopa.tic_tac_toe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.nfc.FormatException;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import javax.security.auth.callback.Callback;

public class Tic_tac_toe extends AppCompatActivity implements View.OnClickListener {

    private Button[][] botones = new Button[3][3];

    private boolean turnoJugador1 = true;

    private int contadorDeRondas;

    private int puntosJugador1;
    private int puntosJugador2;

    private TextView tv_jugador1;
    private TextView tv_jugador2;

    private ValidadorDeDatos validadorDeDatos = new ValidadorDeDatos();

    // ELEMENTOS DEL POPUP
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText et_nombre_pop, et_dni_pop;
    private Button btn_save_pop, btn_playAgain_pop, btn_exit_pop;
    private TextView tv_tituloVictoria_pop;


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

        Button btn_finish = findViewById(R.id.button_finish);
        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(puntosJugador1 != puntosJugador2){
                    mostrarPopUpVictoria();
                } else {
                    Toast.makeText(getApplicationContext(), "No winner yet, keep playing!", Toast.LENGTH_SHORT).show();
                }
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
        Toast.makeText(this, "Player 1 wins!", Toast.LENGTH_SHORT).show();
        actualizarTextoPuntuacion();
        resetearTablero();
    }

    private void ganador_jugador_2(){
        puntosJugador2++;
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

    //=============================================
    // FUNCIONES DEL POP_UP
    //=============================================

    private void mostrarPopUpVictoria(){
        dialogBuilder = new AlertDialog.Builder(this);
        final View victoriaPopUpView = getLayoutInflater().inflate(R.layout.popup_winner, null);
        tv_tituloVictoria_pop = victoriaPopUpView.findViewById(R.id.textview_1_pop);
        et_nombre_pop = victoriaPopUpView.findViewById(R.id.editext_1_pop);
        et_dni_pop = victoriaPopUpView.findViewById(R.id.editext_2_pop);
        btn_save_pop = victoriaPopUpView.findViewById(R.id.button_save_pop);
        btn_playAgain_pop= victoriaPopUpView.findViewById(R.id.button_play_pop);
        btn_exit_pop= victoriaPopUpView.findViewById(R.id.button_exit_pop);

        if(puntosJugador1 > puntosJugador2){
            tv_tituloVictoria_pop.setText("THE WINNER IS... PLAYER 1 !!!");
        } else {
            tv_tituloVictoria_pop.setText("THE WINNER IS... PLAYER 2 !!!");
        }

        dialogBuilder.setView(victoriaPopUpView);
        dialog = dialogBuilder.create();
        dialog.show();

        btn_save_pop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_nombre_pop.getText().toString().isEmpty() || et_dni_pop.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"incomplete entered data!", Toast.LENGTH_SHORT).show();
                } else {
                    int dni = Integer.parseInt(et_dni_pop.getText().toString());
                    if (!validadorDeDatos.validar_dni(dni)) {
                        Toast.makeText(getApplicationContext(), "Invalid DNI, Try again!", Toast.LENGTH_SHORT).show();
                    }else {
                        String nombre = et_nombre_pop.getText().toString();
                        int cantVictorias = Math.max(puntosJugador1, puntosJugador2);
                        DatabaseManager databaseManager = new DatabaseManager();
                        databaseManager.guardarEnBD(dni, nombre, cantVictorias);
                        btn_save_pop.setClickable(false);
                        btn_save_pop.setVisibility(View.INVISIBLE);
                        resetearJuego();
                    }
                }
            }
        });

        btn_playAgain_pop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                resetearJuego();
            }
        });

        btn_exit_pop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finish();
        //overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

   /* private void agregar_jugador_BD(Jugador player){
        DatabaseManager mDatabase = new DatabaseManager();
        mDatabase.checkUserExist(player.getDni(), new BasicE)
        Query queryUserByDni = FirebaseDatabase.getInstance().getReference("Jugadores")
                .orderByChild("dni").equalTo(player.getDni()).limitToFirst(1);
        queryUserByDni.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    // ver ultimo video para rreutilizar BD
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }*/



}