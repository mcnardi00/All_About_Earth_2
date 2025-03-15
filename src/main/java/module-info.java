module com.example.all_about_earth_ {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires org.json;
    requires okhttp3;
    requires java.net.http;
    requires unirest.java;
    requires jlayer;

    exports com.example.all_about_earth_;

    exports com.example.all_about_earth_.Login;
}