// GameState: singleton pattern which tracks information about the game (active effects).
// TODO: transfer the other properties, such as size/rows/cols?

package org.example.mazeproject;

public class GameState {
    private static GameState instance;

    private boolean invertedMovement;
    private boolean invisibleWalls;
    private boolean vignette;
    private boolean flashingWalls;

    private boolean musicActive;
    private int trackIndex;

    private Palette palette;

    private GameState() {
        this.invertedMovement = false;
        this.invisibleWalls = false;
        this.vignette = false;
        this.flashingWalls = false;

        this.musicActive = false;
        this.trackIndex = MusicPlayer.getTrackCount() - 1; // it will increment, modulo trackCount and start from 0

        this.palette = Palette.CLASSIC;
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

    public int getTrackIndex() {
        return trackIndex;
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

    public void setTrackIndex(int trackIndex) {
        this.trackIndex = trackIndex;
    }

    public void reset() {
        this.invertedMovement = false;
        this.invisibleWalls = false;
        this.flashingWalls = false;
        this.vignette = false;
    }
}
