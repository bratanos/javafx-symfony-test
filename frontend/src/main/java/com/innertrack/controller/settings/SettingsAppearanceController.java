package com.innertrack.controller.settings;

import com.innertrack.service.SettingsService;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;

public class SettingsAppearanceController {

    @FXML
    private ToggleButton lightThemeBtn;
    @FXML
    private ToggleButton darkThemeBtn;
    @FXML
    private ComboBox<String> languageComboBox;

    private final SettingsService settingsService = SettingsService.getInstance();
    private ToggleGroup themeGroup;

    @FXML
    public void initialize() {
        themeGroup = new ToggleGroup();
        lightThemeBtn.setToggleGroup(themeGroup);
        darkThemeBtn.setToggleGroup(themeGroup);

        String currentTheme = settingsService.getCurrentSettings().getTheme();
        if ("DARK".equals(currentTheme)) {
            darkThemeBtn.setSelected(true);
        } else {
            lightThemeBtn.setSelected(true);
        }

        languageComboBox.getItems().addAll("English", "Français");
        String currentLang = settingsService.getCurrentSettings().getLanguage();
        languageComboBox.setValue("FR".equals(currentLang) ? "Français" : "English");
    }

    @FXML
    private void setLightTheme() {
        settingsService.updateTheme("LIGHT");
    }

    @FXML
    private void setDarkTheme() {
        settingsService.updateTheme("DARK");
    }

    @FXML
    private void handleLanguageChange() {
        String selected = languageComboBox.getValue();
        settingsService.updateLanguage("Français".equals(selected) ? "FR" : "EN");
    }
}
