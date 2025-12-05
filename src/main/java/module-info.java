module com.example.tvylab {
    requires javafx.controls;
    requires javafx.fxml;
    requires atlantafx.base;
    requires javafx.graphics;


    opens com.example.tvylab to javafx.fxml;
    exports com.example.tvylab;
}