package com.example.all_about_earth_.Object;

import java.io.Serializable;
import java.util.ArrayList;

public class Data implements Serializable {
    private ArrayList<Coordinate> coordinate;
    private User user;
    //private String prompt;

    public Data(ArrayList<Coordinate> coordinate, User user){
        this.coordinate = coordinate;
        this.user = user;
    }

    public ArrayList<Coordinate> getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(ArrayList<Coordinate> coordinate) {
        this.coordinate = coordinate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
