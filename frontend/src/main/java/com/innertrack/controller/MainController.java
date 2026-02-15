package com.innertrack.controller;

import javafx.fxml.FXML;

public class MainController {

    @FXML
    public void initialize() {
        MainLayoutController.getInstance().loadAfficherEvenement();
    }
}
