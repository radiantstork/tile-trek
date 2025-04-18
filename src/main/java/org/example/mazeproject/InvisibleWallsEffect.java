package org.example.mazeproject;

public class InvisibleWallsEffect implements Effect {
    @Override
    public void applyEffect(GameState state) {
        state.setInvisibleWalls(true);
    }

    @Override
    public void cancelEffect(GameState state) {
        state.setInvisibleWalls(false);
    }

    @Override
    public String getEffectName() {
        return "InvisibleWalls";
    }
}
