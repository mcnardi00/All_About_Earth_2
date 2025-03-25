package com.example.all_about_earth_.Applications;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

//todo: leggere da file i menu disponibili
public class LocationsMenu extends Application {
    // Creazione della finestra pop-up con VBox
    VBox menuBox = new VBox();

    @Override
    public void start(Stage stage){

        showHistory();

        // Creazione di una finestra di popup personalizzata
        Scene scene = new Scene(menuBox,200,300);

        stage.setScene(scene);
        stage.setMaxWidth(200);
        stage.setMaxHeight(300);
        stage.setResizable(false);

        stage.show();
    }

    public void showHistory() {
        // Simulazione della cronologia delle ricerche
        String[] locations = {"Milano", "Roma", "Firenze", "Napoli", "Torino"};

        menuBox.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.95); " +
                        "-fx-border-radius: 15px; " +
                        "-fx-padding: 10px; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.3), 10, 0, 2, 2);"
        );

        for (String location : locations) {
            Button locationButton = new Button(location);

            // Stile dei bottoni
            locationButton.setStyle(
                    "-fx-background-color: transparent; " +
                            "-fx-padding: 10px 20px; " +
                            "-fx-font-size: 16px; " +
                            "-fx-font-family: 'Segoe UI'; " +
                            "-fx-text-fill: #2C3E50; " +
                            "-fx-alignment: center-left;"
            );

            // Effetto hover
            locationButton.setOnMouseEntered(e -> locationButton.setStyle(
                    "-fx-background-color: #3498DB; " +
                            "-fx-text-fill: white; " +
                            "-fx-padding: 10px 20px; " +
                            "-fx-font-size: 16px; " +
                            "-fx-font-family: 'Segoe UI';"
            ));

            locationButton.setOnMouseExited(e -> locationButton.setStyle(
                    "-fx-background-color: transparent; " +
                            "-fx-padding: 10px 20px; " +
                            "-fx-font-size: 16px; " +
                            "-fx-font-family: 'Segoe UI'; " +
                            "-fx-text-fill: #2C3E50;"
            ));

            // Azione quando si seleziona una città
            locationButton.setOnAction(e -> {
                System.out.println("Hai selezionato " + location);
            });

            menuBox.getChildren().add(locationButton);
        }

    }
}
