package com.innertrack.service;

import com.innertrack.dao.UserSettingsDao;
import com.innertrack.model.UserSettings;
import com.innertrack.app.MainApp;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.Locale;
import java.util.ResourceBundle;

public class SettingsService {
    private static SettingsService instance;
    private final UserSettingsDao settingsDao = new UserSettingsDao();
    private UserSettings currentSettings;
    private ResourceBundle bundle;

    private SettingsService() {
        // Default to English if not set
        bundle = ResourceBundle.getBundle("messages", Locale.ENGLISH);
    }

    public static synchronized SettingsService getInstance() {
        if (instance == null) {
            instance = new SettingsService();
        }
        return instance;
    }

    public ResourceBundle getBundle() {
        return bundle;
    }

    public void loadSettings(int userId) {
        currentSettings = settingsDao.findByUserId(userId);
        if (currentSettings == null) {
            // Create default settings
            currentSettings = new UserSettings(userId, "LIGHT", "NORMAL", "FR");
            settingsDao.create(currentSettings);
        }
        applyAll();
    }

    public UserSettings getCurrentSettings() {
        return currentSettings;
    }

    public void updateTheme(String theme) {
        currentSettings.setTheme(theme);
        settingsDao.update(currentSettings);
        applyTheme();
    }

    public void updateFontSize(String fontSize) {
        currentSettings.setFontSize(fontSize);
        settingsDao.update(currentSettings);
        applyFontSize();
    }

    public void updateLanguage(String language) {
        currentSettings.setLanguage(language);
        settingsDao.update(currentSettings);
        applyLanguage();
    }

    public void applyAll() {
        applyTheme();
        applyFontSize();
        applyLanguage();
    }

    private void applyTheme() {
        Platform.runLater(() -> {
            Stage stage = MainApp.getPrimaryStage();
            if (stage != null && stage.getScene() != null) {
                Scene scene = stage.getScene();
                javafx.scene.Parent root = scene.getRoot();

                // Ensure themes.css is added if not present
                String themeCss = getClass().getResource("/style/themes.css").toExternalForm();
                if (!scene.getStylesheets().contains(themeCss)) {
                    scene.getStylesheets().add(themeCss);
                }

                root.getStyleClass().removeAll("light-theme", "dark-theme");
                root.getStyleClass().add("DARK".equals(currentSettings.getTheme()) ? "dark-theme" : "light-theme");

                // Force CSS pass
                root.applyCss();
                root.layout();
            }
        });
    }

    private void applyFontSize() {
        Platform.runLater(() -> {
            Stage stage = MainApp.getPrimaryStage();
            if (stage != null && stage.getScene() != null) {
                javafx.scene.Parent root = stage.getScene().getRoot();
                root.getStyleClass().removeAll("font-small", "font-normal", "font-large");
                String fontSizeClass = "font-" + currentSettings.getFontSize().toLowerCase();
                root.getStyleClass().add(fontSizeClass);

                // Force CSS pass
                root.applyCss();
                root.layout();
            }
        });
    }

    private void applyLanguage() {
        Locale locale = "FR".equals(currentSettings.getLanguage()) ? Locale.FRENCH : Locale.ENGLISH;
        Locale.setDefault(locale);
        bundle = ResourceBundle.getBundle("messages", locale);
        // Inform views that language has changed if needed, or reload views
    }

    public String getString(String key) {
        return bundle.getString(key);
    }
}
