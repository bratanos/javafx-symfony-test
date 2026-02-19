package com.innertrack.controller.settings;

import com.innertrack.util.ViewManager;
import com.innertrack.session.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.TabPane;

public class SettingsController {

    @FXML
    private TabPane settingsTabPane;

    @FXML
    public void initialize() {
        // Any general settings hub initialization
    }

    @FXML
    private void handleBack() {
        String role = SessionManager.getInstance().getCurrentUser().getRoles().get(0);
        if (role.contains("ADMIN")) {
            ViewManager.loadView("admin/dashboard");
        } else if (role.contains("PSYCHOLOGUE")) {
            ViewManager.loadView("psychologue/dashboard");
        } else {
            ViewManager.loadView("user/dashboard");
        }
    }
}
