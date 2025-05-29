package com.vari.varidetect.HelperClass;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {
    private static final String PREF_NAME = "language_pref";
    private static final String KEY_LANG = "selected_language";

    private final SharedPreferences prefs;

    public PrefManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
    }

    public void saveLanguage(String langCode){
        prefs.edit().putString(KEY_LANG,langCode).apply();
    }

    public String getLanguage(){
        return prefs.getString(KEY_LANG,"Eng"); // default language code
    }
}
