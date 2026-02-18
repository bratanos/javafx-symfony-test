package com.innertrack.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import java.io.IOException;

public class ViewManager {

    private static Pane contentContainer;

    public static void setContainer(Pane container) {
        contentContainer = container;
    }

    public static <T> T loadView(String fxmlName) {
        if (contentContainer == null) {
            System.err.println("Error: Content container not set in ViewManager.");
            return null;
        }
        return loadView(fxmlName, contentContainer);
    }

    public static <T> T loadView(String fxmlName, Pane container) {
        try {
            container.getChildren().clear();

            String path = null;
            FXMLLoader loader = null;

            // Prioritize auth/ folder for specific auth views if fxmlName doesn't contain a
            // path
            if (!fxmlName.contains("/")) {
                java.util.List<String> authViews = java.util.Arrays.asList("login", "register", "verify_otp",
                        "forgot_password", "reset_password");
                if (authViews.contains(fxmlName)) {
                    path = "/fxml/auth/" + fxmlName + ".fxml";
                    loader = new FXMLLoader(ViewManager.class.getResource(path));
                }
            }

            // If not loaded from auth/ or if fxmlName already contains a path, try the
            // direct path
            if (loader == null || loader.getLocation() == null) {
                path = "/fxml/" + fxmlName + ".fxml";
                loader = new FXMLLoader(ViewManager.class.getResource(path));
            }

            // Check if resource was found
            if (loader.getLocation() == null) {
                System.err.println("Error: FXML resource not found for path: " + path);
                return null;
            }

            Node view = loader.load();
            container.getChildren().add(view);
            return loader.getController();

        } catch (IOException e) {
            System.err.println("Error loading view: " + fxmlName + ". " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
