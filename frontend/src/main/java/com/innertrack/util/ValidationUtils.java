package com.innertrack.util;

import javafx.scene.control.Alert;
import javafx.scene.control.TextInputControl;
import java.time.LocalDate;

public class ValidationUtils {

    // ============================================
    // BASIC VALIDATION
    // ============================================

    public static boolean isNotEmpty(TextInputControl field, String fieldName) {
        if (field.getText() == null || field.getText().trim().isEmpty()) {
            showError(fieldName + " ne peut pas être vide.");
            field.requestFocus();
            return false;
        }
        return true;
    }

    public static boolean isNotNull(Object object, String fieldName) {
        if (object == null) {
            showError(fieldName + " doit être choisi.");
            return false;
        }
        return true;
    }

    // ============================================
    // LENGTH VALIDATION
    // ============================================

    public static boolean hasMinLength(TextInputControl field, String fieldName, int minLength) {
        String text = field.getText().trim();
        if (text.length() < minLength) {
            showError(fieldName + " doit contenir au moins " + minLength + " caractères.");
            field.requestFocus();
            return false;
        }
        return true;
    }

    public static boolean hasMaxLength(TextInputControl field, String fieldName, int maxLength) {
        String text = field.getText().trim();
        if (text.length() > maxLength) {
            showError(fieldName + " ne peut pas dépasser " + maxLength + " caractères.");
            field.requestFocus();
            return false;
        }
        return true;
    }

    public static boolean hasLengthBetween(TextInputControl field, String fieldName, int min, int max) {
        String text = field.getText().trim();
        if (text.length() < min || text.length() > max) {
            showError(fieldName + " doit contenir entre " + min + " et " + max + " caractères.");
            field.requestFocus();
            return false;
        }
        return true;
    }

    // ============================================
    // FORMAT VALIDATION
    // ============================================

    /**
     * Validate text contains only letters, spaces, hyphens and accented characters
     */
    public static boolean isValidName(TextInputControl field, String fieldName) {
        String text = field.getText().trim();
        // Allows: letters, spaces, hyphens, apostrophes, and French accented characters
        if (!text.matches("^[a-zA-ZÀ-ÿ\\s'-]+$")) {
            showError(fieldName + " ne peut contenir que des lettres, espaces, apostrophes et tirets.");
            field.requestFocus();
            return false;
        }
        return true;
    }

    /**
     * Validate text contains only alphanumeric and common punctuation
     */
    public static boolean isValidText(TextInputControl field, String fieldName) {
        String text = field.getText().trim();
        // Allows: letters, numbers, spaces, and common punctuation
        if (!text.matches("^[a-zA-Z0-9À-ÿ\\s.,!?;:'\"-]+$")) {
            showError(fieldName + " contient des caractères non autorisés.");
            field.requestFocus();
            return false;
        }
        return true;
    }

    /**
     * Check for SQL injection patterns (basic security)
     */
    public static boolean isSafeInput(TextInputControl field, String fieldName) {
        String text = field.getText().trim().toLowerCase();
        String[] dangerousPatterns = {"drop ", "delete ", "insert ", "update ", "select ", "--", "/*", "*/", "xp_", "sp_"};

        for (String pattern : dangerousPatterns) {
            if (text.contains(pattern)) {
                showError(fieldName + " contient des caractères ou mots interdits.");
                field.requestFocus();
                return false;
            }
        }
        return true;
    }

    // ============================================
    // DATE VALIDATION
    // ============================================

    public static boolean isDateSelected(LocalDate date, String fieldName) {
        if (date == null) {
            showError(fieldName + " doit être sélectionnée.");
            return false;
        }
        return true;
    }

    public static boolean isNotFutureDate(LocalDate date, String fieldName) {
        if (date != null && date.isAfter(LocalDate.now())) {
            showError(fieldName + " ne peut pas être dans le futur.");
            return false;
        }
        return true;
    }

    public static boolean isDateInRange(LocalDate date, String fieldName, LocalDate min, LocalDate max) {
        if (date != null) {
            if (date.isBefore(min)) {
                showError(fieldName + " ne peut pas être avant " + min + ".");
                return false;
            }
            if (date.isAfter(max)) {
                showError(fieldName + " ne peut pas être après " + max + ".");
                return false;
            }
        }
        return true;
    }

    // ============================================
    // CONTENT VALIDATION
    // ============================================

    /**
     * Validate content has meaningful text (not just spaces/special chars)
     */
    public static boolean hasMeaningfulContent(TextInputControl field, String fieldName) {
        String text = field.getText().trim();
        // Remove all non-letter characters and check if something remains
        String lettersOnly = text.replaceAll("[^a-zA-ZÀ-ÿ]", "");
        if (lettersOnly.length() < 3) {
            showError(fieldName + " doit contenir du texte significatif (au moins 3 lettres).");
            field.requestFocus();
            return false;
        }
        return true;
    }

    // ============================================
    // ALERT DIALOGS
    // ============================================

    public static void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("❌ Erreur de validation");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("✅ Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void showWarning(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("⚠️ Attention");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static boolean confirmDelete(String itemName) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer " + itemName + " ?");
        alert.setContentText("Cette action est irréversible.");
        return alert.showAndWait().filter(r -> r == javafx.scene.control.ButtonType.OK).isPresent();
    }

    public static boolean confirmAction(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        return alert.showAndWait().filter(r -> r == javafx.scene.control.ButtonType.OK).isPresent();
    }
}
