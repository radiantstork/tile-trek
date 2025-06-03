package org.example.mazeproject;

public class UserStats {
    private final int userId;
    private int totalMoves;
    private int totalLevelsBeaten;
    private int totalEffectsPickedUp;

    private boolean tileAchievementsCompleted;
    private boolean effectAchievementsCompleted;
    private boolean levelAchievementsCompleted;

    public UserStats(int userId, int totalMoves, int totalLevelsBeaten, int totalEffectsPickedUp,
                     boolean tileAchievementsCompleted, boolean effectAchievementsCompleted,
                     boolean levelAchievementsCompleted) {
        this.userId = userId;
        this.totalMoves = totalMoves;
        this.totalLevelsBeaten = totalLevelsBeaten;
        this.totalEffectsPickedUp = totalEffectsPickedUp;
        this.tileAchievementsCompleted = tileAchievementsCompleted;
        this.effectAchievementsCompleted = effectAchievementsCompleted;
        this.levelAchievementsCompleted = levelAchievementsCompleted;
    }

    public boolean isTileAchievementsCompleted() {
        return tileAchievementsCompleted;
    }

    public void setTileAchievementsCompleted(boolean completed) {
        this.tileAchievementsCompleted = completed;
    }

    public boolean isEffectAchievementsCompleted() {
        return effectAchievementsCompleted;
    }

    public void setEffectAchievementsCompleted(boolean completed) {
        this.effectAchievementsCompleted = completed;
    }

    public boolean isLevelAchievementsCompleted() {
        return levelAchievementsCompleted;
    }

    public void setLevelAchievementsCompleted(boolean completed) {
        this.levelAchievementsCompleted = completed;
    }

    public int getUserId() {
        return userId;
    }

    public int getTotalMoves() {
        return totalMoves;
    }

    public int getTotalLevelsBeaten() {
        return totalLevelsBeaten;
    }

    public int getTotalEffectsPickedUp() {
        return totalEffectsPickedUp;
    }

    public void add(Player player) {
        totalMoves += player.getCountMoves();
        totalLevelsBeaten += player.getLevelsBeaten();
        totalEffectsPickedUp += player.getEffectsPickedUp();
    }
}
