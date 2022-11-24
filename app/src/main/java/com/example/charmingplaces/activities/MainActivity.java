package com.example.charmingplaces.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;

import com.example.charmingplaces.R;

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
                Intent i = new Intent(getApplicationContext(), MenuActivity.class);
                startActivity(i);

            }

        });

        boton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Con el evento onClick, llamo a la actividad del Registro
                Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(i);

            }

        });
    }
}
