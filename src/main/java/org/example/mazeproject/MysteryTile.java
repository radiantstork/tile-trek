package org.example.mazeproject;

public class MysteryTile extends Tile {
    private Effect effect;

    public MysteryTile(Effect effect) {
        super();
        this.effect = effect;
    }

    @Override
    public boolean hasEffect() {
        return effect != null;
    }

    @Override
    public Effect getEffect() {
        return effect;
    }

    @Override
    public void setEffect(Effect effect) {
        this.effect = effect;
    }
}
