// Used for rendering the maze.
// 1. it draws the start/end points
// 2. it draws the walls for each tile (unless the walls are invisible)
// 3. it draws the question marks (if the tiles have any)
// 4. it draws the player

package org.example.mazeproject;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class MazeRenderer {
    private static boolean wallsVisible = true;

    // main method
    public static Group render(Tile[][] grid, int startX, int startY, int endX, int endY,
                               int rows, int cols, int size, Player player) {
        Group group;
        GameState state;
        Palette palette;
        Color tileColor, wallColor, playerColor;

        group = new Group();
        state = GameState.getInstance();

        palette = state.getPalette();
        tileColor = palette.getTileColor();
        wallColor = palette.getWallColor();
        playerColor = palette.getPlayerColor();

        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {
                drawBackground(group, i, j, startX, startY, endX, endY, size, tileColor);

                if (!state.isInvisibleWalls() && (!state.isFlashingWalls() || wallsVisible)) {
                    drawWalls(group, grid[i][j], j * size, i * size, size, wallColor);
                }

                if (grid[i][j].hasEffect()) {
                    drawQuestionMark(group, i, j, size);
                }
            }
        }
        drawPlayer(group, player, size, playerColor);

        if (state.isVignette()) {
            int playerX, playerY;
            double radius;
            double fadeDistance;
            RadialGradient fogGradient;
            Rectangle overlay;

            playerX = player.getCol() * size + size / 2;
            playerY = player.getRow() * size + size / 2;
            radius = size * 2.5;
            fadeDistance = size * 1.5;

            fogGradient = new RadialGradient(
                    0, 0,
                    playerX, playerY,
                    radius + fadeDistance,
                    false,
                    CycleMethod.NO_CYCLE,
                    new Stop(0, Color.TRANSPARENT),
                    new Stop(radius / (radius + fadeDistance), Color.color(0, 0, 0, 0.6)),
                    new Stop(1.0, Color.color(0, 0, 0, 0.95))
            );

            overlay = new Rectangle(cols * size, rows * size);
            overlay.setFill(fogGradient);
            group.getChildren().add(overlay);
        }

        return group;
    }

    // ---------- HELPERS ----------

    public static void setWallsVisible(boolean visible) {
        wallsVisible = visible;
    }

    // color the backgrounds of start and end points
    private static void drawBackground(Group group, int row, int col, int startX, int startY, int endX, int endY, int size, Color tileColor) {
        Rectangle background;

        background = new Rectangle(col * size, row * size, size, size);

        if ((row == startX) && (col == startY)) {
            background.setFill(Color.LIGHTGREEN);
        } else if ((row == endX) && (col == endY)) {
            background.setFill(Color.INDIANRED);
        } else {
            background.setFill(tileColor);
        }

        group.getChildren().add(background);
    }

    // draw the walls of a tile
    private static void drawWalls(Group group, Tile tile, int x, int y, int size, Color wallColor) {
        if (tile.hasTopWall()) {
            Line top = new Line(x, y, x + size, y);
            top.setStroke(wallColor);
            group.getChildren().add(top);
        }
        if (tile.hasRightWall()) {
            Line right = new Line(x + size, y, x + size, y + size);
            right.setStroke(wallColor);
            group.getChildren().add(right);
        }
        if (tile.hasBottomWall()) {
            Line bottom = new Line(x, y + size, x + size, y + size);
            bottom.setStroke(wallColor);
            group.getChildren().add(bottom);
        }
        if (tile.hasLeftWall()) {
            Line left = new Line(x, y, x, y + size);
            left.setStroke(wallColor);
            group.getChildren().add(left);
        }
    }

    private static void drawPlayer(Group group, Player player, int size, Color playerColor) {
        int row, col;
        double centerX, centerY, radius;
        Circle circle;

        row = player.getRow();
        col = player.getCol();
        centerX = col * size + size / 2.0;
        centerY = row * size + size / 2.0;
        radius = size * 0.3;

        circle = new Circle(centerX, centerY, radius);
        circle.setFill(playerColor);

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
