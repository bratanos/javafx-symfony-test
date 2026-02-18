package com.innertrack.controller.profile;

import com.innertrack.dao.UserDao;
import com.innertrack.model.User;
import com.innertrack.session.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import javafx.scene.control.Alert;
import com.innertrack.util.ViewManager;

public class ProfileController {
    @FXML
    private Circle profileCircle;
    @FXML
    private Label nameLabel;
    @FXML
    private Label roleLabel;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField emailField;

    private final UserDao userDao = new UserDao();
    private User currentUser;

    @FXML
    public void initialize() {
        currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser != null) {
            loadUserData();
        }
    }

    private void loadUserData() {
        nameLabel.setText(currentUser.getFullName());
        if (currentUser.getRoles() != null && !currentUser.getRoles().isEmpty()) {
            roleLabel.setText(currentUser.getRoles().get(0).replace("ROLE_", ""));
        }
        firstNameField.setText(currentUser.getFirstName());
        lastNameField.setText(currentUser.getLastName());
        emailField.setText(currentUser.getEmail());
        updateProfileImage();
    }

    private void updateProfileImage() {
        String picPath = currentUser.getProfilePicture();
        System.out.println("DEBUG: Loading profile picture from: " + picPath);
        if (picPath != null && !picPath.isEmpty()) {
            try {
                File file = new File(picPath);
                if (file.exists()) {
                    System.out.println("DEBUG: File exists, loading image via FileInputStream...");
                    try (java.io.FileInputStream fis = new java.io.FileInputStream(file)) {
                        Image image = new Image(fis);
                        if (image.isError()) {
                            System.err.println("DEBUG: Image load error: " + image.getException().getMessage());
                        } else {
                            // Using scaling constructor for ImagePattern strictly
                            profileCircle.setFill(new ImagePattern(image, 0, 0, 1, 1, true));
                            System.out.println("DEBUG: Image loaded and applied with scaling pattern.");
                        }
                    }
                } else {
                    System.out.println("DEBUG: File does NOT exist at path: " + file.getAbsolutePath());
                }
            } catch (Exception e) {
                System.err.println("Error loading profile image: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("DEBUG: Profile picture path is null or empty.");
        }
    }

    @FXML
    private void handleChangePicture() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionner une photo de profil");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));
        File selectedFile = fileChooser.showOpenDialog(profileCircle.getScene().getWindow());

        if (selectedFile != null) {
            try {
                String uploadDir = "uploads/profiles";
                Path uploadPath = Paths.get(uploadDir).toAbsolutePath();
                Files.createDirectories(uploadPath);

                String fileName = currentUser.getId() + "_" + System.currentTimeMillis() + "_" + selectedFile.getName();
                Path destPath = uploadPath.resolve(fileName);

                Files.copy(selectedFile.toPath(), destPath, StandardCopyOption.REPLACE_EXISTING);

                currentUser.setProfilePicture(destPath.toString());
                userDao.update(currentUser);
                updateProfileImage();

                showFeedback("Succès", "Photo de profil mise à jour avec succès !");

            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleUpdateProfile() {
        currentUser.setFirstName(firstNameField.getText());
        currentUser.setLastName(lastNameField.getText());
        try {
            if (userDao.update(currentUser)) {
                nameLabel.setText(currentUser.getFullName());
                com.innertrack.controller.MainLayoutController.getInstance().updateUiForSession();
                showFeedback("Succès", "Profil mis à jour avec succès !");
                handleBack();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showFeedback("Erreur", "Impossible de mettre à jour le profil.");
        }
    }

    @FXML
    private void handleBack() {
        String role = currentUser.getRoles().get(0);
        if (role.contains("ADMIN")) {
            ViewManager.loadView("admin/dashboard");
        } else if (role.contains("PSYCHOLOGUE")) {
            ViewManager.loadView("psychologue/dashboard");
        } else {
            ViewManager.loadView("user/dashboard");
        }
    }

    private void showFeedback(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
