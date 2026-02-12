package com.innertrack.dao;

import com.innertrack.util.DBConnection;
import java.sql.*;
import java.time.LocalDateTime;

public class PasswordResetDao {
    private final Connection connection = DBConnection.getInstance().getConnection();

    public boolean create(int userId, String code) {
        String sql = "INSERT INTO password_reset_codes (user_id, code, expires_at) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, code);
            stmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now().plusMinutes(10)));
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int findUserIdByValidCode(String email, String code) {
        String sql = "SELECT pr.user_id FROM password_reset_codes pr " +
                "JOIN user u ON pr.user_id = u.id " +
                "WHERE u.email = ? AND pr.code = ? AND pr.expires_at > ? AND pr.used_at IS NULL";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, code);
            stmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("user_id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public boolean markAsUsed(int userId, String code) {
        String sql = "UPDATE password_reset_codes SET used_at = ? WHERE user_id = ? AND code = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setInt(2, userId);
            stmt.setString(3, code);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
