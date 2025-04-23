package org.example.mazeproject;

public class GameState {
    private static GameState instance;
    private boolean invertedMovement = false;
    private boolean invisibleWalls = false;
    private boolean vignette = false;
    private boolean flashingWalls = false;

    private GameState() {
    }

    public static GameState getInstance() {
        if (instance == null) {
            instance = new GameState();
        }
        return instance;
    }

    public boolean isInvertedMovement() {
        return invertedMovement;
    }

    public boolean isInvisibleWalls() {
        return invisibleWalls;
    }

    public boolean isVignette() {
        return vignette;
    }

    public boolean isFlashingWalls() {
        return flashingWalls;
    }

    public void setInvertedMovement(boolean inverted) {
        this.invertedMovement = inverted;
    }

    public void setInvisibleWalls(boolean invisible) {
        this.invisibleWalls = invisible;
    }

    public void setFlashingWalls(boolean flashing) { this.flashingWalls = flashing; }

    public void setVignette(boolean vignette) {
        this.vignette = vignette;
    }

    public void reset() {
        this.invertedMovement = false;
        this.invisibleWalls = false;
        this.flashingWalls = false;
        this.vignette = false;
    }
}
