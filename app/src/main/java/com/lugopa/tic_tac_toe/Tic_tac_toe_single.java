package com.lugopa.tic_tac_toe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.nfc.FormatException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
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
import java.util.Arrays;
import java.util.Random;

import javax.security.auth.callback.Callback;
public class Tic_tac_toe_single extends AppCompatActivity implements View.OnClickListener {

    private Button[][] botones = new Button[3][3];

    private boolean turnoJugador1 = true;

    private int contadorDeRondas;
    private String simbolo_ganador;

    private int puntosJugador1;
    private int puntosComputer;

    private TextView tv_jugador1;
    private TextView tv_computer;

    private Button btn_reset, btn_quit;

    private boolean banderaPrimerJugada=true;
    private boolean jugadaRealizada=false;
    private int ultimaPosicionX = 99;
    private int ultimaPosicionY = 99;

//    private int[] moverEnX = new int[8];
//    private int[] moverEnY = new int[8];

    String[] primeraHorizontal = new String [6];
    String[] segundaHorizontal = new String [6];
    String[] terceraHorizontal = new String [6];
    String[] primeraVertical = new String [6];
    String[] segundaVertical = new String [6];
    String[] terceraVertical = new String [6];
    String[] primeraDiagonal = new String [6];
    String[] segundaDiagonal = new String [6];

