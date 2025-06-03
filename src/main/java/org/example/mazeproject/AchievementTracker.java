package org.example.mazeproject;

import java.util.List;
import java.util.Map;

public class AchievementTracker {
    private final AchievementService achievementService;
    private final UserAchievementService userAchievementService;

    private static final Map<String, Integer> TILE_THRESHOLDS = Map.of(
            "First Steps", 100,
            "Finding Your Path", 500,
            "Maze Runner", 1000,
            "Tireless", 2500,
            "The Explorer", 5000,
            "Traveller", 10000
    );

    private static final Map<String, Integer> EFFECT_THRESHOLDS = Map.of(
            "What's This?", 1,
            "Curious Mind", 10,
            "Mystery Collector", 25,
            "Surprise, Surprise!", 50,
            "How Many More?", 75,
            "Agent Of Chaos", 100
    );

    private static final Map<String, Integer> LEVEL_THRESHOLDS = Map.of(
            "First Time?", 1,
            "Rookie No More", 5,
            "Getting Serious", 10,
            "Veteran", 25,
            "Pathfinder", 50,
            "Addicted", 75,
            "Get A Life", 100
    );

    public AchievementTracker(AchievementService achievementService, UserAchievementService userAchievementService) {
        this.achievementService = achievementService;
        this.userAchievementService = userAchievementService;
    }

    private void checkCategoryAchievements(int userId, String category, int progressValue, boolean alreadyCompleted,
                                           java.util.function.Consumer<Boolean> setCompletedFlag,
                                           Map<String, Integer> thresholds
    ) {
        if (alreadyCompleted) return;

        List<Achievement> achievements = achievementService.getAchievementsByCategory(category);
        boolean allUnlocked = true;

        for (Achievement a : achievements) {
            if (userAchievementService.hasAchievement(userId, a.id())) continue;

            int threshold = thresholds.getOrDefault(a.name(), Integer.MAX_VALUE);
            if (progressValue >= threshold) {
                userAchievementService.unlockAchievement(userId, a.id());

                CommandLogger.log("ACHIEVEMENT_UNLOCKED: " + a.name() + " (" + a.description() + ")");
                System.out.println("ACHIEVEMENT_UNLOCKED: " + a.name() + " (" + a.description() + ")");
            } else {
                allUnlocked = false;
            }
        }

        if (allUnlocked) {
            setCompletedFlag.accept(true);

            CommandLogger.log("ACHIEVEMENT_CATEGORY_COMPLETED: " + category.toUpperCase());
            System.out.println("ACHIEVEMENT_CATEGORY_COMPLETED: " + category.toUpperCase());
        }
    }

    public void checkTileAchievements(int userId, UserStats stats) {
        checkCategoryAchievements(userId, "Tile", stats.getTotalMoves(), stats.isTileAchievementsCompleted(), stats::setTileAchievementsCompleted, TILE_THRESHOLDS);
    }

    public void checkEffectAchievements(int userId, UserStats stats) {
        checkCategoryAchievements(userId, "Effect", stats.getTotalEffectsPickedUp(), stats.isEffectAchievementsCompleted(), stats::setEffectAchievementsCompleted, EFFECT_THRESHOLDS);
    }

    public void checkLevelAchievements(int userId, UserStats stats) {
        checkCategoryAchievements(userId, "Level", stats.getTotalLevelsBeaten(), stats.isLevelAchievementsCompleted(), stats::setLevelAchievementsCompleted, LEVEL_THRESHOLDS);
    }

    public void checkAllAchievements(int userId, UserStats stats) {
        checkTileAchievements(userId, stats);
        checkEffectAchievements(userId, stats);
        checkLevelAchievements(userId, stats);
    }
}

