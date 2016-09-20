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

    private long calculoHoras;
    private long calculoMinutos;
    private long calculoSegundos;
    private long segundosTotais;

    private long seguntosTotaisInicial;

    private Handler handler;
    private Runnable runnable;

    private boolean emExecucao = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewTimer = (TextView) findViewById(R.id.textViewTimer);

        botaoIniciar = (Button) findViewById(R.id.botaoIniciar);
        botaoPausar = (Button) findViewById(R.id.botaoPausar);
        botaoReiniciar = (Button) findViewById(R.id.botaoReiniciar);
        botaoParar = (Button) findViewById(R.id.botaoParar);

        Cronometro cronometro = new Cronometro(textViewTimer, botaoIniciar, botaoPausar, botaoReiniciar, botaoParar);

        /*botaoPausar.setVisibility(View.GONE);
        botaoReiniciar.setVisibility(View.GONE);
        botaoParar.setVisibility(View.GONE);*/

        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        // entradas do usuario
        int segundos = 10; // 59 máximo
//        int minutos = Integer.parseInt(SP.getString("MinutosId", "0")); // 59 máximo
        int minutos = 0; // 59 máximo
        int horas = 0; // 99 máximo

        cronometro.setHorasMinutosSegundos(horas, minutos, segundos);
        cronometro.setTempoTotalEmSegundos();
        cronometro.calculaHorasMinutosSegundos();
        cronometro.exibeTempoFormatado();

        // somatorio das entradas em segundos
        /*setTempoTotalEmSegundos(segundos, minutos, horas);
        calculaHorasMinutosSegundos();
        exibeTempoFormatado();*/

        botaoIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emExecucao = true;
                executaCronometro();

                botaoIniciar.setVisibility(View.GONE);
                botaoPausar.setVisibility(View.VISIBLE);
                botaoReiniciar.setVisibility(View.VISIBLE);
                botaoParar.setVisibility(View.VISIBLE);
            }
        });

        botaoPausar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emExecucao = false;
                handler.removeCallbacks(runnable);

                botaoIniciar.setVisibility(View.VISIBLE);
                botaoPausar.setVisibility(View.GONE);
                botaoReiniciar.setVisibility(View.VISIBLE);
                botaoParar.setVisibility(View.VISIBLE);
            }
        });

        botaoReiniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.removeCallbacks(runnable);
                segundosTotais = seguntosTotaisInicial;
                emExecucao = true;
                executaCronometro();

                botaoIniciar.setVisibility(View.GONE);
                botaoPausar.setVisibility(View.VISIBLE);
                botaoReiniciar.setVisibility(View.VISIBLE);
                botaoParar.setVisibility(View.VISIBLE);
            }
        });

        botaoParar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.removeCallbacks(runnable);
                segundosTotais = seguntosTotaisInicial;
                emExecucao = false;

                calculaHorasMinutosSegundos();
                exibeTempoFormatado();

                botaoIniciar.setVisibility(View.VISIBLE);
                botaoPausar.setVisibility(View.GONE);
                botaoReiniciar.setVisibility(View.GONE);
                botaoParar.setVisibility(View.GONE);
            }
        });

    }

    private void setTempoTotalEmSegundos(int segundos, int minutos, int horas) {
        segundosTotais = (segundos + (minutos * 60) + (horas * 60 * 60));

        // registra tempo total definido para caso o cronometro seja reiniciado ou parado
        seguntosTotaisInicial = segundosTotais;
    }

    private void calculaHorasMinutosSegundos() {
        calculoHoras = Math.abs(segundosTotais / 3600); // horas
        long calculoDiferencaMinutosEmSegundos = calculoHoras * 3600;
        calculoMinutos = Math.abs((calculoDiferencaMinutosEmSegundos - segundosTotais) / 60); // minutos
        long calculoHorasMinutosEmSegundos = (calculoMinutos * 60) + calculoDiferencaMinutosEmSegundos;
        calculoSegundos = segundosTotais - calculoHorasMinutosEmSegundos; // segundos
    }

    private void exibeTempoFormatado() {
        String horas = "";
        String minutos = "";
        String segundos = "";

        if (calculoHoras < 10) {
            horas = "0"+calculoHoras;
        } else {
            horas += calculoHoras;
        }

        if (calculoMinutos < 10) {
            minutos = "0"+calculoMinutos;
        } else {
            minutos += calculoMinutos;
        }

        if (calculoSegundos < 10) {
            segundos = "0"+calculoSegundos;
        } else {
            segundos += calculoSegundos;
        }

        StringBuilder timerDisplay = new StringBuilder();
        timerDisplay.append(horas).append(":");
        timerDisplay.append(minutos).append(":");
        timerDisplay.append(segundos);

        textViewTimer.setText(timerDisplay.toString());
    }

    public void executaCronometro() {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 1000);
                try {
                    if (emExecucao && segundosTotais > 0) {
                        segundosTotais--;
                        calculaHorasMinutosSegundos();
                        exibeTempoFormatado();
                    } else {
                        handler.removeCallbacks(runnable);

                        emExecucao = false;
                        segundosTotais = seguntosTotaisInicial;

                        botaoIniciar.setVisibility(View.VISIBLE);
                        botaoPausar.setVisibility(View.GONE);
                        botaoReiniciar.setVisibility(View.GONE);
                        botaoParar.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        handler.postDelayed(runnable, 0);
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
