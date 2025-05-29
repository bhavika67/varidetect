package com.vari.varidetect.HelperClass;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;

public class LanguageManager {
    private final Context context;

    public LanguageManager(Context context) {
        this.context = context;
    }

    // Set new locale
    public void setLocale(String langCode){
        Locale locale = new Locale(langCode);
        Locale.setDefault(locale);


        Resources resources = context.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);

        resources.updateConfiguration(config,resources.getDisplayMetrics());

    }

    // Reset to default (e.g., English)
    public void resetToDefault(){
        setLocale("Eng"); // Change "en" to your app's default language
    }

}
