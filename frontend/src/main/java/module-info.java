module com.innertrack {
    requires transitive javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires java.sql;
    requires com.google.gson;
    requires atlantafx.base;
    requires jakarta.mail;

    // Security & JWT
    requires jbcrypt;
    requires jjwt.api;

    // UI Libraries
    requires com.jfoenix;
    requires MaterialFX;
    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fontawesome5;

    opens com.innertrack.model to com.google.gson, javafx.base;
    opens com.innertrack.dao to java.sql;
    opens com.innertrack.service to java.base;
    opens com.innertrack.security to jjwt.api;
    opens com.innertrack.controller to javafx.fxml;
    opens com.innertrack.controller.admin to javafx.fxml;
    opens com.innertrack.controller.psychologue to javafx.fxml;
    opens com.innertrack.controller.user to javafx.fxml;
    opens com.innertrack.controller.profile to javafx.fxml;
    opens com.innertrack.util to javafx.fxml;

    // Auth resources are now in a subfolder
    opens fxml.auth to javafx.fxml;

    exports com.innertrack.app;
    exports com.innertrack.model;
    exports com.innertrack.service;
    exports com.innertrack.security;
    exports com.innertrack.session;
    exports com.innertrack.controller;
    exports com.innertrack.controller.admin;
    exports com.innertrack.controller.psychologue;
    exports com.innertrack.controller.user;
    exports com.innertrack.controller.profile;
    exports com.innertrack.util;

    opens com.innertrack.app to javafx.fxml;
}
