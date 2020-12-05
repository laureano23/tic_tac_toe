package com.lugopa.tic_tac_toe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button btn_single, btn_multi, btn_instructions, btn_exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inicializar();

        btn_single.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            // abre activity del tic tac toe
                Intent intent = new Intent(getApplicationContext(), Tic_tac_toe_single.class);
                //intent.putExtra( lo que queramos pasar) // para pasar cosas a la activity
                startActivity(intent);
                //overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        btn_multi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // abre activity del tic tac toe
                Intent intent = new Intent(getApplicationContext(), Tic_tac_toe.class);
                //intent.putExtra( lo que queramos pasar) // para pasar cosas a la activity
                startActivity(intent);
                //overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        btn_instructions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // abre activity del tic tac toe
                Intent intent = new Intent(getApplicationContext(), TablaJugadoresActivity.class);
                //intent.putExtra( lo que queramos pasar) // para pasar cosas a la activity
                startActivity(intent);
                //overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });
    }

    private void inicializar(){
       btn_single = findViewById(R.id.Button1);
       btn_multi = findViewById(R.id.Button2);
       btn_instructions = findViewById(R.id.Button3);
       btn_exit = findViewById(R.id.Button4);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        //overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

}