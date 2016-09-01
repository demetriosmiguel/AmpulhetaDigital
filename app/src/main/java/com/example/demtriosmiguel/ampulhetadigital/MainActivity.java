package com.example.demtriosmiguel.ampulhetadigital;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.widget.*;
import android.view.*;

public class MainActivity extends AppCompatActivity {

    Chronometer chronometer;
    Button btstart, btpause, btStop, btReset;
    boolean Click;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // formula para calculo de tempo
        /*
            1 hora = 60 minutos = 3600 segundos

            2 horas / 30 minutos / 25 segundos
            25 + (30 * 60) + (2 * 60 * 60) = 9025 segundos totais

            9025 / ((60 * 60) = 3600) =  2,5 pegar valor absoluto = 2

            2 * 3600 (7200) - 9025 = 1825 / 60 = 30 pegar valor absoluto

            30 * 60 = 1800 + 7200 = 9000
         */

        // entradas do usuario
        int segundos = 25;
        int minutos = 30;
        int horas = 2;

        // somatorio das entradas em segundos
        long seguntosTotais = segundos + (minutos * 60) + (horas * 60 * 60);

        long calculoHoras = Math.abs(seguntosTotais / 3600); // 2 horas
        long calculoDiferencaMinutosEmSegundos = calculoHoras * 3600; // 7200
        long calculoMinutos = Math.abs((calculoDiferencaMinutosEmSegundos - seguntosTotais) / 60); // 30 minutos
        long calculoHorasMinutosEmSegundos = (calculoMinutos * 60) + calculoDiferencaMinutosEmSegundos; // 9000
        long calculoSegundos = seguntosTotais - calculoHorasMinutosEmSegundos;

        Log.i("HORAS", String.valueOf(calculoHoras));
        Log.i("MINUTOS", String.valueOf(calculoMinutos));
        Log.i("SEGUNDOS", String.valueOf(calculoSegundos));


        // Declarando o tipo click como buleano como verdadeiro;
        // Estanciando as variaveis

        Click = true;
        chronometer = (Chronometer) findViewById(R.id.chronometer);
        btstart = (Button) findViewById(R.id.btstart);
        btpause = (Button) findViewById(R.id.btpause);
        btStop = (Button) findViewById(R.id.btStop);
        btReset = (Button) findViewById(R.id.btReset);

        // habilitando e desabilitando os botões

        btstart.setEnabled(true);
        btpause.setEnabled(false);
        btStop.setEnabled(false);
        btReset.setEnabled(false);

        // setando os botoes passando os evendos de click condicionando o mesmo  e startando o cronometro;

        btstart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                btstart.setEnabled(false);
                btpause.setEnabled(true);
                btStop.setEnabled(true);
                btReset.setEnabled(true);

                if (Click)
                    chronometer.setBase(SystemClock.elapsedRealtime());

                chronometer.start();
            }
        });

        btpause.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                btstart.setEnabled(true);
                btpause.setEnabled(false);
                btStop.setEnabled(true);
                btReset.setEnabled(true);

                Click = false;
                chronometer.stop();

            }
        });

        btStop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                btstart.setEnabled(true);
                btpause.setEnabled(false);
                btStop.setEnabled(false);
                btReset.setEnabled(false);

                Click = true;
                chronometer.stop();
                chronometer.setText("Tempo (00:00)");

            }
        });

        btReset.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                btstart.setEnabled(false);
                btpause.setEnabled(true);
                btStop.setEnabled(true);
                btReset.setEnabled(true);

                Click = true;
                chronometer.stop();
                chronometer.setText("Tempo (00:00)");
                chronometer.setBase(SystemClock.elapsedRealtime());
                chronometer.start();

            }
        });

    }
}
