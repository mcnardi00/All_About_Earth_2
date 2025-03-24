package com.example.all_about_earth_;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Search extends Application {
    private Home home;
    private Illustration illustration = new Illustration();

    public Search(Home home){
        this.home = home;
    }

    private TextField searchField = new TextField();

    @Override
    public void start(Stage stage){
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

        Image searchImage = new Image("lente.png");
        ImageView searchView = new ImageView(searchImage);
        searchView.setFitWidth(35);
        searchView.setFitHeight(35);
        searchView.setSmooth(true);
        searchView.setPreserveRatio(true);

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

            if(!text.isEmpty()){
                home.setTextFromSearch(text);
                illustration.start(new Stage());


                //todo: fare richiesta api
            }else{
                System.out.println("testo vuoto");
            }
        });


        HBox hBox = new HBox(30,searchField,searchButton);

        Scene scene = new Scene(hBox,200,45);

        stage.setScene(scene);
        stage.setMaxWidth(200);
        stage.setMaxHeight(300);
        stage.setX(1175);
        stage.setY(650);

        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);

        stage.show();
    }

    public String getSearchedText(){
        return searchField.getText();
    }
}
