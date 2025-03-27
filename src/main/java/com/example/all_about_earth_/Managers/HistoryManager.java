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

    //crea il file se inesistente
    public void createFile(){
        try {
            if(!f.exists()){
                f.createNewFile();
                FileOutputStream fos = new FileOutputStream(f);
                ObjectOutputStream oos = new ObjectOutputStream(fos);

                //Lo inizializzo con un array vuoto in modo che non sia vuoto il file
                oos.writeObject(new ArrayList<Data>());

                fos.close();
                oos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Prende tutti i dati dal file
    public ArrayList<Data> readFile(){
        try {
            FileInputStream fis = new FileInputStream(f);
            ObjectInputStream ois = new ObjectInputStream(fis);

            return data = (ArrayList<Data>) ois.readObject();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException ignored) {
        }

        return new ArrayList<>();
    }


    //Prende le coordinate dell'utente attuale utente
    public ArrayList<Coordinate> readCoordinateFile(){

        try{
            //Legge tutti i dati
            data = readFile();
        } catch (Exception e) {
            return new ArrayList<>();
        }

        //Prende l'utente attuale
        User user = getActualUser();

        //Cerca i dati di questo utente
        for (Data checkData : data) {
            if (checkData.getUser().getEmail().equals(user.getEmail()) && checkData.getUser().getPsw().equals(user.getPsw())) {
                return checkData.getCoordinate();
            }
        }
        return new ArrayList<>();
    }

    //Legge le cordinate dell'utente e le manda al location menù
    public ArrayList<String> readLocations(){
        ArrayList<String> locations = new ArrayList<>();

        //Legge tutto il file
        readFile();

        //Prende l'utente attuale
        User user = getActualUser();

        try{
            for (Data checkData : data) {
                if (checkData.getUser().getEmail().equals(user.getEmail()) && checkData.getUser().getPsw().equals(user.getPsw())) {
                    for(Coordinate coordinate1 : checkData.getCoordinate()){
                        //Aggiunge alle location questa nuova
                        locations.add(coordinate1.getPlace_name());
                    }
                }
            }
        }catch (Exception e){}

        return locations;
    }


    //Aggiunge un nuovo luogo alla cronologia
    public boolean AddNewPlace(Coordinate coordinate1){

        try{
            //Legge le coordinate dell'utente attuale
            coordinate = readCoordinateFile();
        }catch (NullPointerException e){
            System.out.println("sutti");
        }

        //Se l'array è null lo creo vuoto per evitare problemi
        if (coordinate == null) {
            coordinate = new ArrayList<>();
        }

        //Se non è il primo luogo controlla che non ci sia già
        if(!coordinate.isEmpty()){

            //Controlla se il luogo è gia nella cronologia
            for(Coordinate checkCoordinate : coordinate){
                if(checkCoordinate.getPlace_name().equals(coordinate1.getPlace_name())){
                    isAldreadyAdded = true;
                    return false;
                }
            }

            //Se non è presente lo aggiunge
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

    //Aggiunge le coordinate nuove all'array dell'utente attuale
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


    //Riscrive il file completo
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

    /*  Todo: trovare latitudine e longitudine
    public double[] getCoordinatesByName(String placeName){
        coordinate = readCoordinateFile();

        ArrayList<String> locations = new ArrayList<>();

        //Legge tutto il file
        readFile();

        //Prende l'utente attuale
        User user = getActualUser();

        try{
            for (Data checkData : data) {
                if (checkData.getUser().getEmail().equals(user.getEmail()) && checkData.getUser().getPsw().equals(user.getPsw())) {
                    for(Coordinate coordinate1 : checkData.getCoordinate()){
                        if(coordinate1.getPlace_name().equals(placeName)){

                        }
                    }
                }
            }
        }catch (Exception e){
        }
    }
     */

    //Prende l'utente attuale
    public User getActualUser(){
        ActualUserManager actualUserManager = new ActualUserManager();
        User user = actualUserManager.readFile();

        return user;
    }
}
