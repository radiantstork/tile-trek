package org.example.mazeproject;

import javafx.scene.paint.Color;

public enum Palette {
    // (tileColor, wallColor, playerColor)
    CLASSIC(Color.WHITE, Color.BLACK, Color.BLACK),
    DARK(Color.BLACK, Color.WHITE, Color.WHITE);

    private final Color tileColor;
    private final Color wallColor;
    private final Color playerColor;

    Palette(Color tileColor, Color wallColor, Color playerColor) {
        this.tileColor = tileColor;
        this.wallColor = wallColor;
        this.playerColor = playerColor;
    }

    public Color getTileColor() {
        return tileColor;
    }

    public Color getWallColor() {
        return wallColor;
    }

    public Color getPlayerColor() {
        return playerColor;
    }
}
