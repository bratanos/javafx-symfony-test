package com.innertrack.app;

import com.innertrack.dao.UserDao;
import com.innertrack.model.User;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class CrudTestGui extends Application {

    private UserDao userDao;

    private TextField emailField;
    private PasswordField passwordField;
    private TextField rolesField;
    private TextField statusField;
    private CheckBox verifiedCheckBox;

    private TableView<User> tableView;
    private ObservableList<User> users;

    @Override
    public void start(Stage stage) throws SQLException {
        userDao = new UserDao();
        users = FXCollections.observableArrayList();

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(15));

        root.setTop(createTitle());
        root.setCenter(createTable());
        root.setRight(createForm());

        loadUsers();

        stage.setTitle("User Management");
        stage.setScene(new Scene(root, 900, 500));
        stage.show();
    }

    /* ================= UI ================= */

    private HBox createTitle() {
        Label title = new Label("User Management");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        HBox box = new HBox(title);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(0, 0, 15, 0));
        return box;
    }

    private TableView<User> createTable() {
        tableView = new TableView<>();
        tableView.setItems(users);

        TableColumn<User, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<User, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        TableColumn<User, Boolean> verifiedCol = new TableColumn<>("Verified");
        verifiedCol.setCellValueFactory(new PropertyValueFactory<>("verified"));

        TableColumn<User, List<String>> rolesCol = new TableColumn<>("Roles");
        rolesCol.setCellValueFactory(new PropertyValueFactory<>("roles"));

        tableView.getColumns().addAll(emailCol, statusCol, verifiedCol, rolesCol);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        tableView.getSelectionModel().selectedItemProperty()
                .addListener((obs, old, selected) -> fillForm(selected));

        return tableView;
    }

    private VBox createForm() {
        VBox box = new VBox(10);
        box.setPadding(new Insets(10));
        box.setPrefWidth(300);

        emailField = new TextField();
        emailField.setPromptText("Email");

        passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        rolesField = new TextField("ROLE_USER");

        statusField = new TextField("PENDING");

        verifiedCheckBox = new CheckBox("Verified");

        Button addBtn = new Button("Add");
        addBtn.setOnAction(e -> createUser());

        Button updateBtn = new Button("Update");
        updateBtn.setOnAction(e -> updateUser());

        Button deleteBtn = new Button("Delete");
        deleteBtn.setOnAction(e -> deleteUser());

        box.getChildren().addAll(
                new Label("Email"), emailField,
                new Label("Password"), passwordField,
                new Label("Roles"), rolesField,
                new Label("Status"), statusField,
                verifiedCheckBox,
                addBtn, updateBtn, deleteBtn
        );

        return box;
    }

    /* ================= CRUD ================= */

    private void createUser() {

        if (!validateForm(true)) return;

        try {
            User user = new User();
            user.setEmail(emailField.getText().trim());
            user.setPassword(passwordField.getText());
            user.setRoles(parseRoles(rolesField.getText()));
            user.setStatus(statusField.getText().trim());
            user.setVerified(verifiedCheckBox.isSelected());

            userDao.create(user);
            loadUsers();
            clearForm();
            info("User created successfully");

        } catch (Exception e) {
            error(e.getMessage());
        }
    }

    private void updateUser() {

        User selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            warning("Select a user first");
            return;
        }

        if (!validateForm(false)) return;

        try {
            selected.setEmail(emailField.getText().trim());

            if (!passwordField.getText().trim().isEmpty()) {
                selected.setPassword(passwordField.getText());
            }

            selected.setRoles(parseRoles(rolesField.getText()));
            selected.setStatus(statusField.getText().trim());
            selected.setVerified(verifiedCheckBox.isSelected());

            userDao.update(selected);
            tableView.refresh();
            info("User updated");

        } catch (Exception e) {
            error(e.getMessage());
        }
    }

    private void deleteUser() {

        User selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            warning("Select a user first");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete selected user?", ButtonType.OK, ButtonType.CANCEL);

        confirm.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.OK) {
                try {
                    userDao.delete(selected.getId());
                    loadUsers();
                    clearForm();
                } catch (Exception e) {
                    error(e.getMessage());
                }
            }
        });
    }

    /* ================= Validation ================= */

    private boolean validateForm(boolean isCreate) {

        if (emailField.getText().trim().isEmpty()) {
            warning("Email is required");
            return false;
        }

        if (!emailField.getText().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            warning("Invalid email format");
            return false;
        }

        if (isCreate && passwordField.getText().trim().isEmpty()) {
            warning("Password is required");
            return false;
        }

        if (rolesField.getText().trim().isEmpty()) {
            warning("Roles are required");
            return false;
        }

        if (statusField.getText().trim().isEmpty()) {
            warning("Status is required");
            return false;
        }

        return true;
    }

    /* ================= Helpers ================= */

    private void loadUsers() throws SQLException {
        users.setAll(userDao.findAll());
    }

    private void fillForm(User user) {
        if (user == null) return;
        emailField.setText(user.getEmail());
        passwordField.clear();
        rolesField.setText(String.join(",", user.getRoles()));
        statusField.setText(user.getStatus());
        verifiedCheckBox.setSelected(user.isVerified());
    }

    private void clearForm() {
        emailField.clear();
        passwordField.clear();
        rolesField.setText("ROLE_USER");
        statusField.setText("PENDING");
        verifiedCheckBox.setSelected(false);
        tableView.getSelectionModel().clearSelection();
    }

    private List<String> parseRoles(String text) {
        return Arrays.asList(text.split(","));
    }

    private void info(String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg).showAndWait();
    }

    private void warning(String msg) {
        new Alert(Alert.AlertType.WARNING, msg).showAndWait();
    }

    private void error(String msg) {
        new Alert(Alert.AlertType.ERROR, msg).showAndWait();
    }

    public static void main(String[] args) {
        launch();
    }
}
