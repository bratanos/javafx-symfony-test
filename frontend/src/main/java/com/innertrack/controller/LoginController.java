package com.innertrack.controller;

import com.innertrack.service.AuthService;
import com.innertrack.util.ViewManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    private final AuthService authService = new AuthService();

    @FXML
    public void initialize() {
        MainLayoutController.getInstance().setNavbarVisible(false);
    }

    @FXML
    private void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Veuillez remplir tous les champs.");
            return;
        }

        String result = (String) authService.login(email, password);

        if ("SUCCESS".equals(result)) {
            System.out.println("Login successful for user: " + email);
            // Success! Determine redirection based on role
            com.innertrack.model.User user = com.innertrack.session.SessionManager.getInstance().getCurrentUser();
            String dashboardView;

            if (user instanceof com.innertrack.model.Admin) {
                dashboardView = "admin/dashboard";
            } else if (user instanceof com.innertrack.model.Psychologue) {
                dashboardView = "psychologue/dashboard";
            } else {
                dashboardView = "user/dashboard";
            }

            System.out.println("Redirecting to: " + dashboardView);
            ViewManager.loadView(dashboardView);
            // The dashboard's initialize() will handle its own visibility needs.
            MainLayoutController.getInstance().updateUiForSession();
        } else {
            System.err.println("Login failed: " + result);
            if ("Account not verified. Please verify your email.".equals(result)) {
                VerifyOtpController controller = ViewManager.loadView("verify_otp");
                if (controller != null) {
                    controller.setEmail(email);
                }
            } else {
                errorLabel.setText(result);
            }
        }
    }

    @FXML
    private void goToRegister() {
        ViewManager.loadView("register");
    }

    @FXML
    private void goToForgotPassword() {
        ViewManager.loadView("forgot_password");
    }
}
