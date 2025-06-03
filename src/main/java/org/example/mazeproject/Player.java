package org.example.mazeproject;

public class Player {
    private int row, col;
    private int countMoves = 0;
    private int levelsBeaten = 0;
    private int effectsPickedUp = 0;

    public Player(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getCountMoves() {
        return countMoves;
    }

    public int getLevelsBeaten() {
        return levelsBeaten;
    }

    public int getEffectsPickedUp() {
        return effectsPickedUp;
    }

    public void resetCount() {
        this.countMoves = 0;
    }

    public void incrementLevelsBeaten() {
        levelsBeaten++;
    }

    public void incrementEffectsPickedUp() {
        effectsPickedUp++;
    }

    public void move(int newRow, int newCol) {
        this.row = newRow;
        this.col = newCol;
        this.countMoves++;
    }
}
