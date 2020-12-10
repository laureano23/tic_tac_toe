package com.lugopa.tic_tac_toe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

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
        ImageButton btn_search = findViewById(R.id.button_search_bd);
        final EditText et_search = findViewById(R.id.editext_buscar_dni_db);


        //manejo de lista
        listView = findViewById(R.id.listview_tabla_bd);
        adapter = new JugadorListAdapter(getApplicationContext(), R.layout.adapter_jugadores_layout, lista_jugadores);

        dbJugadores = FirebaseDatabase.getInstance().getReference("Jugadores");
        dbJugadores.addListenerForSingleValueEvent(valueEventListener);

        // manejo del spinner
        final Spinner mSpinner = (Spinner) findViewById(R.id.spinner_orden_db);
        ArrayAdapter<String> mySpinnerAdapter = new ArrayAdapter<String>(TablaJugadoresActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.names));
        mySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(mySpinnerAdapter);

        btn_order_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // determinar Query segun la opcion seleccionada en el SPINNER
                String item = mSpinner.getSelectedItem().toString();
                Query query;
                switch (item){
                    case "DNI":
                        query = dbJugadores.orderByChild("dni");
                        query.addListenerForSingleValueEvent(valueEventListener);
                        break;
                    case "NOMBRE":
                        query = dbJugadores.orderByChild("nombre");
                        query.addListenerForSingleValueEvent(valueEventListener);
                        break;
                    case "VICTORIAS":
                        query = dbJugadores.orderByChild("victorias");
                        query.addListenerForSingleValueEvent(valueEventListener);
                        break;
                }
            }
        });

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidadorDeDatos validadorDeDatos = new ValidadorDeDatos();
                int dni =0 ;
                if(et_search.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Insert a dni number!", Toast.LENGTH_SHORT).show();
                } else {
                    dni = Integer.parseInt(et_search.getText().toString());
                    if (!validadorDeDatos.validar_dni(dni)) {
                        Toast.makeText(getApplicationContext(), "Invalid DNI, Try again!", Toast.LENGTH_SHORT).show();
                    } else {
                        Query query =dbJugadores.orderByChild("dni").equalTo(dni);
                        query.addListenerForSingleValueEvent(valueEventListener);
                    }
                }
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
        Query query =dbJugadores.orderByChild("dni").equalTo(dni);
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