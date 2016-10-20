package com.example.demtriosmiguel.ampulhetadigital;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static final String DEFINIR_TAREFA = "Clique aqui para definir uma tarefa";
    private static final String NENHUMA_TAREFA = "Nenhuma";

    TextView textViewTarefa;
    TextView textViewDescricaoTarefa;
    TextView textViewTimer;
    TextView textViewDefinaTempo;

    Button botaoIniciar;
    Button botaoPausar;
    Button botaoReiniciar;
    Button botaoParar;

    public static LinearLayout linearLayoutDefinaTempo;
    public static LinearLayout linearLayoutComandosCronometro;

    Cronometro cronometro;

    int indexTarefaDefinida;
    private List<String> listaTarefas;
    private Set<String> listaTarefasCadastradas;
    private SharedPreferences tarefasCadastradas;
    private CharSequence[] tarefas;

    public MediaPlayer contagemRegressiva;
    public MediaPlayer tempoEncerrado;

    public Toast feedbackMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences preferencias = getSharedPreferences(ConfiguracaoActivity.PREFERENCIAS_PADRAO, Context.MODE_PRIVATE);

        tempoEncerrado = MediaPlayer.create(MainActivity.this, R.raw.tempo_encerrado);
        contagemRegressiva = MediaPlayer.create(MainActivity.this, R.raw.contagem_regressiva);

        textViewTimer = (TextView) findViewById(R.id.textViewTimer);
        textViewDefinaTempo = (TextView) findViewById(R.id.textViewDefinaTempo);

        botaoIniciar = (Button) findViewById(R.id.botaoIniciar);
        botaoPausar = (Button) findViewById(R.id.botaoPausar);
        botaoReiniciar = (Button) findViewById(R.id.botaoReiniciar);
        botaoParar = (Button) findViewById(R.id.botaoParar);

        linearLayoutDefinaTempo = (LinearLayout) findViewById(R.id.linearLayoutDefinaTempo);
        linearLayoutComandosCronometro = (LinearLayout) findViewById(R.id.linearLayoutComandosCronometro);

        feedbackMessage = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG);

        cronometro = new Cronometro(textViewTimer, botaoIniciar, botaoPausar, botaoReiniciar, botaoParar, tempoEncerrado, contagemRegressiva, preferencias, feedbackMessage);


        cronometro.setHorasMinutosSegundos(preferencias.getInt(ConfiguracaoActivity.HORAS_PADRAO, 0),
                                           preferencias.getInt(ConfiguracaoActivity.MINUTOS_PADRAO, 0),
                                           preferencias.getInt(ConfiguracaoActivity.SEGUNDOS_PADRAO, 0));

        atualizaInterfaceCronometro();

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

    @Override
    protected void onResume() {
        super.onResume();

        if (listaTarefas != null) {
            String tarefaAtual = tarefas[indexTarefaDefinida].toString();

            carregaTarefas();

            int indexAtual = listaTarefas.indexOf(tarefaAtual);

            if (indexAtual >= 0) {
                indexTarefaDefinida = indexAtual+1;
            } else {
                indexTarefaDefinida = 0;
            }

            textViewDescricaoTarefa.setText(tarefas[indexTarefaDefinida].toString());
        } else {
            carregaTarefas();
        }
    }

    private void carregaTarefas() {
        indexTarefaDefinida = 0;
        listaTarefas = new ArrayList<String>();
        tarefasCadastradas = getSharedPreferences(ConfiguracaoActivity.PREFERENCIAS_PADRAO, Context.MODE_PRIVATE);
        listaTarefasCadastradas = tarefasCadastradas.getStringSet(TarefasActivity.TAREFAS, null);

        if (listaTarefasCadastradas != null && listaTarefasCadastradas.size() > 0) {
            tarefas = new CharSequence[listaTarefasCadastradas.size()+1];

            Iterator<String> tarefas = listaTarefasCadastradas.iterator();

            while(tarefas.hasNext()) {
                listaTarefas.add(tarefas.next());
            }
        } else {
            tarefas = new CharSequence[1];
        }

        Collections.sort(listaTarefas);

        tarefas[0] = DEFINIR_TAREFA;
        int index = 1;
        for (String tarefa : listaTarefas) {
            tarefas[index] = tarefa;
            index++;
        }

        textViewTarefa = (TextView) findViewById(R.id.textViewTarefa);
        textViewDescricaoTarefa = (TextView) findViewById(R.id.textViewDescricaoTarefa);
        textViewDescricaoTarefa.setText(tarefas[indexTarefaDefinida].toString());

        textViewDescricaoTarefa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogDefinirTarefa();
            }
        });

        if (listaTarefas.isEmpty()) {
            textViewTarefa.setVisibility(View.GONE);
            textViewDescricaoTarefa.setVisibility(View.GONE);
        } else {
            textViewTarefa.setVisibility(View.VISIBLE);
            textViewDescricaoTarefa.setVisibility(View.VISIBLE);
        }
    }

    private void showDialogDefinirTarefa() {
        Dialog dialog;

        tarefas[0] = NENHUMA_TAREFA;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Definir tarefa");

        builder.setSingleChoiceItems(tarefas, indexTarefaDefinida,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        indexTarefaDefinida = item;
                        tarefas[0] = DEFINIR_TAREFA;
                        textViewDescricaoTarefa.setText(tarefas[indexTarefaDefinida].toString());
                        dialog.dismiss();
                    }
                });
        dialog = builder.create();
        dialog.show();
    }

    private void atualizaInterfaceCronometro() {
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
                atualizaInterfaceCronometro();
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
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent configuracaoActiviy = new Intent(MainActivity.this, ConfiguracaoActivity.class);
            startActivity(configuracaoActiviy);
        } else if (id == R.id.action_tarefas) {
            Intent tarefasActiviy = new Intent(MainActivity.this, TarefasActivity.class);
            startActivity(tarefasActiviy);
        }

        return super.onOptionsItemSelected(item);
    }
}