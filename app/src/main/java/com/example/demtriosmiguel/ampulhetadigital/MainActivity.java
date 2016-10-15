package com.example.demtriosmiguel.ampulhetadigital;

import android.app.Dialog;
import android.content.DialogInterface;
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
    TextView textViewDefinaTempo;
    Button botaoIniciar;
    Button botaoPausar;
    Button botaoReiniciar;
    Button botaoParar;
    public static LinearLayout linearLayoutDefinaTempo;
    public static LinearLayout linearLayoutComandosCronometro;

    Cronometro cronometro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewTimer = (TextView) findViewById(R.id.textViewTimer);
        textViewDefinaTempo = (TextView) findViewById(R.id.textViewDefinaTempo);

        botaoIniciar = (Button) findViewById(R.id.botaoIniciar);
        botaoPausar = (Button) findViewById(R.id.botaoPausar);
        botaoReiniciar = (Button) findViewById(R.id.botaoReiniciar);
        botaoParar = (Button) findViewById(R.id.botaoParar);

        linearLayoutDefinaTempo = (LinearLayout) findViewById(R.id.linearLayoutDefinaTempo);
        linearLayoutComandosCronometro = (LinearLayout) findViewById(R.id.linearLayoutComandosCronometro);

        cronometro = new Cronometro(textViewTimer, botaoIniciar, botaoPausar, botaoReiniciar, botaoParar);

        SharedPreferences configuracoes = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        atualizaComandosCronometro();

        textViewTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogDefinirTempo();
            }
        });

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

    private void atualizaComandosCronometro() {
        if (cronometro.getSegundosTotais() == 0l) {
            linearLayoutDefinaTempo.setVisibility(View.VISIBLE);
            linearLayoutComandosCronometro.setVisibility(View.GONE);
        } else {
            linearLayoutDefinaTempo.setVisibility(View.GONE);
            linearLayoutComandosCronometro.setVisibility(View.VISIBLE);
        }
    }

    private void showDialogDefinirTempo() {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setTitle("Definir tempo");
        dialog.setContentView(R.layout.dialog_definir_tempo);

        Button btnDefinir = (Button) dialog.findViewById(R.id.buttonDefinir);
        Button btnCancelar = (Button) dialog.findViewById(R.id.buttonCancelar);

        final NumberPicker npHoras = (NumberPicker) dialog.findViewById(R.id.numberPickerHoras);
        npHoras.setMaxValue(99);
        npHoras.setMinValue(0);

        final NumberPicker npMinutos = (NumberPicker) dialog.findViewById(R.id.numberPickerMinutos);
        npMinutos.setMaxValue(59);
        npMinutos.setMinValue(0);

        final NumberPicker npSegundos = (NumberPicker) dialog.findViewById(R.id.numberPickerSegundos);
        npSegundos.setMaxValue(59);
        npSegundos.setMinValue(0);

        btnDefinir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cronometro.setHorasMinutosSegundos(npHoras.getValue(), npMinutos.getValue(), npSegundos.getValue());
                atualizaComandosCronometro();
                dialog.dismiss();
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void exibeToastTempoEncerrado() {
        Toast.makeText(getApplicationContext(), "Tempo encerrado", Toast.LENGTH_LONG).show();
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