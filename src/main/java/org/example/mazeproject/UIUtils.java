package org.example.mazeproject;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Font;

public class UIUtils {
    public static Label label(String text) {
        Label label = new Label(text);
        label.setFont(Font.font("Comic Sans MS", 16));
        return label;
    }

    public static Button button(String text) {
        Button button = new Button(text);
        button.setFont(Font.font("Comic Sans MS", 18));
        return button;
    }

    public static void alert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
