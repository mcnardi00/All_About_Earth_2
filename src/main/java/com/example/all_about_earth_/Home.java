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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.PickResult;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
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

    //Pane che contiene tutto ( va inserito nello stack pane)
    private final BorderPane borderPane = new BorderPane();

    private static final float WIDTH = 1400;
    private static final float HEIGHT = 1000;

    private double anchorX, anchorY;
    private double anchorAngleX = 0;
    private double anchorAngleY = 0;
    private final DoubleProperty angleX = new SimpleDoubleProperty(0);
    private final DoubleProperty angleY = new SimpleDoubleProperty(0);

    //Tempo di click della terra per dicidere
    private long mousePressTime;

    private final Sphere sphere = new Sphere(150);

    @Override
    public void start(Stage stage){
        Camera camera = new PerspectiveCamera(true);
        camera.setNearClip(1);
        camera.setFarClip(10000);
        camera.translateZProperty().set(-1000);

        Group world = new Group();
        world.getChildren().add(prepareEarth());

        Slider slider = prepareSlider();
        world.translateZProperty().bind(slider.valueProperty());

        Image randomImage = new Image("random.png");
        ImageView randomView = new ImageView(randomImage);
        randomView.setFitHeight(35);
        randomView.setFitWidth(35);
        randomView.setSmooth(true);
        randomView.setPreserveRatio(true);

        randomPointer.setMaxSize(40,40);
        randomPointer.setGraphic(randomView);

        Image pointerImage = new Image("pointer.png");
        ImageView pointerView = new ImageView(pointerImage);
        pointerView.setFitHeight(30);
        pointerView.setFitWidth(30);
        randomView.setSmooth(true);
        randomView.setPreserveRatio(true);

        pointer.setMaxSize(35,35);
        pointer.setGraphic(pointerView);

        HBox buttonBox = new HBox(10, randomPointer, pointer); // 10 è il padding tra i bottoni
        buttonBox.setPadding(new Insets(0, 0, 20, 20));
        buttonBox.setLayoutX(290);
        buttonBox.setLayoutY(210);

        Group root = new Group();
        root.getChildren().add(world);
        root.getChildren().add(prepareImageView());
        root.getChildren().add(slider);
        root.getChildren().add(buttonBox);

        Scene scene = new Scene(root, WIDTH, HEIGHT, true);
        scene.setFill(Color.SILVER);
        scene.setCamera(camera);

        //scene.setOnMouseClicked(this::handleEarthClick);

        initMouseControl(world, scene, stage);

        stage.setTitle("All About Places");
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.show();

        prepareAnimation();
    }

    public Button createSidebarButton(String text, String image) {
        Image icon = new Image(image);
        ImageView iconView = new ImageView(icon);
        iconView.setFitWidth(45);
        iconView.setFitHeight(45);

        Label label = new Label(text);
        label.setTextFill(Color.web("#ECF0F1"));
        label.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 18));

        HBox buttonContent = new HBox(10, iconView, label); // 10 è il padding tra icona e testo
        buttonContent.setAlignment(Pos.CENTER_LEFT);

        Button button = new Button();
        button.setGraphic(buttonContent);
        button.setStyle("-fx-background-color: transparent; -fx-text-fill: #ECF0F1; -fx-padding: 10; -fx-border-radius: 10; -fx-font-size: 22;");
        button.setMaxWidth(Double.MAX_VALUE); // Rende i bottoni larghi quanto la sidebar

        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #3E556E; -fx-text-fill: #ECF0F1; -fx-padding: 10; -fx-border-radius: 10; -fx-font-size: 22;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: transparent; -fx-text-fill: #ECF0F1; -fx-padding: 10; -fx-border-radius: 10; -fx-font-size: 22;"));

        return button;
    }

    private void prepareAnimation() {
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                sphere.rotateProperty().set(sphere.getRotate() + 0.2);
            }
        };
        timer.start();
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
        earthMaterial.setSpecularMap(new Image("earth-s.jpg"));
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

        scene.setOnMouseDragged(event -> {
            angleX.set(anchorAngleX - (anchorY - event.getSceneY()));
            angleY.set(anchorAngleY + anchorX - event.getSceneX());
        });

        stage.addEventHandler(ScrollEvent.SCROLL, event -> {
            double delta = event.getDeltaY();
            group.translateZProperty().set(group.getTranslateZ() + delta);
        });
    }


    private void handleEarthClick(javafx.scene.input.MouseEvent event) {
        PickResult pickResult = event.getPickResult();
        if (pickResult == null || pickResult.getIntersectedNode() != sphere) {
            return;
        }

        Point3D pickPoint = pickResult.getIntersectedPoint();
        if (pickPoint == null) return;

        // Normalizziamo il punto rispetto al raggio della sfera
        double normX = pickPoint.getX() / sphere.getRadius();
        double normY = pickPoint.getY() / sphere.getRadius();
        double normZ = pickPoint.getZ() / sphere.getRadius();

        // **Latitudine (corretta)**
        double latitude = Math.toDegrees(Math.asin(normY));

        // **Longitudine senza compensazione**
        double rawLongitude = Math.toDegrees(Math.atan2(normZ, normX));

        // **Compensazione della rotazione della sfera**
        double correctedLongitude = rawLongitude + angleY.get();  // Invertito il segno

        // Mantieni la longitudine nell'intervallo [-180, 180]
        correctedLongitude = ((correctedLongitude + 180) % 360 + 360) % 360 - 180;

        System.out.printf("Cliccato su Lat: %.2f, Lon: %.2f%n", latitude, correctedLongitude);
    }



}
