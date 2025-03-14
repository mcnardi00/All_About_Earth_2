module com.example.all_about_earth_ {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires org.json;
    requires okhttp3;

    exports com.example.all_about_earth_;

    exports com.example.all_about_earth_.Login;
}