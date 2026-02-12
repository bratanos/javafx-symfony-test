package com.innertrack.controller;

import com.innertrack.service.AuthService;
import com.innertrack.util.ViewManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

public class ResetPasswordController {

    @FXML
    private TextField code1, code2, code3, code4, code5, code6;
    @FXML
    private PasswordField newPasswordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private Label messageLabel;
    @FXML
    private Label subtitleLabel;

    private String userEmail;
    private final AuthService authService = new AuthService();

    public void setEmail(String email) {
        this.userEmail = email;
        subtitleLabel.setText("Code envoyé à " + email);
    }

    @FXML
    public void initialize() {
        setupCodeFields();
        MainLayoutController.getInstance().setNavbarVisible(false);
    }

    private void setupCodeFields() {
        TextField[] fields = { code1, code2, code3, code4, code5, code6 };
        for (int i = 0; i < fields.length; i++) {
            final int index = i;
            fields[i].textProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal.length() > 1) {
                    fields[index].setText(newVal.substring(newVal.length() - 1));
                }
                if (!newVal.isEmpty() && index < fields.length - 1) {
                    fields[index + 1].requestFocus();
                }
            });

            fields[i].setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.BACK_SPACE) {
                    if (fields[index].getText().isEmpty() && index > 0) {
                        fields[index - 1].requestFocus();
                        fields[index - 1].clear();
                    }
                }
            });
        }
    }

    @FXML
    private void handleResetPassword() {
        String code = code1.getText() + code2.getText() + code3.getText() +
                code4.getText() + code5.getText() + code6.getText();
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (code.length() < 6) {
            showMessage("Veuillez saisir le code à 6 chiffres.", true);
            return;
        }

        if (newPassword.isEmpty() || newPassword.length() < 6) {
            showMessage("Le mot de passe doit faire au moins 6 caractères.", true);
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            showMessage("Les mots de passe ne correspondent pas.", true);
            return;
        }

        String result = authService.resetPassword(userEmail, code, newPassword);

        if ("SUCCESS".equals(result)) {
            showMessage("Mot de passe réinitialisé avec succès !", false);
            // After a short delay or user action, go to login
            goToLogin();
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
