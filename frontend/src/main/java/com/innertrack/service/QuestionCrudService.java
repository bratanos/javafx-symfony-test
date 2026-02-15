package com.innertrack.service;

import com.innertrack.model.Question;
import com.innertrack.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionCrudService
        implements ICrudService<Question> {

    private Connection cnx;

    public QuestionCrudService() {
        cnx = DBConnection.getInstance().getConnection();
    }

    // CREATE
    @Override
    public boolean create(Question q) {
        String sql = "INSERT INTO question (id_test, contenu) VALUES (?, ?)";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setInt(1, q.getIdTest());
            ps.setString(2, q.getContenu());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // READ
    @Override
    public Question read(int id) {
        String sql = "SELECT * FROM question WHERE id_question=?";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Question(
                        rs.getInt("id_question"),
                        rs.getInt("id_test"),
                        rs.getString("contenu")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // UPDATE
    @Override
    public boolean update(Question q) {
        String sql = "UPDATE question SET id_test=?, contenu=? WHERE id_question=?";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setInt(1, q.getIdTest());
            ps.setString(2, q.getContenu());
            ps.setInt(3, q.getIdQuestion());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // DELETE
    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM question WHERE id_question=?";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // FIND ALL
    @Override
    public List<Question> findAll() {
        List<Question> list = new ArrayList<>();
        String sql = "SELECT * FROM question";

        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Question(
                        rs.getInt("id_question"),
                        rs.getInt("id_test"),
                        rs.getString("contenu")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Méthodes spécifiques (optionnelles)
    public List<Question> findByTest(int idTest) {
        List<Question> list = new ArrayList<>();
        String sql = "SELECT * FROM question WHERE id_test=? ORDER BY id_question";

        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setInt(1, idTest);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Question(
                        rs.getInt("id_question"),
                        rs.getInt("id_test"),
                        rs.getString("contenu")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
