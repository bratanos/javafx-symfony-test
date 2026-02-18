package com.innertrack.service;

import com.innertrack.model.User;
import com.innertrack.util.DBConnection;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserService implements ICrudService<User> {
    private final Connection connection;
    private final Gson gson = new Gson();

    public UserService() {
        this.connection = DBConnection.getInstance().getConnection();
    }

    @Override
    public void create(User user) throws SQLException {
        String query = "INSERT INTO user (email, password, roles, is_verified, status) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query,
                Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, gson.toJson(user.getRoles()));
            preparedStatement.setBoolean(4, user.isVerified());
            preparedStatement.setString(5, user.getStatus());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        user.setId(generatedKeys.getInt(1));
                    }
                }
            }
        }
    }

    @Override
    public User read(int id) throws SQLException {
        String query = "SELECT * FROM user WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToUser(resultSet);
                }
            }
        }
        return null;
    }

    @Override
    public void update(User user) throws SQLException {
        String query = "UPDATE user SET email = ?, password = ?, roles = ?, is_verified = ?, status = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, gson.toJson(user.getRoles()));
            preparedStatement.setBoolean(4, user.isVerified());
            preparedStatement.setString(5, user.getStatus());
            preparedStatement.setInt(6, user.getId());

            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String query = "DELETE FROM user WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public List<User> findAll() throws SQLException {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM user";
        try (Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                users.add(mapResultSetToUser(resultSet));
            }
        }
        return users;
    }

    private User mapResultSetToUser(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getInt("id"));
        user.setEmail(resultSet.getString("email"));
        user.setPassword(resultSet.getString("password"));
        user.setVerified(resultSet.getBoolean("is_verified"));
        user.setStatus(resultSet.getString("status"));

        String rolesJson = resultSet.getString("roles");
        List<String> roles = gson.fromJson(rolesJson, new TypeToken<List<String>>() {
        }.getType());
        user.setRoles(roles);

        return user;
    }
}
