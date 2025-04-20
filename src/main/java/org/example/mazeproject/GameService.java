package org.example.mazeproject;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.*;

public class GameService {
    private final int size;
    private int rows, cols;

    private int stepCount;
    private List<Integer> sortedTileCounts = new ArrayList<>();

    private MazeGenerator mazeGen;
    private Player player;
    private int[] start, end;
    private Tile[][] grid;
    private Stage stage;
    private Group mazeGroup;
    private Scene scene;
    private final GameState state = GameState.getInstance();

    public GameService(int size) {
        this.size = size;
    }

    public void startGame(Stage stage, int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.stage = stage;

        state.reset();

        generateMaze();

        setupScene();
        setupInput();

        stage.setScene(scene);
        stage.setTitle("Tile Trek");
        stage.show();
    }

    private void restartNewLevel() {
        System.out.println("Restarting the game.");
        state.reset();
        generateMaze();
        setupScene();
        setupInput();
        stage.setScene(scene);
        stage.show();
    }

    private void generateMaze() {
        mazeGen = new MazeGenerator(rows, cols);
        mazeGen.generateMaze();

        grid = mazeGen.getGrid();
        start = mazeGen.getStart();
        end = mazeGen.getEnd();

        player = new Player(start[0], start[1]);
    }

    private void setupScene() {
        mazeGroup = MazeRenderer.render(grid, start[0], start[1], end[0], end[1], rows, cols, size, player);
        scene = new Scene(mazeGroup, cols * size, rows * size);
    }

    private void setupInput() {
        scene.setOnKeyPressed(ev -> {
            int row, col;
            Tile tile;
            Direction dir = null;

            row = player.getRow();
            col = player.getCol();
            tile = grid[row][col];

            switch (ev.getCode()) {
                case UP:
                    dir = state.isInvertedMovement() ? Direction.DOWN : Direction.UP;
                    break;
                case DOWN:
                    dir = state.isInvertedMovement() ? Direction.UP : Direction.DOWN;
                    break;
                case LEFT:
                    dir = state.isInvertedMovement() ? Direction.RIGHT : Direction.LEFT;
                    break;
                case RIGHT:
                    dir = state.isInvertedMovement() ? Direction.LEFT : Direction.RIGHT;
                    break;
                case R:
                    restartNewLevel();
                    break;
                default:
                    break;
            }

            if (dir != null) {
                int newRow, newCol;

                newRow = row + dir.row;
                newCol = col + dir.col;

                if (inBounds(newRow, newCol) && !hasWall(tile, dir)) {
                    player.move(newRow, newCol);
                    tile = grid[newRow][newCol];
                    handleEffect(tile);
                }
            }

            mazeGroup = MazeRenderer.render(grid, start[0], start[1], end[0], end[1], rows, cols, size, player);
            scene.setRoot(mazeGroup);
            checkWinCondition();
        });
    }

    private boolean inBounds(int row, int col) {
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }

    private boolean hasWall(Tile tile, Direction dir) {
        return switch (dir) {
            case UP -> tile.hasTopWall();
            case DOWN -> tile.hasBottomWall();
            case LEFT -> tile.hasLeftWall();
            case RIGHT -> tile.hasRightWall();
        };
    }

    private void handleEffect(Tile tile) {
        if (tile.hasEffect()) {
            Effect effect;

            effect = tile.getEffect();

            switch (effect.getEffectName()) {
                case "Vignette" -> {
                    if ((state.isVignette())) {
                        effect.cancelEffect(state);
                    } else {
                        effect.applyEffect(state);
                    }
                    System.out.println("Vignette toggled");
                }
                case "InvertedMovement" -> {
                    if (state.isInvertedMovement()) {
                        effect.cancelEffect(state);
                    } else {
                        effect.applyEffect(state);
                    }
                    System.out.println("InvertedMovement toggled");
                }
                case "InvisibleWalls" -> {
                    if (state.isInvisibleWalls()) {
                        effect.cancelEffect(state);
                    } else {
                        effect.applyEffect(state);
                    }
                    System.out.println("InvisibleWalls toggled");
                }
            }

            tile.setEffect(null);
        }
    }

    private void checkWinCondition() {
        ++stepCount;
        if (player.getRow() == end[0] && player.getCol() == end[1]) {
            int uniqueTileCount;

            System.out.println("You won!");
            restartNewLevel();

            sortedTileCounts.add(stepCount);
            sortedTileCounts.sort(Collections.reverseOrder());
            stepCount = 0;

            for (Integer count : sortedTileCounts) {
                System.out.print(count + " ");
            }
            System.out.println();
        }
    }
}
