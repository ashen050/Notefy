package com.example.notesync.api;

import com.google.gson.annotations.SerializedName;

public class ClockResponse {

    @SerializedName("time")
    private String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
