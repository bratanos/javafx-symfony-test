package com.testing.frontend;

import atlantafx.base.theme.PrimerLight;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class App extends Application {
    private TextArea responseArea;
    private Label statusLabel;

    @Override
    public void start(Stage primaryStage) {
        Application.setUserAgentStylesheet(
                new PrimerLight().getUserAgentStylesheet()
        );

        Label titleLabel = new Label("JavaFX + Symfony Test");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Button testButton = new Button("Test API Connection");
        testButton.setPrefWidth(200);
        testButton.setOnAction(e -> testApiConnection());

        Button getUsersButton = new Button("Get Users");
        getUsersButton.setPrefWidth(200);
        getUsersButton.setOnAction(e -> getUsers());

        statusLabel = new Label("Ready - Click a button to test");
        statusLabel.setStyle("-fx-text-fill: gray;");

        responseArea = new TextArea();
        responseArea.setEditable(false);
        responseArea.setPrefHeight(300);
        responseArea.setPromptText("API response will appear here...");

        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(
                titleLabel,
                testButton,
                getUsersButton,
                statusLabel,
                responseArea
        );

        Scene scene = new Scene(root, 600, 500);
        primaryStage.setTitle("JavaFX + Symfony Test");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void testApiConnection() {
        statusLabel.setText("Connecting to Symfony...");
        statusLabel.setStyle("-fx-text-fill: blue;");

        new Thread(() -> {
            try {
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:8000/api/test"))
                        .GET()
                        .build();

                HttpResponse<String> response = client.send(
                        request,
                        HttpResponse.BodyHandlers.ofString()
                );

                Gson gson = new GsonBuilder()
                        .setPrettyPrinting()
                        .create();

                String formatted = gson.toJson(
                        gson.fromJson(response.body(), Object.class)
                );

                javafx.application.Platform.runLater(() -> {
                    responseArea.setText(
                            "Status: " + response.statusCode() + "\n\n" + formatted
                    );
                    statusLabel.setText("✓ Connected successfully!");
                    statusLabel.setStyle("-fx-text-fill: green;");
                });

            } catch (Exception e) {
                javafx.application.Platform.runLater(() -> {
                    responseArea.setText(
                            "Error: " + e.getMessage() +
                                    "\n\nMake sure Symfony server is running on port 8000"
                    );
                    statusLabel.setText("✗ Connection failed");
                    statusLabel.setStyle("-fx-text-fill: red;");
                });
            }
        }).start();
    }

    private void getUsers() {
        statusLabel.setText("Fetching users from Symfony...");
        statusLabel.setStyle("-fx-text-fill: blue;");

        new Thread(() -> {
            try {
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:8000/api/users"))
                        .GET()
                        .build();

                HttpResponse<String> response = client.send(
                        request,
                        HttpResponse.BodyHandlers.ofString()
                );

                Gson gson = new GsonBuilder()
                        .setPrettyPrinting()
                        .create();

                String formatted = gson.toJson(
                        gson.fromJson(response.body(), Object.class)
                );

                javafx.application.Platform.runLater(() -> {
                    responseArea.setText("Users:\n\n" + formatted);
                    statusLabel.setText("✓ Users loaded successfully!");
                    statusLabel.setStyle("-fx-text-fill: green;");
                });

            } catch (Exception e) {
                javafx.application.Platform.runLater(() -> {
                    responseArea.setText("Error: " + e.getMessage());
                    statusLabel.setText("✗ Failed to load users");
                    statusLabel.setStyle("-fx-text-fill: red;");
                });
            }
        }).start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

