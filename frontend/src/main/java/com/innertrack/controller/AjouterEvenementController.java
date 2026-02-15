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
    private Text error;
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


    public void setEventToEdit(Event selected) {

        this.eventToEdit = selected;   // store selected event

        title_input.setText(selected.getTitre());
        description_input.setText(selected.getDescription());
        date_input.setValue(selected.getDateEvent());
        type_combobox.setValue(selected.getTypeEvent().name());
        capacity_input.setText(String.valueOf(selected.getCapacite()));
    }
}