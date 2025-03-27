package com.example.all_about_earth_.Managers;

import com.example.all_about_earth_.Object.Coordinate;
import com.example.all_about_earth_.Object.Data;
import com.example.all_about_earth_.Object.User;

import java.io.*;
import java.util.ArrayList;

public class HistoryManager {
    private File f = new File("History.ser");
    private ArrayList<Data> data = new ArrayList<>();
    private ArrayList<Coordinate> coordinate = new ArrayList<>();


    public void createFile(){
        try {
            if(!f.createNewFile()){
                FileOutputStream fos = new FileOutputStream(f);
                ObjectOutputStream oos = new ObjectOutputStream(fos);

                //User e coordinate di default
                User user = new User("Utente","prova");
                Coordinate coordinata = new Coordinate();
                coordinate.add(coordinata);

                Data data1 = new Data(coordinate, user);
                data.add(data1);

                oos.writeObject(data);
                oos.flush();

                fos.close();
                oos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readFile(){
        try {
            FileInputStream fis = new FileInputStream(f);
            ObjectInputStream ois = new ObjectInputStream(fis);

            coordinate = (ArrayList<Coordinate>) ois.readObject();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException ignored) {
        }
    }

    
}
