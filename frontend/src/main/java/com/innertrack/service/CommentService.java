package com.innertrack.service;

import com.innertrack.model.Comment;
import com.innertrack.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CommentService implements ICrudService<Comment> {
    private final Connection connection = DBConnection.getInstance().getConnection();

    @Override
    public boolean create(Comment comment) {
        String query = "INSERT INTO comments (user, comment) VALUES (?, ?)";
        try(PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, comment.getUser());
            statement.setString(2, comment.getComment());
            return statement.executeUpdate() == 1;

        } catch (SQLException e) {
            System.err.println("Error creating comments: " + e.getMessage());
        }
        return false;
    }

    @Override
    public Comment read(int id) {
        String query = "SELECT * FROM comments WHERE id = ?";
        try(PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();

            return wrap(resultSet);

        } catch (SQLException e) {
            System.err.println("Error reading comment: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean update(Comment comment) {
        String query = "UPDATE comments SET comment = ? WHERE id = ?";
        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, comment.getComment());
            ps.setInt(2, comment.getId());
            return ps.executeUpdate() == 1;

        } catch(SQLException e) {
            System.err.println("Error updating comment: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        String query = "DELETE FROM comments WHERE id = ?";
        try(PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            return statement.executeUpdate() == 1;

        } catch(SQLException e) {
            System.err.println("Error deleting comment: " + e.getMessage());
        }
        return false;
    }

    @Override
    public List<Comment> findAll() {
        String query = "SELECT * FROM comments";
        List<Comment> comments = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {
                comments.add(wrap(resultSet));
            }
        } catch (SQLException e) {
            System.err.println("Error reading comments: " + e.getMessage());
        }
        return comments;
    }

    Comment wrap(ResultSet resultSet) {
        try {
            return new Comment(
                    resultSet.getInt("id"),
                    resultSet.getInt("user"),
                    resultSet.getString("comment"),
                    resultSet.getTimestamp("creation").toLocalDateTime(),
                    resultSet.getBoolean("modified"),
                    resultSet.getInt("likes")
            );
        } catch (SQLException e) {
            System.err.println("Error wrapping comment: " + e.getMessage());
        }
        return null;
    }
}
