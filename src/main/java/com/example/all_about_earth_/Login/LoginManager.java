package com.example.all_about_earth_.Login;


import com.example.all_about_earth_.Object.User;

import java.io.*;
import java.util.ArrayList;

//todo:aggiungere controllo email
public class LoginManager {
    private File f = new File("User.ser");
    private ArrayList<User> users = new ArrayList<>();

    public void createFile(){
        try {
            if(!f.createNewFile()){
                FileOutputStream fos = new FileOutputStream(f);
                ObjectOutputStream oos = new ObjectOutputStream(fos);

                User user = new User("Utente","prova");
                users.add(user);

                oos.writeObject(users);
                oos.flush();

                fos.close();
                oos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean checkUser(User user){
        readFile();

        for(User checkUser : users){
            if(checkUser.getEmail().equals(user.getEmail())){
                if(checkUser.getPsw().equals(user.getPsw())){
                    return true;
                }
            }
        }

        return false;
    }

    public void readFile(){
        try {
            FileInputStream fis = new FileInputStream(f);
            ObjectInputStream ois = new ObjectInputStream(fis);

            users = (ArrayList<User>) ois.readObject();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException ignored) {
        }
    }
}
