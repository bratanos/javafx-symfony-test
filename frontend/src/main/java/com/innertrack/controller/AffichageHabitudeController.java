package com.innertrack.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import com.innertrack.model.Habitude;
import com.innertrack.service.*;

import javafx.geometry.Insets;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

public class AffichageHabitudeController {

    @FXML
    private TableView<Habitude> habitudeTable;

    @FXML
    private TableColumn<Habitude, String> nomColumn;

    @FXML
    private TableColumn<Habitude, String> emotionColumn;

    @FXML
    private TableColumn<Habitude, String> noteColumn;

    @FXML
    private TableColumn<Habitude, Integer> energieColumn;

    @FXML
    private TableColumn<Habitude, Integer> stressColumn;

    @FXML
    private TableColumn<Habitude, Integer> sommeilColumn;

    @FXML
    private TableColumn<Habitude, LocalDate> dateColumn;

    private HabitudeService habitudeService;

    @FXML
    public void initialize() {
        habitudeService = new HabitudeService();
        habitudeTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        chargerHabitudes();
    }

    private void chargerHabitudes() {
        try {
            habitudeTable.setItems(FXCollections.observableArrayList(habitudeService.findAll()));
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Erreur lors du chargement : " + e.getMessage());
            alert.show();
        }
    }

    @FXML
    void ajouterNouvelleHabitude(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AjoutHabitude.fxml"));
        Parent root = loader.load();
        habitudeTable.getScene().setRoot(root);
    }

    @FXML
    void supprimerHabitude(ActionEvent event) {
        Habitude selected = habitudeTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Attention");
            alert.setContentText("Veuillez sélectionner une habitude à supprimer !");
            alert.show();
            return;
        }

        try {
            habitudeService.delete(selected.getIdHabit());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setContentText("Habitude supprimée avec succès !");
            alert.show();
            chargerHabitudes();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Erreur lors de la suppression : " + e.getMessage());
            alert.show();
        }
    }

    @FXML
    void modifierHabitude(ActionEvent event) {
        Habitude selected = habitudeTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Attention");
            alert.setContentText("Veuillez sélectionner une habitude à modifier !");
            alert.show();
            return;
        }

        Dialog<Habitude> dialog = new Dialog<>();
        dialog.setTitle("Modifier l'Habitude");
        dialog.setHeaderText("Modifiez les informations de l'habitude");

        ButtonType sauvegarderButtonType = new ButtonType("Sauvegarder", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(sauvegarderButtonType, ButtonType.CANCEL);

        // Champs du formulaire
        TextField nomField = new TextField(selected.getNomHabitude());

        // ComboBox pour l'émotion (cohérent avec AjoutHabitude)
        ComboBox<String> emotionCombo = new ComboBox<>();
        emotionCombo.getItems().addAll(
                "Joie", "Serenite", "Motivation", "Calme",
                "Energie", "Detente", "Concentration", "Colere");
        emotionCombo.setValue(selected.getEmotionDominantes());
        emotionCombo.setPrefWidth(300);

        TextArea noteArea = new TextArea(selected.getNoteTextuelle());
        noteArea.setWrapText(true);
        noteArea.setPrefRowCount(3);

        // Spinners avec les bons max (cohérents avec les sliders d'ajout)
        Spinner<Integer> energieSpinner = new Spinner<>(0, 20, selected.getNiveauEnergie());
        energieSpinner.setEditable(true);

        Spinner<Integer> stressSpinner = new Spinner<>(0, 20, selected.getNiveauStress());
        stressSpinner.setEditable(true);

        Spinner<Integer> sommeilSpinner = new Spinner<>(0, 10, selected.getQualiteSommeil());
        sommeilSpinner.setEditable(true);

        // Layout
        VBox vbox = new VBox(8);
        vbox.setPadding(new Insets(20));
        vbox.setPrefWidth(380);
        vbox.getChildren().addAll(
                new Label("Nom :"), nomField,
                new Label("Émotion dominante :"), emotionCombo,
                new Label("Note :"), noteArea,
                new Label("Énergie (0-20) :"), energieSpinner,
                new Label("Stress (0-20) :"), stressSpinner,
                new Label("Qualité Sommeil (0-10) :"), sommeilSpinner);
        dialog.getDialogPane().setContent(vbox);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == sauvegarderButtonType) {
                selected.setNomHabitude(nomField.getText());
                selected.setEmotionDominantes(emotionCombo.getValue());
                selected.setNoteTextuelle(noteArea.getText());
                selected.setNiveauEnergie(energieSpinner.getValue());
                selected.setNiveauStress(stressSpinner.getValue());
                selected.setQualiteSommeil(sommeilSpinner.getValue());
                return selected;
            }
            return null;
        });

        dialog.showAndWait().ifPresent(habitude -> {
            try {
                habitudeService.update(habitude);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succès");
                alert.setContentText("Habitude modifiée avec succès !");
                alert.show();
                chargerHabitudes();
            } catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setContentText("Erreur : " + e.getMessage());
                alert.show();
            }
        });
    }

    @FXML
    void allerAuJournal(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AjoutJournal.fxml"));
        Parent root = loader.load();
        habitudeTable.getScene().setRoot(root);
    }
}