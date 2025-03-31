package com.example.all_about_earth_.Applications;

import com.example.all_about_earth_.API.API;
import com.example.all_about_earth_.Managers.HistoryManager;
import com.example.all_about_earth_.Object.Coordinate;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;

public class LocationsMenu extends Application {
    private VBox menuBox = new VBox(10);
    private HBox searchBox = new HBox(10);
    private HistoryManager historyManager = new HistoryManager();
    private Stage locationStage;
    private ScrollPane scrollPane = new ScrollPane();
    private TextField searchField = new TextField();
    private Button searchButton = new Button();
    private BorderPane borderPane = new BorderPane();

    public LocationsMenu(Stage locationStage) {
        this.locationStage = locationStage;
    }

    @Override
    public void start(Stage stage) {
        showHistory();

        searchField.setPromptText("Cerca una località...");
        searchField.setMaxWidth(250);
        searchField.setStyle("-fx-background-color: #34495E; -fx-background-radius: 20; -fx-border-color: #ECF0F1; -fx-border-radius: 20; -fx-text-fill: white; -fx-prompt-text-fill: #BDC3C7;");
        searchField.setMaxWidth(200);
        searchField.setMaxHeight(45);

        ImageView searchView = new ImageView(new Image("sutti.png"));
        searchView.setFitWidth(30);
        searchView.setFitHeight(30);

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

        //Todo:     searchButton.setOnAction(e-> System.out.println("sutti"));

        searchBox.setPadding(new Insets(10));
        searchBox.getChildren().addAll(searchField, searchButton);

        scrollPane.setContent(menuBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setPannable(true);
        scrollPane.setStyle("-fx-background: #1a1a1a; -fx-border-color: transparent;");

        borderPane.setTop(searchBox);
        borderPane.setCenter(scrollPane);
        borderPane.setStyle("-fx-background-color: #2C3E50;");

        Scene scene = new Scene(borderPane, 300, 400);

        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                searchButton.fire();
            }
        });

        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public void showHistory() {
        ArrayList<String> locations = historyManager.readLocations();
        menuBox.getChildren().clear();
        menuBox.setPadding(new Insets(15));

        for (String location : locations) {
            Button locationButton = new Button(location);
            locationButton.setPrefWidth(250);
            locationButton.setStyle("-fx-background-color: #34495E; -fx-border-color: #ECF0F1; -fx-border-radius: 10; -fx-padding: 10; -fx-font-size: 14px; -fx-text-fill: #ffffff;");

            locationButton.setOnMouseEntered(e -> locationButton.setStyle("-fx-background-color: #3498DB; -fx-border-radius: 10; -fx-padding: 10; -fx-font-size: 14px; -fx-text-fill: white;"));
            locationButton.setOnMouseExited(e -> locationButton.setStyle("-fx-background-color: #34495E; -fx-border-color: #ECF0F1; -fx-border-radius: 10; -fx-padding: 10; -fx-font-size: 14px; -fx-text-fill: #ffffff;"));

            locationButton.setOnAction(e -> {
                ArrayList<Coordinate> coordinates = historyManager.readCoordinateFile();
                Illustration illustration = new Illustration(new API(
                        coordinates.get(locations.indexOf(location)).getWrittenSpeech(),
                        coordinates.get(locations.indexOf(location)).getPlace_name(),
                        coordinates.get(locations.indexOf(location)).getPlace_id()
                ));
                illustration.start(new Stage());
                locationStage.close();
            });
            menuBox.getChildren().add(locationButton);
        }
    }
}
