package com.example.all_about_earth_.Object;
import java.io.Serializable;

public class User implements Serializable {
    private final String email;
    private final String psw;

    public User(String email,String psw){
        this.email = email;
        this.psw = psw;
    }

    public String getEmail() {
        return email;
    }

    public String getPsw() {
        return psw;
    }
}
