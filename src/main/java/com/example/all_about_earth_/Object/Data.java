package com.example.all_about_earth_.Object;

import java.io.Serializable;

public class Data implements Serializable {
    private Coordinate coordinate;
    private User user;
    //private String prompt;

    public Data(Coordinate coordinate, User user){
        this.coordinate = coordinate;
        this.user = user;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
