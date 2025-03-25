package com.example.all_about_earth_.Managers;

import com.example.all_about_earth_.Object.Coordinate;
import com.example.all_about_earth_.Object.Data;
import com.example.all_about_earth_.Object.User;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class HistoryManager {
    private File f = new File("History.ser");
    private ArrayList<Data> data = new ArrayList<>();

    public void createFile(){
        try {
            if(!f.createNewFile()){
                FileOutputStream fos = new FileOutputStream(f);
                ObjectOutputStream oos = new ObjectOutputStream(fos);

                //User e coordinate di default
                User user = new User("Utente","prova");
                Coordinate coordinate = new Coordinate(1,1);

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
}
