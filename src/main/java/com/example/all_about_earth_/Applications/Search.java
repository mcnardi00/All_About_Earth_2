package com.example.all_about_earth_.Applications;

import com.example.all_about_earth_.API.API;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Search extends Application {
    private Home home;
    private Stage homeStage;
    private Stage searchStage = new Stage();
    private API api;

    private double screenWidth;
    private double screenEight;

    private boolean isOpened = false;

    public Search(Home home, Stage stage, API api, double screenWidth, double screenEight) {
        this.home = home;
        homeStage = stage;
        this.api = api;
        this.screenWidth = screenWidth;
        this.screenEight = screenEight;
    }

    public Search() {}

    private TextField searchField = new TextField();

    @Override
    public void start(Stage stage) {
        // Creazione del container principale con sfondo a gradienti in linea con Home
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #2C3E50, #34495E);");

        // Impostazioni del TextField con stile moderno ed elegante
        searchField.setPromptText("Cerca una località...");
        searchField.setMaxWidth(250);
        searchField.setMaxHeight(45);
        searchField.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.3);" +
                        "-fx-background-radius: 20;" +
                        "-fx-border-color: rgba(255, 255, 255, 0.5);" +
                        "-fx-border-width: 1px;" +
                        "-fx-border-radius: 20;" +
                        "-fx-text-fill: white;" +
                        "-fx-prompt-text-fill: #BDC3C7;" +
                        "-fx-padding: 10;"
        );

        // Immagine della lente d'ingrandimento
        Image searchImage = new Image("lente.png");
        ImageView searchView = new ImageView(searchImage);
        searchView.setFitWidth(35);
        searchView.setFitHeight(35);
        searchView.setSmooth(true);
        searchView.setPreserveRatio(true);

        // Creazione del pulsante di ricerca con stile in linea con Home
        Button searchButton = new Button();
        searchButton.setMaxSize(40, 40);
        searchButton.setGraphic(searchView);
        searchButton.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #2C3E50, #34495E);" +
                        "-fx-background-radius: 20;" +
                        "-fx-border-color: rgba(255, 255, 255, 0.5);" +
                        "-fx-border-width: 1px;" +
                        "-fx-border-radius: 20;" +
                        "-fx-padding: 10;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 5, 0, 0, 2);"
        );

        // Abilita il click del pulsante anche premendo la barra spaziatrice
        searchField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                searchButton.fire();
            }
        });

        searchButton.setOnAction(e -> {
            String text = searchField.getText().trim();
            if (!text.isEmpty()) {
                api.getCityByText(text);
                if (api.isPlaceFound()) {
                    Illustration illustration = new Illustration(api);
                    illustration.start(new Stage());
                    searchStage.close();
                    homeStage.close();
                }
            } else {
                System.out.println("Testo vuoto");
            }
        });

        // Contenitore orizzontale per il textfield e il pulsante, centrato e con padding
        HBox searchContainer = new HBox(10, searchField, searchButton);
        searchContainer.setAlignment(Pos.CENTER);
        searchContainer.setPadding(new Insets(10));

        root.setCenter(searchContainer);

        Scene scene = new Scene(root, 300, 100);
        searchStage.setScene(scene);
        searchStage.setResizable(false);
        searchStage.initModality(Modality.APPLICATION_MODAL);

        // Posizionamento in base alle dimensioni dello schermo
        if (screenWidth == 1920.0 && screenEight == 1032.0) {
            searchStage.setX(1700);
            searchStage.setY(650);
        } else {
            // TODO: Aggiungere ulteriori condizioni per altre risoluzioni
            searchStage.setX(1300);
            searchStage.setY(630);
        }

        searchStage.show();
    }
}
