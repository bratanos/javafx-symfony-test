package com.innertrack.controller;

import com.innertrack.service.AuthService;
import com.innertrack.util.ViewManager;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.Duration;

public class VerifyOtpController {

    @FXML
    private Label subtitleLabel;

    @FXML
    private TextField otp1, otp2, otp3, otp4, otp5, otp6;

    @FXML
    private Label timerLabel;

    @FXML
    private Button resendButton;

    @FXML
    private Label messageLabel;

    private String userEmail;
    private final AuthService authService = new AuthService();
    private int secondsRemaining = 60;
    private Timeline timeline;

    public void setEmail(String email) {
        this.userEmail = email;
        subtitleLabel.setText("Un code a été envoyé à : " + email);
    }

    @FXML
    public void initialize() {
        MainLayoutController.getInstance().setNavbarVisible(false);
        setupOtpFields();
        startResendTimer();
    }

    private void setupOtpFields() {
        TextField[] fields = { otp1, otp2, otp3, otp4, otp5, otp6 };
        for (int i = 0; i < fields.length; i++) {
            final int index = i;
            fields[i].textProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal.length() > 1) {
                    fields[index].setText(newVal.substring(newVal.length() - 1));
                }
                if (!newVal.isEmpty() && index < fields.length - 1) {
                    fields[index + 1].requestFocus();
                }
            });

            fields[i].setOnKeyPressed(event -> {
                switch (event.getCode()) {
                    case BACK_SPACE:
                        if (fields[index].getText().isEmpty() && index > 0) {
                            fields[index - 1].requestFocus();
                            fields[index - 1].clear();
                        }
                        break;
                    default:
                        break;
                }
            });
        }
    }

    private void startResendTimer() {
        secondsRemaining = 60;
        resendButton.setDisable(true);
        timerLabel.setText("Vous pourrez renvoyer le code dans " + secondsRemaining + "s");

        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            secondsRemaining--;
            if (secondsRemaining <= 0) {
                resendButton.setDisable(false);
                timerLabel.setText("Vous pouvez maintenant renvoyer le code.");
                timeline.stop();
            } else {
                timerLabel.setText("Vous pourrez renvoyer le code dans " + secondsRemaining + "s");
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    @FXML
    private void handleVerify() {
        String code = otp1.getText() + otp2.getText() + otp3.getText() +
                otp4.getText() + otp5.getText() + otp6.getText();
        if (code.length() < 6) {
            showMessage("Veuillez saisir le code à 6 chiffres.", true);
            return;
        }

        String result = authService.verifyOtp(userEmail, code);
        if ("SUCCESS".equals(result)) {
            showMessage("Compte activé avec succès !", false);
            // Redirect to login after a short delay
            Timeline delay = new Timeline(new KeyFrame(Duration.seconds(2), e -> ViewManager.loadView("login")));
            delay.play();
        } else {
            showMessage(result, true);
        }
    }

    @FXML
    private void handleResend() {
        String result = authService.resendOtp(userEmail);
        if ("SUCCESS".equals(result)) {
            showMessage("Nouveau code envoyé !", false);
            startResendTimer();
        } else {
            showMessage(result, true);
        }
    }

    @FXML
    private void goToLogin() {
        if (timeline != null)
            timeline.stop();
        ViewManager.loadView("login");
    }

    private void showMessage(String message, boolean isError) {
        messageLabel.setText(message);
        if (isError) {
            messageLabel.setStyle("-fx-text-fill: -color-danger-emphasis;");
        } else {
            messageLabel.setStyle("-fx-text-fill: -color-success-emphasis;");
        }
    }
}
