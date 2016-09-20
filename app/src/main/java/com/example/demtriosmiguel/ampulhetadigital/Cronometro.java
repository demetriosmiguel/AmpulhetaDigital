package com.example.demtriosmiguel.ampulhetadigital;

import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Cronometro {

    private TextView textViewTimer;
    private Button botaoIniciar;
    private Button botaoPausar;
    private Button botaoReiniciar;
    private Button botaoParar;

    private long calculoHoras;
    private long calculoMinutos;
    private long calculoSegundos;
    private long segundosTotais;

    private int segundos;
    private int minutos;
    private int horas;

    private long seguntosTotaisInicial;

    private Handler handler;
    private Runnable runnable;

    private boolean emExecucao = false;

    public Cronometro(TextView textViewTimer, Button botaoIniciar, Button botaoPausar, Button botaoReiniciar, Button botaoParar) {
        this.textViewTimer = textViewTimer;
        this.botaoIniciar = botaoIniciar;
        this.botaoPausar = botaoPausar;
        this.botaoReiniciar = botaoReiniciar;
        this.botaoParar = botaoParar;

        botaoPausar.setVisibility(View.GONE);
        botaoReiniciar.setVisibility(View.GONE);
        botaoParar.setVisibility(View.GONE);
    }

    public void setHorasMinutosSegundos(int horas, int minutos, int segundos) {
        this.horas = horas;
        this.minutos = minutos;
        this.segundos = segundos;
    }

    public void setTempoTotalEmSegundos() {
        segundosTotais = (segundos + (minutos * 60) + (horas * 60 * 60));

        // registra tempo total definido para caso o cronometro seja reiniciado ou parado
        seguntosTotaisInicial = segundosTotais;
    }

    public void calculaHorasMinutosSegundos() {
        calculoHoras = Math.abs(segundosTotais / 3600); // horas
        long calculoDiferencaMinutosEmSegundos = calculoHoras * 3600;
        calculoMinutos = Math.abs((calculoDiferencaMinutosEmSegundos - segundosTotais) / 60); // minutos
        long calculoHorasMinutosEmSegundos = (calculoMinutos * 60) + calculoDiferencaMinutosEmSegundos;
        calculoSegundos = segundosTotais - calculoHorasMinutosEmSegundos; // segundos
    }

    public void exibeTempoFormatado() {
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

    public int getSegundos() {
        return segundos;
    }

    public void setSegundos(int segundos) {
        this.segundos = segundos;
    }

    public int getMinutos() {
        return minutos;
    }

    public void setMinutos(int minutos) {
        this.minutos = minutos;
    }

    public int getHoras() {
        return horas;
    }

    public void setHoras(int horas) {
        this.horas = horas;
    }
}