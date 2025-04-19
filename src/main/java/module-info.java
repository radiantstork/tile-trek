module org.example.mazeproject {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.almasb.fxgl.all;
    requires javafx.media;

    opens org.example.mazeproject to javafx.fxml;
    exports org.example.mazeproject;
}