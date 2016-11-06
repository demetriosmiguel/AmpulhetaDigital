package com.example.demtriosmiguel.ampulhetadigital;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Cronometro {

    private static final int ONZE_SEGUNDOS = 11 * 1000;
    private static final int UM_SEGUNDO = 1 * 1000;

    private TextView textViewTimer;
    private TextView textViewTimerEmPausa;
    private Button botaoIniciar;
    private Button botaoPausar;
    private Button botaoReiniciar;
    private Button botaoParar;

    private long calculoHoras;
    private long calculoMinutos;
    private long calculoSegundos;
    private long milisegundosTotais;

    private int segundos = 0;
    private int minutos = 0;
    private int horas = 0;

    private long miliseguntosTotaisInicial;

    private boolean emExecucao = false;

    private boolean repetir = false;

    private Handler handler;
    private Runnable runnable;

    // tempo em pausa
    private long calculoHorasEmPausa;
    private long calculoMinutosEmPausa;
    private long calculoSegundosEmPausa;
    private long milisegundosTotaisEmPausa;

    private boolean emExecucaoEmPausa = false;

    private Handler handlerEmPausa;
    private Runnable runnableEmPausa;
    // tempo em pausa

    Toast feedbackMessage;

    MediaPlayer somTempoEncerrado;
    MediaPlayer somContagemRegressiva;

    SharedPreferences preferencias;

    public Cronometro(TextView textViewTimer, TextView textViewTimerEmPausa, Button botaoIniciar, Button botaoPausar, Button botaoReiniciar, Button botaoParar,
                      MediaPlayer somTempoEncerrado, MediaPlayer somContagemRegressiva, SharedPreferences preferencias, Toast feedbackMessage)
    {
        this.textViewTimer = textViewTimer;
        this.textViewTimerEmPausa = textViewTimerEmPausa;
        this.botaoIniciar = botaoIniciar;
        this.botaoPausar = botaoPausar;
        this.botaoReiniciar = botaoReiniciar;
        this.botaoParar = botaoParar;
        this.somTempoEncerrado = somTempoEncerrado;
        this.somContagemRegressiva = somContagemRegressiva;
        this.preferencias = preferencias;
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

        setTempoTotalEmMilisegundos();
        calculaHorasMinutosSegundos();
        exibeTempoFormatado();
    }

    public void setTempoTotalEmMilisegundos() {
        milisegundosTotais = (segundos + (minutos * 60) + (horas * 60 * 60)) * 1000;

        // registra tempo total definido para caso o cronometro seja reiniciado ou parado
        miliseguntosTotaisInicial = milisegundosTotais;
    }

    public void calculaHorasMinutosSegundos() {
        calculoHoras = Math.abs((milisegundosTotais / 1000) / 3600); // horas
        long calculoDiferencaMinutosEmSegundos = calculoHoras * 3600;
        calculoMinutos = Math.abs((calculoDiferencaMinutosEmSegundos - (milisegundosTotais / 1000)) / 60); // minutos
        long calculoHorasMinutosEmSegundos = (calculoMinutos * 60) + calculoDiferencaMinutosEmSegundos;
        calculoSegundos = (milisegundosTotais / 1000) - calculoHorasMinutosEmSegundos; // segundos
    }

    public void calculaHorasMinutosSegundosEmPausa() {
        calculoHorasEmPausa = Math.abs((milisegundosTotaisEmPausa / 1000) / 3600); // horas
        long calculoDiferencaMinutosEmSegundos = calculoHorasEmPausa * 3600;
        calculoMinutosEmPausa = Math.abs((calculoDiferencaMinutosEmSegundos - (milisegundosTotaisEmPausa / 1000)) / 60); // minutos
        long calculoHorasMinutosEmSegundos = (calculoMinutosEmPausa * 60) + calculoDiferencaMinutosEmSegundos;
        calculoSegundosEmPausa = (milisegundosTotaisEmPausa / 1000) - calculoHorasMinutosEmSegundos; // segundos
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

    public void exibeTempoFormatadoEmPausa() {
        String horas = "";
        String minutos = "";
        String segundos = "";

        if (calculoHorasEmPausa < 10) {
            horas = "0"+calculoHorasEmPausa;
        } else {
            horas += calculoHorasEmPausa;
        }

        if (calculoMinutosEmPausa < 10) {
            minutos = "0"+calculoMinutosEmPausa;
        } else {
            minutos += calculoMinutosEmPausa;
        }

        if (calculoSegundosEmPausa < 10) {
            segundos = "0"+calculoSegundosEmPausa;
        } else {
            segundos += calculoSegundosEmPausa;
        }

        StringBuilder timerDisplay = new StringBuilder();
        timerDisplay.append(horas).append(":");
        timerDisplay.append(minutos).append(":");
        timerDisplay.append(segundos);

        textViewTimerEmPausa.setText(timerDisplay.toString());
    }

    public void zeraTempoEmPausa() {
        if (emExecucaoEmPausa) {
            emExecucaoEmPausa = false;
            handlerEmPausa.removeCallbacks(runnableEmPausa);
        }

        milisegundosTotaisEmPausa = 0;
        calculaHorasMinutosSegundosEmPausa();
        exibeTempoFormatadoEmPausa();
    }

    public void iniciar() {
        if (emExecucaoEmPausa) {
            emExecucaoEmPausa = false;
            handlerEmPausa.removeCallbacks(runnableEmPausa);
        }

        emExecucao = true;
        executaCronometro();

        botaoIniciar.setVisibility(View.GONE);
        botaoPausar.setVisibility(View.VISIBLE);
        botaoReiniciar.setVisibility(View.VISIBLE);
        botaoParar.setVisibility(View.VISIBLE);
    }

    public void pausar() {
        if (somContagemRegressiva.isPlaying()) {
            somContagemRegressiva.stop();
            somContagemRegressiva.prepareAsync();
        }

        emExecucao = false;
        handler.removeCallbacks(runnable);

        if (preferencias.getBoolean(ConfiguracaoActivity.TEMPO_EM_PAUSA, false)) {
            emExecucaoEmPausa = true;
            executaCronometroEmPausa();
        }

        botaoIniciar.setVisibility(View.VISIBLE);
        botaoPausar.setVisibility(View.GONE);
        botaoReiniciar.setVisibility(View.VISIBLE);
        botaoParar.setVisibility(View.VISIBLE);
    }

    public void reiniciar() {
        if (somContagemRegressiva.isPlaying()) {
            somContagemRegressiva.stop();
            somContagemRegressiva.prepareAsync();
        }

        handler.removeCallbacks(runnable);
        milisegundosTotais = miliseguntosTotaisInicial;
        emExecucao = true;
        executaCronometro();

        if (preferencias.getBoolean(ConfiguracaoActivity.TEMPO_EM_PAUSA, false)) {
            if (emExecucaoEmPausa) {
                emExecucaoEmPausa = false;
                handlerEmPausa.removeCallbacks(runnableEmPausa);
            }

            zeraTempoEmPausa();
        }

        botaoIniciar.setVisibility(View.GONE);
        botaoPausar.setVisibility(View.VISIBLE);
        botaoReiniciar.setVisibility(View.VISIBLE);
        botaoParar.setVisibility(View.VISIBLE);
    }

    public void parar() {
        if (somContagemRegressiva.isPlaying()) {
            somContagemRegressiva.stop();
            somContagemRegressiva.prepareAsync();
        }

        handler.removeCallbacks(runnable);
        milisegundosTotais = miliseguntosTotaisInicial;
        emExecucao = false;

        calculaHorasMinutosSegundos();
        exibeTempoFormatado();

        if (preferencias.getBoolean(ConfiguracaoActivity.TEMPO_EM_PAUSA, false)) {
            if (emExecucaoEmPausa) {
                emExecucaoEmPausa = false;
                handlerEmPausa.removeCallbacks(runnableEmPausa);
            }

            zeraTempoEmPausa();
        }

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
                handler.postDelayed(this, 100);
                try {
                    if (emExecucao && milisegundosTotais >= 1000) {
                        if (milisegundosTotais < ONZE_SEGUNDOS && milisegundosTotais >= UM_SEGUNDO && preferencias.getBoolean(ConfiguracaoActivity.SOM_CONTAGEM_REGRESSIVA, true)) {
                            somContagemRegressiva.start();
                        }
                        calculaHorasMinutosSegundos();
                        exibeTempoFormatado();
                        milisegundosTotais -= 100;
                    } else {
                        if (somContagemRegressiva.isPlaying()) {
                            somContagemRegressiva.stop();
                            somContagemRegressiva.prepareAsync();
                        }

                        if (preferencias.getBoolean(ConfiguracaoActivity.SOM_TEMPO_ENCERRADO, true)) {
                            somTempoEncerrado.start();
                        }

                        calculaHorasMinutosSegundos();
                        exibeTempoFormatado();

                        handler.removeCallbacks(runnable);

                        emExecucao = false;

                        zeraTempoEmPausa();

                        feedbackMessage.setText("Tempo encerrado");
                        feedbackMessage.show();

                        if (repetir) {
                            reiniciar();
                        } else {
                            botaoIniciar.setVisibility(View.VISIBLE);
                            botaoPausar.setVisibility(View.GONE);
                            botaoReiniciar.setVisibility(View.GONE);
                            botaoParar.setVisibility(View.GONE);

                            MainActivity.linearLayoutComandosCronometro.setVisibility(View.GONE);
                            MainActivity.linearLayoutDefinaTempo.setVisibility(View.VISIBLE);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        handler.postDelayed(runnable, 0);
    }

    public void executaCronometroEmPausa() {
        handlerEmPausa = new Handler();
        runnableEmPausa = new Runnable() {
            @Override
            public void run() {
                handlerEmPausa.postDelayed(this, 100);
                try {
                    if (emExecucaoEmPausa) {
                        calculaHorasMinutosSegundosEmPausa();
                        exibeTempoFormatadoEmPausa();
                        milisegundosTotaisEmPausa += 100;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        handlerEmPausa.postDelayed(runnableEmPausa, 0);
    }

    public boolean isRepetir() {
        return repetir;
    }

    public void setRepetir(boolean repetir) {
        this.repetir = repetir;
    }

    public int getSegundos() {
        return segundos;
    }

    public int getMinutos() {
        return minutos;
    }

    public int getHoras() {
        return horas;
    }

    public long getMilisegundosTotais() {
        return milisegundosTotais;
    }
}
