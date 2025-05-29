package com.vari.varidetect.Model;

import androidx.annotation.NonNull;

public class Reports {
    private String mediaType;
    private String mediaName;
    private long prediction;
    private long timestamp;

    public Reports() {
    }

    public Reports(String mediaType, String mediaName, long prediction, long timestamp) {
        this.mediaType = mediaType;
        this.mediaName = mediaName;
        this.prediction = prediction;
        this.timestamp = timestamp;
    }

    public String getMediaType() {
        return mediaType;
    }

    public String getMediaName() {
        return mediaName;
    }


    public long getPrediction() {
        return prediction;
    }

    public long getTimestamp() {
        return timestamp;
    }



}
