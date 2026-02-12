package com.innertrack.controller;

import com.innertrack.service.AuthService;
import com.innertrack.util.ViewManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ForgotPasswordController {

    @FXML
    private TextField emailField;

    @FXML
    private Label messageLabel;

    private final AuthService authService = new AuthService();

    @FXML
    public void initialize() {
        MainLayoutController.getInstance().setNavbarVisible(false);
    }

    @FXML
    private void handleRequestReset() {
        String email = emailField.getText();
        if (email.isEmpty()) {
            showMessage("Veuillez saisir votre email.", true);
            return;
        }

        // Logic to request reset code
        String result = authService.requestPasswordReset(email);

        if ("SUCCESS".equals(result)) {
            // Navigate to reset password view and pass email
            ResetPasswordController controller = ViewManager.loadView("reset_password");
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
        messageLabel.setStyle("-fx-text-fill: " + (isError ? "#ef4444" : "#10b981") + ";");
    }
}
