package com.innertrack.controller.user;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import com.innertrack.session.SessionManager;

public class UserDashboardController {
    @FXML
    private Label welcomeLabel;
    @FXML
    private Label streakLabel;
    @FXML
    private Label journalEntriesLabel;
    @FXML
    private Label habitsCompletedLabel;
    @FXML
    private javafx.scene.shape.Circle profileCircle;
    @FXML
    private Label userFullNameLabel;

    @FXML
    public void initialize() {
        com.innertrack.model.User user = SessionManager.getInstance().getCurrentUser();
        welcomeLabel.setText("Ravi de vous revoir, " + user.getFullName());
        userFullNameLabel.setText(user.getFullName());
        updateProfileImage(user.getProfilePicture());

        // Hide global navbar as requested
        com.innertrack.controller.MainLayoutController.getInstance().setNavbarVisible(false);
        com.innertrack.controller.MainLayoutController.getInstance().setFooterVisible(false);

        // Dummy stats
        streakLabel.setText("7 jours");
        journalEntriesLabel.setText("24");
        habitsCompletedLabel.setText("85%");
    }

    @FXML
    private void handleGoToJournal() {
        System.out.println("Navigating to Journal...");
    }

    @FXML
    private void handleGoToHabits() {
        System.out.println("Navigating to Habits...");
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
