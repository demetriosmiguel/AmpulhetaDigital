package com.example.demtriosmiguel.ampulhetadigital;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

public class MainActivity extends AppCompatActivity {

    TextView textViewTimer;
    Button botaoIniciar;
    Button botaoPausar;
    Button botaoReiniciar;
    Button botaoParar;

    Cronometro cronometro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewTimer = (TextView) findViewById(R.id.textViewTimer);

        botaoIniciar = (Button) findViewById(R.id.botaoIniciar);
        botaoPausar = (Button) findViewById(R.id.botaoPausar);
        botaoReiniciar = (Button) findViewById(R.id.botaoReiniciar);
        botaoParar = (Button) findViewById(R.id.botaoParar);

        cronometro = new Cronometro(textViewTimer, botaoIniciar, botaoPausar, botaoReiniciar, botaoParar);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        // entradas do usuario
        int segundos = 10; // 59 máximo
//        int minutos = Integer.parseInt(sp.getString("MinutosId", "0")); // 59 máximo
        int minutos = 0; // 59 máximo
        int horas = 0; // 99 máximo

        cronometro.setHorasMinutosSegundos(horas, minutos, segundos);
        cronometro.setTempoTotalEmSegundos();
        cronometro.calculaHorasMinutosSegundos();
        cronometro.exibeTempoFormatado();

        botaoIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cronometro.iniciar();
            }
        });

        botaoPausar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cronometro.pausar();
            }
        });

        botaoReiniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cronometro.reiniciar();
            }
        });

        botaoParar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cronometro.parar();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent novo = new Intent(MainActivity.this, PreferenciasActivity.class);
            startActivity(novo);
        }

        return super.onOptionsItemSelected(item);
    }
}