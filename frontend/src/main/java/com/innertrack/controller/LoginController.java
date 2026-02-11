package com.innertrack.controller;

import com.innertrack.service.AuthService;
import com.innertrack.service.DatabaseService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Label errorLabel;

    private final AuthService authService = new AuthService();

    @FXML
    public void onLoginClicked(ActionEvent event) {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please fill in all fields.");
            return;
        }

        // Disable button to prevent double clicks
        loginButton.setDisable(true);
        errorLabel.setText("Authenticating...");

        // Perform login in background thread to keep UI responsive
        new Thread(() -> {
            boolean success = authService.login(email, password);

            // Update UI on JavaFX Application Thread
            javafx.application.Platform.runLater(() -> {
                loginButton.setDisable(false);
                if (success) {
                    errorLabel.setText("Login Successful!");
                    errorLabel.setStyle("-fx-text-fill: green;");
                    // TODO: Navigate to dashboard
                } else {
                    errorLabel.setText("Invalid credentials.");
                    errorLabel.setStyle("-fx-text-fill: -color-danger;");
                }
            });
        }).start();
    }
}
