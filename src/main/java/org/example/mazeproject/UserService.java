package org.example.mazeproject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserService {
    private static UserService instance;

    private UserService() {}

    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    public void create(User user) {
        String sql = "INSERT INTO users(username, password) VALUES (?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            CommandLogger.error("Failed to create user: " + user.getUsername(), e);
        }
    }

    public User getById(int id) {
        String sql = "SELECT * FROM users WHERE id=?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(rs.getInt("id"), rs.getString("username"), rs.getString("password"));
            }
        } catch (SQLException e) {
            CommandLogger.error("Failed to get user by ID: " + id, e);
        }
        return null;
    }

    public User getByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username=?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(rs.getInt("id"), rs.getString("username"), rs.getString("password"));
            }
        } catch (SQLException e) {
            CommandLogger.error("Failed to get user by username: " + username, e);
        }
        return null;
    }

    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                users.add(new User(rs.getInt("id"), rs.getString("username"), rs.getString("password")));
            }
        } catch (SQLException e) {
            CommandLogger.error("Failed to get all users", e);
        }
        return users;
    }

    public void update(int userId, String newUsername, String newPassword) {
        String sql = "UPDATE users SET username=?, password=? WHERE id=?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newUsername);
            pstmt.setString(2, newPassword);
            pstmt.setInt(3, userId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            CommandLogger.error("Failed to update user: " + userId, e);
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM users WHERE id=?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            CommandLogger.error("Failed to delete user: " + id, e);
        }
    }
}
