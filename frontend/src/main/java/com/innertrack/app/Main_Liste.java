package com.innertrack.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Classe principale de l'application JavaFX
 * Lance l'interface de gestion des tests psychologiques
 */
public class Main_Liste extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Charger le fichier FXML de la liste des tests
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/listeTests.fxml"));
            Parent root = loader.load();

            // Créer la scène
            Scene scene = new Scene(root, 1200, 800);

            // Charger le fichier CSS
            String css = getClass().getResource("/styleListeTests.css").toExternalForm();
            scene.getStylesheets().add(css);

            // Configurer la fenêtre principale
            primaryStage.setTitle("Gestion des Tests Psychologiques");
            primaryStage.setScene(scene);
            primaryStage.setMaximized(true); // Plein écran pour une meilleure expérience
            primaryStage.setMinWidth(1000);
            primaryStage.setMinHeight(600);

            // Afficher la fenêtre
            primaryStage.show();

            System.out.println("✅ Application lancée avec succès!");
            System.out.println("📋 Interface de gestion des tests chargée");

        } catch (Exception e) {
            System.err.println("❌ Erreur lors du chargement de l'interface:");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Lancer l'application JavaFX
        launch(args);
    }
}