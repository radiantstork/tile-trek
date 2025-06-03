package org.example.mazeproject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserAchievementService {
    private static UserAchievementService instance;

    private UserAchievementService() {
    }

    public static UserAchievementService getInstance() {
        if (instance == null) {
            instance = new UserAchievementService();
        }
        return instance;
    }

    public void unlockAchievement(int userId, int achievementId) {
        String sql = "INSERT OR IGNORE INTO user_achievements(user_id, achievement_id) VALUES (?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, achievementId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            CommandLogger.error("Error unlocking achievement", e);
        }
    }

    public boolean hasAchievement(int userId, int achievementId) {
        String sql = "SELECT 1 FROM user_achievements WHERE user_id = ? AND achievement_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, achievementId);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            CommandLogger.error("Error checking achievement", e);
        }
        return false;
    }

    public List<Integer> getUnlockedAchievementIds(int userId) {
        List<Integer> unlocked = new ArrayList<>();
        String sql = "SELECT achievement_id FROM user_achievements WHERE user_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                unlocked.add(rs.getInt("achievement_id"));
            }
        } catch (SQLException e) {
            CommandLogger.error("Error checking unlocked achievements", e);
        }
        return unlocked;
    }

    public void update(int userId, int oldAchievementId, int newAchievementId) {
        String sql = "UPDATE user_achievements SET achievement_id = ? WHERE user_id = ? AND achievement_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, newAchievementId);
            pstmt.setInt(2, userId);
            pstmt.setInt(3, oldAchievementId);
            pstmt.executeUpdate();

            CommandLogger.log("UPDATED_USER_ACHIEVEMENT");
            System.out.println("UPDATED_USER_ACHIEVEMENT");
        } catch (SQLException e) {
            CommandLogger.error("Error updating user achievement", e);
        }
    }

    public void delete(int userId, int achievementId) {
        String sql = "DELETE FROM user_achievements WHERE user_id = ? AND achievement_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, achievementId);
            pstmt.executeUpdate();

            CommandLogger.log("DELETED_USER_ACHIEVEMENT");
            System.out.println("DELETED_USER_ACHIEVEMENT");
        } catch (SQLException e) {
            CommandLogger.error("Error deleting user achievement", e);
        }
    }
}
