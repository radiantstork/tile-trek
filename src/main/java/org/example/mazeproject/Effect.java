package org.example.mazeproject;

public interface Effect {
    void applyEffect(GameState state);

    void cancelEffect(GameState state);

    String getEffectName();
}
