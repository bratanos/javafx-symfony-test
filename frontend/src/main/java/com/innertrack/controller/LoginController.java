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

        String result = authService.login(email, password);

        if ("SUCCESS".equals(result)) {
            // Success! Load the main view
            ViewManager.loadView("main");
            MainLayoutController.getInstance().setNavbarVisible(true);
            MainLayoutController.getInstance().updateUiForSession();
        } else {
            errorLabel.setText(result);
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
