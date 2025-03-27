package com.example.all_about_earth_.Applications;

import com.example.all_about_earth_.Managers.HistoryManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;

public class LocationsMenu extends Application {
    private VBox menuBox = new VBox(10); // Spaziatura tra i bottoni

    private HistoryManager historyManager = new HistoryManager();

    private String placeName;

    @Override
    public void start(Stage stage) {
        showHistory();

        Scene scene = new Scene(menuBox, 220, 320);

        stage.setScene(scene);
        stage.setMaxWidth(220);
        stage.setMaxHeight(320);
        stage.setResizable(false);
        //stage.initModality(Modality.APPLICATION_MODAL);

        stage.show();
    }

    //todo:sistemare che se è vuoto stampa che è vuoto
    public void showHistory() {
        ArrayList<String> locations = historyManager.readLocations();

        menuBox.setStyle(
                "-fx-background-color: #1a1a1a; " + // Sfondo nero
                        "-fx-border-radius: 15px; " +
                        "-fx-padding: 15px; " +
                        "-fx-effect: dropshadow(gaussian, rgba(255, 255, 255, 0.2), 12, 0, 2, 2);"
        );

        for (String location : locations) {
            Button locationButton = new Button(location);
            locationButton.setPrefWidth(180);
            locationButton.setStyle(
                    "-fx-background-color: #2c3e50; " + // Blu scuro per visibilità
                            "-fx-border-color: #ffffff; " + // Bordo bianco
                            "-fx-border-width: 1px; " +
                            "-fx-border-radius: 8px; " +
                            "-fx-padding: 12px; " +
                            "-fx-font-size: 14px; " +
                            "-fx-font-family: 'Arial'; " +
                            "-fx-text-fill: #ffffff; " + // Testo bianco per contrasto
                            "-fx-alignment: center;"
            );

            locationButton.setOnMouseEntered(e -> locationButton.setStyle(
                    "-fx-background-color: #3498DB; " + // Blu acceso per hover
                            "-fx-border-radius: 8px; " +
                            "-fx-padding: 12px; " +
                            "-fx-font-size: 14px; " +
                            "-fx-font-family: 'Arial'; " +
                            "-fx-text-fill: white; " +
                            "-fx-transition: all 0.3s ease;"
            ));

            locationButton.setOnMouseExited(e -> locationButton.setStyle(
                    "-fx-background-color: #2c3e50; " + // Ritorna al blu scuro
                            "-fx-border-color: #ffffff; " +
                            "-fx-border-width: 1px; " +
                            "-fx-border-radius: 8px; " +
                            "-fx-padding: 12px; " +
                            "-fx-font-size: 14px; " +
                            "-fx-font-family: 'Arial'; " +
                            "-fx-text-fill: #ffffff;"
            ));


            //Todo: Far aprire illustration locationButton.setOnAction(e -> {});


            menuBox.getChildren().add(locationButton);
        }
    }

}
