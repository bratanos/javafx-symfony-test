package com.innertrack.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import com.innertrack.model.Habitude;
import com.innertrack.service.*;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import javafx.scene.control.Button;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import com.innertrack.util.PdfExporter;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

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

    @FXML
    private Label statusLabel;

    @FXML
    private Label totalHabitudesLabel;

    @FXML
    private Label moyenneEnergieLabel;

    @FXML
    private Label moyenneStressLabel;

    @FXML
    private Label moyenneSommeilLabel;

    @FXML
    private TextField searchField;

    private HabitudeService habitudeService;

    @FXML
    private Button clearButton;


    @FXML
    public void initialize() {
        habitudeService = new HabitudeService();
        clearButton.setVisible(false);
        clearButton.setOnAction(e -> viderRecherche());
        // LIAISON colonnes <-> propriétés du modèle
        nomColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("nomHabitude"));
        emotionColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("emotionDominantes"));
        noteColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("noteTextuelle"));
        energieColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("niveauEnergie"));
        stressColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("niveauStress"));
        sommeilColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("qualiteSommeil"));
        dateColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("dateCreation"));

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            clearButton.setVisible(!newValue.isEmpty());
            rechercherHabitude();
        });

        // Formater la date
        dateColumn.setCellFactory(column -> new javafx.scene.control.TableCell<Habitude, java.time.LocalDate>() {
            private final java.time.format.DateTimeFormatter formatter =
                    java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
            @Override
            protected void updateItem(java.time.LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setText(empty || date == null ? null : formatter.format(date));
            }
        });

        habitudeTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        chargerHabitudes();
    }

    private void chargerHabitudes() {
        try {
            List<Habitude> habitudes = habitudeService.findAll();
            habitudeTable.setItems(FXCollections.observableArrayList(habitudes));

            int total = habitudes.size();
            totalHabitudesLabel.setText(String.valueOf(total));

            if (total > 0) {
                double moyEnergie = habitudes.stream()
                        .mapToInt(Habitude::getNiveauEnergie)
                        .average().orElse(0);
                double moyStress = habitudes.stream()
                        .mapToInt(Habitude::getNiveauStress)
                        .average().orElse(0);
                double moySommeil = habitudes.stream()
                        .mapToInt(Habitude::getQualiteSommeil)
                        .average().orElse(0);

                moyenneEnergieLabel.setText(String.format("%.1f", moyEnergie));
                moyenneStressLabel.setText(String.format("%.1f", moyStress));
                moyenneSommeilLabel.setText(String.format("%.1f", moySommeil));
            } else {
                moyenneEnergieLabel.setText("—");
                moyenneStressLabel.setText("—");
                moyenneSommeilLabel.setText("—");
            }

            statusLabel.setText(total + " habitude(s) chargée(s)");

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors du chargement : " + e.getMessage());
        }
    }

    @FXML
    void ajouterHabitude(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AjoutHabitude.fxml"));
            Parent root = loader.load();
            habitudeTable.getScene().setRoot(root);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir le formulaire : " + e.getMessage());
        }
    }

    @FXML
    void supprimerHabitude(ActionEvent event) {
        Habitude selected = habitudeTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Attention", "Veuillez sélectionner une habitude à supprimer !");
            return;
        }

        try {
            habitudeService.delete(selected.getIdHabit());
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Habitude supprimée avec succès !");
            chargerHabitudes();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la suppression : " + e.getMessage());
        }
    }

    @FXML
    void modifierHabitude(ActionEvent event) {
        Habitude selected = habitudeTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Attention", "Veuillez sélectionner une habitude à modifier !");
            return;
        }

        Dialog<Habitude> dialog = new Dialog<>();
        dialog.setTitle("Modifier l'Habitude");
        dialog.setHeaderText("Modifiez les informations de l'habitude");

        ButtonType sauvegarderButtonType = new ButtonType("Sauvegarder", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(sauvegarderButtonType, ButtonType.CANCEL);

        TextField nomField = new TextField(selected.getNomHabitude());

        ComboBox<String> emotionCombo = new ComboBox<>();
        emotionCombo.getItems().addAll(
                "Joie", "Serenite", "Motivation", "Calme",
                "Energie", "Detente", "Concentration", "Colere");
        emotionCombo.setValue(selected.getEmotionDominantes());
        emotionCombo.setPrefWidth(300);

        TextArea noteArea = new TextArea(selected.getNoteTextuelle());
        noteArea.setWrapText(true);
        noteArea.setPrefRowCount(3);

        Spinner<Integer> energieSpinner = new Spinner<>(0, 20, selected.getNiveauEnergie());
        energieSpinner.setEditable(true);

        Spinner<Integer> stressSpinner = new Spinner<>(0, 20, selected.getNiveauStress());
        stressSpinner.setEditable(true);

        Spinner<Integer> sommeilSpinner = new Spinner<>(0, 10, selected.getQualiteSommeil());
        sommeilSpinner.setEditable(true);

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
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Habitude modifiée avec succès !");
                chargerHabitudes();
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur : " + e.getMessage());
            }
        });
    }

    @FXML
    void allerAuJournal(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AffichageJournal.fxml"));
        Parent root = loader.load();
        habitudeTable.getScene().setRoot(root);
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }

    @FXML
    void rechercherHabitude() {
        try {
            String keyword = searchField.getText();

            if (keyword == null || keyword.isEmpty()) {
                habitudeTable.setItems(FXCollections.observableArrayList(
                        habitudeService.findAll()
                ));
            } else {
                habitudeTable.setItems(FXCollections.observableArrayList(
                        habitudeService.search(keyword)
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void viderRecherche() {
        searchField.clear();
        chargerHabitudes();
    }

    @FXML
    void exporterPDF() {
        try {

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Enregistrer le fichier PDF");

            // filtre pour autoriser seulement PDF
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Fichiers PDF", "*.pdf")
            );

            fileChooser.setInitialFileName("Habitudes.pdf");

            Stage stage = (Stage) habitudeTable.getScene().getWindow();
            File file = fileChooser.showSaveDialog(stage);

            if (file != null) {
                PdfExporter.exportHabitudes(
                        habitudeService.findAll(),
                        file.getAbsolutePath()
                );
                statusLabel.setText("✅ PDF enregistré avec succès !");
            }

        } catch (Exception e) {
            statusLabel.setText("❌ Erreur export PDF");
            e.printStackTrace();
        }
    }
}

