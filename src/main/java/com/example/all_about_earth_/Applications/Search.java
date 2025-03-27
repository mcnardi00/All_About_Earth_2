package com.example.all_about_earth_.Applications;

import com.example.all_about_earth_.API.API;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

//Stage che fa aprire la barra di ricerca
public class Search extends Application {
    private Home home;
    private Stage homeStage;
    private Stage searchStage = new Stage();
    private API api;

    private double screenWidth;
    private double screenEight;

    private boolean isOpened = false;

    public Search(Home home, Stage stage, API api, double screenWidth, double screenEight){
        this.home = home;
        homeStage = stage;
        this.api = api;
        this.screenWidth = screenWidth;
        this.screenEight = screenEight;
    }

    public Search(){}

    private TextField searchField = new TextField();

    @Override
    public void start(Stage stage){

        //Textfield della barra di ricerca
        searchField.setPromptText("Cerca una località...");
        searchField.setMaxWidth(200);
        searchField.setMaxHeight(45);
        searchField.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.3);" +
                        "-fx-background-radius: 20;" +
                        "-fx-padding: 5;" +
                        "-fx-border-color: rgba(255, 255, 255, 0.5);" +
                        "-fx-border-width: 1px;" +
                        "-fx-border-radius: 20;" +
                        "-fx-text-fill: black;" +  // Cambia il colore del testo
                        "-fx-prompt-text-fill: rgba(0, 0, 0, 0.5);" // Anche il placeholder più scuro
        );

        //Immagine della lente d'ingrandimento
        Image searchImage = new Image("lente.png");
        ImageView searchView = new ImageView(searchImage);
        searchView.setFitWidth(35);
        searchView.setFitHeight(35);
        searchView.setSmooth(true);
        searchView.setPreserveRatio(true);

        //Bottone per inviare la ricerca
        Button searchButton = new Button();
        searchButton.setMaxSize(40, 40);
        searchButton.setGraphic(searchView);
        searchButton.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.2); " +
                        "-fx-background-radius: 20; " +
                        "-fx-padding: 5; " +
                        "-fx-border-color: rgba(255, 255, 255, 0.5); " +
                        "-fx-border-width: 1px; " +
                        "-fx-border-radius: 20;"
        );

        searchButton.setOnAction(e->{
            String text = searchField.getText();

            //Se il testo non è vuoto invio il testo e apro lo stage
            if(!text.isEmpty()){
                //home.setTextFromSearch(text);
                api.getCityByText(text.trim());

                if(api.isPlaceFound()){
                    Illustration illustration = new Illustration(api);
                    illustration.start(new Stage());
                    searchStage.close();
                    homeStage.close();
                }
            }else{
                System.out.println("testo vuoto");
            }
        });


        HBox hBox = new HBox(30,searchField,searchButton);

        Scene scene = new Scene(hBox,200,45);

        searchStage.setScene(scene);
        searchStage.setMaxWidth(200);
        searchStage.setMaxHeight(300);

        //Grandezza computer scuola
        if(screenWidth == 1920.0 && screenEight == 1032.0){
            searchStage.setX(1700);
            searchStage.setY(650);
        }else{      //Todo: aggiungere l'if per grandezza 1920x1080
            searchStage.setX(1300);
            searchStage.setY(650);
        }


        searchStage.setResizable(false);

        if(!(searchStage.getModality() == Modality.APPLICATION_MODAL)){
            searchStage.initModality(Modality.APPLICATION_MODAL);
        }

        searchStage.show();

    }

    public void setIsOpened(boolean isOpened){
        this.isOpened = isOpened;
    }
}
