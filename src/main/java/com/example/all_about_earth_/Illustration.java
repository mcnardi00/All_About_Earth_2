package com.example.all_about_earth_;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Illustration extends Application {
    //Pane che contiene tutto
    private BorderPane borderPane = new BorderPane();

    //Ipotetico pane se volessi mettere lo sfondo
    private StackPane generalPane = new StackPane();

    //Contiene immagini e testo
    private HBox imageAndText = new HBox(20);

    private Label title = new Label("Titolo che andrà messo in modo dinamico");

    @Override
    public void start(Stage stage) {
        // **Titolo con effetto gradiente**
        Label title = new Label("All About Earth");
        title.setFont(Font.font("Roboto", FontWeight.BOLD, 80));
        title.setTextFill(Color.WHITE);
        title.setStyle("-fx-background-color: linear-gradient(to right, #00c6ff, #0072ff); " +
                "-fx-padding: 10px; -fx-background-radius: 15px;"); // Sfondo sfumato al titolo
        title.setEffect(new DropShadow(10, Color.BLACK));

        HBox titleBox = new HBox(title);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setPadding(new Insets(20, 0, 20, 0));

        borderPane.setTop(titleBox);

        // **Caricamento immagine con ombra**
        Image image = new Image("provaImmagine.jpg");
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(400);
        imageView.setFitWidth(400);
        imageView.setPreserveRatio(true);
        imageView.setEffect(new DropShadow(20, Color.BLACK));

        // **Testo con sfondo semi-trasparente per migliore leggibilità**
        Label text = new Label(
                "🌍 La Terra è l'unico pianeta conosciuto per ospitare la vita.\n\n" +
                        "🏞️ Coperta da oceani, foreste e montagne, offre un ambiente unico.\n\n" +
                        "🌱 Studiare il nostro pianeta ci aiuta a preservarlo per il futuro."
        );
        text.setFont(Font.font("Sans-serif", FontWeight.MEDIUM, 20));
        text.setTextFill(Color.WHITE);
        text.setWrapText(true);
        text.setMaxWidth(450);
        text.setPadding(new Insets(20));
        text.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5); -fx-background-radius: 15px;");

        // **Sfondo con gradiente moderno**
        LinearGradient gradient = new LinearGradient(
                0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#1E3C72")),
                new Stop(1, Color.web("#2A5298"))
        );
        borderPane.setBackground(new Background(new BackgroundFill(gradient, CornerRadii.EMPTY, Insets.EMPTY)));

        // **Aggiunta elementi al layout**
        imageAndText.getChildren().addAll(imageView, text);
        imageAndText.setAlignment(Pos.CENTER);
        borderPane.setCenter(imageAndText);

        Scene scene = new Scene(borderPane, 1000, 700);

        stage.setScene(scene);
        stage.setTitle("All About Earth 🌎");
        stage.setFullScreen(true);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
