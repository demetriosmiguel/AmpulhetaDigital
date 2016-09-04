package com.example.demtriosmiguel.ampulhetadigital;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    private long seguntosTotais;

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

        botaoPausar.setVisibility(View.GONE);
        botaoReiniciar.setVisibility(View.GONE);
        botaoParar.setVisibility(View.GONE);

        // entradas do usuario
        int segundos = 10; // 59 máximo
        int minutos = 0; // 59 máximo
        int horas = 0; // 99 máximo

        // somatorio das entradas em segundos
        setTempoTotalEmSeguntos(segundos, minutos, horas);
        calculaHorasMinutosSegundos();
        exibeTempoFormatado();

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
                seguntosTotais = seguntosTotaisInicial;
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
                seguntosTotais = seguntosTotaisInicial;
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

    private void setTempoTotalEmSeguntos(int segundos, int minutos, int horas) {
        seguntosTotais = (segundos + (minutos * 60) + (horas * 60 * 60));

        // registra tempo total definido para caso o cronometro seja reiniciado ou parado
        seguntosTotaisInicial = seguntosTotais;
    }

    private void calculaHorasMinutosSegundos() {
        calculoHoras = Math.abs(seguntosTotais / 3600); // horas
        long calculoDiferencaMinutosEmSegundos = calculoHoras * 3600;
        calculoMinutos = Math.abs((calculoDiferencaMinutosEmSegundos - seguntosTotais) / 60); // minutos
        long calculoHorasMinutosEmSegundos = (calculoMinutos * 60) + calculoDiferencaMinutosEmSegundos;
        calculoSegundos = seguntosTotais - calculoHorasMinutosEmSegundos; // segundos
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
                    if (emExecucao && seguntosTotais > 0) {
                        seguntosTotais--;
                        calculaHorasMinutosSegundos();
                        exibeTempoFormatado();
                    } else {
                        handler.removeCallbacks(runnable);

                        emExecucao = false;
                        seguntosTotais = seguntosTotaisInicial;

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
}
