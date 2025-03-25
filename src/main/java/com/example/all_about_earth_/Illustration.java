package com.example.all_about_earth_;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Illustration extends Application {
    private final Home home = new Home(new Stage());

    private final BorderPane borderPane = new BorderPane();
    private final StackPane generalPane = new StackPane();
    private final HBox imageAndText = new HBox(30);

    private VBox BottomContainer = new VBox(10);
    private final Slider audioSlider = new Slider();

    private final Button audioButton = new Button();

    private final Button getBackButton = new Button();

    private final ImageView imageView = new ImageView();
    private int currentImageIndex = 0;
    private final String[] photoUrls = home.getApi().getPlacePhotos();


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
        text.setMaxHeight(300);
        text.setPadding(new Insets(25));
        text.setStyle("-fx-background-color: rgba(0, 0, 0, 0.6); -fx-background-radius: 20px;");

        // **Immagine di prova**
        imageView.setImage(new Image(photoUrls[0]));
        imageView.setFitWidth(450);
        imageView.setFitHeight(300);
        imageView.setStyle("-fx-padding: 3;");

        //Contenitore immagine e testo
        imageAndText.getChildren().addAll(imageView, text);
        imageAndText.setAlignment(Pos.CENTER);
        borderPane.setCenter(imageAndText);

        //Bottone audio
        Image icon = new Image("audio.png");
        ImageView iconView = new ImageView(icon);
        iconView.setFitWidth(35);
        iconView.setFitHeight(35);

        audioButton.setGraphic(iconView);
        audioButton.setMinSize(70, 70);

        setAudioButtonStyle();
        setAudioSliderStyle();

        //Box per audio in basso a destra
        HBox audioBox = new HBox(10, audioSlider, audioButton);
        audioBox.setAlignment(Pos.CENTER_RIGHT);
        audioSlider.setVisible(false);
        audioButton.setOnAction(e -> audioSlider.setVisible(!audioSlider.isVisible()));

        //Bottone torna indietro
        Image getBack = new Image("back.png");
        ImageView getBackView = new ImageView(getBack);
        getBackView.setFitWidth(35);
        getBackView.setFitHeight(35);

        getBackButton.setGraphic(getBackView);
        getBackButton.setMinSize(70, 70);

        getBackButton.setOnAction(e->{
            home.start(new Stage());
            stage.close();

        });

        setBackButtonStyle();

        //Box per bottone torna indietro in basso a sinistra
        HBox backBox = new HBox(getBackButton);
        backBox.setAlignment(Pos.CENTER_LEFT);

        //Container inferiore
        BorderPane bottomLayout = new BorderPane();
        bottomLayout.setPadding(new Insets(20)); // Padding uniforme
        bottomLayout.setLeft(backBox);
        bottomLayout.setRight(audioBox);

        borderPane.setBottom(bottomLayout);
        generalPane.getChildren().add(borderPane);

        Scene scene = new Scene(generalPane, 1500, 1200);
        stage.setScene(scene);
        stage.setTitle("All About Earth 🌍");
        stage.setMaximized(true);
        stage.show();
        startImageSlideshow();
    }


    //Fornisce stile allo slider
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

    //Fornisce stile al bottone dell'audio
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

    //Fornisce lo stile al bottone per tornare indietro
    public void setBackButtonStyle() {
        getBackButton.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #0077b6, #023e8a); " + // Blu elegante
                        "-fx-background-radius: 50px; " +
                        "-fx-border-color: white; " +
                        "-fx-border-width: 3px; " +
                        "-fx-border-radius: 50px;"
        );

        getBackButton.setOnMouseEntered(e -> getBackButton.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #023e8a, #0077b6); " + // Inversione del gradiente
                        "-fx-background-radius: 50px; " +
                        "-fx-border-color: white; " +
                        "-fx-border-width: 3px; " +
                        "-fx-border-radius: 50px;"
        ));

        getBackButton.setOnMouseExited(e -> getBackButton.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #0077b6, #023e8a); " +
                        "-fx-background-radius: 50px; " +
                        "-fx-border-color: white; " +
                        "-fx-border-width: 3px; " +
                        "-fx-border-radius: 50px;"
        ));

        getBackButton.setEffect(new DropShadow(15, Color.BLACK)); // Ombra elegante
    }

    private void startImageSlideshow() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(10), event -> {
            if (photoUrls.length > 0) {
                currentImageIndex = (currentImageIndex + 1) % photoUrls.length;
                Image newImage = new Image(photoUrls[currentImageIndex]);
                imageView.setImage(newImage);
            }
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }



    public static void main(String[] args) {
        launch(args);
    }

    public VBox getBottomContainer() {
        return BottomContainer;
    }

    public void setBottomContainer(VBox bottomContainer) {
        BottomContainer = bottomContainer;
    }
}
