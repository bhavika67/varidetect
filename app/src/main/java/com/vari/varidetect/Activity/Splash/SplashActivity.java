package com.vari.varidetect.Activity.Splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.vari.varidetect.Activity.OnBoard.OnBoardActivity;
import com.vari.varidetect.Application.BaseActivity;
import com.vari.varidetect.R;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, OnBoardActivity.class));
                finish();
            }
        },2000);
    }
}