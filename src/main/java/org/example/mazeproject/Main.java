package org.example.mazeproject;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        GameService gameService;

        gameService = new GameService();
        gameService.startGame(stage, 20, 20, 25);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