    // VARIABLES PARA CONTROL DEL POP-UP
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private TextView tv_victoria_pop;
    private Button btn_playAgain_pop, btn_exit_pop;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tic_tac_toe);

        tv_jugador1 = findViewById(R.id.textView_P1);
        tv_computer = findViewById(R.id.textView_P2);

        tv_jugador1.setText("You: 0");
        tv_computer.setText("Computer: 0");

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

        btn_quit = findViewById(R.id.button_finish);
        //btn_finish.setVisibility(View.GONE);
        btn_quit.setText("Quit");

        btn_quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                //imprimirVectores();
            }
        });

    }

    private void copiarMatriz(){

        // ---------------------------------- Primera Horizontal -----------------------------------

        int cantPHjugador = 0;
        int cantPHmaquina = 0;
        for (int i = 0; i < 3; i++) {

            if(botones[0][i].getText().toString().equals("X")) {
                cantPHjugador++;
            }

            if(botones[0][i].getText().toString().equals("O")) {
                cantPHmaquina++;
            }

            primeraHorizontal[i] = botones[0][i].getText().toString();
        }

        //En la tercera posición pongo la cantidad de repetidos del Jugador
        //En la cuarta posición pongo la cantidad de repetidos de la máquina

        primeraHorizontal[3] = String.valueOf(cantPHjugador);
        primeraHorizontal[4] = String.valueOf(cantPHmaquina);

        //-------------------------------- Segunda Horizontal --------------------------------------
        int cantSHjugador = 0;
        int cantSHmaquina = 0;
        for (int i = 0; i < 3; i++) {

            if(botones[1][i].getText().toString().equals("X")) {
                cantSHjugador++;
            }

            if(botones[1][i].getText().toString().equals("O")) {
                cantSHmaquina++;
            }

            segundaHorizontal[i] = botones[1][i].getText().toString();
        }

        //En la tercera posición pongo la cantidad de repetidos del Jugador
        //En la cuarta posición pongo la cantidad de repetidos de la máquina

        segundaHorizontal[3] = String.valueOf(cantSHjugador);
        segundaHorizontal[4] = String.valueOf(cantSHmaquina);

        // ------------------------------------- Tercera Horizontal --------------------------------

        int cantTHjugador = 0;
        int cantTHmaquina = 0;
        for (int i = 0; i < 3; i++) {

            if(botones[2][i].getText().toString().equals("X")) {
                cantTHjugador++;
            }

            if(botones[2][i].getText().toString().equals("O")) {
                cantTHmaquina++;
            }

            terceraHorizontal[i] = botones[2][i].getText().toString();
        }

        //En la tercera posición pongo la cantidad de repetidos del Jugador
        //En la cuarta posición pongo la cantidad de repetidos de la máquina

        terceraHorizontal[3] = String.valueOf(cantTHjugador);
        terceraHorizontal[4] = String.valueOf(cantTHmaquina);

        // *********************************** Primera Vertical ************************************

        int cantPVjugador = 0;
        int cantPVmaquina = 0;
        for (int i = 0; i < 3; i++) {

            if(botones[i][0].getText().toString().equals("X")) {
                cantPVjugador++;
            }

            if(botones[i][0].getText().toString().equals("O")) {
                cantPVmaquina++;
            }
            primeraVertical[i] = botones[i][0].getText().toString();
        }

        //En la tercera posición pongo la cantidad de repetidos del Jugador
        //En la cuarta posición pongo la cantidad de repetidos de la máquina

        primeraVertical[3] = String.valueOf(cantPVjugador);
        primeraVertical[4] = String.valueOf(cantPVmaquina);

        // ********************************** Segunda Vertical *************************************

        int cantSVjugador = 0;
        int cantSVmaquina = 0;
        for (int i = 0; i < 3; i++) {

            if(botones[i][1].getText().toString().equals("X")) {
                cantSVjugador++;
            }

            if(botones[i][1].getText().toString().equals("O")) {
                cantSVmaquina++;
            }
            segundaVertical[i] = botones[i][1].getText().toString();
        }

        //En la tercera posición pongo la cantidad de repetidos del Jugador
        //En la cuarta posición pongo la cantidad de repetidos de la máquina

        segundaVertical[3] = String.valueOf(cantSVjugador);
        segundaVertical[4] = String.valueOf(cantSVmaquina);

        // ********************************* Tercera Vertical **************************************

        int cantTVjugador = 0;
        int cantTVmaquina = 0;
        for (int i = 0; i < 3; i++) {

            if(botones[i][2].getText().toString().equals("X")) {
                cantTVjugador++;
            }

            if(botones[i][2].getText().toString().equals("O")) {
                cantTVmaquina++;
            }

            terceraVertical[i] = botones[i][2].getText().toString();
        }

        //En la tercera posición pongo la cantidad de repetidos del Jugador
        //En la cuarta posición pongo la cantidad de repetidos de la máquina

        terceraVertical[3] = String.valueOf(cantTVjugador);
        terceraVertical[4] = String.valueOf(cantTVmaquina);

        // ,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,, Primera Diagonal ,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,

        int cantPDjugador = 0;
        int cantPDmaquina = 0;
        int k = 0;
        for (int i = 0; i < 3; i++) {

            if(botones[i][k].getText().toString().equals("X")) {
                cantPDjugador++;
            }

            if(botones[i][k].getText().toString().equals("O")) {
                cantPDmaquina++;
            }

            primeraDiagonal[i] = botones[i][k].getText().toString();
            k++;
        }

        //En la tercera posición pongo la cantidad de repetidos del Jugador
        //En la cuarta posición pongo la cantidad de repetidos de la máquina

        primeraDiagonal[3] = String.valueOf(cantPDjugador);
        primeraDiagonal[4] = String.valueOf(cantPDmaquina);

        // ,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,, Segunda Diagonal ,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,

        int cantSDjugador = 0;
        int cantSDmaquina = 0;
        int l = 2;
        for (int i = 0; i < 3; i++) {

            if(botones[i][l].getText().toString().equals("X")) {
                cantSDjugador++;
            }

            if(botones[i][l].getText().toString().equals("O")) {
                cantSDmaquina++;
            }

            segundaDiagonal[i] = botones[i][l].getText().toString();
            l--;
        }

        //En la tercera posición pongo la cantidad de repetidos del Jugador
        //En la cuarta posición pongo la cantidad de repetidos de la máquina

        segundaDiagonal[3] = String.valueOf(cantSDjugador);
        segundaDiagonal[4] = String.valueOf(cantSDmaquina);

    }

    private void imprimirVectores()
    {
        System.out.println("------------------------------------------");
        System.out.println("Primera Horizontal: "+ Arrays.toString(primeraHorizontal));
        System.out.println("Segunda Horizontal: "+ Arrays.toString(segundaHorizontal));
        System.out.println("Tercera Horizontal: "+ Arrays.toString(terceraHorizontal));
        System.out.println("Primera Vertical: "+ Arrays.toString(primeraVertical));
        System.out.println("Segunda Vertical: "+ Arrays.toString(segundaVertical));
        System.out.println("Tercera Vertical: "+ Arrays.toString(terceraVertical));
        System.out.println("Primera Diagonal: "+ Arrays.toString(primeraDiagonal));
        System.out.println("Segunda Diagonal: "+ Arrays.toString(segundaDiagonal));
        System.out.println("------------------------------------------");
    }

    private void actualizarTextoPuntuacion(){
        tv_jugador1.setText("Player 1: " + puntosJugador1);
        tv_computer.setText("Computer: " + puntosComputer);
    }

    private void resetearTablero() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                botones[i][j].setText("");
            }
            contadorDeRondas = 0;
        }
        resetearColorMatriz();
    }

    private void resetearJuego() {
        puntosJugador1 = 0;
        puntosComputer = 0;
        actualizarTextoPuntuacion();
        resetearTablero();
        banderaPrimerJugada = true;
        jugadaRealizada = false;
    }

    private void ganador_you(){
        puntosJugador1++;
        Toast.makeText(this, "You Win!", Toast.LENGTH_SHORT).show();
        actualizarTextoPuntuacion();
        resetearTablero();
        banderaPrimerJugada = true;
        jugadaRealizada = false;
    }

    private void ganador_computer(){
        puntosComputer++;
        Toast.makeText(this, "Computer Wins!", Toast.LENGTH_SHORT).show();
        actualizarTextoPuntuacion();
        resetearTablero();
        banderaPrimerJugada = true;
        jugadaRealizada = false;
    }

    private void empate(){
        Toast.makeText(this, "It is a Tie!", Toast.LENGTH_SHORT).show();
        resetearTablero();
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
                simbolo_ganador=campo[i][0];
                colorearJugadaGanadora( i, 0, i, 1, i, 2);
                return true;
            }
        }
        // lineas verticales
        for (int i = 0; i < 3; i++) {
            if (campo[0][i].equals(campo[1][i])
                    && campo[0][i].equals(campo[2][i])
                    && !campo[0][i].equals("")) {
                simbolo_ganador=campo[0][i];
                colorearJugadaGanadora( 0, i, 1, i, 2, i);
                return true;
            }
        }
        // diagonal izq --> der
        if (campo[0][0].equals(campo[1][1])
                && campo[0][0].equals(campo[2][2])
                && !campo[0][0].equals("")) {
            simbolo_ganador=campo[0][0];
            colorearJugadaGanadora( 0, 0, 1, 1, 2, 2);
            return true;
        }
        // diagonal der --> izq
        if (campo[0][2].equals(campo[1][1])
                && campo[0][2].equals(campo[2][0])
                && !campo[0][2].equals("")) {
            simbolo_ganador=campo[0][2];
            colorearJugadaGanadora( 0, 2, 1, 1, 2, 0);
            return true;
        }
        return false;
    }


    private void blanqueovectores(){

        System.out.println("BLANQUEOOOOOO");
        for (int i = 0; i < 6; i++) {
            primeraHorizontal[i]= "";
        }
        for (int i = 0; i < 6; i++) {
            segundaHorizontal[i]= "";
        }
        for (int i = 0; i < 6; i++) {
            terceraHorizontal[i]= "";
        }
        for (int i = 0; i < 6; i++) {
            primeraVertical[i]= "";
        }
        for (int i = 0; i < 6; i++) {
            segundaVertical[i]= "";
        }
        for (int i = 0; i < 6; i++) {
            terceraVertical[i]= "";
        }
        for (int i = 0; i < 6; i++) {
            primeraDiagonal[i]= "";
        }

        for (int i = 0; i < 6; i++) {
            segundaDiagonal[i]= "";
        }

        imprimirVectores();

    }

    @Override
    public void onClick(View v) {// Permite administrar el click de c/u de los botones inicializados


        if (!((Button) v).getText().toString().equals("")) { // revisa si tiene un texto vacio el boton
            return;
        }

        /*ultimaPosicionX=0;
        ultimaPosicionY=0;
        botones[0][0].setText("O");*/

        ((Button) v).setText("X");

        blanqueovectores();
        //blanqueoMovimientos();

        computerCanWin();

        if(jugadaRealizada==false){
            playerIsAboutToWin();
        }

        if(jugadaRealizada==false){
            SomethingElseHappens();
        }

        jugadaRealizada=false;

        contadorDeRondas++;

        if (hayGanador()) { // si hay alguien hizo tateti
            if(simbolo_ganador.equals("X")){
                ganador_you();
            }
            else{
                ganador_computer();
            }
            if(puntosJugador1 == 3){
                mostrarPopUpVictoria();
            }else if(puntosComputer == 3){
                mostrarPopUpVictoria();
            }
        }
        else{
            if (contadorDeRondas == 5) { // si no hubo ganador, puede ser empate
                empate();
            }
        }

    }

    //No hay turnos, las fichas de la PC se ponen automáticamente cuando juega el jugador
    //Antes de que gane la PC, fijarse de poner un DELAY para que se vea que la PC completo la casilla correspondiente
    //Ver cómo se maneja el tema de los puntajes en la otra clase ya desarrollada
    //No se guarda en la base de datos
    //Es al mejor de 5 (El primero que llega a 3). Empate no se considera punto para ninguno

    private void computerCanWin(){
        //Tengo que fijarme la forma de chequear que haya 2 números consecutivos iguales para poner el que falta
        //Hacerlo por el primero que encuentra. Usar una bandera para que no entre al resto de los ifs cuando encuentra el indicado

        copiarMatriz();
        boolean control = true; //Si entra a false no ingresa en otro if

        if((control) && primeraHorizontal[4].equals(String.valueOf(2))){
            System.out.println("*****************************************************************Entro");
            for(int i=0;i<3; i++){
                if(primeraHorizontal[i].equals("")){
                    botones[0][i].setText("O");
                    control=false;
                }
            }
        }

        if((control) && (segundaHorizontal[4].equals(String.valueOf(2)))){
            for(int i=0;i<3; i++){
                if(segundaHorizontal[i].equals("")){
                    botones[1][i].setText("O");
                    control=false;
                }
            }
        }

        if((control) && (terceraHorizontal[4].equals(String.valueOf(2)))){
            for(int i=0;i<3; i++){
                if(terceraHorizontal[i].equals("")){
                    botones[2][i].setText("O");
                    control=false;
                }
            }
        }

        if((control) && (primeraVertical[4].equals(String.valueOf(2)))){
            for(int i=0;i<3; i++){
                if(primeraVertical[i].equals("")){
                    botones[i][0].setText("O");
                    control=false;
                }
            }
        }

        if((control) && (segundaVertical[4].equals(String.valueOf(2)))){
            for(int i=0;i<3; i++){
                if(segundaVertical[i].equals("")){
                    botones[i][1].setText("O");
                    control=false;
                }
            }
        }

        if((control) && (terceraVertical[4].equals(String.valueOf(2)))){
            for(int i=0;i<3; i++){
                if(terceraVertical[i].equals("")){
                    botones[i][2].setText("O");
                    control=false;
                }
            }
        }

        if((control) && (primeraDiagonal[4].equals(String.valueOf(2)))){
            int k = 0;
            for(int i=0;i<3; i++){
                if(primeraDiagonal[i].equals("")){
                    botones[i][k].setText("O");
                    control=false;
                }
                k++;
            }
        }

        if((control) && (segundaDiagonal[4].equals(String.valueOf(2)))){
            int k = 2;
            for(int i=0;i<3; i++){
                if(segundaDiagonal[i].equals("")){
                    botones[i][k].setText("O");
                    control=false;
                }
                k--;
            }
        }

        if(control==false){
            jugadaRealizada=true;
        }

        blanqueovectores();

    }

