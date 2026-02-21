package com.innertrack.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import com.innertrack.model.EntreeJournal;
import com.innertrack.service.JournalService;

import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import com.innertrack.util.PdfExporter;

import javafx.geometry.Insets;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class AffichageJournalController {

    @FXML
    private TableView<EntreeJournal> journalTable;

    @FXML
    private TableColumn<EntreeJournal, Integer> humeurColumn;

    @FXML
    private TableColumn<EntreeJournal, String> noteColumn;

    @FXML
    private TableColumn<EntreeJournal, LocalDate> dateColumn;

    @FXML
    private Label statusLabel;

    @FXML
    private Label totalEntreesLabel;

    @FXML
    private Label moyenneHumeurLabel;

    @FXML
    private TextField searchField;

    @FXML
    private Button clearButton;

    private JournalService journalService;

    @FXML
    public void initialize() {
        journalService = new JournalService();

        clearButton.setVisible(false);
        clearButton.setOnAction(e -> viderRecherche());

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            clearButton.setVisible(!newValue.isEmpty());
            rechercherJournal();
        });

        // ✅ LIAISON colonnes <-> propriétés du modèle
        humeurColumn.setCellValueFactory(new PropertyValueFactory<>("humeur"));
        noteColumn.setCellValueFactory(new PropertyValueFactory<>("noteTextuelle"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateSaisie"));

        // Formater la date en "dd/MM/yyyy"
        dateColumn.setCellFactory(column -> new javafx.scene.control.TableCell<EntreeJournal, LocalDate>() {
            private final java.time.format.DateTimeFormatter formatter =
                    java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");

            @Override
            protected void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (empty || date == null) {
                    setText(null);
                } else {
                    setText(formatter.format(date));
                }
            }
        });

        chargerJournaux();
    }

    private void chargerJournaux() {
        try {
            List<EntreeJournal> entrees = journalService.findAll();
            journalTable.setItems(FXCollections.observableArrayList(entrees));

            // Mettre à jour les statistiques
            int total = entrees.size();
            totalEntreesLabel.setText(String.valueOf(total));

            if (total > 0) {
                double moyenne = entrees.stream()
                        .mapToInt(EntreeJournal::getHumeur)
                        .average()
                        .orElse(0);
                moyenneHumeurLabel.setText(String.format("%.1f", moyenne));
            } else {
                moyenneHumeurLabel.setText("—");
            }

            statusLabel.setText(total + " entrée(s) chargée(s)");

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors du chargement : " + e.getMessage());
        }
    }

    @FXML
    void ajouterNouveauJournal(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AjoutJournal.fxml"));
        Parent root = loader.load();
        journalTable.getScene().setRoot(root);
    }

    @FXML
    void supprimerJournal(ActionEvent event) {
        EntreeJournal selected = journalTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Attention", "Veuillez sélectionner une entrée à supprimer !");
            return;
        }

        try {
            journalService.delete(selected.getIdJournal());
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Entrée supprimée avec succès !");
            chargerJournaux();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la suppression : " + e.getMessage());
        }
    }

    @FXML
    void modifierJournal(ActionEvent event) {
        EntreeJournal selected = journalTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Attention", "Veuillez sélectionner une entrée à modifier !");
            return;
        }

        Dialog<EntreeJournal> dialog = new Dialog<>();
        dialog.setTitle("Modifier le Journal");
        dialog.setHeaderText("Modifiez les informations de l'entrée");

        ButtonType sauvegarderButtonType = new ButtonType("Sauvegarder", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(sauvegarderButtonType, ButtonType.CANCEL);

        Spinner<Integer> humeurSpinner = new Spinner<>(0, 10, selected.getHumeur());
        humeurSpinner.setEditable(true);
        TextArea noteTextArea = new TextArea(selected.getNoteTextuelle());
        noteTextArea.setWrapText(true);
        noteTextArea.setPrefRowCount(4);

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(20));
        vbox.getChildren().addAll(
                new Label("Humeur (0-10) :"), humeurSpinner,
                new Label("Note :"), noteTextArea);
        dialog.getDialogPane().setContent(vbox);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == sauvegarderButtonType) {
                selected.setHumeur(humeurSpinner.getValue());
                selected.setNoteTextuelle(noteTextArea.getText());
                return selected;
            }
            return null;
        });

        dialog.showAndWait().ifPresent(entree -> {
            try {
                journalService.update(selected);
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Entrée modifiée avec succès !");
                chargerJournaux();
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur : " + e.getMessage());
            }
        });
    }

    public void rafraichir() {
        chargerJournaux();
    }

    @FXML
    void allerAuxHabitudes(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AffichageHabitude.fxml"));
        Parent root = loader.load();
        journalTable.getScene().setRoot(root);
    }

    @FXML
    void rechercherJournal() {
        try {
            String keyword = searchField.getText();
            if (keyword == null || keyword.isEmpty()) {
                journalTable.setItems(FXCollections.observableArrayList(
                        journalService.findAll()
                ));
            } else {
                journalTable.setItems(FXCollections.observableArrayList(
                        journalService.search(keyword)
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void viderRecherche() {
        searchField.clear();
    }

    @FXML
    void exporterPDF() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Enregistrer le fichier PDF");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Fichiers PDF", "*.pdf")
            );
            fileChooser.setInitialFileName("Journal.pdf");

            Stage stage = (Stage) journalTable.getScene().getWindow();
            File file = fileChooser.showSaveDialog(stage);

            if (file != null) {
                PdfExporter.exportJournal(
                        journalService.findAll(),
                        file.getAbsolutePath()
                );
                statusLabel.setText("✅ PDF enregistré avec succès !");
            }
        } catch (Exception e) {
            statusLabel.setText("❌ Erreur export PDF");
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }
}