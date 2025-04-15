package org.example.mazeproject;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        int rows = 15;
        int cols = 15;
        int tileSize = 40;

        MazeGenerator mazeGen = new MazeGenerator();
        mazeGen.generateMaze(rows, cols);

        Tile[][] grid = mazeGen.getGrid();
        int[] start = mazeGen.getStart();
        int[] end = mazeGen.getEnd();

        Group maze = MazeRenderer.render(grid, start[0], start[1], end[0], end[1], rows, cols, tileSize);

        Scene scene = new Scene(maze, rows * tileSize, cols * tileSize);

        stage.setTitle("Maze Game");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
