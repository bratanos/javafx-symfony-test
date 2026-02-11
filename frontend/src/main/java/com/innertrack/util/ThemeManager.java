package com.innertrack.util;

import atlantafx.base.theme.PrimerLight;
import javafx.application.Application;
import javafx.scene.Scene;

import java.util.Objects;

public class ThemeManager {

    /**
     * Initializes the theme for the application.
     * Sets the base AtlantaFX theme and adds custom stylesheets.
     */
    public static void init(Scene scene) {
        // Apply AtlantaFX Primer Light theme
        Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());

        // Add our custom modular stylesheets
        addStylesheet(scene, "/css/variables.css");
        addStylesheet(scene, "/css/base.css");
        addStylesheet(scene, "/css/utility.css");
    }

    /**
     * Adds a specific stylesheet to the scene if it exists.
     */
    public static void addStylesheet(Scene scene, String resourcePath) {
        try {
            String cssPath = Objects.requireNonNull(ThemeManager.class.getResource(resourcePath)).toExternalForm();
            scene.getStylesheets().add(cssPath);
        } catch (Exception e) {
            System.err.println("Could not load stylesheet: " + resourcePath + " - " + e.getMessage());
        }
    }

    /**
     * Adds a component-specific stylesheet.
     */
    public static void addComponentStyle(Scene scene, String componentName) {
        addStylesheet(scene, "/css/components/" + componentName + ".css");
    }
}
