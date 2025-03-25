package com.example.all_about_earth_.Object;

import java.io.Serializable;

public class Coordinate implements Serializable {
    private float Latitudine;
    private float longitudine;

    public Coordinate(float latitudine, float longitudine){
        this.Latitudine = latitudine;
        this.longitudine = longitudine;
    }

    public float getLatitudine() {
        return Latitudine;
    }

    public void setLatitudine(float latitudine) {
        Latitudine = latitudine;
    }

    public float getLongitudine() {
        return longitudine;
    }

    public void setLongitudine(float longitudine) {
        this.longitudine = longitudine;
    }
}
