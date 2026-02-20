package com.innertrack.service;

import com.innertrack.util.DBConnection;
import com.innertrack.model.Habitude;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HabitudeService implements ICrudService<Habitude> {
    private Connection connection;

    public HabitudeService() {
        connection = DBConnection.getInstance().getConnection();
    }

    @Override
    public void create(Habitude habitude) throws SQLException {
        String sql = "INSERT INTO habittracker (nom_habitude, emotion_dominantes, note_textuelle, " +
                "niveau_energie, niveau_stress, qualite_sommeil, date_creation, id, id_journal) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, habitude.getNomHabitude());
            ps.setString(2, habitude.getEmotionDominantes());
            ps.setString(3, habitude.getNoteTextuelle());
            ps.setInt(4, habitude.getNiveauEnergie());
            ps.setInt(5, habitude.getNiveauStress());
            ps.setInt(6, habitude.getQualiteSommeil());
            ps.setDate(7, java.sql.Date.valueOf(habitude.getDateCreation()));
            ps.setInt(8, habitude.getIdUser());

            if (habitude.getIdJournal() != null) {
                ps.setInt(9, habitude.getIdJournal());
            } else {
                ps.setNull(9, java.sql.Types.INTEGER);
            }

            ps.executeUpdate();
        }
    }

    @Override
    public Habitude read(int id) throws SQLException {
        String sql = "select * from habittracker where id_habit = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Habitude h = new Habitude();
                    h.setIdHabit(rs.getInt("id_habit"));
                    h.setNomHabitude(rs.getString("nom_habitude"));
                    h.setEmotionDominantes(rs.getString("emotion_dominantes"));
                    h.setNoteTextuelle(rs.getString("note_textuelle"));
                    h.setNiveauEnergie(rs.getInt("niveau_energie"));
                    h.setNiveauStress(rs.getInt("niveau_stress"));
                    h.setQualiteSommeil(rs.getInt("qualite_sommeil"));
                    h.setDateCreation(rs.getDate("date_creation").toLocalDate());
                    h.setIdUser(rs.getInt("id"));

                    int idJournal = rs.getInt("id_journal");
                    if (!rs.wasNull()) {
                        h.setIdJournal(idJournal);
                    }
                    return h;
                }
            }
        }
        return null;
    }

    @Override
    public void update(Habitude habitude) throws SQLException {
        String sql = "update habittracker set nom_habitude = ?, emotion_dominantes = ?, " +
                "note_textuelle = ?, niveau_energie = ?, niveau_stress = ?, " +
                "qualite_sommeil = ?, date_creation = ? where id_habit = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, habitude.getNomHabitude());
            ps.setString(2, habitude.getEmotionDominantes());
            ps.setString(3, habitude.getNoteTextuelle());
            ps.setInt(4, habitude.getNiveauEnergie());
            ps.setInt(5, habitude.getNiveauStress());
            ps.setInt(6, habitude.getQualiteSommeil());
            ps.setDate(7, Date.valueOf(habitude.getDateCreation()));
            ps.setInt(8, habitude.getIdHabit());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "delete from habittracker where id_habit = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    @Override
    public List<Habitude> findAll() throws SQLException {
        String sql = "select * from habittracker";
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            List<Habitude> habitudes = new ArrayList<>();
            while (rs.next()) {
                Habitude h = new Habitude();
                h.setIdHabit(rs.getInt("id_habit"));
                h.setNomHabitude(rs.getString("nom_habitude"));
                h.setEmotionDominantes(rs.getString("emotion_dominantes"));
                h.setNoteTextuelle(rs.getString("note_textuelle"));
                h.setNiveauEnergie(rs.getInt("niveau_energie"));
                h.setNiveauStress(rs.getInt("niveau_stress"));
                h.setQualiteSommeil(rs.getInt("qualite_sommeil"));
                h.setDateCreation(rs.getDate("date_creation").toLocalDate());
                h.setIdUser(rs.getInt("id"));

                int idJournal = rs.getInt("id_journal");
                if (!rs.wasNull()) {
                    h.setIdJournal(idJournal);
                }

                habitudes.add(h);
            }
            return habitudes;
        }
    }

    public List<Habitude> search(String keyword) throws SQLException {
        String sql = "SELECT * FROM habittracker WHERE nom_habitude LIKE ? " +
                "OR emotion_dominantes LIKE ? " +
                "OR note_textuelle LIKE ?";

        List<Habitude> habitudes = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            String pattern = "%" + keyword + "%";
            ps.setString(1, pattern);
            ps.setString(2, pattern);
            ps.setString(3, pattern);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Habitude h = new Habitude();
                h.setIdHabit(rs.getInt("id_habit"));
                h.setNomHabitude(rs.getString("nom_habitude"));
                h.setEmotionDominantes(rs.getString("emotion_dominantes"));
                h.setNoteTextuelle(rs.getString("note_textuelle"));
                h.setNiveauEnergie(rs.getInt("niveau_energie"));
                h.setNiveauStress(rs.getInt("niveau_stress"));
                h.setQualiteSommeil(rs.getInt("qualite_sommeil"));
                h.setDateCreation(rs.getDate("date_creation").toLocalDate());
                h.setIdUser(rs.getInt("id"));

                int idJournal = rs.getInt("id_journal");
                if (!rs.wasNull()) {
                    h.setIdJournal(idJournal);
                }

                habitudes.add(h);
            }
        }
        return habitudes;
    }
}