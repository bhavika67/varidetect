package com.vari.varidetect.Model;

public class LanguageModel {

    private final String langName;
    private final String langCode;

    public LanguageModel(String langName, String langCode) {
        this.langName = langName;
        this.langCode = langCode;
    }

    public String getLangName() {
        return langName;
    }

    public String getLangCode() {
        return langCode;
    }
}
