package com.example.all_about_earth_.Object;

import java.io.Serializable;

public class Coordinate implements Serializable {
    private String writtenSpeech;
    private String place_name;
    private String place_id;

    public Coordinate(String writtenSpeech, String place_id, String place_name) {
        this.writtenSpeech = writtenSpeech;
        this.place_id = place_id;
        this.place_name = place_name;
    }

    public String getWrittenSpeech() {
        return writtenSpeech;
    }

    public void setWrittenSpeech(String writtenSpeech) {
        this.writtenSpeech = writtenSpeech;
    }

    public String getPlace_name() {
        return place_name;
    }

    public void setPlace_name(String place_name) {
        this.place_name = place_name;
    }

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }
}
