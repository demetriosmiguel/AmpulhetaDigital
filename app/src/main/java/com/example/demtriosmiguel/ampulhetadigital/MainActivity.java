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

    }
}
