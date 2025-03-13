package com.example.all_about_earth_;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Point3D;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.PickResult;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

public class Home extends Application {

    //BOTTONI
    private final Button randomPointer = new Button();    //Bottone random
    private final Button pointer = new Button();  //Bottone a scelta
    private final Button sidebarButton = new Button();

    private Button searchButton;

    //Bottoni sideBar
    private Button settings;
    private Button audio;
    private Button history;

    private HBox buttonBox;

    private TextField searchField = new TextField();

    //Dimensioni schermo
    private static final float WIDTH = 1400;
    private static final float HEIGHT = 1000;

    //Servono per la rotazione della sfera
    private double anchorX, anchorY;
    private double anchorAngleX = 0;
    private double anchorAngleY = 0;
    private final DoubleProperty angleX = new SimpleDoubleProperty(0);
    private final DoubleProperty angleY = new SimpleDoubleProperty(0);

    //Tempo di click della terra per dicidere
    private long mousePressTime;

    private final Sphere sphere = new Sphere(150);

    private boolean isClicked = false;

    private boolean isRotating = true; // La Terra parte ruotando
    private AnimationTimer rotationTimer;

    @Override
    public void start(Stage stage) {

        //Setto la camera e la sua visuale
        Camera camera = new PerspectiveCamera(true);
        camera.setNearClip(1);
        camera.setFarClip(10000);
        camera.translateZProperty().set(-1000);

        //Terra
        Group world = new Group();
        world.getChildren().add(prepareEarth());

        //Slider
        Slider slider = prepareSlider();
        world.translateZProperty().bind(slider.valueProperty());

        //Tasto randomPointer
        Image randomImage = new Image("random.png");
        ImageView randomView = new ImageView(randomImage);
        randomView.setFitHeight(35);
        randomView.setFitWidth(35);
        randomView.setSmooth(true);
        randomView.setPreserveRatio(true);
        randomPointer.setMaxSize(40, 40);
        randomPointer.setGraphic(randomView);

        //Tasto pointer
        Image pointerImage = new Image("pointer.png");
        ImageView pointerView = new ImageView(pointerImage);
        pointerView.setFitHeight(30);
        pointerView.setFitWidth(30);
        pointerView.setSmooth(true);
        pointerView.setPreserveRatio(true);
        pointer.setMaxSize(35, 35);
        pointer.setGraphic(pointerView);

        //Li inserisco nel HBox
        buttonBox = new HBox(10, randomPointer, pointer); // 10 è il padding tra i bottoni
        buttonBox.setPadding(new Insets(0, 0, 20, 20));
        buttonBox.setLayoutX(290);
        buttonBox.setLayoutY(200);

        //Tasto sideBar
        Image sidebarImage = new Image("play2.png");
        ImageView sidebarView = new ImageView(sidebarImage);
        sidebarView.setFitHeight(50);
        sidebarView.setFitWidth(50);
        sidebarView.setSmooth(true);
        sidebarView.setPreserveRatio(true);
        sidebarButton.setMaxSize(50, 50);
        sidebarButton.setGraphic(sidebarView);
        //sidebarButton.setStyle("-fx-background-color: white; -fx-border-color: transparent; -fx-padding: 0;");

        //Hbox della sideBar
        HBox sidebarBox = new HBox(sidebarButton);
        sidebarBox.setLayoutX(-430);
        sidebarBox.setLayoutY(0);

        //Fornisce lo stile ai bottoni
        setButtonStyle();

        //Inserisco tutto nei bottoni
        Group root = new Group();
        root.getChildren().add(world);
        root.getChildren().add(prepareImageView());
        root.getChildren().add(slider);
        root.getChildren().add(buttonBox);
        root.getChildren().add(sidebarBox);

        VBox sideBar = createSidebar();

        //Crea la sidebar
        sidebarButton.setOnAction(e -> {
            System.out.println("sideButton cliccato");

            if (isClicked) {  //Se la sidebar è aperta
                root.getChildren().remove(sideBar);
                isClicked = false;
            } else {  //Se la sidebar è chiusa
                root.getChildren().add(sideBar);
                isClicked = true;
            }

        });

        HBox searchBar = createSearchBar();
        root.getChildren().add(searchBar);

        //Setto alla scena la camera
        Scene scene = new Scene(root, WIDTH, HEIGHT, true);
        scene.setFill(Color.SILVER);
        scene.setCamera(camera);

        //Gestisce il controllo del mouse e del click
        initMouseControl(world, scene, stage);

        //scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        stage.setTitle("All About Places");
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.show();

        //Prepara l'animazione della terra che ruota
        prepareAnimation();
    }

