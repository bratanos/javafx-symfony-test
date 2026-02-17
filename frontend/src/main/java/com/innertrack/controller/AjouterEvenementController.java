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
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.time.LocalDate;

public class AjouterEvenementController {

    private final EventService eventService = new EventService();

    @FXML
    private TextField title_input;

    @FXML
    private TextArea description_input;

    @FXML
    private DatePicker date_input;

    @FXML
    private ComboBox<String> type_combobox;

    @FXML
    private TextField capacity_input;

    @FXML
    private Label error;
    private Object eventToEdit ;

    @FXML
    void initialize() {
        // Fill combobox with TypeEvent options
        ObservableList<String> typeEventList = FXCollections.observableArrayList(
                "CONFERENCE",
                "ATELIER",
                "FORUM",
                "WEBINAIRE"
        );
        type_combobox.setItems(typeEventList);
    }

    @FXML
    void submit_event(ActionEvent event) throws SQLException {

        if (!validateForm()) return;

        Event ev = new Event(
                0,
                title_input.getText(),
                description_input.getText(),
                date_input.getValue(),
                TypeEvent.valueOf(type_combobox.getValue()),
                LocalDate.now(),
                Integer.parseInt(capacity_input.getText()),
                true
        );

        eventService.ajouter(ev);

        // Navigate back to AfficherEvenement
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AfficherEvenement.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) title_input.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Afficher Evenement");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void reset_input(ActionEvent event) {
        title_input.clear();
        description_input.clear();
        date_input.setValue(null);
        type_combobox.setValue(null);
        capacity_input.clear();
        error.setVisible(false);
    }

    private boolean validateForm() {

        error.setVisible(false);

        // Reset styles
        resetFieldStyles();

        // TITLE
        if (title_input.getText().trim().length() < 3) {
            showError("Title must contain at least 3 characters");
            markInvalid(title_input);
            return false;
        }

        // DESCRIPTION
        if (description_input.getText().trim().length() < 10) {
            showError("Description must contain at least 10 characters");
            markInvalid(description_input);
            return false;
        }

        // DATE
        if (date_input.getValue() == null) {
            showError("Please select a date");
            markInvalid(date_input);
            return false;
        }

        if (date_input.getValue().isBefore(LocalDate.now())) {
            showError("Event date cannot be in the past");
            markInvalid(date_input);
            return false;
        }

        // TYPE
        if (type_combobox.getValue() == null) {
            showError("Please select event type");
            markInvalid(type_combobox);
            return false;
        }

        // CAPACITY
        if (!capacity_input.getText().matches("\\d+")) {
            showError("Capacity must be a number");
            markInvalid(capacity_input);
            return false;
        }

        int capacity = Integer.parseInt(capacity_input.getText());
        if (capacity <= 0) {
            showError("Capacity must be greater than 0");
            markInvalid(capacity_input);
            return false;
        }

        return true;
    }

    private void resetFieldStyles() {
        title_input.setStyle(null);
        description_input.setStyle(null);
        date_input.setStyle(null);
        type_combobox.setStyle(null);
        capacity_input.setStyle(null);
    }
    private void showError(String message) {
        error.setText(message);
        error.setVisible(true);
    }

    private void markInvalid(javafx.scene.control.Control field) {
        field.setStyle("-fx-border-color: red; -fx-border-width: 2;");
    }


    public void setEventToEdit(Event selected) {

        this.eventToEdit = selected;   // store selected event

        title_input.setText(selected.getTitre());
        description_input.setText(selected.getDescription());
        date_input.setValue(selected.getDateEvent());
        type_combobox.setValue(selected.getTypeEvent().name());
        capacity_input.setText(String.valueOf(selected.getCapacite()));
    }
}