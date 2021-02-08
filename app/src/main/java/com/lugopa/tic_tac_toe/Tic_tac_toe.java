package com.lugopa.tic_tac_toe;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Tic_tac_toe extends AppCompatActivity implements View.OnClickListener {

    private Button[][] botones = new Button[3][3];

    private boolean turnoJugador1 = true;

    private boolean flag = false;

    private int contadorDeRondas;

    private int puntosJugador1;
    private int puntosJugador2;

    private TextView tv_jugador1;
    private TextView tv_jugador2;

    private Button btn_reset;
    private Button btn_finish;


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

        btn_reset = findViewById(R.id.button_reset);
        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetearJuego();
            }
        });

        btn_finish = findViewById(R.id.button_finish);
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

        if (!hayGanador() && contadorDeRondas == 9 ) { // si no hay un ganador, puede ser empate
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
        //imprimirMatriz_consola(campo);
        // lineas horizontales
        for (int i = 0; i < 3; i++) {
            if (campo[i][0].equals(campo[i][1])
                    && campo[i][0].equals(campo[i][2])
                    && !campo[i][0].equals("")) {
                colorearJugadaGanadora( i, 0, i, 1, i, 2);
                return true;
            }
        }
        // lineas verticales
        for (int i = 0; i < 3; i++) {
            if (campo[0][i].equals(campo[1][i])
                    && campo[0][i].equals(campo[2][i])
                    && !campo[0][i].equals("")) {
                colorearJugadaGanadora( 0, i, 1, i, 2, i);
                return true;
            }
        }
        // diagonal izq --> der
        if (campo[0][0].equals(campo[1][1])
                && campo[0][0].equals(campo[2][2])
                && !campo[0][0].equals("")) {
            colorearJugadaGanadora( 0, 0, 1, 1, 2, 2);
            return true;
        }
        // diagonal der --> izq
        if (campo[0][2].equals(campo[1][1])
                && campo[0][2].equals(campo[2][0])
                && !campo[0][2].equals("")) {
            colorearJugadaGanadora( 0, 2, 1, 1, 2, 0);
            return true;
        }
        System.out.println("========= NO HAY GANADOR, SE RETORNARA FALSO=================");
        return false;
    }

    private void imprimirMatriz_consola(String[][] matriz){
        System.out.println("++++++++++++++++++++ IMPRIMIENDO MATRIZ ++++++++++++++++++++++++++++++++++++++++++\n");
        for (int i = 0; i < 3; i++) {
            for (int h = 0; h < 3; h++) {
                System.out.print("\t");
                if(matriz[i][h].equals("")){
                    System.out.print("=");
                }
                else{
                    System.out.print(matriz[i][h]);
                }
            }
            System.out.print("\n");
        }
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

    private void colorearJugadaGanadora(int x1, int y1, int x2, int y2, int x3, int y3){
        // formo la matriz de strings
//        botones[x1][y1].setBackgroundResource(R.drawable.buttons_tic_tac_toe_green);
//        botones[x2][y2].setBackgroundResource(R.drawable.buttons_tic_tac_toe_green);
//        botones[x3][y3].setBackgroundResource(R.drawable.buttons_tic_tac_toe_green);
        PintarJugadaTask pintor = new PintarJugadaTask(x1, y1, x2, y2, x3, y3);
        pintor.execute();
    }

    private void resetearColorMatriz(){
        for (int i = 0; i < 3; i++) {
            for (int h = 0; h < 3; h++) {
                botones[i][h].setBackgroundResource(R.drawable.buttons_tic_tac_toe);
            }
        }
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
            resetearColorMatriz();
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
                        databaseManager.guardarJugador_BD(dni, nombre, cantVictorias);
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

   private void bloquearBotonesJuego(){
       for (int i = 0; i < 3; i++) {
           for (int h = 0; h < 3; h++) {
               botones[i][h].setClickable(false);
           }
       }
   }

   private void desbloquearBotonesJuego(){
       for (int i = 0; i < 3; i++) {
           for (int h = 0; h < 3; h++) {
               botones[i][h].setClickable(true);
           }
       }
   }

    class PintarJugadaTask extends AsyncTask<Void, Void, Void>{

        public PintarJugadaTask(Integer... integers){
            super();
            int x1 = integers[0];
            int y1 = integers[1];
            int x2 = integers[2];
            int y2 = integers[3];
            int x3 = integers[4];
            int y3 = integers[5];

            botones[x1][y1].setBackgroundResource(R.drawable.buttons_tic_tac_toe_green);
            botones[x2][y2].setBackgroundResource(R.drawable.buttons_tic_tac_toe_green);
            botones[x3][y3].setBackgroundResource(R.drawable.buttons_tic_tac_toe_green);
        }

        @Override
       protected void onPreExecute() {
           btn_finish.setClickable(false);
           btn_reset.setClickable(false);
           bloquearBotonesJuego();
       }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Thread.sleep(2000);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (turnoJugador1){
                ganador_jugador_1();
            }else{
                ganador_jugador_2();
            }
            btn_finish.setClickable(true);
            btn_reset.setClickable(true);
            desbloquearBotonesJuego();
        }
    }


}