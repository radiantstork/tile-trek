// Flashing walls (1-second timer).

package org.example.mazeproject;

public class FlashingWallsEffect implements Effect {
    @Override
    public void applyEffect(GameState state) {
        boolean isInvisible;

        isInvisible = state.isInvisibleWalls();
        if (isInvisible) {
            state.setInvisibleWalls(false);
        }

        state.setFlashingWalls(true);
    }

    @Override
    public void cancelEffect(GameState state) {
        state.setFlashingWalls(false);
    }

    @Override
    public String getEffectName() {
        return "FlashingWalls";
    }
}
