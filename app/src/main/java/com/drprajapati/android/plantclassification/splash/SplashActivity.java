package com.drprajapati.android.plantclassification.splash;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.drprajapati.android.plantclassification.MainActivity;
import com.drprajapati.android.plantclassification.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

@SuppressLint("Registered")
public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.SplashTheme);
        startActivity(MainActivity.getIntent(this));
        finish();
    }
}
