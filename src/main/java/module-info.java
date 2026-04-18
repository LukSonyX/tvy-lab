module com.example.tvylab {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.desktop;
    requires javafx.base;
    requires com.fasterxml.jackson.databind;


    opens com.example.tvylab to javafx.fxml;
    exports com.example.tvylab;
    exports com.example.tvylab.sandbox;
    opens com.example.tvylab.sandbox to javafx.fxml;
    exports com.example.tvylab.lessons;
    opens com.example.tvylab.lessons to javafx.fxml;
    exports com.example.tvylab.settings;
    opens com.example.tvylab.settings to javafx.fxml;
}