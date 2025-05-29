package com.vari.varidetect.Model;

import android.util.Log;

public class FilterOptions {
    public String sortBy;
    public String fileType;
    public int minConfidence;
    public boolean reset;

    public FilterOptions(String sortBy, String fileType, int minConfidence, boolean reset) {
        this.sortBy = sortBy;
        this.fileType = fileType;
        this.minConfidence = minConfidence;
        this.reset = reset;
    }

    public void printDebug() {
        Log.d("FILTER_DEBUG", "Sort By: " + sortBy);
        Log.d("FILTER_DEBUG", "File Type: " + fileType);
        Log.d("FILTER_DEBUG", "Min Confidence: " + minConfidence);
        Log.d("FILTER_DEBUG", "Reset: " + reset);
    }
}
