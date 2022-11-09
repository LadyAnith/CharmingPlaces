package com.example.charmingplaces;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button boton;
    Button boton2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boton = (Button) findViewById(R.id.btnInicio);
        boton2 = (Button) findViewById(R.id.btnRegistro);
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Con el evento onClick, llamo a la actividad del juego
                Intent i = new Intent(getApplicationContext(), CapturePlace.class);
                startActivity(i);

            }

        });

        boton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Con el evento onClick, llamo a la actividad del juego
                Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(i);

            }

        });
    }
}
