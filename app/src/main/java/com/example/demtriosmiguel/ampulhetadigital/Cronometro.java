package com.example.demtriosmiguel.ampulhetadigital;

import android.media.MediaPlayer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

    private int segundos = 0;
    private int minutos = 0;
    private int horas = 0;

    private long seguntosTotaisInicial;

    private Handler handler;
    private Runnable runnable;

    private boolean emExecucao = false;

    Toast feedbackMessage;

    MediaPlayer tempoEncerrado;
    MediaPlayer contagemRegressiva;

    public Cronometro(TextView textViewTimer, Button botaoIniciar, Button botaoPausar, Button botaoReiniciar, Button botaoParar, MediaPlayer tempoEncerrado, MediaPlayer contagemRegressiva, Toast feedbackMessage) {
        this.textViewTimer = textViewTimer;
        this.botaoIniciar = botaoIniciar;
        this.botaoPausar = botaoPausar;
        this.botaoReiniciar = botaoReiniciar;
        this.botaoParar = botaoParar;
        this.tempoEncerrado = tempoEncerrado;
        this.contagemRegressiva = contagemRegressiva;
        this.feedbackMessage = feedbackMessage;

        botaoPausar.setVisibility(View.GONE);
        botaoReiniciar.setVisibility(View.GONE);
        botaoParar.setVisibility(View.GONE);
    }

    public void setHorasMinutosSegundos(int horas, int minutos, int segundos) {
        this.horas = horas;
        this.minutos = minutos;
        this.segundos = segundos;

        emExecucao = false;

        if (handler != null) {
            handler.removeCallbacks(runnable);
        }

        botaoIniciar.setVisibility(View.VISIBLE);
        botaoPausar.setVisibility(View.GONE);
        botaoReiniciar.setVisibility(View.GONE);
        botaoParar.setVisibility(View.GONE);

        setTempoTotalEmSegundos();
        calculaHorasMinutosSegundos();
        exibeTempoFormatado();
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

    public void iniciar() {
        emExecucao = true;
        executaCronometro();

        botaoIniciar.setVisibility(View.GONE);
        botaoPausar.setVisibility(View.VISIBLE);
        botaoReiniciar.setVisibility(View.VISIBLE);
        botaoParar.setVisibility(View.VISIBLE);
    }

    public void pausar() {
        emExecucao = false;
        handler.removeCallbacks(runnable);

        botaoIniciar.setVisibility(View.VISIBLE);
        botaoPausar.setVisibility(View.GONE);
        botaoReiniciar.setVisibility(View.VISIBLE);
        botaoParar.setVisibility(View.VISIBLE);
    }

    public void reiniciar() {
        handler.removeCallbacks(runnable);
        segundosTotais = seguntosTotaisInicial;
        emExecucao = true;
        executaCronometro();

        botaoIniciar.setVisibility(View.GONE);
        botaoPausar.setVisibility(View.VISIBLE);
        botaoReiniciar.setVisibility(View.VISIBLE);
        botaoParar.setVisibility(View.VISIBLE);
    }

    public void parar() {
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

    public void executaCronometro() {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 1000);
                try {
                    if (emExecucao && segundosTotais > 0) {
                        if (segundosTotais <= 10 && segundosTotais >= 1) {
                            contagemRegressiva.start();
                        }
                        calculaHorasMinutosSegundos();
                        exibeTempoFormatado();
                        segundosTotais--;
                    } else {
                        if (contagemRegressiva.isPlaying()) {
                            contagemRegressiva.pause();
                            contagemRegressiva.seekTo(0);
                            tempoEncerrado.start();
                        }

                        calculaHorasMinutosSegundos();
                        exibeTempoFormatado();

                        handler.removeCallbacks(runnable);

                        emExecucao = false;

                        botaoIniciar.setVisibility(View.VISIBLE);
                        botaoPausar.setVisibility(View.GONE);
                        botaoReiniciar.setVisibility(View.GONE);
                        botaoParar.setVisibility(View.GONE);

                        MainActivity.linearLayoutComandosCronometro.setVisibility(View.GONE);
                        MainActivity.linearLayoutDefinaTempo.setVisibility(View.VISIBLE);

                        feedbackMessage.setText("Tempo encerrado");
                        feedbackMessage.show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        handler.postDelayed(runnable, 0);
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

    public long getSegundosTotais() {
        return segundosTotais;
    }

    public void setSegundosTotais(long segundosTotais) {
        this.segundosTotais = segundosTotais;
    }
}
