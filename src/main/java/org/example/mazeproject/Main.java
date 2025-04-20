package org.example.mazeproject;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    private static final int SIZE = 15;
    private static final int ROWS = 30;
    private static final int COLS = 30;

    private MazeGenerator mazeGen;
    private Player player;
    private int[] start, end;
    private Scene scene;
    private Group maze;
    private Tile[][] grid;

    @Override
    public void start(Stage stage) {
        GameService gameService;

        gameService = new GameService(20);
        MusicPlayer.playMusic();
        gameService.startGame(stage, 20, 20);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
