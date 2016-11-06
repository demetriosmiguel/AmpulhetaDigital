package com.example.demtriosmiguel.ampulhetadigital;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class TarefasActivity extends AppCompatActivity {

    public static final String TAREFAS = "tarefas";

    private List<String> listaTarefas;
    private ListView listViewTarefas;
    private Set<String> listaTarefasCadastradas;
    private EditText edtTxtTarefa;
    private EditText edtTxtNovaTarefa;
    private Button btnVoltar;
    private Button btnNovaTarefa;
    private SharedPreferences preferencia;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarefas);

        listViewTarefas = (ListView) findViewById(R.id.listViewTarefas);

        listaTarefas = new ArrayList<String>();

        carregaListaTarefas();

        listViewTarefas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String a = (String) listViewTarefas.getItemAtPosition(i);
                showDialogEdicaoTarefa(a);
            }
        });

        btnVoltar = (Button) findViewById(R.id.buttonVoltarTarefa);
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnNovaTarefa = (Button) findViewById(R.id.buttonNovaTarefa);
        btnNovaTarefa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogNovaTarefa();
            }
        });
    }

    private void carregaListaTarefas() {
        listaTarefas.clear();

        preferencia = getSharedPreferences(ConfiguracaoActivity.PREFERENCIAS_PADRAO, Context.MODE_PRIVATE);
        editor = preferencia.edit();

        listaTarefasCadastradas = preferencia.getStringSet(TAREFAS, null);

        if (listaTarefasCadastradas != null) {
            Iterator<String> tarefas = listaTarefasCadastradas.iterator();

            while(tarefas.hasNext()) {
                listaTarefas.add(tarefas.next());
            }
        }

        Collections.sort(listaTarefas);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listaTarefas);

        listViewTarefas.setAdapter(arrayAdapter);
    }

    private void showDialogNovaTarefa() {
        final Dialog dialog = new Dialog(TarefasActivity.this);
        dialog.setTitle("Nova Tarefa");
        dialog.setContentView(R.layout.dialog_nova_tarefa);

        edtTxtNovaTarefa = (EditText) dialog.findViewById(R.id.editTextNovaTarefa);

        Button btnSalvar = (Button) dialog.findViewById(R.id.buttonSalvarNovaTarefa);
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listaTarefasCadastradas == null) {
                    listaTarefasCadastradas = new HashSet<String>();
                } else {
                    editor.remove(TAREFAS);
                    editor.commit();
                }

                listaTarefasCadastradas.add(edtTxtNovaTarefa.getText().toString());

                editor.putStringSet(TAREFAS, listaTarefasCadastradas);

                if (!editor.commit()) {
                    Toast.makeText(getApplicationContext(), "Erro. Tente novamente", Toast.LENGTH_LONG).show();
                } else {
                    carregaListaTarefas();
                    dialog.dismiss();
                }
            }
        });

        dialog.show();
    }

    private void showDialogEdicaoTarefa(final String tarefa) {
        final Dialog dialog = new Dialog(TarefasActivity.this);
        dialog.setTitle("Tarefa");
        dialog.setContentView(R.layout.dialog_edicao_tarefa);

        Button btnAtualizar = (Button) dialog.findViewById(R.id.buttonAtualizar);
        Button btnExcluir = (Button) dialog.findViewById(R.id.buttonExcluir);

        edtTxtTarefa = (EditText) dialog.findViewById(R.id.editTextTarefa);
        edtTxtTarefa.setText(tarefa);

        btnAtualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.remove(TAREFAS);
                editor.commit();

                listaTarefasCadastradas.remove(tarefa);
                listaTarefasCadastradas.add(edtTxtTarefa.getText().toString());

                editor.putStringSet(TAREFAS, listaTarefasCadastradas);

                if (!editor.commit()) {
                    Toast.makeText(getApplicationContext(), "Erro. Tente novamente", Toast.LENGTH_LONG).show();
                } else {
                    carregaListaTarefas();
                    dialog.dismiss();
                }
            }
        });

        btnExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.remove(TAREFAS);
                editor.commit();

                listaTarefasCadastradas.remove(tarefa);

                editor.putStringSet(TAREFAS, listaTarefasCadastradas);

                if (!editor.commit()) {
                    Toast.makeText(getApplicationContext(), "Erro. Tente novamente", Toast.LENGTH_LONG).show();
                } else {
                    carregaListaTarefas();
                    dialog.dismiss();
                }
            }
        });

        dialog.show();
    }
}
