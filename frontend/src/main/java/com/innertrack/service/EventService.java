package com.innertrack.service;

import com.innertrack.model.Event;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.innertrack.model.TypeEvent;

public class EventService {

    private Connection connection;

    public EventService() {
        connection = com.innertrack.util.DBConnection.getInstance().getConnection();
    }

    // ✅ AJOUTER

    public void ajouter(Event event) throws SQLException {

        String sql = "INSERT INTO event (titre, description, date_event, type_event, date_creation, capacite, statut) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement ps = connection.prepareStatement(sql);

        ps.setString(1, event.getTitre());
        ps.setString(2, event.getDescription());
        ps.setDate(3, Date.valueOf(event.getDateEvent()));
        ps.setString(4, event.getTypeEvent().name());
        ps.setDate(5, Date.valueOf(event.getDateCreation()));
        ps.setInt(6, event.getCapacite());
        ps.setBoolean(7, event.isStatut());

        ps.executeUpdate();
    }

    // ✅ MODIFIER
    public void modifier(Event event) throws SQLException {

        String sql = "UPDATE event SET titre=?, description=?, date_event=?, type_event=?, capacite=?, statut=? WHERE id_event=?";

        PreparedStatement ps = connection.prepareStatement(sql);

        ps.setString(1, event.getTitre());
        ps.setString(2, event.getDescription());
        ps.setDate(3, java.sql.Date.valueOf(event.getDateEvent()));
        ps.setString(4, event.getTypeEvent().name());
        ps.setInt(5, event.getCapacite());
        ps.setBoolean(6, event.isStatus());
        ps.setInt(7, event.getIdEvent());

        ps.executeUpdate();
    }

    // ✅ SUPPRIMER

    public void supprimer(int id) throws SQLException {

        String sql = "DELETE FROM event WHERE id_event=?";

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, id);

        ps.executeUpdate();
    }

    // ✅ RECUPERER
    public List<Event> recuperer() throws SQLException {

        String sql = "SELECT * FROM event";

        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);

        List<Event> events = new ArrayList<>();

        while (rs.next()) {

            Event e = new Event();

            e.setIdEvent(rs.getInt("id_event"));
            e.setTitre(rs.getString("titre"));
            e.setDescription(rs.getString("description"));
            e.setDateEvent(rs.getDate("date_event").toLocalDate());
            // ✅ Safe enum conversion
            String typeStr = rs.getString("type_event");
            TypeEvent type = typeStr == null ? null : TypeEvent.valueOf(typeStr.toUpperCase());
            e.setTypeEvent(type);            e.setDateCreation(rs.getDate("date_creation").toLocalDate());
            e.setCapacite(rs.getInt("capacite"));
            e.setStatut(rs.getBoolean("statut"));

            events.add(e);
        }

        return events;
    }


}
