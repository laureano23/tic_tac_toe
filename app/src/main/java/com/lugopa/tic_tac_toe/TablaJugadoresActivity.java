package com.lugopa.tic_tac_toe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;

public class TablaJugadoresActivity extends AppCompatActivity {

    private EditText et_nombre, et_dni, et_cant;
    private DatabaseManager databaseManager = new DatabaseManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabla_jugadores);

        et_nombre = findViewById(R.id.editext_nombre_jugador);
        et_dni = findViewById(R.id.editext_dni_jugador);
        et_cant = findViewById(R.id.editext_cantidad_victorias);
        Button btn_get_data = findViewById(R.id.button_get_data_db);

        btn_get_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int dni = Integer.parseInt(et_dni.getText().toString());
                Jugador player = databaseManager.get_jugador_byDni(dni);
                et_dni.setText(player.getDni());
                et_nombre.setText(player.getNombre());
                et_cant.setText(player.getVictorias());
            }
        });

    }
}