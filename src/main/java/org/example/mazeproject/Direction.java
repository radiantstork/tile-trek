// Direction: the player may move UP/DOWN/LEFT/RIGHT.

package org.example.mazeproject;

public enum Direction {
    UP(-1,0), DOWN(1,0), LEFT(0,-1), RIGHT(0,1);

    public final int row;
    public final int col;

    Direction(int row, int col) {
        this.row = row;
        this.col = col;
    }
}
