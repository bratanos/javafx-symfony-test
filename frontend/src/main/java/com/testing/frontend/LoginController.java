package com.testing.frontend;
import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;


public class LoginController {
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label errorLabel;
    private final Gson gson = new Gson();

    @FXML
    public void onLoginClicked(){
        errorLabel.setText("");

        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        if (email.isEmpty() || password.isEmpty()){
            errorLabel.setText("Please fill all the fields");
            return;
        }

        new Thread(() -> {
            try{
                HttpClient client = HttpClient.newHttpClient();

                Map<String, String> data = new HashMap<>();
                data.put("email", email);
                data.put("password", password);

                String requestBody = gson.toJson(data);

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:8000/api/login"))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200 ) {
                    //success
                    Map<String, Object> res = gson.fromJson(response.body(), Map.class);
                    String token = (String) res.get("token");

                    Platform.runLater(() -> {
                        errorLabel.setStyle("-fx-text-fill: green;");
                        errorLabel.setText("Login successful! token: " + token);
                    });
                } else {
                    Platform.runLater(() -> {
                        errorLabel.setStyle("-fx-text-fill: red;");
                        errorLabel.setText("Login failed: " + response.body());
                    });
                }
            } catch(Exception ex) {
                Platform.runLater(() -> {
                    errorLabel.setStyle("-fx-text-fill: red;");
                    errorLabel.setText("Error: " + ex.getMessage());
                });
            }
        }).start();
    }
}
