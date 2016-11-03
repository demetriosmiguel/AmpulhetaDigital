package com.example.demtriosmiguel.ampulhetadigital;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.Toast;

public class ConfiguracaoActivity extends AppCompatActivity {

    public static final String PREFERENCIAS_PADRAO = "preferenciasPadrao";

    public static final String HORAS_PADRAO = "horasPadrao";
    public static final String MINUTOS_PADRAO = "minutossPadrao";
    public static final String SEGUNDOS_PADRAO = "segundosPadrao";
    public static final String TEMPO_EM_PAUSA = "tempoEmPausa";
    public static final String SOM_TEMPO_ENCERRADO = "somTempoEncerrado";
    public static final String SOM_CONTAGEM_REGRESSIVA = "somContagemRegressiva";

    private NumberPicker npHorasPadrao;
    private NumberPicker npMinutossPadrao;
    private NumberPicker npSegundosPadrao;

    private Switch switchTempoEmPausa;
    private Switch switchTempoEncerrado;
    private Switch switchContagemRegressiva;

    private Button btnSalvar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracao);

        SharedPreferences preferencias = getSharedPreferences(PREFERENCIAS_PADRAO, 0);

        npHorasPadrao = (NumberPicker) findViewById(R.id.numberPickerHorasPadrao);
        npHorasPadrao.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        npHorasPadrao.setMaxValue(99);
        npHorasPadrao.setMinValue(0);

        npMinutossPadrao = (NumberPicker) findViewById(R.id.numberPickerMinutosPadrao);
        npMinutossPadrao.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        npMinutossPadrao.setMaxValue(59);
        npMinutossPadrao.setMinValue(0);

        npSegundosPadrao = (NumberPicker) findViewById(R.id.numberPickerSegundosPadrao);
        npSegundosPadrao.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        npSegundosPadrao.setMaxValue(59);
        npSegundosPadrao.setMinValue(0);

        npHorasPadrao.setValue(preferencias.getInt(HORAS_PADRAO, 0));
        npMinutossPadrao.setValue(preferencias.getInt(MINUTOS_PADRAO, 0));
        npSegundosPadrao.setValue(preferencias.getInt(SEGUNDOS_PADRAO, 0));

        switchTempoEmPausa = (Switch) findViewById(R.id.switchTempoEmPausa);
        switchTempoEmPausa.setChecked(preferencias.getBoolean(TEMPO_EM_PAUSA, false));

        switchTempoEncerrado = (Switch) findViewById(R.id.switchTempoEncerrado);
        switchTempoEncerrado.setChecked(preferencias.getBoolean(SOM_TEMPO_ENCERRADO, true));

        switchContagemRegressiva = (Switch) findViewById(R.id.switchContagemRegressiva);
        switchContagemRegressiva.setChecked(preferencias.getBoolean(SOM_CONTAGEM_REGRESSIVA, true));

        btnSalvar = (Button) findViewById(R.id.buttonSalvarPreferencias);

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences settings = getSharedPreferences(PREFERENCIAS_PADRAO, 0);
                SharedPreferences.Editor editor = settings.edit();

                editor.putInt(HORAS_PADRAO, npHorasPadrao.getValue());
                editor.putInt(MINUTOS_PADRAO, npMinutossPadrao.getValue());
                editor.putInt(SEGUNDOS_PADRAO, npSegundosPadrao.getValue());

                editor.putBoolean(TEMPO_EM_PAUSA, switchTempoEmPausa.isChecked());

                editor.putBoolean(SOM_TEMPO_ENCERRADO, switchTempoEncerrado.isChecked());
                editor.putBoolean(SOM_CONTAGEM_REGRESSIVA, switchContagemRegressiva.isChecked());

                if (editor.commit()) {
                    Toast.makeText(getApplicationContext(), "Preferencias salva com sucesso!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Não foi possível salvar. Tente novamente", Toast.LENGTH_LONG).show();
                }

                finish();
            }
        });
    }
}
