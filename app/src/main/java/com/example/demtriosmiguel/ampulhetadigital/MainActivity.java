package com.example.demtriosmiguel.ampulhetadigital;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

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

    int indexTarefaDefinida = 0;
    private List<String> listaTarefas;
    private Set<String> listaTarefasCadastradas;
    private SharedPreferences tarefasCadastradas;
    private CharSequence[] tarefas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        tarefas[0] = "Não definida";
        int index = 1;
        for (String tarefa : listaTarefas) {
            tarefas[index] = tarefa;
            index++;
        }

        textViewDescricaoTarefa = (TextView) findViewById(R.id.textViewDescricaoTarefa);
        textViewDescricaoTarefa.setText(tarefas[indexTarefaDefinida].toString());

        textViewDescricaoTarefa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogDefinirTarefa();
            }
        });

        textViewTimer = (TextView) findViewById(R.id.textViewTimer);
        textViewDefinaTempo = (TextView) findViewById(R.id.textViewDefinaTempo);

        botaoIniciar = (Button) findViewById(R.id.botaoIniciar);
        botaoPausar = (Button) findViewById(R.id.botaoPausar);
        botaoReiniciar = (Button) findViewById(R.id.botaoReiniciar);
        botaoParar = (Button) findViewById(R.id.botaoParar);

        linearLayoutDefinaTempo = (LinearLayout) findViewById(R.id.linearLayoutDefinaTempo);
        linearLayoutComandosCronometro = (LinearLayout) findViewById(R.id.linearLayoutComandosCronometro);

        cronometro = new Cronometro(textViewTimer, botaoIniciar, botaoPausar, botaoReiniciar, botaoParar);

        SharedPreferences preferencias = getSharedPreferences(ConfiguracaoActivity.PREFERENCIAS_PADRAO, Context.MODE_PRIVATE);

        cronometro.setHorasMinutosSegundos(preferencias.getInt(ConfiguracaoActivity.HORAS_PADRAO, 0),
                                           preferencias.getInt(ConfiguracaoActivity.MINUTOS_PADRAO, 0),
                                           preferencias.getInt(ConfiguracaoActivity.SEGUNDOS_PADRAO, 0));

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

    private void showDialogDefinirTarefa() {
        Dialog dialog;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Definir tarefa");

        builder.setSingleChoiceItems(tarefas, indexTarefaDefinida,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        indexTarefaDefinida = item;
                        textViewDescricaoTarefa.setText(tarefas[indexTarefaDefinida].toString());
                        dialog.dismiss();
                    }
                });
        dialog = builder.create();
        dialog.show();
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