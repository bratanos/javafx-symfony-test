package com.innertrack.controller;

import com.innertrack.service.EventService;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import com.innertrack.model.Event;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;


public class AfficherEvenementController {

    private final com.innertrack.service.EventService eventService = new EventService();
    private final ObservableList<Event> data = FXCollections.observableArrayList();

    @FXML
    private TableView<Event> table_events;


    @FXML
    private TableColumn<Event, Void> col_participer;
    @FXML
    private TableColumn<Event, Number> col_id;
    @FXML
    private TableColumn<Event, String> col_title;
    @FXML
    private TableColumn<Event, String> col_description;
    @FXML
    private TableColumn<Event, String> col_date;
    @FXML
    private TableColumn<Event, String> col_type;
    @FXML
    private TableColumn<Event, Number> col_capacity;
    @FXML
    private TableColumn<Event, Boolean> col_status;
    @FXML
    private TableColumn<Event, LocalDate> dateCreationColumn;

    @FXML
    private Label error_label;

    @FXML
    void initialize() {
        // Safety: if you forgot fx:id in FXML you’ll see it immediately
        if (table_events == null) throw new IllegalStateException("table_events is not injected. Check fx:id in FXML.");

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Map Event -> table columns
        col_id.setCellValueFactory(cell ->
                new SimpleIntegerProperty(cell.getValue().getIdEvent()));

        col_title.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getTitre()));

        col_description.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getDescription()));


        col_date.setCellValueFactory(cell -> {
            if (cell.getValue().getDateEvent() == null) return new SimpleStringProperty("");
            return new SimpleStringProperty(cell.getValue().getDateEvent().format(fmt));
        });

        col_type.setCellValueFactory(cell ->
                new SimpleStringProperty(
                        cell.getValue().getTypeEvent() == null ? "" : cell.getValue().getTypeEvent().name()
                ));

        col_capacity.setCellValueFactory(cell ->
                new SimpleIntegerProperty(cell.getValue().getCapacite()));

        col_status.setCellValueFactory(cell ->
                new SimpleBooleanProperty(cell.getValue().isStatus()));

        dateCreationColumn.setCellValueFactory(cell ->
                new javafx.beans.property.SimpleObjectProperty<>(cell.getValue().getDateCreation())
        );

        table_events.setItems(data);
        addParticiperButtonToTable();


        // Load data
        refresh();
    }

    private void addParticiperButtonToTable() {
        col_participer.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button("Participer");

            {
                btn.setOnAction(e -> {
                    Event ev = getTableView().getItems().get(getIndex());

                    // Option: bloquer si event inactif
                    if (!ev.isStatus()) {
                        new Alert(Alert.AlertType.WARNING, "Événement inactif.").showAndWait();
                        return;
                    }

                    TextInputDialog nomDialog = new TextInputDialog();
                    nomDialog.setTitle("Inscription");
                    nomDialog.setHeaderText("Participer à : " + ev.getTitre());
                    nomDialog.setContentText("Nom :");
                    var nomOpt = nomDialog.showAndWait();
                    String nom = nomOpt.get().trim();

                    if (!isValidName(nom)) {
                        new Alert(Alert.AlertType.WARNING,
                                "Nom invalide !\nSeulement des lettres, minimum 3 caractères.")
                                .showAndWait();
                        return;
                    }
                    TextInputDialog emailDialog = new TextInputDialog();
                    emailDialog.setTitle("Inscription");
                    emailDialog.setHeaderText("Participer à : " + ev.getTitre());
                    emailDialog.setContentText("Email :");
                    var emailOpt = emailDialog.showAndWait();
                    String email = emailOpt.get().trim();

                    if (!isValidEmail(email)) {
                        new Alert(Alert.AlertType.WARNING,
                                "Email invalide !\nExemple: test@gmail.com")
                                .showAndWait();
                        return;
                    }
                    try {
                        new com.innertrack.service.InscriptionService()
                                .participer(ev.getIdEvent(), nomOpt.get().trim(), emailOpt.get().trim());

                        new Alert(Alert.AlertType.INFORMATION, "Inscription réussie ✅").showAndWait();
                    } catch (java.sql.SQLIntegrityConstraintViolationException dup) {
                        new Alert(Alert.AlertType.WARNING, "Déjà inscrit avec cet email.").showAndWait();
                    } catch (Exception ex) {
                        new Alert(Alert.AlertType.ERROR, "Erreur: " + ex.getMessage()).showAndWait();
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });
    }

    private boolean isValidName(String name) {
        return name.matches("^[A-Za-zÀ-ÿ ]{3,}$");
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }


    @FXML
    public void refresh() {
        try {
            error_label.setText("");
            error_label.setVisible(false);

            data.clear();

            // IMPORTANT: you must have a method in EventService that returns List<Event>
            List<Event> events = eventService.recuperer();
            data.addAll(events);

        } catch (SQLException e) {
            error_label.setText("DB error: " + e.getMessage());
            error_label.setVisible(true);
            e.printStackTrace();
        }
    }

    @FXML
    public void deleteSelected() {
        Event selected = table_events.getSelectionModel().getSelectedItem();
        if (selected == null) {
            error_label.setText("Select an event first.");
            error_label.setVisible(true);
            return;
        }

        try {
            eventService.supprimer(selected.getIdEvent()); // you need supprimer(id)
            refresh();
        } catch (SQLException e) {
            error_label.setText("Delete error: " + e.getMessage());
            error_label.setVisible(true);
            e.printStackTrace();
        }

    }


    public void handleAjouterEvenement(javafx.event.ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AjouterEvenement.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) table_events.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter Evenement");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void handleModifierEvenement(ActionEvent actionEvent) {
        Event selected = table_events.getSelectionModel().getSelectedItem();

        if (selected == null) {
            error_label.setText("Select an event to modify.");
            error_label.setVisible(true);
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ModifierEvenement.fxml"));
            Parent root = loader.load();
            ModifierEvenementController controller = loader.getController();
            controller.setEvent(selected); // SEND EVENT


            Stage stage = (Stage) table_events.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier Evenement");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}