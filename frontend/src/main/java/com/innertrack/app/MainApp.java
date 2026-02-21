package com.innertrack.app;

import atlantafx.base.theme.PrimerLight;
import com.innertrack.util.ViewManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Set the AtlantaFX theme
        Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());

        // Load the main layout
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AffichageJournal.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        primaryStage.setTitle("InnerTrack - Gestion de Santé Mentale");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();

        // Load the initial view (login or main)
        ViewManager.loadView("login");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
