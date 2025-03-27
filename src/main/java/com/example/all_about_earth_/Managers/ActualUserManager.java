package com.example.all_about_earth_.Managers;

import com.example.all_about_earth_.Object.User;

import java.io.*;
import java.util.ArrayList;

public class ActualUserManager {
    private File f = new File("ActualUser.ser");
    private ArrayList<User> users = new ArrayList<>();

    public void createFile(){
        try {
            if(!f.exists()){
                f.createNewFile();
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

    public User readFile(){
        try {
            FileInputStream fis = new FileInputStream(f);
            ObjectInputStream ois = new ObjectInputStream(fis);

            users = (ArrayList<User>) ois.readObject();
            User user = users.getFirst();

            return user;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException ignored) {
        }
        return null;
    }

    //Todo: Da usare se dovessi aggiungere la pagina di registrazione
    public void changeUser(){}
}

