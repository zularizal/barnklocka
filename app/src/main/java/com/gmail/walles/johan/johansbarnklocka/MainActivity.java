package com.gmail.walles.johan.johansbarnklocka;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private AnalogClock analogClock;
    private final TimeReadout timeReadout = new TimeReadout();

    @Nullable
    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        analogClock = findViewById(R.id.analogClock);
        analogClock.setOnTimeChanged(new AnalogClock.OnTimeChanged() {
            @Override
            public void onTimeChanged(int newHour, int newMinute) {
                setTime(newHour, newMinute);
            }
        });
        setTime(analogClock.getHour(), analogClock.getMinute());

        final Button analogReadout = findViewById(R.id.analogReadout);
        final Button digitalClock = findViewById(R.id.digitalClock);
        final Button digitalReadout = findViewById(R.id.digitalReadout);
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    analogReadout.setEnabled(true);
                    digitalClock.setEnabled(true);
                    digitalReadout.setEnabled(true);
                }
            }
        });
        analogReadout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tts.speak(analogReadout.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
            }
        });
        digitalClock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Speak the digital readout text
                tts.speak(digitalReadout.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
            }
        });
        digitalReadout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tts.speak(digitalReadout.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (tts != null) {
            tts.shutdown();
        }
    }

    private void setTime(int hour, int minute) {
        final Button digitalClock = findViewById(R.id.digitalClock);
        digitalClock.setText(
                String.format(Locale.getDefault(),
                        "%02d:%02d", analogClock.getHour(), analogClock.getMinute()));

        final Button analogReadout = findViewById(R.id.analogReadout);
        analogReadout.setText(timeReadout.formatAnalog(hour, minute));

        final Button digitalReadout = findViewById(R.id.digitalReadout);
        digitalReadout.setText(timeReadout.formatDigital(hour, minute));
    }
}
