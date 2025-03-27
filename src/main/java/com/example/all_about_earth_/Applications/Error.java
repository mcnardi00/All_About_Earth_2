package com.example.all_about_earth_.Applications;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Error extends Application {
    private String errorMessage;
    private StackPane pane = new StackPane();

    public Error(String errorMessage){
        this.errorMessage = errorMessage;
    }

    @Override
    public void start(Stage stage){
        //Configura il nuovo stage per il messaggio di errore
        stage.setTitle("Errore");

        VBox content = new VBox(20);
        content.setStyle("-fx-background-color: #34495E; -fx-padding: 20; -fx-border-radius: 10; -fx-background-radius: 10;");
        content.setMaxSize(400, 200);

        Label messageLabel = new Label(errorMessage);
        messageLabel.setTextAlignment(TextAlignment.CENTER);

        //messageLabel.setWrapText(true);
        messageLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        messageLabel.setTextFill(Color.WHITE);

        Button closeButton = new Button("Chiudi");
        closeButton.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-font-size: 14; -fx-padding: 5 10;");
        closeButton.setOnAction(e -> stage.close()); // Chiude solo la finestra di errore

        content.getChildren().addAll(messageLabel, closeButton);
        content.setStyle("-fx-alignment: center;");

        pane.setAlignment(Pos.CENTER);
        pane.setStyle("-fx-background-color: #34495E;");
        pane.getChildren().add(content);
        Scene errorScene = new Scene(pane, 400, 200);
        stage.setScene(errorScene);

        stage.initModality(Modality.APPLICATION_MODAL);

        //Mostra la finestra di errore
        stage.showAndWait(); // Blocca finché non viene chiusa
    }
}
