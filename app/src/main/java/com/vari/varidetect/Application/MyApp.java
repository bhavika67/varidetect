package com.vari.varidetect.Application;

import android.app.Application;

import com.vari.varidetect.HelperClass.LanguageManager;
import com.vari.varidetect.HelperClass.PrefManager;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LanguageManager languageManager = new LanguageManager(this);
        PrefManager prefManager = new PrefManager(this);
        languageManager.setLocale(prefManager.getLanguage());
    }
}
