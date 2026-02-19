package com.innertrack.dao;

import com.innertrack.model.UserSettings;
import com.innertrack.util.DBConnection;
import java.sql.*;

public class UserSettingsDao {
    private final Connection connection;

    public UserSettingsDao() {
        this.connection = DBConnection.getInstance().getConnection();
    }

    public UserSettings findByUserId(int userId) {
        String sql = "SELECT * FROM user_settings WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new UserSettings(
                            rs.getInt("user_id"),
                            rs.getString("theme"),
                            rs.getString("font_size"),
                            rs.getString("language"));
                }
            }
        } catch (SQLException e) {
            System.err.println("UserSettingsDao findByUserId error: " + e.getMessage());
        }
        return null;
    }

    public boolean create(UserSettings settings) {
        String sql = "INSERT INTO user_settings (user_id, theme, font_size, language) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, settings.getUserId());
            stmt.setString(2, settings.getTheme());
            stmt.setString(3, settings.getFontSize());
            stmt.setString(4, settings.getLanguage());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("UserSettingsDao create error: " + e.getMessage());
        }
        return false;
    }

    public boolean update(UserSettings settings) {
        String sql = "UPDATE user_settings SET theme = ?, font_size = ?, language = ? WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, settings.getTheme());
            stmt.setString(2, settings.getFontSize());
            stmt.setString(3, settings.getLanguage());
            stmt.setInt(4, settings.getUserId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("UserSettingsDao update error: " + e.getMessage());
        }
        return false;
    }
}
