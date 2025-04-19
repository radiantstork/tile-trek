package org.example.mazeproject;

public class Player {
    private int row, col;

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

    public void move(int newRow, int newCol) {
        this.row = newRow;
        this.col = newCol;
    }
}
