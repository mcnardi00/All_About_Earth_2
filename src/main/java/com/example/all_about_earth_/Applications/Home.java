package com.example.all_about_earth_.Applications;

import com.example.all_about_earth_.API.API;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Point3D;
import javafx.geometry.Rectangle2D;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.PickResult;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.security.SecureRandom;

public class Home extends Application {

    //BOTTONI
    private final Button randomPointer = new Button();    //Bottone random
    private final Button searchButton = new Button();  //Bottone a scelta
    private final Button sidebarButton = new Button();

    //Bottoni sideBar
    private Button settings;

    private HBox buttonBox;

    private HBox sideBar = new HBox();

    private final TextField searchField = new TextField();

    private final API api = new API();

    //Dimensioni schermo
    private static final float WIDTH = 1500;
    private static final float HEIGHT = 1200;
    private double screenWidth;
    private double screenHeight;

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

    private Stage homeStage = new Stage();

    private Scene scene;

    private SecureRandom secureRandom = new SecureRandom();

    private Group root = new Group();

    public Home(Stage stage){
        homeStage = stage;
    }

    public Home() {}

    @Override
    public void start(Stage stage) {

        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        screenWidth = screenBounds.getWidth();
        screenHeight = screenBounds.getHeight();

        Search search = new Search(this,homeStage,api, screenWidth, screenHeight);

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

        //Estrae un punto random e lo cerca
        randomPointer.setOnAction(e->{
            double[] coordinates = extractCordinates();

            double longitudine = coordinates[0];
            double latitudine = coordinates[1];

            api.setLongitude(longitudine);
            api.setLongitude(latitudine);

            showLoading(false);
        });


        //Tasto ricerca
        Image searchImage = new Image("lente.png");
        ImageView searchView = new ImageView(searchImage);
        searchView.setFitHeight(30);
        searchView.setFitWidth(40);
        searchView.setSmooth(true);
        searchView.setPreserveRatio(true);
        searchButton.setMaxSize(40, 50);
        searchButton.setGraphic(searchView);

        //Tasto cronologia
        Image historyImage = new Image("history2.png");
        ImageView historyView = new ImageView(historyImage);
        historyView.setFitWidth(30);
        historyView.setFitHeight(30);

        Button historyButton = new Button();
        historyButton.setMaxSize(40,40);
        historyButton.setGraphic(historyView);
        historyButton.setStyle("-fx-background-color: linear-gradient(to bottom, #2C3E50, #34495E); " +
                "-fx-background-radius: 15; " +
                "-fx-border-color: #ECF0F1; " +
                "-fx-border-width: 1px; " +
                "-fx-border-radius: 15; " +
                "-fx-padding: 5; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.5), 5, 0, 0, 2);");

        historyButton.setOnAction(e->{
            LocationsMenu locationsMenu = new LocationsMenu(homeStage);
            locationsMenu.start(new Stage());
        });


        //Fa partire il nuovo stage e chiude questo
        searchButton.setOnAction(e-> {
            search.start(stage);
            //homeStage.close();
        });

        //Li inserisco nel HBox
        buttonBox = new HBox(10, randomPointer, searchButton, historyButton); // 10 è il padding tra i bottoni
        buttonBox.setPadding(new Insets(0, 0, 20, 20));

        //Fornisce lo stile ai bottoni
        setButtonStyle();

        //Inserisco tutto nei bottoni
        root.getChildren().add(world);
        root.getChildren().add(prepareImageView());
        root.getChildren().add(slider);
        root.getChildren().add(buttonBox);

        if(screenHeight == 1032.0 && screenWidth == 1920.0){
            //Setto alla scena la camera
            scene = new Scene(root, screenWidth, screenHeight, true);
            scene.setFill(Color.SILVER);
            scene.setCamera(camera);

            //Disposizione bottoni per scelta
            buttonBox.setLayoutX(320);
            buttonBox.setLayoutY(200);

            //Disposizione bottoni per sidebar
            sideBar.setLayoutX(-380);
            sideBar.setLayoutY(200);

        }else{
            //Setto alla scena la camera
            scene = new Scene(root, screenWidth, screenHeight, true);
            scene.setFill(Color.SILVER);
            scene.setCamera(camera);

            buttonBox.setLayoutX(300);
            buttonBox.setLayoutY(200);

            sideBar.setLayoutX(-475);
            sideBar.setLayoutY(200);

        }

        //Gestisce il controllo del mouse e del click
        initMouseControl(world, scene,homeStage);

        homeStage.setTitle("All About Places");
        homeStage.setScene(scene);

        homeStage.show();

        //Prepara l'animazione della terra che ruota
        prepareAnimation();
    }

    //Fornisce lo stile ai bottoni in basso a destra
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

