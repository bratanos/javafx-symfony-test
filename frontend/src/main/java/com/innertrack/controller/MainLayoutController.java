package com.innertrack.controller;

import com.innertrack.session.SessionManager;
import com.innertrack.util.ViewManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.time.Year;

public class MainLayoutController {

    private static MainLayoutController instance;

    @FXML
    private HBox navbar;

    @FXML
    private VBox contentContainer;

    @FXML
    private Hyperlink loginLink;

    @FXML
    private Hyperlink registerLink;

    @FXML
    private Button profileBtn;

    @FXML
    private Label footerLabel;

    public static MainLayoutController getInstance() {
        return instance;
    }

    public void setNavbarVisible(boolean visible) {
        navbar.setVisible(visible);
        navbar.setManaged(visible);
    }

    @FXML
    public void initialize() {
        instance = this;
        ViewManager.setContainer(contentContainer);
        footerLabel.setText("© " + Year.now().getValue() + " InnerTrack — All rights reserved");
        updateUiForSession();
        loadAfficherEvenement();
    }

    public void loadAfficherEvenement() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AfficherEvenement.fxml"));
            VBox page = loader.load();
            contentContainer.getChildren().setAll(page);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateUiForSession() {
        boolean loggedIn = SessionManager.getInstance().getCurrentUser() != null;
        loginLink.setVisible(!loggedIn);
        registerLink.setVisible(!loggedIn);
        profileBtn.setVisible(loggedIn);
    }

    public void loadAfficherInscription() {
        try {
            VBox page = FXMLLoader.load(
                    getClass().getResource("/fxml/AfficherInscription.fxml")
            );
            contentContainer.getChildren().setAll(page);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToHome() {
        ViewManager.loadView("main");
    }

    @FXML
    private void goToLogin() {
        ViewManager.loadView("login");
    }

    @FXML
    private void goToRegister() {
        ViewManager.loadView("register");
    }

    @FXML
    public void goToInscriptions(ActionEvent actionEvent) {
        loadAfficherInscription();
    }
}
