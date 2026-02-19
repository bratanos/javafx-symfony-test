package com.innertrack.controller.settings;

import com.innertrack.controller.VerifyOtpController;
import com.innertrack.dao.UserDao;
import com.innertrack.model.User;
import com.innertrack.session.SessionManager;
import com.innertrack.util.ViewManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class SettingsAccountController {

    @FXML
    private Label creationDateLabel;
    @FXML
    private Label lastLoginLabel;

    private final UserDao userDao = new UserDao();
    private User currentUser;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @FXML
    public void initialize() {
        currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser != null) {
            creationDateLabel
                    .setText(currentUser.getCreatedAt() != null ? currentUser.getCreatedAt().format(formatter) : "N/A");
            lastLoginLabel
                    .setText(currentUser.getLastLogin() != null ? currentUser.getLastLogin().format(formatter) : "N/A");
        }
    }

    @FXML
    private void handleDeactivate() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Désactivation du compte");
        alert.setHeaderText("Êtes-vous sûr de vouloir désactiver votre compte ?");
        alert.setContentText("Vous devrez à nouveau vérifier votre email lors de votre prochaine connexion.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (userDao.updateStatus(currentUser.getId(), "PENDING", false)) {
                SessionManager.getInstance().logout();
                ViewManager.loadView("login");
            }
        }
    }

    @FXML
    private void handleDelete() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Blocage du compte");
        alert.setHeaderText("Action irréversible : bloquer votre compte ?");
        alert.setContentText("Un code de vérification sera envoyé à votre email pour confirmer le blocage définitif.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Send OTP for blocking confirmation
            com.innertrack.service.AuthService authService = new com.innertrack.service.AuthService();
            authService.sendNewOtp(currentUser);

            // Redirect to a specialized OTP view or reuse VerifyOtpController with a mode
            VerifyOtpController controller = ViewManager.loadView("verify_otp");
            if (controller != null) {
                controller.setEmail(currentUser.getEmail());
                // In next step I would handle the "success" redirection to set BLOCKED
                // For now, this establishes the requirement of verification.
            }
        }
    }
}