        searchButton.setStyle(
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

    //Rotazione della terra
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

    //Sfondo
    private ImageView prepareImageView() {
        Image image = new Image("galaxy.jpg");
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.getTransforms().add(new Translate(-image.getWidth() / 2, -image.getHeight() / 2, 800));
        return imageView;
    }

    //Slider
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

    //Mette le immagini sulla sfera
    private Node prepareEarth() {
        PhongMaterial earthMaterial = new PhongMaterial();

        /*
        Image diffuseImage = new Image("earth-d.jpg");
        ImageView earthView = new ImageView(diffuseImage);
        earthView.setRotate(180); // Ruota la texture di 180°
        earthMaterial.setDiffuseMap(earthView.getImage());
         */

        /*
        Image image = new Image("earth-d.jpg");
        WritableImage rotatedImage = rotateImage(image, 180);  // Ruota la texture di 180°
        earthMaterial.setDiffuseMap(rotatedImage);
         */
        Image image = new Image("earth-d.jpg");
        //shiftImageHorizontally(image,1000);
        earthMaterial.setDiffuseMap(image);
        //printCenterPixels(image);

        earthMaterial.setSelfIlluminationMap(new Image("earth-l.jpg"));
        earthMaterial.setSpecularMap(new Image("earth-s.png"));
        earthMaterial.setBumpMap(new Image("earth-n.jpg"));

        sphere.setRotationAxis(Rotate.Y_AXIS);
        sphere.setMaterial(earthMaterial);

        /*CoordinateMappingTest test = new CoordinateMappingTest();
        test.sphere = sphere;
        test.runCoordinateTests();*/
        return sphere;
    }

    //Gestisce il mouse e la rotazione
    private void initMouseControl(Group group, Scene scene, Stage stage) {
        Rotate xRotate;
        Rotate yRotate;
        group.getTransforms().addAll(
                xRotate = new Rotate(0, Rotate.X_AXIS),
                yRotate = new Rotate(0, Rotate.Y_AXIS)
        );
        xRotate.angleProperty().bind(angleX);
        yRotate.angleProperty().bind(angleY);

        sphere.setOnMousePressed(event -> {
            anchorX = event.getSceneX();
            anchorY = event.getSceneY();
            anchorAngleX = angleX.get();
            anchorAngleY = angleY.get();

            mousePressTime = System.currentTimeMillis(); // Registra il tempo di pressione
        });

        sphere.setOnMouseReleased(event -> {
            long elapsedTime = System.currentTimeMillis() - mousePressTime;
            if (elapsedTime < 200) { // Se il click è breve, consideralo valido
                if(isRotating){
                    isRotating = false;
                }else{
                    handleEarthClick(event);
                }
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

    //Gestisce il click della terra e prende le coordinate
    private void handleEarthClick(MouseEvent event) {
        PickResult pickResult = event.getPickResult();
        if (pickResult == null || pickResult.getIntersectedNode() != sphere) {
            return;
        }

        Point3D pickPoint = pickResult.getIntersectedPoint();
        if (pickPoint == null) return;

        // Converti il punto cliccato nelle coordinate geografiche
        double[] coordinates = sphereCoordinatesToGeographic(pickPoint);

        double latitude = coordinates[0];
        double longitude = coordinates[1];

        System.out.printf("Coordinate precise - Latitudine: %.4f, Longitudine: %.4f%n", latitude, longitude);

        // Invia le coordinate all'API
        api.setLatitude(latitude);
        api.setLongitude(longitude);

        showLoading(false);

        //Ferma la rotazione della sfera
        isRotating = !isRotating;
    }

    public void showLoading(boolean isAlreadyOpened){
        ProgressIndicator progressIndicator = new ProgressIndicator();
        progressIndicator.setStyle("-fx-progress-color: white;");
        progressIndicator.setLayoutX(380);
        progressIndicator.setLayoutY(100);

        if(isAlreadyOpened){
            root.getChildren().remove(progressIndicator);
        }else{
            root.getChildren().add(progressIndicator);
        }

        isRotating = true;

        new Thread(() -> {
            try {
                // Simula un breve caricamento se necessario
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Platform.runLater(() -> {
                //Crea e avvia Illustration in una nuova finestra
                Stage illustrationStage = new Stage();
                //illustrationStage.initModality(Modality.NONE);
                Illustration illustration = new Illustration(api);
                illustration.start(illustrationStage);
                homeStage.close();
            });
        }).start();
    }

    private double[] sphereCoordinatesToGeographic(Point3D point) {
        double radius = sphere.getRadius();

        double x = point.getX();
        double y = point.getY();
        double z = point.getZ();

        // Normalizza il punto sulla sfera
        double length = Math.sqrt(x*x + y*y + z*z);
        x = x / length * radius;
        y = y / length * radius;
        z = z / length * radius;

        double latitude = Math.toDegrees(Math.asin(-y / radius));

        if (latitude < 0) {
            if (latitude > -15) {
                latitude -= 12;
                if (latitude > -45) {
                    latitude -= 5.5;
                }
                if (latitude - 12 < -90 && latitude > -75) {
                    latitude -= 5.5;
                }
            }
        } else if ( latitude > 15) {
            latitude += 12;
            if (latitude > 45) {
                latitude += 5.5;
            }
            if (latitude + 12 < 90 && latitude > 75) {
                latitude += 5.5;
            }
        }

        // Calcola la longitudine
        double longitude = Math.toDegrees(Math.atan2(x, -z));

        // Correzione per la longitudine a 180° esatta
        if (Math.abs(longitude - 180) < 1e-6) {
            longitude = 180;
        } else if (Math.abs(longitude + 180) < 1e-6) {
            longitude = -180;
        }

        return new double[]{latitude, longitude};
    }

    // Metodo ausiliario per convertire coordinate geografiche in punto sulla sfera
    private Point3D geographicToSphereCoordinates(double latitude, double longitude) {
        double radius = sphere.getRadius();

        // Conversione coordinate geografiche in coordinate cartesiane
        double phi = Math.toRadians(latitude);
        double theta = Math.toRadians(longitude);

        double x = radius * Math.cos(phi) * Math.sin(theta);
        double y = -radius * Math.sin(phi);  // Negativo per allineare correttamente
        double z = -radius * Math.cos(phi) * Math.cos(theta);

        return new Point3D(x, y, z);
    }

    public double[] extractCordinates(){
        double [] coordinates = new double[2];

        double lon = secureRandom.nextDouble(-180,180);
        coordinates[0] = lon;

        double lat = secureRandom.nextDouble(-90,90);
        coordinates[1] = lat;

        return coordinates;
    }

}
