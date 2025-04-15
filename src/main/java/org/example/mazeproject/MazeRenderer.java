// Used for rendering the maze, given a Tile-matrix representation.

package org.example.mazeproject;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

public class MazeRenderer {
    // main method
    public static Group render(Tile[][] grid, int startX, int startY, int endX, int endY,
                               int rows, int cols, int size) {
        Group group = new Group();

        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {
                drawBackground(group, i, j, startX, startY, endX, endY, size);
                drawWalls(group, grid[i][j], j * size, i * size, size);
            }
        }
        return group;
    }

    // ---------- HELPERS ----------

    // color the backgrounds of start and end points
    private static void drawBackground(Group group, int row, int col, int startX, int startY, int endX, int endY, int size) {
        Rectangle background = null;
        if ((row == startX) && (col == startY)) {
            background = new Rectangle(row * size, col * size, size, size);
            background.setFill(Color.LIGHTGREEN);
        } else if ((row == endX) && (col == endY)) {
            background = new Rectangle(row * size, col * size, size, size);
            background.setFill(Color.INDIANRED);
        }
        if (background != null) group.getChildren().add(background);
    }

    // draw the walls of a tile
    private static void drawWalls(Group group, Tile tile, int x, int y, int size) {
        if (tile.hasTopWall()) {
            Line top = new Line(x, y, x + size, y);
            group.getChildren().add(top);
        }
        if (tile.hasRightWall()) {
            Line right = new Line(x + size, y, x + size, y + size);
            group.getChildren().add(right);
        }
        if (tile.hasBottomWall()) {
            Line bottom = new Line(x, y + size, x + size, y + size);
            group.getChildren().add(bottom);
        }
        if (tile.hasLeftWall()) {
            Line left = new Line(x, y, x, y + size);
            group.getChildren().add(left);
        }
    }
}
