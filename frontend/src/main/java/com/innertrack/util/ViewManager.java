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

        try {
            String path = "/fxml/" + fxmlName + ".fxml";
            FXMLLoader loader = new FXMLLoader(ViewManager.class.getResource(path));
            Node view = loader.load();

            contentContainer.getChildren().setAll(view);
            return loader.getController();

        } catch (IOException e) {
            System.err.println("Error loading view: " + fxmlName + ". " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
