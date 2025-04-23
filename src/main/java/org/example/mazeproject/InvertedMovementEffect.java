// InvertedMovementEffect: directions get inverted.

package org.example.mazeproject;

public class InvertedMovementEffect implements Effect {
    @Override
    public void applyEffect(GameState state) {
        state.setInvertedMovement(true);
    }

    @Override
    public void cancelEffect(GameState state) {
        state.setInvertedMovement(false);
    }

    @Override
    public String getEffectName() {
        return "InvertedMovement";
    }
}
