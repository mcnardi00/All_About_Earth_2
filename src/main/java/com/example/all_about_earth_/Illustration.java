package com.example.all_about_earth_;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Illustration extends Application {
    private BorderPane borderPane = new BorderPane();
    private StackPane generalPane = new StackPane();
    private HBox imageAndText = new HBox(30);
    private Button audioButton = new Button();
    private VBox audioContainer = new VBox(10);
    private Slider audioSlider = new Slider();

    @Override
    public void start(Stage stage) {
        // **Sfondo con immagine**
        Image backgroundImage = new Image("galaxy.jpg");
        BackgroundImage background = new BackgroundImage(backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(100, 100, true, true, true, true));
        generalPane.setBackground(new Background(background));

        // **Titolo moderno**
        Label title = new Label("All About Earth");
        title.setFont(Font.font("Roboto", FontWeight.BOLD, 80));
        title.setTextFill(Color.WHITE);
        title.setEffect(new DropShadow(15, Color.BLACK));

        HBox titleBox = new HBox(title);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setPadding(new Insets(30, 0, 30, 0));
        borderPane.setTop(titleBox);

        // **Caricamento immagine con bordo luminoso**
        Image image = new Image("provaImmagine.jpg");
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(400);
        imageView.setFitWidth(400);
        imageView.setPreserveRatio(true);
        imageView.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,255,255,0.8), 20, 0.3, 0, 0);");

        // **Testo descrittivo con box trasparente**
        Label text = new Label(
                "\uD83C\uDF0D La Terra è l'unico pianeta conosciuto per ospitare la vita.\n\n" +
                        "\uD83C\uDFDE️ Coperta da oceani, foreste e montagne, offre un ambiente unico.\n\n" +
                        "\uD83C\uDF31 Studiare il nostro pianeta ci aiuta a preservarlo per il futuro."
        );
        text.setFont(Font.font("Sans-serif", FontWeight.MEDIUM, 22));
        text.setTextFill(Color.WHITE);
        text.setWrapText(true);
        text.setMaxWidth(450);
        text.setPadding(new Insets(25));
        text.setStyle("-fx-background-color: rgba(0, 0, 0, 0.6); -fx-background-radius: 20px;");

        // **Layout immagini e testo**
        imageAndText.getChildren().addAll(imageView, text);
        imageAndText.setAlignment(Pos.CENTER);
        borderPane.setCenter(imageAndText);

        // **Pulsante audio stilizzato**
        Image icon = new Image("audio.png");
        ImageView iconView = new ImageView(icon);
        iconView.setFitWidth(35);
        iconView.setFitHeight(35);

        audioButton.setGraphic(iconView);
        audioButton.setMinSize(70, 70);

        setAudioButtonStyle();

        setAudioSliderStyle();
        

        audioContainer.getChildren().addAll(audioSlider, audioButton);
        audioContainer.setAlignment(Pos.BOTTOM_RIGHT);
        audioContainer.setPadding(new Insets(20));

        borderPane.setBottom(audioContainer);

        audioButton.setOnAction(e -> {
            audioSlider.setVisible(!audioSlider.isVisible());
        });

        generalPane.getChildren().add(borderPane);

        Scene scene = new Scene(generalPane, 1100, 750);
        stage.setScene(scene);
        stage.setTitle("All About Earth 🌍");
        stage.show();
    }

    public void setAudioSliderStyle() {
        // **Slider audio inizialmente nascosto**
        audioSlider.setMax(10);
        audioSlider.setMin(0);
        audioSlider.setPrefWidth(30);
        audioSlider.setShowTickLabels(true);
        audioSlider.setValue(5);
        audioSlider.setOrientation(Orientation.VERTICAL);
        audioSlider.setStyle("-fx-base: black");
        audioSlider.setVisible(false);
    }

    public void setAudioButtonStyle(){
        audioButton.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #ff8c00, #ff4500); " +
                        "-fx-background-radius: 50px; " +
                        "-fx-border-color: white; " +
                        "-fx-border-width: 3px; " +
                        "-fx-border-radius: 50px;"
        );
        audioButton.setOnMouseEntered(e -> audioButton.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #ff4500, #ff8c00); " +
                        "-fx-background-radius: 50px; " +
                        "-fx-border-color: white; " +
                        "-fx-border-width: 3px; " +
                        "-fx-border-radius: 50px;"
        ));
        audioButton.setOnMouseExited(e -> audioButton.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #ff8c00, #ff4500); " +
                        "-fx-background-radius: 50px; " +
                        "-fx-border-color: white; " +
                        "-fx-border-width: 3px; " +
                        "-fx-border-radius: 50px;"
        ));
        audioButton.setEffect(new DropShadow(15, Color.BLACK));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
