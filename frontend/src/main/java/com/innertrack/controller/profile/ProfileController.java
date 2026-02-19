package com.innertrack.controller.profile;

import com.innertrack.dao.UserDao;
import com.innertrack.model.User;
import com.innertrack.service.SettingsService;
import com.innertrack.session.SessionManager;
import com.innertrack.util.ViewManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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
import java.time.format.DateTimeFormatter;
import java.util.Optional;

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

    // Settings Components
    @FXML
    private ToggleButton lightThemeBtn;
    @FXML
    private ToggleButton darkThemeBtn;
    @FXML
    private ComboBox<String> languageComboBox;
    @FXML
    private RadioButton fontSmallBtn;
    @FXML
    private RadioButton fontNormalBtn;
    @FXML
    private RadioButton fontLargeBtn;
    @FXML
    private Label creationDateLabel;
    @FXML
    private Label lastLoginLabel;

    private final UserDao userDao = new UserDao();
    private final SettingsService settingsService = SettingsService.getInstance();
    private User currentUser;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @FXML
    public void initialize() {
        currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser != null) {
            loadUserData();
            initSettings();
        }
    }

    private void initSettings() {
        // Theme
        ToggleGroup themeGroup = new ToggleGroup();
        lightThemeBtn.setToggleGroup(themeGroup);
        darkThemeBtn.setToggleGroup(themeGroup);

        String currentTheme = settingsService.getCurrentSettings().getTheme();
        if ("DARK".equals(currentTheme)) {
            darkThemeBtn.setSelected(true);
        } else {
            lightThemeBtn.setSelected(true);
        }

        // Language
        languageComboBox.getItems().addAll("English", "Français");
        String currentLang = settingsService.getCurrentSettings().getLanguage();
        languageComboBox.setValue("FR".equals(currentLang) ? "Français" : "English");

        // Font Size
        ToggleGroup fontGroup = new ToggleGroup();
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

        // Account Meta
        creationDateLabel
                .setText(currentUser.getCreatedAt() != null ? currentUser.getCreatedAt().format(dateFormatter) : "N/A");
        lastLoginLabel
                .setText(currentUser.getLastLogin() != null ? currentUser.getLastLogin().format(dateFormatter) : "N/A");
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
    private void handleChangePassword() {
        ViewManager.loadView("profile/change_password");
    }

    // Settings Handlers
    @FXML
    private void setLightTheme() {
        settingsService.updateTheme("LIGHT");
    }

    @FXML
    private void setDarkTheme() {
        settingsService.updateTheme("DARK");
    }

    @FXML
    private void handleLanguageChange() {
        String selected = languageComboBox.getValue();
        settingsService.updateLanguage("Français".equals(selected) ? "FR" : "EN");

        // Reload the view to apply internationalization
        ViewManager.loadView("profile/settings");
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

    @FXML
    private void handleDeactivate() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Désactivation du compte");
        alert.setHeaderText("Êtes-vous sûr de vouloir désactiver votre compte ?");
        alert.setContentText("Vous devrez à nouveau vérifier votre email lors de votre prochaine connexion.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (userDao.updateStatus(currentUser.getId(), "PENDING", false)) {
                SessionManager.getInstance().logout();
                ViewManager.loadView("login");
            }
        }
    }

    @FXML
    private void handleDelete() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Blocage du compte");
        alert.setHeaderText("Action irréversible : bloquer votre compte ?");
        alert.setContentText("Un code de vérification sera envoyé à votre email pour confirmer le blocage définitif.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            com.innertrack.service.AuthService authService = new com.innertrack.service.AuthService();
            authService.sendNewOtp(currentUser);

            com.innertrack.controller.VerifyOtpController controller = ViewManager.loadView("verify_otp");
            if (controller != null) {
                controller.setEmail(currentUser.getEmail());
            }
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
