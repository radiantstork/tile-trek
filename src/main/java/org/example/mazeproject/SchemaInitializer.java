package org.example.mazeproject;

import java.sql.*;

public class SchemaInitializer {
    public static void initialize() {
        createUsersTable();
        createUserStatsTable();
        createAchievementsTable();
        createUserAchievementsTable();
    }

    private static void executeSql(String sql, String tableName) {
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            CommandLogger.error("Couldn't create table: " + tableName, e);
        }
    }

    private static void createUsersTable() {
        executeSql("""
                    CREATE TABLE IF NOT EXISTS users (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        username TEXT UNIQUE NOT NULL,
                        password TEXT NOT NULL
                    );
                """, "users");
    }

    private static void createUserStatsTable() {
        executeSql("""
                    CREATE TABLE IF NOT EXISTS user_stats (
                        user_id INTEGER PRIMARY KEY,
                        total_moves INTEGER DEFAULT 0,
                        total_levels_beaten INTEGER DEFAULT 0,
                        total_effects_picked INTEGER DEFAULT 0,
                        tile_achievements_completed BOOLEAN DEFAULT FALSE,
                        effect_achievements_completed BOOLEAN DEFAULT FALSE,
                        level_achievements_completed BOOLEAN DEFAULT FALSE,
                        FOREIGN KEY(user_id) REFERENCES users(id)
                    );
                """, "user_stats");
    }

    private static void createAchievementsTable() {
        try (Connection conn = DatabaseManager.getConnection()) {
            conn.createStatement().execute("""
                        CREATE TABLE IF NOT EXISTS achievements (
                            id INTEGER PRIMARY KEY AUTOINCREMENT,
                            name TEXT UNIQUE NOT NULL,
                            description TEXT NOT NULL,
                            category TEXT NOT NULL
                        );
                    """);

            boolean seed = false;

            try (var stmt = conn.createStatement();
                 var rs = stmt.executeQuery("SELECT COUNT(*) FROM achievements")) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    if (count == 0) {
                        seed = true;
                    } else {
                        CommandLogger.log("ACHIEVEMENTS_ALREADY_SEEDED");
                    }
                }
            }

            if (seed) {
                AchievementService.getInstance().seedDefaultAchievements();
            }

        } catch (SQLException e) {
            CommandLogger.error("Couldn't create achievements table or seed data", e);
        }
    }

    private static void createUserAchievementsTable() {
        executeSql("""
                    CREATE TABLE IF NOT EXISTS user_achievements (
                        user_id INTEGER,
                        achievement_id INTEGER,
                        PRIMARY KEY (user_id, achievement_id),
                        FOREIGN KEY (user_id) REFERENCES users(id),
                        FOREIGN KEY (achievement_id) REFERENCES achievements(id)
                    );
                """, "user_achievements");
    }
}

