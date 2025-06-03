package org.example.mazeproject;

import javafx.application.Application;
import javafx.stage.Stage;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        SchemaInitializer.initialize();

//        achievementCRUD();
//        userCRUD();
//        userStatsCRUD();
//        userAchievementsCRUD();

        LoginScreen loginScreen = new LoginScreen();
        loginScreen.show(stage);

        loginScreen.setOnLoginSuccess(user -> {
            int userId = user.getId();

            UserStatsService statsService = UserStatsService.getInstance();
            UserAchievementService userAchievementService = UserAchievementService.getInstance();
            AchievementService achievementService = AchievementService.getInstance();
            AchievementTracker tracker = new AchievementTracker(achievementService, userAchievementService);

            if (!statsService.checkIfStatsExist(userId)) {
                statsService.createForUser(userId);
            }

            UserStats stats = statsService.getStats(userId);
            AtomicReference<UserStats> statsRef = new AtomicReference<>(stats);

            GameService gameService = new GameService();
            gameService.setOnLevelComplete((Player player) -> {
                UserStats updated = statsRef.get();
                updated.add(player);
                statsService.updateStats(updated);
                tracker.checkAllAchievements(userId, updated);
            });

            gameService.startGame(stage, 20, 20, 25);
        });
    }

    public static void userCRUD() {
        UserService u = UserService.getInstance();

        u.create(new User("test", "test"));
        u.create(new User("mihail", "1234"));

        User user = u.getById(1);
        u.update(user.getId(), "update", user.getPassword());

        List<User> users = u.getAll();
        for (User x : users) {
            System.out.println(x.getUsername() + " " + x.getPassword());
        }

        u.delete(2);

         users = u.getAll();
        for (User x : users) {
            System.out.println(x.getUsername() + " " + x.getPassword());
        }
    }

    public static void userStatsCRUD() {
        UserStatsService s = UserStatsService.getInstance();

        s.createForUser(1);
        s.updateStats(new UserStats(1, 100, 3, 14,
                false, false,
                false));


        UserStats a = s.getStats(1);
        System.out.println(a.getTotalMoves() + ", " + a.getTotalLevelsBeaten() + ", " + a.getTotalEffectsPickedUp());

        s.deleteStats(1);
    }

    public static void achievementCRUD() {
        AchievementService a = AchievementService.getInstance();

        List<Achievement> achievements = a.getAllAchievements();
        for (Achievement achievement : achievements) {
            System.out.println(achievement.id() + " " + achievement.name() + " " + achievement.description());
        }

        a.delete(19);
        a.create("TEST", "TEST", "TEST");

        Achievement toUpdate = a.getById(18);
        a.update("Addicted", "UpdateOperation", toUpdate.description(), toUpdate.category());

        achievements = a.getAchievementsByCategory("LEVels");
        for (Achievement achievement : achievements) {
            System.out.println(achievement.id() + " " + achievement.name() + " " + achievement.description());
        }
    }

    public static void userAchievementsCRUD() {
        UserAchievementService u = UserAchievementService.getInstance();
        u.update(1, 13, 5);
        u.delete(1, 7);

        System.out.println(u.hasAchievement(1, 7));
        System.out.println(u.hasAchievement(1, 13));
        System.out.println(u.hasAchievement(1, 5));
    }

    public static void main(String[] args) {
        launch(args);
    }
}