//    private void blanqueoMovimientos(){
//        for (int i=0;i<8;i++){
//            moverEnX[i]=0;
//            moverEnY[i]=0;
//        }
//    }

    private void playerIsAboutToWin(){
        //Tengo que fijarme si hay 2 fichas iguales del jugador en la línea y colocar la de la PC en la faltante
        //Hacerlo por el primero que encuentra. Usar una bandera para que no entre al resto de los ifs cuando encuentra el indicado

        copiarMatriz();
        boolean control = true; //Si entra a false no ingresa en otro if

        if((control) && primeraHorizontal[3].equals(String.valueOf(2))){
            System.out.println("*****************************************************************Entro");
            for(int i=0;i<3; i++){
                if(primeraHorizontal[i].equals("")){
                    botones[0][i].setText("O");
                    control=false;
                }
            }
        }

        if((control) && (segundaHorizontal[3].equals(String.valueOf(2)))){
            for(int i=0;i<3; i++){
                if(segundaHorizontal[i].equals("")){
                    botones[1][i].setText("O");
                    control=false;
                }
            }
        }

        if((control) && (terceraHorizontal[3].equals(String.valueOf(2)))){
            for(int i=0;i<3; i++){
                if(terceraHorizontal[i].equals("")){
                    botones[2][i].setText("O");
                    control=false;
                }
            }
        }

        if((control) && (primeraVertical[3].equals(String.valueOf(2)))){
            for(int i=0;i<3; i++){
                if(primeraVertical[i].equals("")){
                    botones[i][0].setText("O");
                    control=false;
                }
            }
        }

        if((control) && (segundaVertical[3].equals(String.valueOf(2)))){
            for(int i=0;i<3; i++){
                if(segundaVertical[i].equals("")){
                    botones[i][1].setText("O");
                    control=false;
                }
            }
        }

        if((control) && (terceraVertical[3].equals(String.valueOf(2)))){
            for(int i=0;i<3; i++){
                if(terceraVertical[i].equals("")){
                    botones[i][2].setText("O");
                    control=false;
                }
            }
        }

        if((control) && (primeraDiagonal[3].equals(String.valueOf(2)))){
            int k = 0;
            for(int i=0;i<3; i++){
                if(primeraDiagonal[i].equals("")){
                    botones[i][k].setText("O");
                    control=false;
                }
                k++;
            }
        }

        if((control) && (segundaDiagonal[3].equals(String.valueOf(2)))){
            int k = 2;
            for(int i=0;i<3; i++){
                if(segundaDiagonal[i].equals("")){
                    botones[i][k].setText("O");
                    control=false;
                }
                k--;
            }
        }

        if(control==false){
            jugadaRealizada=true;
        }

        blanqueovectores();

    }

    private void SomethingElseHappens(){
        //Tengo que ver si tengo alguna ficha de la PC ya jugada, y en ese caso, si puedo poner una al lado de esa o si el casillero está ocupado por el Jugador
        //Si no pasa ninguna de las otras, colocar ficha en un lugar al azar

        if(banderaPrimerJugada){
            System.out.println("¨PREVIO A PRIMER JUGADA");
            primerJugadaMaquina();
        }
        else{
            System.out.println("¨PROXIMA JUGADA");
            proximaJugada();
        }

        //blanqueoMovimientos();
    }

    private void proximaJugada(){

        boolean bloqueado=false;

        if (bloqueado==false && ultimaPosicionX==0 && ultimaPosicionY==0){
            System.out.println("MAAAAMIIIICHULAAAAA ESQUINA SUPERIOR IZQUIERDAAAAAAAA");
            int[] candidatoA = new int[2];
            candidatoA[0]=9;
            candidatoA[1]=9;
            int[] candidatoB = new int[2];
            candidatoB[0]=9;
            candidatoB[1]=9;
            int[] candidatoC = new int[2];
            candidatoC[0]=9;
            candidatoC[1]=9;

            int contadorCandidatos=1; //inicializo el 1 para que el random no tire error. Cantidad de vias libres para hacer tateti

            if(botones[0][1].getText().toString().equals("") && botones[0][1].getText().toString().equals("")){
                candidatoA[0]=0;
                candidatoA[1]=1;
                contadorCandidatos++;
            }

            if(botones[1][1].getText().toString().equals("") && botones[2][2].getText().toString().equals("")){
                candidatoB[0]=1;
                candidatoB[1]=1;
                contadorCandidatos++;
            }

            if(botones[1][0].getText().toString().equals("") && botones[2][0].getText().toString().equals("")){
                candidatoC[0]=1;
                candidatoC[1]=0;
                contadorCandidatos++;
            }

            if(contadorCandidatos>1){

                boolean encontrado=false;
                while(encontrado==false){

                    Random r = new Random();
                    int alAzar = r.nextInt(3-0)+ 0;//El cero de la punta está incluido, el contador no. Osea para elgir entre 0 y 5 --> r.netInt(6-0)+0
                    System.out.println("El numero al azar es:"+alAzar);

                    if(alAzar==0 && candidatoA[0]!=9){
                        botones[candidatoA[0]][candidatoA[1]].setText("O");
                        encontrado=true;
                        ultimaPosicionX=candidatoA[0];
                        ultimaPosicionY=candidatoA[1];
                        bloqueado=true;
                    }

                    if(alAzar==1 && candidatoB[0]!=9){
                        botones[candidatoB[0]][candidatoB[1]].setText("O");
                        encontrado=true;
                        ultimaPosicionX=candidatoB[0];
                        ultimaPosicionY=candidatoB[1];
                        bloqueado=true;
                    }

                    if(alAzar==2 && candidatoC[0]!=9){
                        botones[candidatoC[0]][candidatoC[1]].setText("O");
                        encontrado=true;
                        ultimaPosicionX=candidatoC[0];
                        ultimaPosicionY=candidatoC[1];
                        bloqueado=true;
                    }

                }
            }
            else{
                ubicacionAlAzar();
                bloqueado=true;
            }

        }

        if (bloqueado==false && ultimaPosicionX==0 && ultimaPosicionY==1){
            System.out.println("MAAAAMIIIICHULAAAAA ARRRIBAAAAA AL MEDIO PA");
            int[] candidatoA = new int[2];
            candidatoA[0]=9;
            candidatoA[1]=9;
            int[] candidatoB = new int[2];
            candidatoB[0]=9;
            candidatoB[1]=9;

            int contadorCandidatos=1; //inicializo el 1 para que el random no tire error. Cantidad de vias libres para hacer tateti

            if(botones[0][0].getText().toString().equals("") && botones[0][2].getText().toString().equals("")){
                candidatoA[0]=0;//HC
                candidatoA[1]=0;
                contadorCandidatos++;
            }

            if(botones[1][1].getText().toString().equals("") && botones[2][1].getText().toString().equals("")){
                candidatoB[0]=1;
                candidatoB[1]=1;
                contadorCandidatos++;
            }

            if(contadorCandidatos>1){

                boolean encontrado=false;
                while(encontrado==false){

                    Random r = new Random();
                    int alAzar = r.nextInt(2-0)+ 0;//El cero de la punta está incluido, el contador no. Osea para elgir entre 0 y 5 --> r.netInt(6-0)+0
                    System.out.println("El numero al azar es:"+alAzar);

                    if(alAzar==0 && candidatoA[0]!=9){
                        botones[candidatoA[0]][candidatoA[1]].setText("O");
                        encontrado=true;
                        ultimaPosicionX=candidatoA[0];
                        ultimaPosicionY=candidatoA[1];
                        bloqueado=true;
                    }

                    if(alAzar==1 && candidatoB[0]!=9){
                        botones[candidatoB[0]][candidatoB[1]].setText("O");
                        encontrado=true;
                        ultimaPosicionX=candidatoB[0];
                        ultimaPosicionY=candidatoB[1];
                        bloqueado=true;
                    }

                }
            }
            else{
                ubicacionAlAzar();
                bloqueado=true;
            }
        }

        if (bloqueado==false && ultimaPosicionX==0 && ultimaPosicionY==2){
            System.out.println("MAAAAMIIIICHULAAAAA ESQUINA SUPERIOR DERECHAAAAAAA");
            int[] candidatoA = new int[2];
            candidatoA[0]=9;
            candidatoA[1]=9;
            int[] candidatoB = new int[2];
            candidatoB[0]=9;
            candidatoB[1]=9;
            int[] candidatoC = new int[2];
            candidatoC[0]=9;
            candidatoC[1]=9;

            int contadorCandidatos=1; //inicializo el 1 para que le random no tire error

            if(botones[0][1].getText().toString().equals("") && botones[0][0].getText().toString().equals("")){
                candidatoA[0]=0;
                candidatoA[1]=1;
                contadorCandidatos++;
            }

            if(botones[1][1].getText().toString().equals("") && botones[2][0].getText().toString().equals("")){
                candidatoB[0]=1;
                candidatoB[1]=1;
                contadorCandidatos++;
            }

            if(botones[1][2].getText().toString().equals("") && botones[2][2].getText().toString().equals("")){
                candidatoC[0]=1;
                candidatoC[1]=2;
                contadorCandidatos++;
            }

            if(contadorCandidatos>1){
                boolean encontrado=false;

                while(encontrado==false){

                    Random r = new Random();
                    int alAzar = r.nextInt(3-0)+ 0;//El cero de la punta está incluido, el contador no. Osea para elgir entre 0 y 5 --> r.netInt(6-0)+0
                    System.out.println("El numero al azar es:"+alAzar);

                    if(alAzar==0 && candidatoA[0]!=9){
                        botones[candidatoA[0]][candidatoA[1]].setText("O");
                        encontrado=true;
                        ultimaPosicionX=candidatoA[0];
                        ultimaPosicionY=candidatoA[1];
                        bloqueado=true;
                    }

                    if(alAzar==1 && candidatoB[0]!=9){
                        botones[candidatoB[0]][candidatoB[1]].setText("O");
                        encontrado=true;
                        ultimaPosicionX=candidatoB[0];
                        ultimaPosicionY=candidatoB[1];
                        bloqueado=true;
                    }

                    if(alAzar==2 && candidatoC[0]!=9){
                        botones[candidatoC[0]][candidatoC[1]].setText("O");
                        encontrado=true;
                        ultimaPosicionX=candidatoC[0];
                        ultimaPosicionY=candidatoC[1];
                        bloqueado=true;
                    }
                }
            }
            else{
                ubicacionAlAzar();
                bloqueado=true;
            }
        }

        if (bloqueado==false && ultimaPosicionX==1 && ultimaPosicionY==0){
            System.out.println("MAAAAMIIIICHULAAAAA ABAJO AL MEDIO PA");
            int[] candidatoA = new int[2];
            candidatoA[0]=9;
            candidatoA[1]=9;
            int[] candidatoB = new int[2];
            candidatoB[0]=9;
            candidatoB[1]=9;

            int contadorCandidatos=1; //inicializo el 1 para que el random no tire error. Cantidad de vias libres para hacer tateti

            if(botones[0][0].getText().toString().equals("") && botones[2][0].getText().toString().equals("")){
                candidatoA[0]=0;//HC
                candidatoA[1]=0;
                contadorCandidatos++;
            }

            if(botones[1][1].getText().toString().equals("") && botones[1][2].getText().toString().equals("")){
                candidatoB[0]=1;
                candidatoB[1]=1;
                contadorCandidatos++;
            }

            if(contadorCandidatos>1){

                boolean encontrado=false;
                while(encontrado==false){

                    Random r = new Random();
                    int alAzar = r.nextInt(2-0)+ 0;//El cero de la punta está incluido, el contador no. Osea para elgir entre 0 y 5 --> r.netInt(6-0)+0
                    System.out.println("El numero al azar es:"+alAzar);

                    if(alAzar==0 && candidatoA[0]!=9){
                        botones[candidatoA[0]][candidatoA[1]].setText("O");
                        encontrado=true;
                        ultimaPosicionX=candidatoA[0];
                        ultimaPosicionY=candidatoA[1];
                        bloqueado=true;
                    }

                    if(alAzar==1 && candidatoB[0]!=9){
                        botones[candidatoB[0]][candidatoB[1]].setText("O");
                        encontrado=true;
                        ultimaPosicionX=candidatoB[0];
                        ultimaPosicionY=candidatoB[1];
                        bloqueado=true;
                    }

                }
            }
            else{
                ubicacionAlAzar();
                bloqueado=true;
            }
        }

        if (bloqueado==false && ultimaPosicionX==1 && ultimaPosicionY==1){
            System.out.println("MAAAAMIIIICHULAAAAA AL MEDIO MEDIOOOOO");
            int[] candidatoA = new int[2];
            candidatoA[0]=9;
            candidatoA[1]=9;
            int[] candidatoB = new int[2];
            candidatoB[0]=9;
            candidatoB[1]=9;
            int[] candidatoC = new int[2];
            candidatoC[0]=9;
            candidatoC[1]=9;
            int[] candidatoD = new int[2];
            candidatoD[0]=9;
            candidatoD[1]=9;

            int contadorCandidatos=1; //inicializo el 1 para que le random no tire error

            if(botones[0][1].getText().toString().equals("") && botones[2][1].getText().toString().equals("")){
                candidatoA[0]=0;
                candidatoA[1]=1;
                contadorCandidatos++;
            }

            if(botones[1][0].getText().toString().equals("") && botones[1][2].getText().toString().equals("")){
                candidatoB[0]=1;
                candidatoB[1]=0;
                contadorCandidatos++;
            }

            if(botones[0][0].getText().toString().equals("") && botones[2][2].getText().toString().equals("")){
                candidatoC[0]=0;
                candidatoC[1]=0;
                contadorCandidatos++;
            }

            if(botones[2][0].getText().toString().equals("") && botones[0][2].getText().toString().equals("")){
                candidatoD[0]=2;
                candidatoD[1]=0;
                contadorCandidatos++;
            }

            if(contadorCandidatos>1){
                boolean encontrado=false;

                while(encontrado==false){

                    Random r = new Random();
                    int alAzar = r.nextInt(4-0)+ 0;//El cero de la punta está incluido, el contador no. Osea para elgir entre 0 y 5 --> r.netInt(6-0)+0
                    System.out.println("El numero al azar es:"+alAzar);

                    if(alAzar==0 && candidatoA[0]!=9){
                        botones[candidatoA[0]][candidatoA[1]].setText("O");
                        encontrado=true;
                        ultimaPosicionX=candidatoA[0];
                        ultimaPosicionY=candidatoA[1];
                        bloqueado=true;
                    }

                    if(alAzar==1 && candidatoB[0]!=9){
                        botones[candidatoB[0]][candidatoB[1]].setText("O");
                        encontrado=true;
                        ultimaPosicionX=candidatoB[0];
                        ultimaPosicionY=candidatoB[1];
                        bloqueado=true;
                    }

                    if(alAzar==2 && candidatoC[0]!=9){
                        botones[candidatoC[0]][candidatoC[1]].setText("O");
                        encontrado=true;
                        ultimaPosicionX=candidatoC[0];
                        ultimaPosicionY=candidatoC[1];
                        bloqueado=true;
                    }

                    if(alAzar==3 && candidatoD[0]!=9){
                        botones[candidatoD[0]][candidatoD[1]].setText("O");
                        encontrado=true;
                        ultimaPosicionX=candidatoD[0];
                        ultimaPosicionY=candidatoD[1];
                        bloqueado=true;
                    }
                }
            }
            else{
                ubicacionAlAzar();
                bloqueado=true;
            }

        }

        if (bloqueado==false && ultimaPosicionX==1 && ultimaPosicionY==2){
            System.out.println("MAAAAMIIIICHULAAAAA DERECHA AL MEDIO PA");
            int[] candidatoA = new int[2];
            candidatoA[0]=9;
            candidatoA[1]=9;
            int[] candidatoB = new int[2];
            candidatoB[0]=9;
            candidatoB[1]=9;

            int contadorCandidatos=1; //inicializo el 1 para que el random no tire error. Cantidad de vias libres para hacer tateti

            if(botones[0][2].getText().toString().equals("") && botones[2][2].getText().toString().equals("")){
                candidatoA[0]=0;//HC
                candidatoA[1]=2;
                contadorCandidatos++;
            }

            if(botones[1][1].getText().toString().equals("") && botones[1][0].getText().toString().equals("")){
                candidatoB[0]=1;
                candidatoB[1]=1;
                contadorCandidatos++;
            }

            if(contadorCandidatos>1){

                boolean encontrado=false;
                while(encontrado==false){

                    Random r = new Random();
                    int alAzar = r.nextInt(2-0)+ 0;//El cero de la punta está incluido, el contador no. Osea para elgir entre 0 y 5 --> r.netInt(6-0)+0
                    System.out.println("El numero al azar es:"+alAzar);

                    if(alAzar==0 && candidatoA[0]!=9){
                        botones[candidatoA[0]][candidatoA[1]].setText("O");
                        encontrado=true;
                        ultimaPosicionX=candidatoA[0];
                        ultimaPosicionY=candidatoA[1];
                        bloqueado=true;
                    }

                    if(alAzar==1 && candidatoB[0]!=9){
                        botones[candidatoB[0]][candidatoB[1]].setText("O");
                        encontrado=true;
                        ultimaPosicionX=candidatoB[0];
                        ultimaPosicionY=candidatoB[1];
                        bloqueado=true;
                    }

                }
            }
            else{
                ubicacionAlAzar();
                bloqueado=true;
            }
        }

        if (bloqueado==false && ultimaPosicionX==2 && ultimaPosicionY==0){
            System.out.println("MAAAAMIIIICHULAAAAA ESQUINA INFERIOR IZQUIERDAAAAA");
            int[] candidatoA = new int[2];
            candidatoA[0]=9;
            candidatoA[1]=9;
            int[] candidatoB = new int[2];
            candidatoB[0]=9;
            candidatoB[1]=9;
            int[] candidatoC = new int[2];
            candidatoC[0]=9;
            candidatoC[1]=9;

            int contadorCandidatos=1; //inicializo el 1 para que le random no tire error

            if(botones[1][0].getText().toString().equals("") && botones[0][0].getText().toString().equals("")){
                candidatoA[0]=1;
                candidatoA[1]=0;
                contadorCandidatos++;
            }

            if(botones[1][1].getText().toString().equals("") && botones[0][2].getText().toString().equals("")){
                candidatoB[0]=1;
                candidatoB[1]=1;
                contadorCandidatos++;
            }

            if(botones[2][1].getText().toString().equals("") && botones[2][2].getText().toString().equals("")){
                candidatoC[0]=2;
                candidatoC[1]=1;
                contadorCandidatos++;
            }

            if(contadorCandidatos>1){

                boolean encontrado=false;

                    while(encontrado==false){

                        Random r = new Random();
                        int alAzar = r.nextInt(3-0)+ 0;//El cero de la punta está incluido, el contador no. Osea para elgir entre 0 y 5 --> r.netInt(6-0)+0
                        System.out.println("El numero al azar es:"+alAzar);

                        if(alAzar==0 && candidatoA[0]!=9){
                            botones[candidatoA[0]][candidatoA[1]].setText("O");
                            encontrado=true;
                            ultimaPosicionX=candidatoA[0];
                            ultimaPosicionY=candidatoA[1];
                            bloqueado=true;
                        }

                        if(alAzar==1 && candidatoB[0]!=9){
                            botones[candidatoB[0]][candidatoB[1]].setText("O");
                            encontrado=true;
                            ultimaPosicionX=candidatoB[0];
                            ultimaPosicionY=candidatoB[1];
                            bloqueado=true;
                        }

                        if(alAzar==2 && candidatoC[0]!=9){
                            botones[candidatoC[0]][candidatoC[1]].setText("O");
                            encontrado=true;
                            ultimaPosicionX=candidatoC[0];
                            ultimaPosicionY=candidatoC[1];
                            bloqueado=true;
                        }
                }

            }
            else{
                ubicacionAlAzar();
                bloqueado=true;
            }
        }

        if (bloqueado==false && ultimaPosicionX==2 && ultimaPosicionY==1){
            System.out.println("MAAAAMIIIICHULAAAAA ABAJO AL MEDIO PA");
            int[] candidatoA = new int[2];
            candidatoA[0]=9;
            candidatoA[1]=9;
            int[] candidatoB = new int[2];
            candidatoB[0]=9;
            candidatoB[1]=9;

            int contadorCandidatos=1; //inicializo el 1 para que el random no tire error. Cantidad de vias libres para hacer tateti

            if(botones[2][0].getText().toString().equals("") && botones[2][2].getText().toString().equals("")){
                candidatoA[0]=2;//HC
                candidatoA[1]=0;
                contadorCandidatos++;
            }

            if(botones[1][1].getText().toString().equals("") && botones[0][1].getText().toString().equals("")){
                candidatoB[0]=1;
                candidatoB[1]=1;
                contadorCandidatos++;
            }

            if(contadorCandidatos>1){

                boolean encontrado=false;
                while(encontrado==false){

                    Random r = new Random();
                    int alAzar = r.nextInt(2-0)+ 0;//El cero de la punta está incluido, el contador no. Osea para elgir entre 0 y 5 --> r.netInt(6-0)+0
                    System.out.println("El numero al azar es:"+alAzar);

                    if(alAzar==0 && candidatoA[0]!=9){
                        botones[candidatoA[0]][candidatoA[1]].setText("O");
                        encontrado=true;
                        ultimaPosicionX=candidatoA[0];
                        ultimaPosicionY=candidatoA[1];
                        bloqueado=true;
                    }

                    if(alAzar==1 && candidatoB[0]!=9){
                        botones[candidatoB[0]][candidatoB[1]].setText("O");
                        encontrado=true;
                        ultimaPosicionX=candidatoB[0];
                        ultimaPosicionY=candidatoB[1];
                        bloqueado=true;
                    }

                }
            }
            else{
                ubicacionAlAzar();
                bloqueado=true;
            }
        }

        if (bloqueado==false && ultimaPosicionX==2 && ultimaPosicionY==2){
            System.out.println("MAAAAMIIIICHULAAAAA ESQUINA INFERIOR DERECHAAAAAA");
            int[] candidatoA = new int[2];
            candidatoA[0]=9;
            candidatoA[1]=9;
            int[] candidatoB = new int[2];
            candidatoB[0]=9;
            candidatoB[1]=9;
            int[] candidatoC = new int[2];
            candidatoC[0]=9;
            candidatoC[1]=9;

            int contadorCandidatos=1; //inicializo el 1 para que le random no tire error

            if(botones[1][2].getText().toString().equals("") && botones[0][2].getText().toString().equals("")){
                candidatoA[0]=1;
                candidatoA[1]=2;
                contadorCandidatos++;
            }

            if(botones[1][1].getText().toString().equals("") && botones[0][0].getText().toString().equals("")){
                candidatoB[0]=1;
                candidatoB[1]=1;
                contadorCandidatos++;
            }

            if(botones[2][1].getText().toString().equals("") && botones[2][0].getText().toString().equals("")){
                candidatoC[0]=2;
                candidatoC[1]=1;
                contadorCandidatos++;
            }

            if(contadorCandidatos>1){
                boolean encontrado=false;

                while(encontrado==false){

                    Random r = new Random();
                    int alAzar = r.nextInt(3-0)+ 0;//El cero de la punta está incluido, el contador no. Osea para elgir entre 0 y 5 --> r.netInt(6-0)+0
                    System.out.println("El numero al azar es:"+alAzar);

                    if(alAzar==0 && candidatoA[0]!=9){
                        botones[candidatoA[0]][candidatoA[1]].setText("O");
                        encontrado=true;
                        ultimaPosicionX=candidatoA[0];
                        ultimaPosicionY=candidatoA[1];
                        bloqueado=true;
                    }

                    if(alAzar==1 && candidatoB[0]!=9){
                        botones[candidatoB[0]][candidatoB[1]].setText("O");
                        encontrado=true;
                        ultimaPosicionX=candidatoB[0];
                        ultimaPosicionY=candidatoB[1];
                        bloqueado=true;
                    }

                    if(alAzar==2 && candidatoC[0]!=9){
                        botones[candidatoC[0]][candidatoC[1]].setText("O");
                        encontrado=true;
                        ultimaPosicionX=candidatoC[0];
                        ultimaPosicionY=candidatoC[1];
                        bloqueado=true;
                    }
                }
            }
            else{
                ubicacionAlAzar();
                bloqueado=true;
            }
        }

        bloqueado=false;

    }

    private void ubicacionAlAzar(){
        int vectorPosicionEnX[] = new int[9];
        int vectorPosicionEnY[] = new int[9];

        int l=0;//Posición en los vectores auxiliares para marcar la posición al azar
        for(int i=0;i<3;i++){
            for(int k=0;k<3;k++){
                if(botones[i][k].getText().toString().equals("")){
                    vectorPosicionEnX[l]=i;
                    vectorPosicionEnY[l]=k;
                    l++;
                }
            }
        }
        if(l==0){
            System.out.println("No hay posiciones disponibles");
        }
        else{
            Random r = new Random();
            int alAzar = r.nextInt(l-0)+ 0;
            botones[vectorPosicionEnX[alAzar]][vectorPosicionEnY[alAzar]].setText("O");
            ultimaPosicionX=vectorPosicionEnX[alAzar];
            ultimaPosicionY=vectorPosicionEnY[alAzar];

        }
    }

    private void primerJugadaMaquina(){

        System.out.println("Entro a la PRIMER JUGADAAAAAAAAAAAAAAAAAAAAAAA");
        //inicializo con cualquier numero para no confundir cuando pruebe
        int posXprimero=99;
        int posYprimero=99;
        int azarX=99;
        int azarY=99;

        for(int i=0;i<3;i++){
            for(int k=0;k<3;k++){
                if(botones[i][k].getText().toString().equals("X")){
                    posXprimero=i;
                    posYprimero=k;
                }
            }
        }
        //System.out.println("Cant fichas jugador"+cantFichasJugador);
        System.out.println("Pos X primero"+posXprimero);
        System.out.println("Pos Y primero"+posYprimero);

        Random r = new Random();
        azarX = r.nextInt(3-0)+ 0;
        azarY = r.nextInt(3-0)+ 0;

        while(posXprimero==azarX && posYprimero==azarY)
        {
            azarX = r.nextInt(3-0)+ 0;
            azarY = r.nextInt(3-0)+ 0;
        }

        System.out.println("LO PONGO EN X "+azarX);
        System.out.println("LO PONGO EN Y "+azarY);
        botones[azarX][azarY].setText("O");

        banderaPrimerJugada = false;
        ultimaPosicionX=azarX;
        ultimaPosicionY=azarY;
    }

    private void mostrarPopUpVictoria() {
        dialogBuilder = new AlertDialog.Builder(this);
        final View victoriaPopUpView = getLayoutInflater().inflate(R.layout.popup_single_winner, null);
        tv_victoria_pop = victoriaPopUpView.findViewById(R.id.textview_1_pop_sing);
        btn_playAgain_pop = victoriaPopUpView.findViewById(R.id.button_play_pop_sing);
        btn_exit_pop = victoriaPopUpView.findViewById(R.id.button_exit_pop_sing);

        if (puntosJugador1 > puntosComputer) {
            tv_victoria_pop.setText("YOU ARE THE WINNER!!!");
        } else {
            tv_victoria_pop.setText("YOU ARE A LOSER!!!");
        }

        dialogBuilder.setView(victoriaPopUpView);
        dialog = dialogBuilder.create();
        dialog.show();

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

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                resetearJuego();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    //=======================================================================================================================================================
    //============================ CODIGO AGREGADO (EN PRUEBA) ===============================================================================================
    // ======================================================================================================================================================

    private void colorearJugadaGanadora(int x1, int y1, int x2, int y2, int x3, int y3){
        // formo la matriz de strings
//        botones[x1][y1].setBackgroundResource(R.drawable.buttons_tic_tac_toe_green);
//        botones[x2][y2].setBackgroundResource(R.drawable.buttons_tic_tac_toe_green);
//        botones[x3][y3].setBackgroundResource(R.drawable.buttons_tic_tac_toe_green);
        PintarJugadaTask_sing pintor = new PintarJugadaTask_sing(x1, y1, x2, y2, x3, y3);
        pintor.execute();
    }

    class PintarJugadaTask_sing extends AsyncTask<Void, Void, Void> {

        public PintarJugadaTask_sing(Integer... integers){
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
            btn_quit.setClickable(false);
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
            if(simbolo_ganador.equals("X")){
                ganador_you();
            }
            else{
                ganador_computer();
            }
            if(puntosJugador1 == 3){
                mostrarPopUpVictoria();
            }else if(puntosComputer == 3){
                mostrarPopUpVictoria();
            }
            btn_quit.setClickable(true);
            btn_reset.setClickable(true);
            desbloquearBotonesJuego();
        }
    }

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

    private void resetearColorMatriz(){
        for (int i = 0; i < 3; i++) {
            for (int h = 0; h < 3; h++) {
                botones[i][h].setBackgroundResource(R.drawable.buttons_tic_tac_toe);
            }
        }
    }



}
