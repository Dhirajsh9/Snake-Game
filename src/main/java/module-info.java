module sample.hello {
    requires javafx.controls;
    requires javafx.fxml;


    opens sample.hello to javafx.fxml;
    exports sample.hello;
}