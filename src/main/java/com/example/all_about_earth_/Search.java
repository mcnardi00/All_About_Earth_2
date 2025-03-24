package com.example.all_about_earth_;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Search extends Application {
    @Override
    public void start(Stage stage){
        TextField searchField = new TextField();
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


        Scene scene = new Scene(searchField);

        stage.setScene(scene);
        stage.setMaxWidth(200);
        stage.setMaxHeight(300);
        stage.setResizable(false);

        stage.show();
    }
}
