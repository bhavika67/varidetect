package com.vari.varidetect.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.vari.varidetect.Application.BaseActivity;
import com.vari.varidetect.R;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}