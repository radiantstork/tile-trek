package org.example.mazeproject;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.function.Consumer;

public class LoginScreen {
    private Consumer<User> onLoginSuccess;
    private Stage loginDialog;
    private Stage registerDialog;
    private Stage parentStage;

    public void setOnLoginSuccess(Consumer<User> onLoginSuccess) {
        this.onLoginSuccess = onLoginSuccess;
    }

    public void show(Stage stage) {
        this.parentStage = stage;

        Button loginBtn = new Button("Login");
        Button registerBtn = new Button("Register");

        loginBtn.setFont(Font.font("Comic Sans MS", 20));
        registerBtn.setFont(Font.font("Comic Sans MS", 20));

        VBox layout = new VBox(20, loginBtn, registerBtn);
        layout.setAlignment(javafx.geometry.Pos.CENTER);

        stage.setScene(new Scene(layout, 300, 200));
        stage.setTitle("Login/Register");
        stage.show();

        loginBtn.setOnAction(_ -> openLoginDialog());
        registerBtn.setOnAction(_ -> openRegisterDialog());
    }

    private void openLoginDialog() {
        CommandLogger.log("RENDER_LOGIN");
        System.out.println("RENDER_LOGIN");

        if (registerDialog != null && registerDialog.isShowing()) registerDialog.close();
        if (loginDialog != null && loginDialog.isShowing()) loginDialog.close();

        loginDialog = new LoginDialog(parentStage, onLoginSuccess).getStage();
        loginDialog.show();
    }

    private void openRegisterDialog() {
        CommandLogger.log("RENDER_REGISTER");
        System.out.println("RENDER_REGISTER");

        if (loginDialog != null && loginDialog.isShowing()) loginDialog.close();
        if (registerDialog != null && registerDialog.isShowing()) registerDialog.close();

        registerDialog = new RegisterDialog(parentStage, onLoginSuccess).getStage();
        registerDialog.show();
    }
}
