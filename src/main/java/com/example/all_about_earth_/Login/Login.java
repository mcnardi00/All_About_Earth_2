package com.example.all_about_earth_.Login;

import com.example.all_about_earth_.Home;
import com.example.all_about_earth_.Object.User;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;


public class Login extends Application {

    private LoginManager loginManager = new LoginManager();

    private Color allColor = Color.rgb(255, 255, 255);

    private TextField gmailField = new TextField();
    private PasswordField passwordField = new PasswordField();

    private Button loginButton = new Button("Login");

    @Override
    public void start(Stage stage) {

        // Titolo in alto con font professionale
        Label title = new Label("    All ");
        title.setFont(Font.loadFont(getClass().getResourceAsStream("/LuckiestGuy-Regular.ttf"), 96));
        title.setTextFill(allColor);

        // Titolo in alto con font professionale
        Label title2 = new Label("About ");
        title2.setFont(Font.loadFont(getClass().getResourceAsStream("/LuckiestGuy-Regular.ttf"), 96));
        title2.setStyle("-fx-text-fill: #2980B9");

        Label title3 = new Label("Earth");
        title3.setFont(Font.loadFont(getClass().getResourceAsStream("/LuckiestGuy-Regular.ttf"), 96));
        title3.setTextFill(allColor);

        HBox titleBox = new HBox(title,title2,title3);
        titleBox.setAlignment(Pos.TOP_CENTER);
        titleBox.setPadding(new Insets(50, 0, 40, 0)); // Distanza dal bordo

        // Immagine della Terra con proporzioni corrette
        Image earthImage = new Image("prova.jpg");
        ImageView earthView = new ImageView(earthImage);
        earthView.setPreserveRatio(true);
        earthView.setFitHeight(600);
        earthView.setSmooth(true);

        // Stile per i campi di input
        String inputStyle = "-fx-background-color: rgba(0, 0, 0, 0.6); -fx-text-fill: white; " +
                "-fx-border-color: #2980B9; -fx-border-radius: 10; -fx-background-radius: 10; " +
                "-fx-padding: 12; -fx-font-size: 18px; -fx-font-family: 'Segoe UI';";


        gmailField.setPromptText("Inserisci username");
        gmailField.setStyle(inputStyle);
        gmailField.setMaxWidth(300);

        passwordField.setPromptText("Inserisci password");
        passwordField.setStyle(inputStyle);
        passwordField.setMaxWidth(300);

        // Stile bottone
        loginButton.setTextFill(Color.WHITE);
        loginButton.setStyle("-fx-background-color: #3498DB; -fx-border-radius: 10; " +
                "-fx-background-radius: 10; -fx-padding: 12 30; -fx-font-size: 20px;");
        loginButton.setMaxWidth(200);

        // Effetto hover per il pulsante
        loginButton.setOnMouseEntered(e -> loginButton.setStyle("-fx-background-color: #2980B9; -fx-border-radius: 10; " +
                "-fx-background-radius: 10; -fx-padding: 12 30; -fx-font-size: 20px;"));
        loginButton.setOnMouseExited(e -> loginButton.setStyle("-fx-background-color: #3498DB; -fx-border-radius: 10; " +
                "-fx-background-radius: 10; -fx-padding: 12 30; -fx-font-size: 20px;"));

        loginButton.setOnAction(e-> handLogin());

        // Box per i campi di input e il bottone (centrato rispetto alla finestra)
        VBox textFieldBox = new VBox(20, gmailField, passwordField, loginButton);
        textFieldBox.setAlignment(Pos.CENTER);
        textFieldBox.setPadding(new Insets(30));
        textFieldBox.setStyle("-fx-background-color: rgba(0, 0, 0, 0.4); " +
                "-fx-background-radius: 20; -fx-padding: 30;");

        // Box contenente l'immagine e i campi centrati
        StackPane imageOverlay = new StackPane();
        imageOverlay.getChildren().addAll(earthView, textFieldBox);
        StackPane.setAlignment(textFieldBox, Pos.CENTER);

        // Layout principale con il titolo in alto e il resto centrato
        VBox mainLayout = new VBox(50, titleBox, imageOverlay);
        mainLayout.setAlignment(Pos.TOP_CENTER);
        mainLayout.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));

        // Scena a schermo intero
        Scene scene = new Scene(mainLayout, 1280, 800);
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.show();
    }

    public void handLogin(){
        String gmail = gmailField.getText();
        String psw = passwordField.getText();

        if(loginManager.checkUser(new User(gmail,psw))){
            //EarthViewer earthViewer = new EarthViewer();
            //earthViewer.start(new Stage());
            Home home = new Home();
            home.start(new Stage());
        }else{
            //Todo: print error
        }
    }

    public static void main(String[] args){
        LoginManager loginManager = new LoginManager();
        loginManager.createFile();

        launch(args);
    }
}
