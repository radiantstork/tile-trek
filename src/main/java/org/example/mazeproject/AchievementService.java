package org.example.mazeproject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AchievementService {
    private static AchievementService instance;

    private AchievementService() {
    }

    public static AchievementService getInstance() {
        if (instance == null) {
            instance = new AchievementService();
        }
        return instance;
    }

    public void seedDefaultAchievements() {
        String[][] defaultAchievements = {
                {"First Steps", "Walk a total of 100 tiles", "Tile"},
                {"Finding Your Path", "Walk a total of 500 tiles", "Tile"},
                {"Maze Runner", "Walk a total of 1000 tiles", "Tile"},
                {"Tireless", "Walk a total of 2500 tiles", "Tile"},
                {"The Explorer", "Walk a total of 5000 tiles", "Tile"},
                {"Traveller", "Walk a total of 10000 tiles", "Tile"},
                {"What's This?", "Pick up a mystery effect", "Effect"},
                {"Curious Mind", "Pick up 10 mystery effects", "Effect"},
                {"Mystery Collector", "Pick up 25 mystery effects", "Effect"},
                {"Surprise, Surprise!", "Pick up 50 mystery effects", "Effect"},
                {"How Many More?", "Pick up 75 mystery effects", "Effect"},
                {"Agent Of Chaos", "Pick up 100 mystery effects", "Effect"},
                {"First Time?", "Beat a level", "Level"},
                {"Rookie No More", "Beat 5 levels", "Level"},
                {"Getting Serious", "Beat 10 levels", "Level"},
                {"Veteran", "Beat 25 levels", "Level"},
                {"Pathfinder", "Beat 50 levels", "Level"},
                {"Addicted", "Beat 75 levels", "Level"},
                {"Get A Life", "Beat 100 levels", "Level"}
        };

        for (String[] data : defaultAchievements) {
            create(data[0], data[1], data[2]);
        }
    }

    public List<Achievement> getAllAchievements() {
        List<Achievement> list = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM achievements")) {

            while (rs.next()) {
                list.add(new Achievement(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("category")
                ));
            }

        } catch (SQLException e) {
            CommandLogger.error("Getting achievements", e);
        }
        return list;
    }

    public List<Achievement> getAchievementsByCategory(String category) {
        return getAllAchievements().stream()
                .filter(a -> a.category().equalsIgnoreCase(category))
                .toList();
    }

    public Achievement getById(int id) {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM achievements WHERE id = ?")) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Achievement(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("category")
                );
            }

        } catch (SQLException e) {
            CommandLogger.error("Getting achievement by ID", e);
        }
        return null;
    }

    public void create(String name, String description, String category) {
        String sql = "INSERT OR IGNORE INTO achievements (name, description, category) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setString(2, description);
            pstmt.setString(3, category);
            pstmt.executeUpdate();

            CommandLogger.log("CREATE_ACHIEVEMENT: " + name);
            System.out.println("CREATE_ACHIEVEMENT: " + name);
        } catch (SQLException e) {
            CommandLogger.error("Error creating achievement", e);
        }
    }

    public void update(String oldName, String newName, String description, String category) {
        String sql = "UPDATE achievements SET name = ?, description = ?, category = ? WHERE name = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newName);
            pstmt.setString(2, description);
            pstmt.setString(3, category);
            pstmt.setString(4, oldName);
            pstmt.executeUpdate();

            CommandLogger.log("UPDATE_ACHIEVEMENT: " + category + " - " + newName + " (" + description + ")");
            System.out.println("UPDATE_ACHIEVEMENT: " + category + " - " + newName + " (" + description + ")");
        } catch (SQLException e) {
            CommandLogger.error("Updating achievement", e);
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM achievements WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();

            CommandLogger.log("DELETE_ACHIEVEMENT: " + id);
            System.out.println("DELETE_ACHIEVEMENT: " + id);
        } catch (SQLException e) {
            CommandLogger.error("Deleting achievement", e);
        }
    }
}
