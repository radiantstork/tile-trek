package org.example.mazeproject;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.function.Consumer;

public class LoginDialog {
    private final Stage stage;
    private final AuthenticationService authService = new AuthenticationService();
    private final Consumer<User> onLoginSuccess;

    public LoginDialog(Stage parent, Consumer<User> onLoginSuccess) {
        this.stage = new Stage();
        this.onLoginSuccess = onLoginSuccess;

        VBox layout = new VBox(10);
        layout.setAlignment(javafx.geometry.Pos.CENTER);
        layout.setStyle("-fx-background-color: #fefbd8;");

        Label userLabel = UIUtils.label("Username:");
        TextField userField = new TextField();

        Label passLabel = UIUtils.label("Password:");
        PasswordField passField = new PasswordField();

        Button submit = UIUtils.button("Login");
        submit.setOnAction(_ -> {
            try {
                String username = userField.getText();
                String password = passField.getText();

                User user = authService.login(username, password);

                CommandLogger.log("LOGIN_SUCCESSFUL");
                System.out.println("LOGIN_SUCCESSFUL");

                UIUtils.alert(Alert.AlertType.INFORMATION, "Success", "Login successful!");
                stage.close();

                if (onLoginSuccess != null) {
                    onLoginSuccess.accept(user);
                }
            } catch (IllegalArgumentException ex) {
                CommandLogger.log("LOGIN_ERROR_" + ex.getMessage().toUpperCase().replace(" ", "_"));
                System.out.println("LOGIN_ERROR_" + ex.getMessage().toUpperCase().replace(" ", "_"));

                UIUtils.alert(Alert.AlertType.ERROR, "Login Failed", ex.getMessage());
            }
        });

        layout.getChildren().addAll(userLabel, userField, passLabel, passField, submit);
        stage.setScene(new Scene(layout, 300, 250));
        stage.setTitle("Login");
        stage.initOwner(parent);
    }

    public Stage getStage() {
        return stage;
    }
}
