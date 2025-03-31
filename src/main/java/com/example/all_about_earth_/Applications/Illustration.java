package com.example.all_about_earth_.Applications;

import com.example.all_about_earth_.API.API;
import com.example.all_about_earth_.Managers.ActualUserManager;
import com.example.all_about_earth_.Managers.HistoryManager;
import com.example.all_about_earth_.Object.Coordinate;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Illustration extends Application {
    private final Home home = new Home(new Stage());
    private ActualUserManager actualUserManager = new ActualUserManager();
    private HistoryManager historyManager = new HistoryManager();
    private API api;

    private final BorderPane borderPane = new BorderPane();
    private final StackPane generalPane = new StackPane();
    private final HBox imageAndText = new HBox(30);

    private final VBox BottomContainer = new VBox(10);
    private final Slider audioSlider = new Slider();

    private final Button audioButton = new Button();

    private final Button getBackButton = new Button();

    private final ImageView imageView = new ImageView();
    private int currentImageIndex = 0;
    private String[] photoUrls;

    private double screenWidth;
    private double screenHeight;

    private boolean ExceptionOpened = false;


    public Illustration(API api){
        this.api = api;
    }

    public Illustration(){}

    @Override
    public void start(Stage stage) {
        // **Sfondo con immagine**
        Image backgroundImage = new Image("galaxy.jpg");
        BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(100, 100, true, true, true, true));
        generalPane.setBackground(new Background(background));

        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        screenWidth = screenBounds.getWidth();
        screenHeight = screenBounds.getHeight();

        // **Titolo moderno**
        Label title = new Label("All About Earth");
        title.setFont(Font.font("Roboto", FontWeight.BOLD, 80));
        title.setTextFill(Color.WHITE);
        title.setEffect(new DropShadow(15, Color.BLACK));

        HBox titleBox = new HBox(title);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setPadding(new Insets(30, 0, 30, 0));
        borderPane.setTop(titleBox);

        String formattedText = "";
        try{
            // **Testo descrittivo con box trasparente**
            boolean itWorks = api.getPlaceNameFromCoordinates();

            //Se è un luogo sconosciuto
            if(!itWorks){
                stage.close();
                home.start(new Stage());
                return;
            }

            formattedText = api.getWrittenSpeech().replace("**", "").replace("*  ","").trim();
        } catch (Exception e) {
            home.showLoading(true);
        }

        Label text = new Label(formattedText);
        System.out.println(formattedText);
        text.setFont(Font.font("Sans-serif", FontWeight.MEDIUM, 18));
        text.setTextFill(Color.WHITE);
        text.setWrapText(true);

        ScrollPane scrollPane = new ScrollPane(text);
        scrollPane.setFitToWidth(true);
        scrollPane.setPannable(true);
        scrollPane.setStyle("-fx-background: black");
        scrollPane.setPadding(new Insets(25));

        if(screenHeight == 1080.0 && screenWidth == 1920.0){
            scrollPane.setMaxWidth(800);
            scrollPane.setMaxHeight(700);
        }else{
            scrollPane.setMaxWidth(600);
            scrollPane.setMaxHeight(500);
        }

        if (!api.isExceptionOpened()) {
            photoUrls = api.getPlacePhotos();
            boolean foundImage = false;

            for (String photoUrl : photoUrls) {
                if (photoUrl != null) {
                    imageView.setImage(new Image(photoUrl));
                    foundImage = true;
                    break;
                }
            }

            if (!foundImage) {
                Label noImageLabel = new Label("Nessuna immagine disponibile");
                noImageLabel.setFont(Font.font("Sans-serif", FontWeight.BOLD, 18));
                noImageLabel.setTextFill(Color.WHITE);
                noImageLabel.setAlignment(Pos.CENTER);
                imageAndText.getChildren().add(noImageLabel);
            } else {
                imageAndText.getChildren().add(imageView);
            }
        } else {
            Label noImageLabel = new Label("Nessuna immagine disponibile");
            noImageLabel.setFont(Font.font("Sans-serif", FontWeight.BOLD, 18));
            noImageLabel.setTextFill(Color.WHITE);
            imageAndText.getChildren().add(noImageLabel);
        }
        
        if(screenHeight == 1032.0 && screenWidth == 1920.0){
            imageView.setFitWidth(1050);
            imageView.setFitHeight(700);
        }else{
            imageView.setFitWidth(650);
            imageView.setFitHeight(500);
        }

        imageView.setStyle("-fx-padding: 3;");

        //Contenitore immagine e testo
        imageAndText.getChildren().addAll(scrollPane);
        imageAndText.setAlignment(Pos.CENTER);
        borderPane.setCenter(imageAndText);

        //Salvo su file
        Coordinate coordinate = new Coordinate(api.getWrittenSpeech(), api.getPlace_id(), api.getPlace_name());
        historyManager.AddNewPlace(coordinate);

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

        /*
        try {
            Player player = new Player(api.getSpokenSpeech());
            player.play();
        } catch (JavaLayerException e) {
            Error error = new Error("Problemi nella creazione del audio");
            error.start(new Stage());
        }

         */

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

        Scene scene = new Scene(generalPane, screenWidth, screenHeight);
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
        if (!api.isExceptionOpened()) {
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(10), event -> {
                if (photoUrls.length > 0) {
                    if (currentImageIndex == (photoUrls.length - 1)) {
                        currentImageIndex = 0;
                    } else {
                        currentImageIndex = (currentImageIndex + 1) % photoUrls.length;
                    }
                    if (photoUrls[currentImageIndex] != null){
                        Image newImage = new Image(photoUrls[currentImageIndex]);
                        imageView.setImage(newImage);
                    }
                }
            }));
            timeline.setCycleCount(Animation.INDEFINITE);
            timeline.play();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

