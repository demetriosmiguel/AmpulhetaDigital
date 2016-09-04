package com.example.demtriosmiguel.ampulhetadigital;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.*;

public class MainActivity extends AppCompatActivity {

    TextView textViewTimer;

    private long calculoHoras;
    private long calculoMinutos;
    private long calculoSegundos;
    private long seguntosTotais;

    private long seguntosTotaisInicial;

    private Handler handler;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewTimer = (TextView) findViewById(R.id.textViewTimer);

        // formula para calculo de tempo
        /*
            1 hora = 60 minutos = 3600 segundos

            2 horas long/ 30 minutos / 25 segundos
            25 + (30 * 60) + (2 * 60 * 60) = 9025 segundos totais

            9025 /long ((60 * 60) = 3600) =  2,5 pegar valor absoluto = 2

            2 * 3600 (long7200) - 9025 = 1825 / 60 = 30 pegar valor absoluto

            30 * 60 = 1800 + 7200 = 9000
         */

        // entradas do usuario
        int segundos = 25; // 99 máximo
        int minutos = 30; // 59 máximo
        int horas = 2; // 59 máximo

        // somatorio das entradas em segundos
        setTempoTotalEmSeguntos(segundos, minutos, horas);

//        calculaHorasMinutosSegundos();
//        exibeTempoFormatado();

        countDownStart();
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

    public void countDownStart() {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 1000);
                try {
                    if (true) {
                        calculaHorasMinutosSegundos();
                        exibeTempoFormatado();
                        seguntosTotais--;
                    } else {
                        handler.removeCallbacks(runnable);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        handler.postDelayed(runnable, 0);
    }
}
