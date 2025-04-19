package org.example.mazeproject;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    private static final int SIZE = 20;
    private static final int ROWS = 10;
    private static final int COLS = 10;

    private MazeGenerator mazeGen;
    private Player player;
    private int[] start, end;
    private Scene scene;
    private Group maze;
    private Tile[][] grid;

    @Override
    public void start(Stage stage) {
        generateMaze();
        setupScene(stage);
        setupMovement();
        stage.show();
    }

    private void generateMaze() {
        mazeGen = new MazeGenerator();
        mazeGen.generateMaze(ROWS, COLS);

        grid = mazeGen.getGrid();
        start = mazeGen.getStart();
        end = mazeGen.getEnd();

        player = new Player(start[0], start[1]);
    }

    private void setupScene(Stage stage) {
        maze = MazeRenderer.render(grid, start[0], start[1], end[0], end[1], ROWS, COLS, SIZE, player);
        scene = new Scene(maze, ROWS * SIZE, COLS * SIZE);
        stage.setScene(scene);
        stage.setTitle("Maze Game");
    }

    private void setupMovement() {
        GameState state;

        state = GameState.getInstance();

//        state.setInvertedMovement(true); // can comment this

        scene.setOnKeyPressed(ev -> {
            int row, col;
            Tile tile;
            Direction dir;

            row = player.getRow();
            col = player.getCol();
            tile = grid[row][col];

            dir = switch (ev.getCode()) {
                case UP -> (state.isInvertedMovement()) ? Direction.DOWN : Direction.UP;
                case DOWN -> (state.isInvertedMovement()) ? Direction.UP : Direction.DOWN;
                case LEFT -> (state.isInvertedMovement()) ? Direction.RIGHT : Direction.LEFT;
                case RIGHT -> (state.isInvertedMovement()) ? Direction.LEFT : Direction.RIGHT;
                default -> null;
            };

            if (dir != null) {
                int newRow, newCol;

                newRow = row + dir.row;
                newCol = col + dir.col;

                if (inBounds(newRow, newCol) && !hasWall(tile, dir)) {
                    player.move(newRow, newCol);
                }
            }

            maze = MazeRenderer.render(grid, start[0], start[1], end[0], end[1], ROWS, COLS, SIZE, player);
            scene.setRoot(maze);
            checkWinCondition(player, end);
        });
    }

    private boolean inBounds(int row, int col) {
        return row >= 0 && row < ROWS && col >= 0 && col < COLS;
    }

    private boolean hasWall(Tile tile, Direction dir) {
        return switch (dir) {
            case UP -> tile.hasTopWall();
            case DOWN -> tile.hasBottomWall();
            case LEFT -> tile.hasLeftWall();
            case RIGHT -> tile.hasRightWall();
        };
    }

    private void checkWinCondition(Player player, int[] end) {
        if (player.getRow() == end[0] && player.getCol() == end[1]) {
            System.out.println("You won!");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
