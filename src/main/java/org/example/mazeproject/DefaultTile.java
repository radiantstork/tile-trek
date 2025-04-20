package org.example.mazeproject;

public class DefaultTile extends Tile {
    public DefaultTile() {
        super();
    }

    @Override
    public boolean hasEffect() {
        return false;
    }

    @Override
    public Effect getEffect() {
        return null;
    }

    @Override
    public void setEffect(Effect effect) {
    }
}
