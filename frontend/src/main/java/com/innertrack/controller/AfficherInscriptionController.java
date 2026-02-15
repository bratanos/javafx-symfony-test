package com.innertrack.controller;

import com.innertrack.model.Inscription;
import com.innertrack.service.InscriptionService;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AfficherInscriptionController {

    private final InscriptionService inscriptionService = new InscriptionService();
    private final ObservableList<Inscription> data = FXCollections.observableArrayList();

    @FXML private TableView<Inscription> table_inscriptions;

    @FXML private TableColumn<Inscription, String> col_titre;

    @FXML private TableColumn<Inscription, String> col_nom;

    @FXML private TableColumn<Inscription, String> col_email;

    @FXML private TableColumn<Inscription, String> col_date;

    @FXML private TableColumn<Inscription, Void> col_actions;



    @FXML
    private Label error_label;

    @FXML
    public void initialize() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        col_titre.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTitreEvent()));
        col_nom.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNomParticipant()));
        col_email.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getEmailParticipant()));
        col_date.setCellValueFactory(c -> {
            LocalDate d = c.getValue().getDateInscription();
            return new SimpleStringProperty(d == null ? "" : d.format(fmt));
        });
        initActionsColumn();
        table_inscriptions.setItems(data);
        col_titre.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTitreEvent()));


        refresh();
    }



    public void refresh() {
        try {
            data.clear();
            List<Inscription> inscriptions = inscriptionService.recuperer();
            data.addAll(inscriptions);
            if (error_label != null) {
                error_label.setVisible(false);
            }
        } catch (SQLException e) {
            if (error_label != null) {
                error_label.setText("DB Error: " + e.getMessage());
                error_label.setVisible(true);
            }
            e.printStackTrace();
        }
    }

    private void handleDelete(Inscription ins) {
        if (ins == null) return;

        javafx.scene.control.Alert confirm = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.CONFIRMATION
        );
        confirm.setTitle("Confirmation");
        confirm.setHeaderText("Supprimer cette inscription ?");
        confirm.setContentText("Participant: " + ins.getNomParticipant() +
                "\nEvent: " + ins.getTitreEvent());

        confirm.showAndWait().ifPresent(btnType -> {
            if (btnType == javafx.scene.control.ButtonType.OK) {
                try {
                    inscriptionService.supprimer(ins.getIdInscription());
                    refresh();
                } catch (SQLException ex) {
                    if (error_label != null) {
                        error_label.setText("DB Error: " + ex.getMessage());
                        error_label.setVisible(true);
                    }
                    ex.printStackTrace();
                }
            }
        });
    }
        void initActionsColumn() {
            if (col_actions == null) return; // au cas où tu n'as pas encore ajouté la colonne

            col_actions.setCellFactory(col -> new javafx.scene.control.TableCell<>() {
                private final javafx.scene.control.Button btn = new javafx.scene.control.Button("Supprimer");

                {
                    btn.setStyle("-fx-background-color: #d9534f; -fx-text-fill: white;");
                    btn.setOnAction(e -> {
                        Inscription ins = getTableView().getItems().get(getIndex());
                        handleDelete(ins);
                    });
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(null);
                    setGraphic(empty ? null : btn);
                }
            });
        }

    public void goBackToEvents(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AfficherEvenement.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) table_inscriptions.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            if (error_label != null) {
                error_label.setText("Navigation Error: " + e.getMessage());
                error_label.setVisible(true);
            }
            e.printStackTrace();
        }
    }
}
