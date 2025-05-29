package com.vari.varidetect.Application;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.vari.varidetect.HelperClass.PrefManager;

import java.util.Locale;

public class BaseActivity extends AppCompatActivity {

    private static final String PREF_NAME = "theme_prefs";
    private static final String IS_DARK_MODE = "is_dark_mode";

    @Override
    protected void attachBaseContext(Context newBase) {
        // Load the saved language
        PrefManager prefManager = new PrefManager(newBase);
        String langCode = prefManager.getLanguage();

        Context context = updateBaseContextLocale(newBase,langCode);
        super.attachBaseContext(context);

    }

    public static Context updateBaseContextLocale(Context context, String language){
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.setLocale(locale);

        return context.createConfigurationContext(config);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        applySavedTheme(); // Apply theme before super.onCreate
        super.onCreate(savedInstanceState);
    }

    private void applySavedTheme(){
        SharedPreferences preferences = getSharedPreferences(PREF_NAME,MODE_PRIVATE);
        boolean isDarkMode = preferences.getBoolean(IS_DARK_MODE,false);

        AppCompatDelegate.setDefaultNightMode(
                isDarkMode ? AppCompatDelegate.MODE_NIGHT_YES :AppCompatDelegate.MODE_NIGHT_NO
        );
    }

    protected boolean isDarkModeEnabled(){
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return currentNightMode == Configuration.UI_MODE_NIGHT_YES;
    }
}