    //Crea la sidebar e i suoi bottoni
    public VBox createSidebar() {
        VBox sideBox = new VBox();
        sideBox.setSpacing(15);
        sideBox.setPadding(new Insets(20));
        //sideBox.setAlignment(Pos.CENTER_LEFT);

        settings = createSidebarButton("Impostazioni", "settingsWhite.png");
        audio = createSidebarButton("Audio", "audio2.png");
        history = createSidebarButton("History", "history.png");

        sideBox.getChildren().addAll(settings, audio, history);
        settings.setOnAction(e -> System.out.println("Impostazioni cliccate"));
        setSideButtonStyle();

        sideBox.setLayoutY(-50);
        sideBox.setLayoutX(-380);

        return sideBox;
    }

    private HBox createSearchBar() {
        searchField.setPromptText("Cerca una località...");
        searchField.setPrefWidth(200);
        searchField.setPrefHeight(45);
        searchField.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.3);" +  // Sfondo semi-trasparente
                        "-fx-background-radius: 20;" +
                        "-fx-padding: 5;" +
                        "-fx-border-color: rgba(255, 255, 255, 0.5);" +
                        "-fx-border-width: 1px;" +
                        "-fx-border-radius: 20;" +
                        "-fx-text-fill: white;" +  // Testo bianco
                        "-fx-prompt-text-fill: rgba(255, 255, 255, 0.7);" // Testo del placeholder visibile
        );

        searchField.setOnMouseClicked(e -> searchField.requestFocus());

        Image searchImage = new Image("lente.png");
        ImageView searchView = new ImageView(searchImage);
        searchView.setFitWidth(35);
        searchView.setFitHeight(35);
        searchView.setSmooth(true);
        searchView.setPreserveRatio(true);

        searchButton = new Button();
        searchButton.setMaxSize(40, 40);
        searchButton.setGraphic(searchView);
        searchButton.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.2); " +
                        "-fx-background-radius: 20; " +
                        "-fx-padding: 5; " +
                        "-fx-border-color: rgba(255, 255, 255, 0.5); " +
                        "-fx-border-width: 1px; " +
                        "-fx-border-radius: 20;"
        );

        searchButton.setOnAction(e -> {
            String location = searchField.getText();
            if (!location.isEmpty()) {
                //
            }
        });

        HBox searchBox = new HBox(10, searchButton, searchField);
        searchBox.setPadding(new Insets(10));
        searchBox.setLayoutX(-140);
        searchBox.setLayoutY(-270);

        return searchBox;
    }

    public void setButtonStyle() {
        randomPointer.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #2C3E50, #34495E); " +
                        "-fx-background-radius: 15; " +
                        "-fx-border-color: #ECF0F1; " +
                        "-fx-border-width: 1px; " +
                        "-fx-border-radius: 15; " +
                        "-fx-padding: 5; " +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.5), 5, 0, 0, 2);"
        );

        pointer.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #2C3E50, #34495E); " +
                        "-fx-background-radius: 15; " +
                        "-fx-border-color: #ECF0F1; " +
                        "-fx-border-width: 1px; " +
                        "-fx-border-radius: 15; " +
                        "-fx-padding: 5; " +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.5), 5, 0, 0, 2);"
        );

        buttonBox.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.2); " +
                        "-fx-background-radius: 20; " +
                        "-fx-padding: 10; " +
                        "-fx-border-color: rgba(255, 255, 255, 0.5); " +
                        "-fx-border-width: 1px; " +
                        "-fx-border-radius: 20;"
        );
    }

    public void setSideButtonStyle() {
        settings.setStyle("-fx-background-color: rgba(255, 255, 255, 0.2); " +
                "-fx-background-radius: 20; " +
                "-fx-padding: 10; " +
                "-fx-border-color: rgba(255, 255, 255, 0.5); " +
                "-fx-border-width: 1px; " +
                "-fx-border-radius: 20;");

        audio.setStyle("-fx-background-color: rgba(255, 255, 255, 0.2); " +
                "-fx-background-radius: 20; " +
                "-fx-padding: 10; " +
                "-fx-border-color: rgba(255, 255, 255, 0.5); " +
                "-fx-border-width: 1px; " +
                "-fx-border-radius: 20;");

        history.setStyle("-fx-background-color: rgba(255, 255, 255, 0.2); " +
                "-fx-background-radius: 20; " +
                "-fx-padding: 10; " +
                "-fx-border-color: rgba(255, 255, 255, 0.5); " +
                "-fx-border-width: 1px; " +
                "-fx-border-radius: 20;");
    }


    public Button createSidebarButton(String text, String image) {
        Image icon = new Image(image);
        ImageView iconView = new ImageView(icon);
        iconView.setFitWidth(30);
        iconView.setFitHeight(30);

        Label label = new Label(text);
        label.setTextFill(Color.web("#ECF0F1"));
        label.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));

        HBox buttonContent = new HBox(15, iconView, label);
        buttonContent.setAlignment(Pos.CENTER_LEFT);

        Button button = new Button();
        button.setGraphic(buttonContent);
        button.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-padding: 12px;" +
                        "-fx-border-radius: 10px;" +
                        "-fx-font-size: 18px;" +
                        "-fx-text-fill: #ECF0F1;"
        );

        button.setMaxWidth(Double.MAX_VALUE); // Rende i bottoni larghi quanto la sidebar
        return button;
    }

    private void prepareAnimation() {
        rotationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (isRotating) {
                    sphere.rotateProperty().set(sphere.getRotate() + 0.2);
                }
            }
        };
        rotationTimer.start();
    }


    private ImageView prepareImageView() {
        Image image = new Image("galaxy.jpg");
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.getTransforms().add(new Translate(-image.getWidth() / 2, -image.getHeight() / 2, 800));
        return imageView;
    }

    private Slider prepareSlider() {
        Slider slider = new Slider();
        slider.setMax(800);
        slider.setMin(-250);
        slider.setPrefWidth(300d);
        slider.setLayoutX(-150);
        slider.setLayoutY(230);
        slider.setShowTickLabels(true);
        slider.setTranslateZ(5);
        slider.setStyle("-fx-base: black");
        return slider;
    }

    private Node prepareEarth() {
        PhongMaterial earthMaterial = new PhongMaterial();
        earthMaterial.setDiffuseMap(new Image("earth-d.jpg"));
        earthMaterial.setSelfIlluminationMap(new Image("earth-l.jpg"));
        earthMaterial.setSpecularMap(new Image("earth-s.png"));
        earthMaterial.setBumpMap(new Image("earth-n.jpg"));

        sphere.setRotationAxis(Rotate.Y_AXIS);
        sphere.setMaterial(earthMaterial);
        return sphere;
    }

    private void initMouseControl(Group group, Scene scene, Stage stage) {
        Rotate xRotate;
        Rotate yRotate;
        group.getTransforms().addAll(
                xRotate = new Rotate(0, Rotate.X_AXIS),
                yRotate = new Rotate(0, Rotate.Y_AXIS)
        );
        xRotate.angleProperty().bind(angleX);
        yRotate.angleProperty().bind(angleY);

        scene.setOnMousePressed(event -> {
            anchorX = event.getSceneX();
            anchorY = event.getSceneY();
            anchorAngleX = angleX.get();
            anchorAngleY = angleY.get();

            mousePressTime = System.currentTimeMillis(); // Registra il tempo di pressione
        });

        scene.setOnMouseReleased(event -> {
            long elapsedTime = System.currentTimeMillis() - mousePressTime;
            if (elapsedTime < 200) { // Se il click è breve, consideralo valido
                handleEarthClick(event);
            }
        });

        sphere.setOnMouseDragged(event -> {
            angleX.set(anchorAngleX - (anchorY - event.getSceneY()));
            angleY.set(anchorAngleY + anchorX - event.getSceneX());
        });

        sphere.addEventHandler(ScrollEvent.SCROLL, event -> {
            double delta = event.getDeltaY();

            // Rimuove il binding prima di modificare manualmente
            group.translateZProperty().unbind();

            // Imposta il nuovo valore di translateZ
            group.setTranslateZ(group.getTranslateZ() + delta);

        });

    }


    private void handleEarthClick(javafx.scene.input.MouseEvent event) {
        PickResult pickResult = event.getPickResult();
        if (pickResult == null || pickResult.getIntersectedNode() != sphere) {
            return;
        }

        Point3D pickPoint = pickResult.getIntersectedPoint();
        if (pickPoint == null) return;

        double normX = pickPoint.getX() / sphere.getRadius();
        double normY = pickPoint.getY() / sphere.getRadius();
        double normZ = pickPoint.getZ() / sphere.getRadius();

        double latitude = Math.toDegrees(Math.asin(normY));
        double rawLongitude = Math.toDegrees(Math.atan2(normZ, normX));
        double correctedLongitude = rawLongitude + angleY.get();
        correctedLongitude = ((correctedLongitude + 180) % 360 + 360) % 360 - 180;

        System.out.printf("Cliccato su Lat: %.2f, Lon: %.2f%n", latitude, correctedLongitude);

        // 🔄 Alterna lo stato della rotazione al click
        isRotating = !isRotating;
    }


}
