package com.innertrack.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import com.innertrack.model.Habitude;
import com.innertrack.service.HabitudeService;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AjoutHabitudeController implements Initializable {

    @FXML private TextField nomHabitudeField;
    @FXML private ComboBox<String> emotionComboBox;
    @FXML private TextArea noteTextArea;
    @FXML private Slider energieSlider;
    @FXML private Slider stressSlider;
    @FXML private Slider sommeilSlider;
    @FXML private DatePicker datePicker;
    @FXML private Label nomErrorLabel;
    @FXML private Label emotionErrorLabel;
    @FXML private Label dateErrorLabel;
    @FXML private Label energieValueLabel;
    @FXML private Label stressValueLabel;
    @FXML private Label sommeilValueLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        datePicker.setValue(LocalDate.now());
        nomErrorLabel.setVisible(false);
        emotionErrorLabel.setVisible(false);
        dateErrorLabel.setVisible(false);

        // Remplir la ComboBox des émotions
        emotionComboBox.getItems().addAll(
                "Joie", "Serenite", "Motivation", "Calme",
                "Energie", "Detente", "Concentration", "Colere");

        // Affichage dynamique des valeurs des sliders
        energieValueLabel.setText(String.valueOf((int) energieSlider.getValue()));
        energieSlider.valueProperty().addListener((obs, oldVal, newVal) ->
                energieValueLabel.setText(String.valueOf(newVal.intValue())));

        stressValueLabel.setText(String.valueOf((int) stressSlider.getValue()));
        stressSlider.valueProperty().addListener((obs, oldVal, newVal) ->
                stressValueLabel.setText(String.valueOf(newVal.intValue())));

        sommeilValueLabel.setText(String.valueOf((int) sommeilSlider.getValue()));
        sommeilSlider.valueProperty().addListener((obs, oldVal, newVal) ->
                sommeilValueLabel.setText(String.valueOf(newVal.intValue())));
    }

    @FXML
    void ajouterHabitude(ActionEvent event) throws IOException {
        boolean valide = true;

        String nom = nomHabitudeField.getText();
        if (nom == null || nom.trim().isEmpty()) {
            nomErrorLabel.setVisible(true);
            nomHabitudeField.setStyle("-fx-border-color: #e53e3e; -fx-border-width: 2px;");
            valide = false;
        } else {
            nomErrorLabel.setVisible(false);
            nomHabitudeField.setStyle("");
        }

        String emotion = emotionComboBox.getValue();
        if (emotion == null || emotion.trim().isEmpty()) {
            emotionErrorLabel.setVisible(true);
            emotionComboBox.setStyle("-fx-border-color: #e53e3e; -fx-border-width: 2px;");
            valide = false;
        } else {
            emotionErrorLabel.setVisible(false);
            emotionComboBox.setStyle("");
        }

        LocalDate dateChoisie = datePicker.getValue();
        if (dateChoisie == null) {
            dateErrorLabel.setVisible(true);
            datePicker.setStyle("-fx-border-color: #e53e3e; -fx-border-width: 2px;");
            valide = false;
        } else {
            dateErrorLabel.setVisible(false);
            datePicker.setStyle("");
        }

        if (!valide) return;

        HabitudeService habitudeService = new HabitudeService();

        try {
            int idUserParDefaut = 1;
            habitudeService.create(new Habitude(
                    nom.trim(),
                    emotion,
                    noteTextArea.getText().trim(),
                    (int) energieSlider.getValue(),
                    (int) stressSlider.getValue(),
                    (int) sommeilSlider.getValue(),
                    dateChoisie,
                    idUserParDefaut));

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Habitude ajoutée");
            alert.setHeaderText(null);
            alert.setContentText("Habitude ajoutée avec succès !");
            alert.showAndWait();

            naviguerVers("/fxml/AffichageHabitude.fxml");

        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Erreur lors de l'ajout : " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void afficherHabitudes() {
        try {
            naviguerVers("/fxml/AffichageHabitude.fxml");
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Impossible d'ouvrir la liste : " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    void allerAuJournal(ActionEvent event) throws IOException {
        naviguerVers("/fxml/AjoutJournal.fxml");
    }

    private void naviguerVers(String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = loader.load();
        nomHabitudeField.getScene().setRoot(root);
    }
}