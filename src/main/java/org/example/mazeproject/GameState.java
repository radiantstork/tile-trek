// GameState: singleton pattern which tracks information about the game (active effects).
// TODO: transfer the other properties, such as size/rows/cols?

package org.example.mazeproject;

public class GameState {
    private static GameState instance;
    private boolean invertedMovement = false;
    private boolean invisibleWalls = false;
    private boolean vignette = false;
    private boolean flashingWalls = false;
    private boolean musicActive = false;
    private Palette palette = Palette.DARK;

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

    public boolean isMusicActive() {
        return musicActive;
    }

    public Palette getPalette() {
        return palette;
    }

    public void setInvertedMovement(boolean inverted) {
        this.invertedMovement = inverted;
    }

    public void setInvisibleWalls(boolean invisible) {
        this.invisibleWalls = invisible;
    }

    public void setFlashingWalls(boolean flashing) {
        this.flashingWalls = flashing;
    }

    public void setVignette(boolean vignette) {
        this.vignette = vignette;
    }

    public void setPalette(Palette palette) {
        this.palette = palette;
    }

    public void setMusic(boolean music) {
        this.musicActive = music;
    }

    public void reset() {
        this.invertedMovement = false;
        this.invisibleWalls = false;
        this.flashingWalls = false;
        this.vignette = false;
    }
}
