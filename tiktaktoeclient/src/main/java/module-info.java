module com.example.tiktaktoeclient {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.tiktaktoeclient to javafx.fxml;
    exports com.example.tiktaktoeclient;
}