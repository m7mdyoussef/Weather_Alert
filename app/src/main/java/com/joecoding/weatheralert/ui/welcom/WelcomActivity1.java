package com.joecoding.weatheralert.ui.welcom;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.cuberto.liquid_swipe.LiquidPager;
import com.joecoding.weatheralert.MainActivity;
import com.joecoding.weatheralert.R;
import com.joecoding.weatheralert.providers.SharedPreferencesProvider;

public class WelcomActivity1 extends AppCompatActivity {

    Button skip;
    LiquidPager pager;
    ViewPager viewPager;

    private SharedPreferencesProvider sharedPreferencesProvider;


    @Override
    protected void onStart() {
        super.onStart();
        // Checking for first time launch - before calling setContentView()
        sharedPreferencesProvider = new SharedPreferencesProvider(this);
        if (!sharedPreferencesProvider.isFirstTimeLaunch1()) { // if not the first time
            launchHomeScreen();
            finish();
        }
            // Making notification bar transparent
            if (Build.VERSION.SDK_INT >= 21) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            }
        }

        private void launchHomeScreen() {
            sharedPreferencesProvider.setFirstTimeLaunch1(false);
            startActivity(new Intent(WelcomActivity1.this, SplashScreen.class));
            finish();
        }


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        pager=findViewById(R.id.pager);
        skip=findViewById(R.id.skip);

        viewPager= new ViewPager(getSupportFragmentManager(),1);
        pager.setAdapter(viewPager);

    }

    public void skip(View view) {

        launchHomeScreen();
    }
}
