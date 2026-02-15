package com.innertrack.controller;

import com.innertrack.model.Event;
import com.innertrack.model.TypeEvent;
import com.innertrack.service.EventService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.SQLException;

public class ModifierEvenementController {

    private final EventService eventService = new EventService();

    private Event eventToEdit; // ← receives selected event

    @FXML private TextField title_input;
    @FXML private TextArea description_input;
    @FXML private DatePicker date_input;
    @FXML private ComboBox<String> type_combobox;
    @FXML private TextField capacity_input;
    @FXML private Text error;

    @FXML
    void initialize() {

        ObservableList<String> types = FXCollections.observableArrayList(
                "CONFERENCE",
                "ATELIER",
                "FORUM",
                "WEBINAIRE"
        );
        type_combobox.setItems(types);
    }

    // =========================================================
    // RECEIVE EVENT FROM TABLE
    // =========================================================
    public void setEvent(Event e) {
        this.eventToEdit = e;

        // fill fields
        title_input.setText(e.getTitre());
        description_input.setText(e.getDescription());
        date_input.setValue(e.getDateEvent());
        type_combobox.setValue(e.getTypeEvent().name());
        capacity_input.setText(String.valueOf(e.getCapacite()));
    }

    // =========================================================
    // UPDATE EVENT
    // =========================================================
    @FXML
    void modifier_event(ActionEvent event) throws SQLException {

        if (!validateForm()) return;

        eventToEdit.setTitre(title_input.getText());
        eventToEdit.setDescription(description_input.getText());
        eventToEdit.setDateEvent(date_input.getValue());
        eventToEdit.setTypeEvent(TypeEvent.valueOf(type_combobox.getValue()));
        eventToEdit.setCapacite(Integer.parseInt(capacity_input.getText()));

        eventService.modifier(eventToEdit);

        goBack();
    }

    private void goBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AfficherEvenement.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) title_input.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Liste Evenements");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean validateForm() {

        if (title_input.getText().isEmpty() ||
                description_input.getText().isEmpty() ||
                date_input.getValue() == null ||
                type_combobox.getValue() == null ||
                capacity_input.getText().isEmpty()) {

            error.setText("All fields must be filled");
            error.setVisible(true);
            return false;
        }

        try {
            int capacity = Integer.parseInt(capacity_input.getText());
            if (capacity <= 0) {
                error.setText("Capacity must be positive");
                error.setVisible(true);
                return false;
            }
        } catch (NumberFormatException e) {
            error.setText("Capacity must be a number");
            error.setVisible(true);
            return false;
        }

        return true;
    }
}