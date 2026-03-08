module com.example.tvylab {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.desktop;
    requires javafx.base;


    opens com.example.tvylab to javafx.fxml;
    exports com.example.tvylab;
    exports com.example.tvylab.sandbox;
    opens com.example.tvylab.sandbox to javafx.fxml;
    exports com.example.tvylab.sandbox.bs;
    opens com.example.tvylab.sandbox.bs to javafx.fxml;
}