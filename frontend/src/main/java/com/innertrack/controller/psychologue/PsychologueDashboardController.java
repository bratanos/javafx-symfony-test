package com.innertrack.controller.psychologue;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import com.innertrack.session.SessionManager;

public class PsychologueDashboardController {
    @FXML
    private Label welcomeLabel;
    @FXML
    private Label totalPatientsLabel;
    @FXML
    private Label appointmentsTodayLabel;
    @FXML
    private Label totalConsultationsLabel;
    @FXML
    private javafx.scene.shape.Circle profileCircle;
    @FXML
    private Label userFullNameLabel;

    @FXML
    public void initialize() {
        com.innertrack.model.User psych = SessionManager.getInstance().getCurrentUser();
        welcomeLabel.setText("Bonjour, Dr. " + psych.getFullName());
        userFullNameLabel.setText(psych.getFullName());
        updateProfileImage(psych.getProfilePicture());

        // Hide global navbar as requested
        com.innertrack.controller.MainLayoutController.getInstance().setNavbarVisible(false);
        com.innertrack.controller.MainLayoutController.getInstance().setFooterVisible(false);

        // Dummy stats
        totalPatientsLabel.setText("42");
        appointmentsTodayLabel.setText("5");
        totalConsultationsLabel.setText("156");
    }

    @FXML
    private void handleViewPatients() {
        System.out.println("Navigating to Patients List...");
    }

    @FXML
    private void handleViewAgenda() {
        System.out.println("Navigating to Agenda...");
    }

    @FXML
    private void handleGoToSettings() {
        com.innertrack.util.ViewManager.loadView("profile/settings");
    }

    @FXML
    private void handleLogout() {
        SessionManager.getInstance().cleanSession();
        com.innertrack.util.ViewManager.loadView("login");
    }

    private void updateProfileImage(String picPath) {
        if (picPath != null && !picPath.isEmpty()) {
            try {
                java.io.File file = new java.io.File(picPath);
                if (file.exists()) {
                    try (java.io.FileInputStream fis = new java.io.FileInputStream(file)) {
                        javafx.scene.image.Image image = new javafx.scene.image.Image(fis);
                        // Scale pattern to fill circle (0,0 to 1,1 proportional)
                        profileCircle.setFill(new javafx.scene.paint.ImagePattern(image, 0, 0, 1, 1, true));
                    }
                }
            } catch (Exception e) {
                System.err.println("Error loading profile image: " + e.getMessage());
            }
        }
    }
}
