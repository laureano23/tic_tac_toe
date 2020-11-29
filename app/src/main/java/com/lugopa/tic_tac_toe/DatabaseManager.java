package com.lugopa.tic_tac_toe;

import android.content.Intent;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;

public class DatabaseManager {

    private ArrayList<Jugador> lista_jugadores = new ArrayList<>();

    public DatabaseManager() {
        //get_tablaJugadores_BD();
    }


    public void guardarJugador_BD(final int dni, final String nom, final int cant ){
        final DatabaseReference dbJugadores = FirebaseDatabase.getInstance().getReference("Jugadores");
        Query query =dbJugadores.orderByChild("dni").equalTo(dni);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lista_jugadores.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Jugador player = ds.getValue(Jugador.class);
                        lista_jugadores.add(player);
                    }
                    Jugador jugadorGuardado = lista_jugadores.get(0);
                    int cantVictorias = jugadorGuardado.getVictorias();
                    cantVictorias = cantVictorias + cant;
                    jugadorGuardado.setVictorias(cantVictorias);
                    String key = jugadorGuardado.getKey();
                    dbJugadores.child(key).child("victorias").setValue(cantVictorias);
                } else {
                    String key = dbJugadores.push().getKey();
                    Jugador jugadorNuevo = new Jugador(dni, nom, cant,key);
                    dbJugadores.child(key).setValue(jugadorNuevo);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("ERROR REC. FIREBASE ", "ERROR - No se pudieron recuperar los datos de Firebase");
            }
        });
    }


    /*
    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange( DataSnapshot dataSnapshot) {
            lista_jugadores.clear(); // limpio la lista antes de volver a llenarla
            if (dataSnapshot.exists()) {
                // OBTENEMOS LOS DATOS DEL PUNTAJE DE LA BASE DE DATOS
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Jugador player = ds.getValue(Jugador.class);
                    lista_jugadores.add(player);
                }
            }

        }

        @Override
        public void onCancelled( DatabaseError error) {
            Log.d("ERROR REC. FIREBASE ", "ERROR - No se pudieron recuperar los datos de Firebase");
            //progressBar.setVisibility(View.INVISIBLE);
        }
    };
     */

}