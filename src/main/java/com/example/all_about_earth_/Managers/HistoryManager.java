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
        data = readFile();

        User user = getActualUser();

        for (Data checkData : data) {
            if (checkData.getUser().getEmail().equals(user.getEmail()) && checkData.getUser().getPsw().equals(user.getPsw())) {
                return checkData.getCoordinate();
            }
        }
        return null;
    }

    public ArrayList<String> readLocations(){
        ArrayList<String> locations = new ArrayList<>();
        readFile();

        User user = getActualUser();

        try{
            for (Data checkData : data) {
                if (checkData.getUser().getEmail().equals(user.getEmail()) && checkData.getUser().getPsw().equals(user.getPsw())) {
                    for(Coordinate coordinate1 : checkData.getCoordinate()){
                        locations.add(coordinate1.getPlace_name());
                    }
                }
            }
        }catch (Exception e){
        }


        return locations;
    }

    public boolean AddNewPlace(Coordinate coordinate1){

        coordinate = readCoordinateFile();

        //Se non è il primo luogo controlla che non ci sia già
        if(!coordinate.isEmpty()){
            coordinate = readCoordinateFile();

            for(Coordinate checkCoordinate : coordinate){
                if(checkCoordinate.getPlace_id() == coordinate1.getPlace_id()){
                    isAldreadyAdded = true;
                    return false;
                }
            }

            if(!isAldreadyAdded){
                coordinate.add(coordinate1);
                addCoordinateInData(coordinate);
                writeFile();
                return true;
            }
        }else{      //Se è il primo luogo lo inserisce direttamente
            coordinate.add(coordinate1);
            addCoordinateInData(coordinate);
            writeFile();
            return true;
        }

        return false;
    }

    public void addCoordinateInData(ArrayList<Coordinate> coordinate){
        User user = getActualUser();

        try{
            if(!data.isEmpty()){
                data = readFile();
                for (Data checkData : data) {
                    if (checkData.getUser().getEmail().equals(user.getEmail()) && checkData.getUser().getPsw().equals(user.getPsw())) {
                        checkData.setCoordinate(coordinate);
                    }
                }
            }else{
                Data data1 = new Data(coordinate,user);
                data.add(data1);
            }

        }catch (Exception e){
        }
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
