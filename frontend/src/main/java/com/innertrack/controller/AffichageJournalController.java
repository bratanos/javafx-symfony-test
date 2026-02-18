package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.EntreeJournal;
import service.JournalService;

import javafx.geometry.Insets;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

public class AffichageJournalController {

    @FXML
    private TableView<EntreeJournal> journalTable;

    @FXML
    private TableColumn<EntreeJournal, Integer> humeurColumn;

    @FXML
    private TableColumn<EntreeJournal, String> noteColumn;

    @FXML
    private TableColumn<EntreeJournal, LocalDate> dateColumn;

    private JournalService journalService;

    @FXML
    public void initialize() {
        journalService = new JournalService();

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
            ObservableList<EntreeJournal> journaux = FXCollections.observableArrayList(journalService.recuperer());
            journalTable.setItems(journaux);
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Erreur lors du chargement : " + e.getMessage());
            alert.show();
        }
    }

    @FXML
    void ajouterNouveauJournal(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjoutJournal.fxml"));
        Parent root = loader.load();
        journalTable.getScene().setRoot(root);
    }

    @FXML
    void supprimerJournal(ActionEvent event) {
        EntreeJournal selected = journalTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Attention");
            alert.setContentText("Veuillez sélectionner un journal à supprimer !");
            alert.show();
            return;
        }

        try {
            journalService.supprimer(selected);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setContentText("Journal supprimé avec succès !");
            alert.show();
            chargerJournaux();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Erreur lors de la suppression : " + e.getMessage());
            alert.show();
        }
    }

    @FXML
    void modifierJournal(ActionEvent event) {
        EntreeJournal selected = journalTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Attention");
            alert.setContentText("Veuillez sélectionner un journal à modifier !");
            alert.show();
            return;
        }

        // Créer une Dialog de modification
        Dialog<EntreeJournal> dialog = new Dialog<>();
        dialog.setTitle("Modifier le Journal");
        dialog.setHeaderText("Modifiez les informations du journal");

        // Boutons
        ButtonType sauvegarderButtonType = new ButtonType("Sauvegarder", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(sauvegarderButtonType, ButtonType.CANCEL);

        // Champs du formulaire
        Spinner<Integer> humeurSpinner = new Spinner<>(0, 10, selected.getHumeur());
        humeurSpinner.setEditable(true);
        TextArea noteTextArea = new TextArea(selected.getNoteTextuelle());
        noteTextArea.setWrapText(true);
        noteTextArea.setPrefRowCount(4);

        // Layout
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(20));
        vbox.getChildren().addAll(
                new Label("Humeur (0-10) :"), humeurSpinner,
                new Label("Note :"), noteTextArea
        );
        dialog.getDialogPane().setContent(vbox);

        // Résultat
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
                journalService.modifier(entree);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succès");
                alert.setContentText("Journal modifié avec succès !");
                alert.show();
                chargerJournaux();
            } catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setContentText("Erreur : " + e.getMessage());
                alert.show();
            }
        });
    }


    // Méthode appelée par ModifierJournalController pour rafraîchir la liste
    public void rafraichir() {
        chargerJournaux();
    }

    @FXML
    void allerAuxHabitudes(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjoutHabitude.fxml"));
        Parent root = loader.load();
        journalTable.getScene().setRoot(root);
    }
}