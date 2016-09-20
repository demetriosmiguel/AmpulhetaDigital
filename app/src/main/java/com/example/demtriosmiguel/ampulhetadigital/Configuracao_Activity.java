package com.example.demtriosmiguel.ampulhetadigital;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Configuracao_Activity extends AppCompatActivity {

    private static String ARQUIVO_PREFERENCIA = "arquivoPreferencia";
    EditText valorTempo;
    Button gravarValor;
    TextView textoExibicao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracao);

        valorTempo = (EditText) findViewById(R.id.tempoId);
        gravarValor = (Button) findViewById(R.id.gravarValor);
        textoExibicao = (TextView) findViewById(R.id.textViewExibicaoId);


        gravarValor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPreferences = getSharedPreferences(ARQUIVO_PREFERENCIA, 0);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                if (valorTempo.getText().toString().equals(" ")) {
                    Toast.makeText(Configuracao_Activity.this, "digite os minutos", Toast.LENGTH_LONG).show();
                } else {
                    editor.putString("valor", valorTempo.getText().toString());
                    editor.commit();
                    textoExibicao.setText("o valor do tempo é: "+ valorTempo.getText().toString());

                }

            }
        });

        // recupera dados salvos
        SharedPreferences sharedPreferences = getSharedPreferences(ARQUIVO_PREFERENCIA, 0);
        if (sharedPreferences.contains("valor")) {
            String valorTempo = sharedPreferences.getString("valor", "valor naõ definido");
            textoExibicao.setText(valorTempo);

        }else {
            textoExibicao.setText("valor não definido");

        }

    }
}
