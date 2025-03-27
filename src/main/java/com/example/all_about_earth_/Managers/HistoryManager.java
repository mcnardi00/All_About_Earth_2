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

    private boolean isAldreadyAdded = false;

    public void createFile(){
        try {
            if(!f.exists()){
                f.createNewFile();
                FileOutputStream fos = new FileOutputStream(f);
                ObjectOutputStream oos = new ObjectOutputStream(fos);

                //User e coordinate di default
                User user = new User("Utente","prova");

                Data data1 = new Data(null, user);
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

    public ArrayList<Data> readFile(){
        try {
            FileInputStream fis = new FileInputStream(f);
            ObjectInputStream ois = new ObjectInputStream(fis);

            return data = (ArrayList<Data>) ois.readObject();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException ignored) {
        }

        return null;
    }


    public ArrayList<Coordinate> readCoordinateFile(){
        try {
            FileInputStream fis = new FileInputStream(f);
            ObjectInputStream ois = new ObjectInputStream(fis);

            User user = getActualUser();

            data = (ArrayList<Data>) ois.readObject();

            for(Data checkData : data){
                if(checkData.getUser().getEmail().equals(user.getEmail()) && checkData.getUser().getPsw().equals(user.getPsw())){
                    return checkData.getCoordinate();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException ignored) {
        }
        return null;
    }

    public boolean AddNewPlace(Coordinate coordinate){
        ArrayList<Coordinate> coordinates = readCoordinateFile();

        for(Coordinate checkCoordinate : coordinates){
            if(checkCoordinate.getPlace_id() == coordinate.getPlace_id()){
                isAldreadyAdded = true;
                return false;
            }
        }

        if(!isAldreadyAdded){
            coordinates.add(coordinate);
            return true;
        }

        return false;
    }


    public void writeFile(){
        try {
            if(!f.createNewFile()){
                FileOutputStream fos = new FileOutputStream(f);
                ObjectOutputStream oos = new ObjectOutputStream(fos);

                oos.writeObject(data);
                oos.flush();

                fos.close();
                oos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public User getActualUser(){
        ActualUserManager actualUserManager = new ActualUserManager();
        User user = actualUserManager.readFile();

        return user;
    }
}
