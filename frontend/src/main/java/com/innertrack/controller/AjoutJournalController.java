package com.innertrack.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import com.innertrack.model.EntreeJournal;
import com.innertrack.service.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AjoutJournalController implements Initializable {

    @FXML
    private Slider humeurSlider;

    @FXML
    private TextArea noteTextArea;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Label dateErrorLabel;

    @FXML
    private Label descErrorLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        datePicker.setValue(LocalDate.now());
        dateErrorLabel.setVisible(false);
        descErrorLabel.setVisible(false);
    }

    @FXML
    void ajouterJournal(ActionEvent event) throws IOException {

        boolean valide = true;

        // Validation description
        String description = noteTextArea.getText();
        if (description == null || description.trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Champ manquant");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez entrer une description de votre journee.");
            alert.showAndWait();
            descErrorLabel.setVisible(true);
            noteTextArea.setStyle("-fx-border-color: red; -fx-border-width: 1.5px;");
            valide = false;
        } else {
            descErrorLabel.setVisible(false);
            noteTextArea.setStyle("");
        }

        // Validation date
        LocalDate dateChoisie = datePicker.getValue();
        if (dateChoisie == null) {
            dateErrorLabel.setVisible(true);
            datePicker.setStyle("-fx-border-color: red; -fx-border-width: 1.5px;");
            valide = false;
        } else {
            dateErrorLabel.setVisible(false);
            datePicker.setStyle("");
        }

        // Si un champ est invalide, on stoppe
        if (!valide) {
            return;
        }

        JournalService journalService = new JournalService();

        try {
            int idUserParDefaut = 1;

            journalService.create(new EntreeJournal(
                    (int) humeurSlider.getValue(),
                    description,
                    dateChoisie,
                    idUserParDefaut));

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Journal ajoute");
            alert.setContentText("Journal ajoute avec succes !");
            alert.show();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AffichageJournal.fxml"));
            Parent root = loader.load();
            noteTextArea.getScene().setRoot(root);

        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Erreur lors de l'ajout : " + e.getMessage());
            alert.show();
        }
    }

    @FXML
    void allerAuxHabitudes(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AjoutHabitude.fxml"));
        Parent root = loader.load();
        noteTextArea.getScene().setRoot(root);
    }

    @FXML
    void allerAuJournal(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AffichageJournal.fxml"));
        Parent root = loader.load();
        noteTextArea.getScene().setRoot(root);
    }
}