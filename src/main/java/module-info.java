module com.example.all_about_earth_ {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires org.json;
    requires okhttp3;
    requires java.net.http;
    requires unirest.java;
    requires jlayer;
    opens com.example.all_about_earth_.Applications to javafx.graphics;

    exports com.example.all_about_earth_.Login;
    exports com.example.all_about_earth_.Object;
    exports com.example.all_about_earth_.Applications;
    exports com.example.all_about_earth_.Managers;
}