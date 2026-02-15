package com.innertrack.service;

import com.innertrack.model.TestPsychologique;
import com.innertrack.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TestPsychologiqueCrudService
        implements ICrudService<TestPsychologique> {

    private Connection cnx;

    public TestPsychologiqueCrudService() {
        cnx = DBConnection.getInstance().getConnection();
    }

    // CREATE
    @Override
    public boolean create(TestPsychologique t) {
        String sql = """
            INSERT INTO test_psychologique
            (titre, id_type, description, nombre_questions)
            VALUES (?, ?, ?, ?)
        """;
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setString(1, t.getTitre());
            ps.setInt(2, t.getIdType());
            ps.setString(3, t.getDescription());
            ps.setInt(4, t.getNombreQuestions());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // READ
    @Override
    public TestPsychologique read(int id) {
        String sql = "SELECT * FROM test_psychologique WHERE id_test=?";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new TestPsychologique(
                        rs.getInt("id_test"),
                        rs.getString("titre"),
                        rs.getInt("id_type"),
                        rs.getString("description"),
                        rs.getInt("nombre_questions")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // UPDATE
    @Override
    public boolean update(TestPsychologique t) {
        String sql = """
            UPDATE test_psychologique
            SET titre=?, id_type=?, description=?, nombre_questions=?
            WHERE id_test=?
        """;
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setString(1, t.getTitre());
            ps.setInt(2, t.getIdType());
            ps.setString(3, t.getDescription());
            ps.setInt(4, t.getNombreQuestions());
            ps.setInt(5, t.getIdTest());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // DELETE
    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM test_psychologique WHERE id_test=?";
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
    public List<TestPsychologique> findAll() {
        List<TestPsychologique> list = new ArrayList<>();
        String sql = "SELECT * FROM test_psychologique";

        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new TestPsychologique(
                        rs.getInt("id_test"),
                        rs.getString("titre"),
                        rs.getInt("id_type"),
                        rs.getString("description"),
                        rs.getInt("nombre_questions")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
