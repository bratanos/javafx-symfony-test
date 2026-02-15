package com.innertrack.service;
import com.innertrack.model.Inscription;
import com.innertrack.util.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

    public class InscriptionService {

        private final Connection connection;

        public InscriptionService() {
            connection = DBConnection.getInstance().getConnection();
        }

        // 🔹 Ajouter inscription
        public void ajouter(Inscription inscription) throws SQLException {
            String sql = """
        INSERT INTO inscription (id_evenement, date_inscription, email_participant, nom_participant)
        VALUES (?, ?, ?, ?)
    """;

            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, inscription.getIdEvent());
                ps.setDate(2, Date.valueOf(inscription.getDateInscription()));
                ps.setString(3, inscription.getEmailParticipant());
                ps.setString(4, inscription.getNomParticipant());
                ps.executeUpdate();
            }
        }

        // 🔹 Recuperer toutes les inscriptions
        public List<Inscription> recuperer() throws SQLException {

            List<Inscription> list = new ArrayList<>();

            String sql = """
                    SELECT i.id_inscription,
                           i.id_evenement,
                           i.nom_participant,
                           i.email_participant,
                           i.date_inscription,
                           e.titre AS titre_event
                    FROM inscription i
                    JOIN event e ON e.id_event = i.id_evenement
                """;

            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {

                Inscription i = new Inscription(
                        rs.getInt("id_inscription"),
                        rs.getInt("id_evenement"),
                        rs.getString("nom_participant"),
                        rs.getString("email_participant"),
                        rs.getDate("date_inscription").toLocalDate(),
                        rs.getString("titre_event")
                );


                list.add(i);
            }

            return list;
        }

        // 🔹 Supprimer inscription
        public void supprimer(int id) throws SQLException {

            String sql = "DELETE FROM inscription WHERE id_inscription = ?";

            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        }

        public void participer(int idEvent, String nom, String email) throws SQLException {
            String sql = """
        INSERT INTO inscription (id_evenement, date_inscription, email_participant, nom_participant)
        VALUES (?, ?, ?, ?)
    """;

            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, idEvent);
                ps.setDate(2, Date.valueOf(LocalDate.now()));
                ps.setString(3, email);
                ps.setString(4, nom);
                ps.executeUpdate();
            }
        }

    }

