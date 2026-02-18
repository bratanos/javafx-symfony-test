package com.innertrack.service;

import com.innertrack.model.EntreeJournal;
import com.innertrack.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JournalService implements ICrudService<EntreeJournal> {
    private Connection connection;

    public JournalService() {
        connection = DBConnection.getInstance().getConnection();
    }

    @Override
    public void create(EntreeJournal entree) throws SQLException {
        String sql = "INSERT INTO journal (humeur, note_textuelle, date_saisie, id_user) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, entree.getHumeur());
            ps.setString(2, entree.getNoteTextuelle());
            ps.setDate(3, Date.valueOf(entree.getDateSaisie()));
            ps.setInt(4, entree.getIdUser());
            ps.executeUpdate();
        }
    }

    @Override
    public EntreeJournal read(int id) throws SQLException {
        String sql = "SELECT * FROM journal WHERE id_journal = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new EntreeJournal(
                            rs.getInt("id_journal"),
                            rs.getInt("humeur"),
                            rs.getString("note_textuelle"),
                            rs.getDate("date_saisie").toLocalDate(),
                            rs.getInt("id_user"));
                }
            }
        }
        return null;
    }

    @Override
    public void update(EntreeJournal entree) throws SQLException {
        String sql = "UPDATE journal SET humeur = ?, note_textuelle = ?, date_saisie = ? WHERE id_journal = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, entree.getHumeur());
            ps.setString(2, entree.getNoteTextuelle());
            ps.setDate(3, Date.valueOf(entree.getDateSaisie()));
            ps.setInt(4, entree.getIdJournal());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM journal WHERE id_journal = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    @Override
    public List<EntreeJournal> findAll() throws SQLException {
        String sql = "SELECT * FROM journal";
        List<EntreeJournal> entrees = new ArrayList<>();
        try (Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                EntreeJournal e = new EntreeJournal(
                        rs.getInt("id_journal"),
                        rs.getInt("humeur"),
                        rs.getString("note_textuelle"),
                        rs.getDate("date_saisie").toLocalDate(),
                        rs.getInt("id_user"));
                entrees.add(e);
            }
        }
        return entrees;
    }
}
