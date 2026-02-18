package com.innertrack.dao;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.innertrack.model.User;
import com.innertrack.service.ICrudService;
import com.innertrack.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao implements ICrudService<User> {
    private final Connection connection;
    private final Gson gson = new Gson();

    public UserDao() {
        this.connection = DBConnection.getInstance().getConnection();
    }

    public UserDao(Connection connection) {
        this.connection = connection;
    }

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

    @Override
    public boolean create(User user) throws SQLException {
        String sql = "INSERT INTO user (email, password, roles, is_verified, status, first_name, last_name, profile_picture) VALUES (?,?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, gson.toJson(user.getRoles()));
            stmt.setBoolean(4, user.isVerified());
            stmt.setString(5, user.getStatus());
            stmt.setString(6, user.getFirstName());
            stmt.setString(7, user.getLastName());
            stmt.setString(8, user.getProfilePicture());
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
        user.setFirstName(rs.getString("first_name"));
        user.setLastName(rs.getString("last_name"));
        user.setProfilePicture(rs.getString("profile_picture"));

        String rolesJson = rs.getString("roles");
        List<String> roles;
        try {
            if (rolesJson != null && rolesJson.startsWith("[")) {
                roles = gson.fromJson(rolesJson, new TypeToken<List<String>>() {
                }.getType());
            } else if (rolesJson != null && !rolesJson.isEmpty()) {
                // If it's a single string, wrap it in a list
                roles = new java.util.ArrayList<>();
                roles.add(rolesJson.replace("\"", "")); // Remove quotes if present
            } else {
                roles = new java.util.ArrayList<>();
            }
        } catch (Exception e) {
            System.err.println("Error parsing roles JSON: " + rolesJson);
            roles = new java.util.ArrayList<>();
            roles.add("ROLE_USER"); // Default fallback
        }
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

    @Override
    public User read(int id) throws SQLException {
        String sql = "SELECT * FROM user WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return map(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("findById error : " + e.getMessage());
            throw e;
        }
        return null;
    }

    @Override
    public List<User> findAll() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM user";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                users.add(map(rs));
            }
        } catch (SQLException e) {
            System.err.println("findAll error : " + e.getMessage());
        }
        return users;
    }

    @Override
    public boolean update(User user) throws SQLException {
        // Generic update for all fields except password (handled separately usually)
        String sql = "UPDATE user SET email = ?, is_verified = ?, status = ?, roles = ?, first_name = ?, last_name = ?, profile_picture = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, user.getEmail());
            stmt.setBoolean(2, user.isVerified());
            stmt.setString(3, user.getStatus());
            stmt.setString(4, gson.toJson(user.getRoles()));
            stmt.setString(5, user.getFirstName());
            stmt.setString(6, user.getLastName());
            stmt.setString(7, user.getProfilePicture());
            stmt.setInt(8, user.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("update error : " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM user WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("delete error : " + e.getMessage());
        }
        return false;
    }
}
