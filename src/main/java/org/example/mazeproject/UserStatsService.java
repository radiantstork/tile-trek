package org.example.mazeproject;

import java.sql.*;

public class UserStatsService {
    private static UserStatsService instance;

    private UserStatsService() {
    }

    public static UserStatsService getInstance() {
        if (instance == null) {
            instance = new UserStatsService();
        }
        return instance;
    }

    public boolean checkIfStatsExist(int userId) {
        String sql = "SELECT 1 FROM user_stats WHERE user_id=?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            CommandLogger.error("Error checking if stats exist", e);
            return false;
        }
    }

    public void createForUser(int userId) {
        String sql = "INSERT OR IGNORE INTO user_stats(user_id) VALUES(?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            CommandLogger.error("Error creating user stats", e);
        }
    }

    public UserStats getStats(int userId) {
        String sql = "SELECT * FROM user_stats WHERE user_id=?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new UserStats(
                        userId,
                        rs.getInt("total_moves"),
                        rs.getInt("total_levels_beaten"),
                        rs.getInt("total_effects_picked"),
                        rs.getBoolean("tile_achievements_completed"),
                        rs.getBoolean("effect_achievements_completed"),
                        rs.getBoolean("level_achievements_completed")
                );
            }
        } catch (SQLException e) {
            CommandLogger.error("Error getting stats", e);
        }
        // fallback if user not found
        return new UserStats(userId, 0, 0, 0, false, false, false);
    }

    public void updateStats(UserStats stats) {
        String sql = """
                    UPDATE user_stats SET
                        total_moves=?,
                        total_levels_beaten=?,
                        total_effects_picked=?,
                        tile_achievements_completed=?,
                        effect_achievements_completed=?,
                        level_achievements_completed=?
                    WHERE user_id=?;
                """;
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, stats.getTotalMoves());
            stmt.setInt(2, stats.getTotalLevelsBeaten());
            stmt.setInt(3, stats.getTotalEffectsPickedUp());
            stmt.setBoolean(4, stats.isTileAchievementsCompleted());
            stmt.setBoolean(5, stats.isEffectAchievementsCompleted());
            stmt.setBoolean(6, stats.isLevelAchievementsCompleted());
            stmt.setInt(7, stats.getUserId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            CommandLogger.error("Error updating user stats", e);
        }
    }

    public void deleteStats(int userId) {
        String sql = "DELETE FROM user_stats WHERE user_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();

            CommandLogger.log("Deleted stats for user ID: " + userId);
            System.out.println("Deleted stats for user ID: " + userId);
        } catch (SQLException e) {
            CommandLogger.error("Error deleting user stats", e);
        }
    }
}
