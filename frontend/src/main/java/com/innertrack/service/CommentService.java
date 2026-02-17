package com.innertrack.service;

import com.innertrack.model.Comment;
import com.innertrack.util.DBConnection;

import java.sql.*;
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

    public int addComment(Comment comment) {
        String query = "INSERT INTO comments (user, comment) VALUES (?, ?)";
        try(PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, comment.getUser());
            statement.setString(2, comment.getComment());

            if(statement.executeUpdate() >= 1) {
                ResultSet rs = statement.getGeneratedKeys();
                if(rs.next()) {
                    return rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error creating comments: " + e.getMessage());
        }

        return -1;
    }

    public int addReply(Comment comment) {
        String query = "INSERT INTO comments (user, comment, parent) VALUES (?, ?, ?)";
        try(PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, comment.getUser());
            statement.setString(2, comment.getComment());
            statement.setInt(3, comment.getParent());

            if(statement.executeUpdate() >= 1) {
                ResultSet rs = statement.getGeneratedKeys();
                if(rs.next()) {
                    return rs.getInt(1);
                }
            }

        } catch(SQLException e) {
            System.err.println("Error adding comments: " + e.getMessage());
        }
        return -1;
    }

    @Override
    public Comment read(int id) {
        String query = "SELECT c.*, u.email FROM comments c, user u WHERE c.id = ? and c.user = u.id";
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
        String query = "SELECT c.*, u.email FROM comments c, user u where c.user = u.id";
        List<Comment> comments = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                comments.add(wrap(resultSet));
            }
        } catch (SQLException e) {
            System.err.println("Error reading comments: " + e.getMessage());
        }
        return comments;
    }

    public List<Comment> getRaw() {
        String query = "SELECT c.*, u.email FROM comments c, user u WHERE parent is null and c.user = u.id";
        List<Comment> comments = new ArrayList<>();
        try{
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {
                comments.add(wrap(resultSet));
            }
        } catch(SQLException e) {
            System.err.println("Error reading comments: " + e.getMessage());
        }
        return comments;
    }

    public List<Comment> getReplies(Comment comment) {
        String query = "SELECT c.*, u.email FROM comments c, user u WHERE parent = ? and  c.user = u.id";
        List<Comment> comments = new ArrayList<>();
        try{
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, comment.getId());
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {
                comments.add(wrap(resultSet));
            }
        } catch(SQLException e) {
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
                    resultSet.getInt("likes"),
                    resultSet.getString("email")
            );
        } catch (SQLException e) {
            System.err.println("Error wrapping comment: " + e.getMessage());
        }
        return null;
    }
}
