package com.innertrack.service;

import com.google.protobuf.Service;
import com.innertrack.util.DBConnection;
import model.Habitude;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HabitudeService implements ICrudService<Habitude> {
    private Connection connection;

    public HabitudeService() {
        connection = DBConnection.getInstance().getConnection();
    }

    public void ajouter(Habitude habitude) throws SQLException {
        String sql = "INSERT INTO habittracker (nom_habitude, emotion_dominantes, note_textuelle, " +
                "niveau_energie, niveau_stress, qualite_sommeil, date_creation, id_user, id_journal) " +
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

    public void modifier(Habitude habitude) throws SQLException {
        String sql = "update habittracker set nom_habitude = ?, emotion_dominantes = ?, " +
                "note_textuelle = ?, niveau_energie = ?, niveau_stress = ?, " +
                "qualite_sommeil = ?, date_creation = ? where id_habit = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, habitude.getNomHabitude());
        preparedStatement.setString(2, habitude.getEmotionDominantes());
        preparedStatement.setString(3, habitude.getNoteTextuelle());
        preparedStatement.setInt(4, habitude.getNiveauEnergie());
        preparedStatement.setInt(5, habitude.getNiveauStress());
        preparedStatement.setInt(6, habitude.getQualiteSommeil());
        preparedStatement.setDate(7, Date.valueOf(habitude.getDateCreation()));
        preparedStatement.setInt(8, habitude.getIdHabit());
        preparedStatement.executeUpdate();
    }

    public void supprimer(Habitude habitude) throws SQLException {
        String sql = "delete from habittracker where id_habit = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, habitude.getIdHabit());
        preparedStatement.executeUpdate();
    }

    public List<Habitude> recuperer() throws SQLException {
        String sql = "select * from habittracker";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
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
            h.setIdUser(rs.getInt("id_user"));

            int idJournal = rs.getInt("id_journal");
            if (!rs.wasNull()) {
                h.setIdJournal(idJournal);
            }

            habitudes.add(h);
        }
        return habitudes;
    }


}
