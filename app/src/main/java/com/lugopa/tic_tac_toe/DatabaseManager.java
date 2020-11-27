package com.lugopa.tic_tac_toe;

import android.content.Intent;
import android.provider.ContactsContract;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public class DatabaseManager {

    DatabaseReference dbJugadores;
    private List<Jugador> lista_jugadores;
    //private DatabaseReference nodoJugadores;

    public DatabaseManager() {
        //DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        //DatabaseReference nodoJugadores = mDatabaseReference.child("Jugadores");
        //obtenerDeBD();
        lista_jugadores = new ArrayList<>();
    }

    public List<Jugador> getLista_jugadores() {
        return lista_jugadores;
    }

    // deberia ser private
    public void guardarEnBD(int doc, String nom, int cant) {
        DatabaseReference mDataBase = FirebaseDatabase.getInstance().getReference();
        //Map<String, Puntaje> users = new HashMap<>();
        Jugador puntajeBD = new Jugador(doc, nom, cant);
        mDataBase.child("Jugadores").push().setValue(puntajeBD);
    }

   /* private void obtenerDeBD(){
        nodoJugadores.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // OBTENEMOS LOS DATOS DEL PUNTAJE DE LA BASE DE DATOS
                    lista_jugadores.clear(); // limpio la lista antes de volver a llenarla
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Jugador player = ds.getValue(Jugador.class);
                        lista_jugadores.add(player);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("ERROR REC. FIREBASE ", "ERROR - No se pudieron recuperar los datos de Firebase");
                //progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }*/

    public Jugador get_jugador_byDni(int dni){
        // buscar en la lista el jugador con el DNI correspondiente
        Query query_jugador = FirebaseDatabase.getInstance().getReference("Jugadores")
                .orderByChild("nombre")
                .equalTo("jorgelin");
        query_jugador.addListenerForSingleValueEvent(valueEventListener);
        return lista_jugadores.get(1);
    }

    public List<Jugador> get_tablaJugadores(){
        // buscar todos los datos de la BD
        dbJugadores = FirebaseDatabase.getInstance().getReference("Jugadores");
        dbJugadores.addListenerForSingleValueEvent(valueEventListener);
        return lista_jugadores;
    }





    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                // OBTENEMOS LOS DATOS DEL PUNTAJE DE LA BASE DE DATOS
                lista_jugadores.clear(); // limpio la lista antes de volver a llenarla
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Jugador player = ds.getValue(Jugador.class);
                    lista_jugadores.add(player);
                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Log.d("ERROR REC. FIREBASE ", "ERROR - No se pudieron recuperar los datos de Firebase");
            //progressBar.setVisibility(View.INVISIBLE);
        }
    };

}