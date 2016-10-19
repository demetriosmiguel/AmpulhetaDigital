package com.example.demtriosmiguel.ampulhetadigital;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

public class ConfiguracaoActivity extends AppCompatActivity {

    public static final String PREFERENCIAS_PADRAO = "preferenciasPadrao";
    public static final String HORAS_PADRAO = "horasPadrao";
    public static final String MINUTOS_PADRAO = "minutossPadrao";
    public static final String SEGUNDOS_PADRAO = "segundosPadrao";

    private NumberPicker npHorasPadrao;
    private NumberPicker npMinutossPadrao;
    private NumberPicker npSegundosPadrao;

    private Button btnSalvar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracao);

        npHorasPadrao = (NumberPicker) findViewById(R.id.numberPickerHorasPadrao);
        npHorasPadrao.setMaxValue(99);
        npHorasPadrao.setMinValue(0);

        npMinutossPadrao = (NumberPicker) findViewById(R.id.numberPickerMinutosPadrao);
        npMinutossPadrao.setMaxValue(59);
        npMinutossPadrao.setMinValue(0);

        npSegundosPadrao = (NumberPicker) findViewById(R.id.numberPickerSegundosPadrao);
        npSegundosPadrao.setMaxValue(59);
        npSegundosPadrao.setMinValue(0);

        SharedPreferences settings = getSharedPreferences(PREFERENCIAS_PADRAO, 0);
        npHorasPadrao.setValue(settings.getInt(HORAS_PADRAO, 0));
        npMinutossPadrao.setValue(settings.getInt(MINUTOS_PADRAO, 0));
        npSegundosPadrao.setValue(settings.getInt(SEGUNDOS_PADRAO, 0));

        btnSalvar = (Button) findViewById(R.id.buttonSalvarPreferencias);

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences settings = getSharedPreferences(PREFERENCIAS_PADRAO, 0);
                SharedPreferences.Editor editor = settings.edit();

                editor.putInt(HORAS_PADRAO, npHorasPadrao.getValue());
                editor.putInt(MINUTOS_PADRAO, npMinutossPadrao.getValue());
                editor.putInt(SEGUNDOS_PADRAO, npSegundosPadrao.getValue());

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
