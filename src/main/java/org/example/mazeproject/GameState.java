package org.example.mazeproject;

public class GameState {
    private static GameState instance;
    private boolean invertedMovement = false;

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

    public void setInvertedMovement(boolean inverted) {
        this.invertedMovement = inverted;
    }

    public void reset() {
        this.invertedMovement = false;
    }
}
