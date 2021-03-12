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

    LottieAnimationView splashlottie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        splashlottie=findViewById(R.id.splashlottie);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(SplashScreen.this, MainActivity.class);
                SplashScreen.this.startActivity(mainIntent);

                SplashScreen.this.finish();

            }
        }, 4000);




    }
}