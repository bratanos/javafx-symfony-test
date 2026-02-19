package com.innertrack.controller.profile;

import com.innertrack.dao.UserDao;
import com.innertrack.model.User;
import com.innertrack.security.BCryptHasher;
import com.innertrack.session.SessionManager;
import com.innertrack.util.ViewManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import java.sql.SQLException;

public class ChangePasswordController {

    @FXML
    private PasswordField currentPasswordField;

    @FXML
    private PasswordField newPasswordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Label messageLabel;

    private final UserDao userDao = new UserDao();
    private User currentUser;

    @FXML
    public void initialize() {
        currentUser = SessionManager.getInstance().getCurrentUser();
    }

    @FXML
    private void handleChangePassword() {
        String currentPassword = currentPasswordField.getText();
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            showMessage("Tous les champs sont obligatoires.", true);
            return;
        }

        if (!BCryptHasher.check(currentPassword, currentUser.getPassword())) {
            showMessage("Le mot de passe actuel est incorrect.", true);
            return;
        }

        if (newPassword.length() < 6) {
            showMessage("Le nouveau mot de passe doit contenir au moins 6 caractères.", true);
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            showMessage("Les nouveaux mots de passe ne correspondent pas.", true);
            return;
        }

        try {
            String hashedNewPassword = BCryptHasher.hash(newPassword);
            if (userDao.updatePassword(currentUser.getId(), hashedNewPassword)) {
                currentUser.setPassword(hashedNewPassword);
                showAlert("Succès", "Votre mot de passe a été mis à jour avec succès.");
                handleBack();
            } else {
                showMessage("Erreur lors de la mise à jour du mot de passe.", true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showMessage("Une erreur est survenue.", true);
        }
    }

    @FXML
    private void handleBack() {
        ViewManager.loadView("profile/settings");
    }

    private void showMessage(String message, boolean isError) {
        messageLabel.setText(message);
        if (isError) {
            messageLabel.setStyle("-fx-text-fill: -color-danger-emphasis;");
        } else {
            messageLabel.setStyle("-fx-text-fill: -color-success-emphasis;");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
