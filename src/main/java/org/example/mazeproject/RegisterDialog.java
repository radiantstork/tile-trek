package org.example.mazeproject;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.function.Consumer;

public class RegisterDialog {
    private final Stage stage;
    private final AuthenticationService authService = new AuthenticationService();
    private final Consumer<User> onRegisterSuccess;

    public RegisterDialog(Stage parent, Consumer<User> onRegisterSuccess) {
        this.stage = new Stage();
        this.onRegisterSuccess = onRegisterSuccess;

        VBox layout = new VBox(10);
        layout.setAlignment(javafx.geometry.Pos.CENTER);
        layout.setStyle("-fx-background-color: #fefbd8;");

        Label userLabel = UIUtils.label("Choose Username:");
        TextField userField = new TextField();

        Label passLabel = UIUtils.label("Choose Password:");
        PasswordField passField = new PasswordField();

        Label confirmLabel = UIUtils.label("Confirm Password:");
        PasswordField confirmField = new PasswordField();

        Button submit = UIUtils.button("Register");
        submit.setOnAction(_ -> {
            try {
                String username = userField.getText();
                String password = passField.getText();
                String confirm = confirmField.getText();

                User user = authService.register(username, password, confirm);

                CommandLogger.log("REGISTER_SUCCESSFUL");
                System.out.println("REGISTER_SUCCESSFUL");

                UIUtils.alert(Alert.AlertType.INFORMATION, "Success", "User registered!");
                stage.close();

                if (onRegisterSuccess != null) {
                    onRegisterSuccess.accept(user);
                }
            } catch (IllegalArgumentException ex) {
                CommandLogger.log("REGISTER_ERROR_" + ex.getMessage().toUpperCase().replace(" ", "_"));
                System.out.println("REGISTER_ERROR_" + ex.getMessage().toUpperCase().replace(" ", "_"));

                UIUtils.alert(Alert.AlertType.ERROR, "Register Failed", ex.getMessage());
            }
        });

        layout.getChildren().addAll(userLabel, userField, passLabel, passField, confirmLabel, confirmField, submit);
        stage.setScene(new Scene(layout, 300, 300));
        stage.setTitle("Register");
        stage.initOwner(parent);
    }

    public Stage getStage() {
        return stage;
    }
}
