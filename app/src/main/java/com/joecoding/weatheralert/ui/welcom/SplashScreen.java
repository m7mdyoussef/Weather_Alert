package com.joecoding.weatheralert.ui.welcom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import com.joecoding.weatheralert.R;
import com.airbnb.lottie.LottieAnimationView;
import com.joecoding.weatheralert.MainActivity;
import com.scwang.wave.MultiWaveHeader;

public class SplashScreen extends AppCompatActivity {

    MultiWaveHeader waveHeader, waveFooter;
    LottieAnimationView splashlottie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        waveHeader=findViewById(R.id.waveHeader);
        waveFooter=findViewById(R.id.waveFooter);
        splashlottie=findViewById(R.id.splashlottie);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(SplashScreen.this, MainActivity.class);
                SplashScreen.this.startActivity(mainIntent);

                SplashScreen.this.finish();

            }
        }, 2000);


        waveHeader.setVelocity(1);
        waveHeader.setProgress(1);
        waveHeader.isRunning();
        waveHeader.setGradientAngle(45);
        waveHeader.setWaveHeight(40);
        waveHeader.setStartColor(Color.RED);
        waveHeader.setCloseColor(Color.CYAN);


        waveFooter.setVelocity(1);
        waveFooter.setProgress(1);
        waveFooter.isRunning();
        waveFooter.setGradientAngle(45);
        waveFooter.setWaveHeight(40);
        waveFooter.setStartColor(Color.MAGENTA);
        waveFooter.setCloseColor(Color.YELLOW);



    }
}