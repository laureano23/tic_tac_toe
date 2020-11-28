package com.lugopa.tic_tac_toe;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TablaJugadoresActivity extends AppCompatActivity {

    //private DatabaseManager databaseManager = new DatabaseManager();
    private DatabaseReference dbJugadores;

    ListView listView;
    private ArrayList<Jugador> lista_jugadores = new ArrayList<>();
    JugadorListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabla_jugadores);

        Button btn_order_data = findViewById(R.id.button_order_data_db);
        //manejo de lista
        listView = findViewById(R.id.listview_tabla_bd);
        adapter = new JugadorListAdapter(getApplicationContext(), R.layout.adapter_jugadores_layout, lista_jugadores);

        dbJugadores = FirebaseDatabase.getInstance().getReference("Jugadores");
        dbJugadores.addListenerForSingleValueEvent(valueEventListener);

        btn_order_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // determinar Query segun la opcion seleccionada en el SPINNER
                //get_jugador_byDni(dni);
            }
        });
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finish();
        //overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public void get_jugador_byDni(int dni) {
        Query query = FirebaseDatabase.getInstance().getReference("Jugadores")
                .orderByChild("dni")
                .equalTo(dni);
        query.addListenerForSingleValueEvent(valueEventListener);
    }

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
                listView.setAdapter(adapter);
            }

        }

        @Override
        public void onCancelled( DatabaseError error) {
            Log.d("ERROR REC. FIREBASE ", "ERROR - No se pudieron recuperar los datos de Firebase");
            //progressBar.setVisibility(View.INVISIBLE);
        }
    };

}