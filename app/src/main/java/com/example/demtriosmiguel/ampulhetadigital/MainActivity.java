package com.example.demtriosmiguel.ampulhetadigital;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.*;
import android.view.*;

public class MainActivity extends AppCompatActivity {

    Chronometer chronometer;
    Button btstart, btpause, btStop, btReset;
    boolean Click;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

// Declarando o tipo click como buleano como verdadeiro;
// Estanciando as variaveis

        Click = true;
        chronometer = (Chronometer) findViewById(R.id.chronometer);
        btstart = (Button) findViewById(R.id.btstart);
        btpause = (Button) findViewById(R.id.btpause);
        btStop = (Button) findViewById(R.id.btStop);
        btReset = (Button) findViewById(R.id.btReset);

// abiliitando e desabilitando os botões

        btstart.setEnabled(true);
        btpause.setEnabled(false);
        btStop.setEnabled(false);
        btReset.setEnabled(false);



// setando os botoes passando os evendos de click condicionando o mesmo  e startando o cronometro;

        btstart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {


                btstart.setEnabled(false);
                btpause.setEnabled(true);
                btStop.setEnabled(true);
                btReset.setEnabled(true);


                if (Click)
                    chronometer.setBase(SystemClock.elapsedRealtime());
                chronometer.start();
            }
        });

        btpause.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                btstart.setEnabled(true);
                btpause.setEnabled(false);
                btStop.setEnabled(true);
                btReset.setEnabled(true);


                Click = false;
                chronometer.stop();

            }
        });

        btStop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                btstart.setEnabled(true);
                btpause.setEnabled(false);
                btStop.setEnabled(false);
                btReset.setEnabled(false);

                Click = true;
                chronometer.stop();
                chronometer.setText("Tempo (00:00)");

            }
        });

        btReset.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                btstart.setEnabled(false);
                btpause.setEnabled(true);
                btStop.setEnabled(true);
                btReset.setEnabled(true);

                Click = true;
                chronometer.stop();
                chronometer.setText("Tempo (00:00)");
                chronometer.setBase(SystemClock.elapsedRealtime());
                chronometer.start();

            }
        });


    }
}
