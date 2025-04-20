// Used for rendering the maze.
// 1. it draws the start/end points
// 2. it draws the walls for each tile (unless the walls are invisible)
// 3. it draws the question marks (if the tiles have any)
// 4. it draws the player

package org.example.mazeproject;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class MazeRenderer {
    // main method
    public static Group render(Tile[][] grid, int startX, int startY, int endX, int endY,
                               int rows, int cols, int size, Player player) {
        Group group;
        GameState state;

        group = new Group();
        state = GameState.getInstance();

        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {
                drawBackground(group, i, j, startX, startY, endX, endY, size); // todo: get this out of the loop

                if (!state.isInvisibleWalls()) {
                    drawWalls(group, grid[i][j], j * size, i * size, size);
                }

                if (grid[i][j].hasEffect()) {
                    drawQuestionMark(group, i, j, size);
                }
            }
        }
        drawPlayer(group, player, size);
        return group;
    }

    // ---------- HELPERS ----------

    // color the backgrounds of start and end points
    private static void drawBackground(Group group, int row, int col, int startX, int startY, int endX, int endY, int size) {
        Rectangle background;

        background = null;

        if ((row == startX) && (col == startY)) {
            background = new Rectangle(col * size, row * size, size, size);
            background.setFill(Color.LIGHTGREEN);
        } else if ((row == endX) && (col == endY)) {
            background = new Rectangle(col * size, row * size, size, size);
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

    private static void drawPlayer(Group group, Player player, int size) {
        int row, col;
        double centerX, centerY, radius;
        Circle circle;

        row = player.getRow();
        col = player.getCol();
        centerX = col * size + size / 2.0;
        centerY = row * size + size / 2.0;
        radius = size * 0.3;

        circle = new Circle(centerX, centerY, radius);
        circle.setFill(Color.BLACK);

        group.getChildren().add(circle);
    }

    private static void drawQuestionMark(Group group, int row, int col, int size) {
        Text question;

        question = new Text("?");
        question.setFill(Color.CADETBLUE);
        question.setFont(Font.font(size * 0.7));
        question.setX(col * size + size * 0.3);
        question.setY(row * size + size * 0.7);
        group.getChildren().add(question);
    }
}
