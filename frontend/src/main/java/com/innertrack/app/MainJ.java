package main;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import com.innertrack.util.DBConnection;

public class MainJ extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {

            // 1️⃣ Charger l'interface immédiatement
            Parent root = FXMLLoader.load(
                    getClass().getResource("/AffichageJournal.fxml")
            );

            Scene scene = new Scene(root, 800, 600);
            primaryStage.setTitle("InnerTrack - Gestion Émotionnelle");
            primaryStage.setScene(scene);
            primaryStage.setMinWidth(600);
            primaryStage.setMinHeight(500);
            primaryStage.show();

            System.out.println("✓ Interface affichée");

            // 2️⃣ Initialiser la DB en arrière-plan
            initDatabaseAsync();

        } catch (Exception e) {
            e.printStackTrace();
            showError("Erreur de chargement : " + e.getMessage());
        }
    }

    private void initDatabaseAsync() {
        new Thread(() -> {
            try {
                System.out.println("Connexion à la base en cours...");

                DBConnection db = DBConnection.getInstance();

                if (db.getConnection() == null) {
                    Platform.runLater(() ->
                            showError("Impossible de se connecter à la base de données.")
                    );
                } else {
                    System.out.println("✓ Connexion DB établie");
                }

            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() ->
                        showError("Erreur DB : " + e.getMessage())
                );
            }
        }).start();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText("Problème de connexion");
        alert.setContentText(message);
        alert.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}