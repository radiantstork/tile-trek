// GameService: API with operations such as running the game, restarting the level, handling effects, etc.

package org.example.mazeproject;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.*;

public class GameService {
    private int size;
    private int rows, cols;

    private Timeline flashingWallsTimeline;
    private boolean wallsVisible = true;

    private int stepCount;
    private final List<Integer> sortedTileCounts = new ArrayList<>();

    private Player player;
    private int[] start, end;
    private Tile[][] grid;
    private Stage stage;
    private Group mazeGroup;
    private Scene scene;
    private final GameState state = GameState.getInstance();

    public GameService() {
    }

    // calls the methods for generating/rendering the maze, setting up player controls, etc.
    public void startGame(Stage stage, int rows, int cols, int size) {
        this.rows = rows;
        this.cols = cols;
        this.stage = stage;
        this.size = size;

        state.reset();

        generateMaze();

        setupScene();
        setupInput();

        stage.setScene(scene);
        stage.setTitle("Tile Trek");
        stage.show();

        toggleMusic();
    }

    // ----- HELPER METHODS -----

    // restarts the game with a new level of the same dimensions.
    private void restartNewLevel() {
        System.out.println("Restarting the game.");
        state.reset();
        generateMaze();
        setupScene();
        setupInput();
        stage.setScene(scene);
        stage.show();
    }

    // restarts the current level.
    private void restartCurrentLevel() {
        System.out.println("Restarting the current level.");
        player.move(start[0], start[1]);
        mazeGroup = MazeRenderer.render(grid, start[0], start[1], end[0], end[1], rows, cols, size, player);
        scene.setRoot(mazeGroup);
    }

    private void toggleMusic() {
        boolean musicState;

        musicState = !state.isMusicActive();
        state.setMusic(musicState);

        if (musicState) {
            MusicPlayer.playMusic();
        } else {
            MusicPlayer.stopMusic();
        }
    }

    // generates the maze, gets the grid/start/end/player attributes.
    private void generateMaze() {
        MazeGenerator mazeGen = new MazeGenerator(rows, cols);
        mazeGen.generateMaze();

        grid = mazeGen.getGrid();
        start = mazeGen.getStart();
        end = mazeGen.getEnd();

        player = new Player(start[0], start[1]);
    }

    // renders the maze.
    private void setupScene() {
        mazeGroup = MazeRenderer.render(grid, start[0], start[1], end[0], end[1], rows, cols, size, player);
        scene = new Scene(mazeGroup, cols * size, rows * size);
    }

    // sets up player controls.
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
                case C:
                    restartCurrentLevel();
                    break;
                case M:
                    toggleMusic();
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

    // checks if the position is legal.
    private boolean inBounds(int row, int col) {
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }

    // checks if there's a wall where the player wants to move.
    private boolean hasWall(Tile tile, Direction dir) {
        return switch (dir) {
            case UP -> tile.hasTopWall();
            case DOWN -> tile.hasBottomWall();
            case LEFT -> tile.hasLeftWall();
            case RIGHT -> tile.hasRightWall();
        };
    }

    // handles effects.
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
                        stopFlashingWalls();
                    }
                    System.out.println("InvisibleWalls toggled");
                }
                case "FlashingWalls" -> {
                    if (state.isFlashingWalls()) {
                        effect.cancelEffect(state);
                        stopFlashingWalls();
                    } else {
                        effect.applyEffect(state);
                        startFlashingWalls();
                    }
                    System.out.println("FlashingWalls toggled");
                }
            }

            tile.setEffect(null);
        }
    }

    // checks if the player won the game every time they move.
    private void checkWinCondition() {
        ++stepCount;
        if (player.getRow() == end[0] && player.getCol() == end[1]) {
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

    // ----- HELPER METHODS FOR flashingWallsEffect ------

    private void startFlashingWalls() {
        stopFlashingWalls();

        flashingWallsTimeline = new Timeline(
                new KeyFrame(Duration.seconds(1), _ -> {
                    wallsVisible = !wallsVisible;
                    MazeRenderer.setWallsVisible(wallsVisible);
                    mazeGroup = MazeRenderer.render(grid, start[0], start[1], end[0], end[1], rows, cols, size, player);
                    scene.setRoot(mazeGroup);
                })
        );

        flashingWallsTimeline.setCycleCount(Timeline.INDEFINITE);
        flashingWallsTimeline.play();
    }

    private void stopFlashingWalls() {
        if (flashingWallsTimeline != null) {
            flashingWallsTimeline.stop();
        }

        wallsVisible = true;
        MazeRenderer.setWallsVisible(true);
    }

}
