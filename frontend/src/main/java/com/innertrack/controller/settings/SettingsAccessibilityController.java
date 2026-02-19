package com.innertrack.controller.settings;

import com.innertrack.service.SettingsService;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

public class SettingsAccessibilityController {

    @FXML
    private RadioButton fontSmallBtn;
    @FXML
    private RadioButton fontNormalBtn;
    @FXML
    private RadioButton fontLargeBtn;

    private final SettingsService settingsService = SettingsService.getInstance();
    private ToggleGroup fontGroup;

    @FXML
    public void initialize() {
        fontGroup = new ToggleGroup();
        fontSmallBtn.setToggleGroup(fontGroup);
        fontNormalBtn.setToggleGroup(fontGroup);
        fontLargeBtn.setToggleGroup(fontGroup);

        String currentFontSize = settingsService.getCurrentSettings().getFontSize();
        switch (currentFontSize) {
            case "SMALL":
                fontSmallBtn.setSelected(true);
                break;
            case "LARGE":
                fontLargeBtn.setSelected(true);
                break;
            default:
                fontNormalBtn.setSelected(true);
                break;
        }
    }

    @FXML
    private void setFontSmall() {
        settingsService.updateFontSize("SMALL");
    }

    @FXML
    private void setFontNormal() {
        settingsService.updateFontSize("NORMAL");
    }

    @FXML
    private void setFontLarge() {
        settingsService.updateFontSize("LARGE");
    }
}
