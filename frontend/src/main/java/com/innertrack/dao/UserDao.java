package com.innertrack.dao;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.innertrack.model.User;
import com.innertrack.util.DBConnection;
import java.sql.*;
import java.util.List;

public class UserDao {
    private final Connection connection = DBConnection.getInstance().getConnection();
    private final Gson gson = new Gson();

    public User findByEmail(String email) {
        String sql = "SELECT * FROM user WHERE email = ? ";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next())
                    return map(rs);
            }
        } catch (SQLException e) {
            System.err.println("findByEmail error : " + e.getMessage());
        }
        return null;
    }

    public boolean updateStatus(int userId, String status, boolean isVerified) {
        String sql = "UPDATE user SET status = ?, is_verified = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setBoolean(2, isVerified);
            stmt.setInt(3, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("updateStatus error : " + e.getMessage());
        }
        return false;
    }

    public boolean create(User user) {
        String sql = "INSERT INTO user (email, password, roles, is_verified, status) VALUES (?,?,?,?,?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, gson.toJson(user.getRoles()));
            stmt.setBoolean(4, user.isVerified());
            stmt.setString(5, user.getStatus());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Create user error : " + e.getMessage());
        }
        return false;
    }

    private User map(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setVerified(rs.getBoolean("is_verified"));
        user.setStatus(rs.getString("status"));

        String rolesJson = rs.getString("roles");
        List<String> roles = gson.fromJson(rolesJson, new TypeToken<List<String>>() {
        }.getType());
        user.setRoles(roles);

        return user;
    }

    public boolean updatePassword(int userId, String hashedPassword) {
        String sql = "UPDATE user SET password = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, hashedPassword);
            stmt.setInt(2, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("updatePassword error : " + e.getMessage());
        }
        return false;
    }
}
