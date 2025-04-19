package org.example.mazeproject;

public class VignetteEffect implements Effect {
    @Override
    public void applyEffect(GameState state) {
        state.setVignette(true);
    }

    @Override
    public void cancelEffect(GameState state) {
        state.setVignette(false);
    }

    @Override
    public String getEffectName() {
        return "Vignette";
    }
}
