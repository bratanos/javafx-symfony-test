package com.innertrack.controller;

import com.innertrack.service.AuthService;
import com.innertrack.util.ViewManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.Collections;

public class RegisterController {

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private ComboBox<String> roleComboBox;

    @FXML
    private Label messageLabel;

    private final AuthService authService = new AuthService();

    @FXML
    public void initialize() {
        com.innertrack.controller.MainLayoutController.getInstance().setNavbarVisible(false);
        com.innertrack.controller.MainLayoutController.getInstance().setFooterVisible(false);
        roleComboBox.setItems(FXCollections.observableArrayList("Normal User", "Psychologue"));
    }

    @FXML
    private void handleRegister() {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String selectedRole = roleComboBox.getValue();

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()
                || confirmPassword.isEmpty() || selectedRole == null) {
            showMessage("Tous les champs sont obligatoires.", true);
            return;
        }

        if (!password.equals(confirmPassword)) {
            showMessage("Les mots de passe ne correspondent pas.", true);
            return;
        }

        // Map UI role names to ROLE_ codes
        String roleCode = selectedRole.equals("Psychologue") ? "ROLE_PSYCHOLOGUE" : "ROLE_USER";

        String result = authService.register(email, password, firstName, lastName, Collections.singletonList(roleCode));

        if ("SUCCESS".equals(result)) {
            // Success! Load OTP view and pass email
            VerifyOtpController controller = ViewManager.loadView("verify_otp");
            if (controller != null) {
                controller.setEmail(email);
            }
        } else {
            showMessage(result, true);
        }
    }

    @FXML
    private void goToLogin() {
        ViewManager.loadView("login");
    }

    private void showMessage(String message, boolean isError) {
        messageLabel.setText(message);
        if (isError) {
            messageLabel.setStyle("-fx-text-fill: -color-danger-emphasis;");
        } else {
            messageLabel.setStyle("-fx-text-fill: -color-success-emphasis;");
        }
    }
}
