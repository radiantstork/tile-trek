// GameService: API with operations such as running the game, restarting the level, handling effects, etc.

package org.example.mazeproject;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.*;
import java.util.function.Consumer;

public class GameService {
    private int size;
    private int rows, cols;

    private static final Palette[] palettes = Palette.values();

    private Timeline flashingWallsTimeline;
    private boolean wallsVisible = true;

    private final List<Integer> sortedTileCounts = new ArrayList<>();

    private Player player;
    private int[] start, end;
    private Tile[][] grid;
    private Stage stage;
    private Group mazeGroup;
    private Scene scene;
    private final GameState state = GameState.getInstance();

    private Consumer<Player> onLevelComplete;

    public void setOnLevelComplete(Consumer<Player> callback) {
        this.onLevelComplete = callback;
    }

    // calls the methods for generating/rendering the maze, setting up player controls, etc.
    public void startGame(Stage stage, int rows, int cols, int size) {
        this.rows = rows;
        this.cols = cols;
        this.stage = stage;
        this.size = size;

        stage.setTitle("Tile Trek");
        toggleMusic();
        startNewLevel();
    }

    // ----- HELPER METHODS -----

    // restarts the game with a new level of the same dimensions.
    private void startNewLevel() {
        state.reset();
        generateMaze();
        setupScene();
        setupInput();
        stage.setScene(scene);
        stage.show();

        CommandLogger.log("START_LEVEL");
        System.out.println("START_LEVEL");
    }

    // restarts the current level.
    private void restartCurrentLevel() {
        player.move(start[0], start[1]);
        mazeGroup = MazeRenderer.render(grid, start[0], start[1], end[0], end[1], rows, cols, size, player);
        scene.setRoot(mazeGroup);

        CommandLogger.log("RESTART_LEVEL");
        System.out.println("RESTART_LEVEL");
    }

    private void toggleMusic() {
        boolean musicState;

        musicState = !state.isMusicActive();
        state.setMusic(musicState);

        if (musicState) {
            playNextTrack();
        } else {
            MusicPlayer.stopMusic();
        }

        CommandLogger.log("TOGGLE_MUSIC");
        System.out.println("TOGGLE_MUSIC");
    }

    private void playNextTrack() {
        boolean musicState;

        musicState = state.isMusicActive();
        if (musicState) {
            int nextTrackIndex;

            nextTrackIndex = (state.getTrackIndex() + 1) % MusicPlayer.getTrackCount();
            state.setTrackIndex(nextTrackIndex);

            MusicPlayer.stopMusic();
            MusicPlayer.playMusic(nextTrackIndex);

            CommandLogger.log("PLAY_NEXT_TRACK");
            System.out.println("PLAY_NEXT_TRACK");
        }
    }

    private void switchPalette() {
        Palette currentPalette;
        int index;

        currentPalette = state.getPalette();
        index = currentPalette.ordinal();
        state.setPalette(palettes[(index + 1) % palettes.length]);

        CommandLogger.log("SWITCH_PALETTE");
        System.out.println("SWITCH_PALETTE");
    }

    // generates the maze, gets the grid/start/end/player attributes.
    private void generateMaze() {
        MazeGenerator mazeGen = new MazeGenerator(rows, cols);
        mazeGen.generateMaze();

        grid = mazeGen.getGrid();
        start = mazeGen.getStart();
        end = mazeGen.getEnd();

        player = new Player(start[0], start[1]);

        CommandLogger.log("GENERATE_MAZE");
        System.out.println("GENERATE_MAZE");
    }

    // renders the maze.
    private void setupScene() {
        mazeGroup = MazeRenderer.render(grid, start[0], start[1], end[0], end[1], rows, cols, size, player);
        scene = new Scene(mazeGroup, cols * size, rows * size);

        CommandLogger.log("RENDER_MAZE");
        System.out.println("RENDER_MAZE");
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
                    startNewLevel();
                    break;
                case C:
                    restartCurrentLevel();
                    break;
                case M:
                    playNextTrack();
                    break;
                case N:
                    toggleMusic();
                    break;
                case P:
                    switchPalette();
                    break;
                case MINUS:
                    MusicPlayer.decreaseVolume();
                    break;
                case EQUALS:
                    MusicPlayer.increaseVolume();
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

        CommandLogger.log("SET_PLAYER_CONTROLS");
        System.out.println("SET_PLAYER_CONTROLS");
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
            player.incrementEffectsPickedUp();

            switch (effect.getEffectName()) {
                case "Vignette" -> {
                    if ((state.isVignette())) {
                        effect.cancelEffect(state);
                    } else {
                        effect.applyEffect(state);
                    }

                    CommandLogger.log("EFFECT_TOGGLED_VIGNETTE");
                    System.out.println("EFFECT_TOGGLED_VIGNETTE");
                }
                case "InvertedMovement" -> {
                    if (state.isInvertedMovement()) {
                        effect.cancelEffect(state);
                    } else {
                        effect.applyEffect(state);
                    }

                    CommandLogger.log("EFFECT_TOGGLED_INVERTED_MOVEMENT");
                    System.out.println("EFFECT_TOGGLED_INVERTED_MOVEMENT");
                }
                case "InvisibleWalls" -> {
                    if (state.isInvisibleWalls()) {
                        effect.cancelEffect(state);
                    } else {
                        effect.applyEffect(state);
                        stopFlashingWalls();
                    }

                    CommandLogger.log("EFFECT_TOGGLED_INVISIBLE_WALLS");
                    System.out.println("EFFECT_TOGGLED_INVISIBLE_WALLS");
                }
                case "FlashingWalls" -> {
                    if (state.isFlashingWalls()) {
                        effect.cancelEffect(state);
                        stopFlashingWalls();
                    } else {
                        effect.applyEffect(state);
                        startFlashingWalls();
                    }

                    CommandLogger.log("EFFECT_TOGGLED_FLASHING_WALLS");
                    System.out.println("EFFECT_TOGGLED_FLASHING_WALLS");
                }
            }
            tile.setEffect(null);
        }
    }

    // checks if the player won the game every time they move.
    private void checkWinCondition() {
        if (player.getRow() == end[0] && player.getCol() == end[1]) {
            CommandLogger.log("LEVEL_COMPLETED");
            System.out.println("LEVEL_COMPLETED");

            player.incrementLevelsBeaten();

            if (onLevelComplete != null) onLevelComplete.accept(player);

            sortedTileCounts.add(player.getCountMoves());
            sortedTileCounts.sort(Collections.reverseOrder());

            startNewLevel();
            player.resetCount();

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
