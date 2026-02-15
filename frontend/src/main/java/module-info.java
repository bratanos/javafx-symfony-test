module com.innertrack {
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
    requires javafx.base;
    requires jdk.jfr;
    requires javafx.graphics;
    requires java.desktop;

    opens com.innertrack.model to com.google.gson, javafx.base;
    opens com.innertrack.dao to java.sql;
    opens com.innertrack.service to java.base;
    opens com.innertrack.security to jjwt.api;
    opens com.innertrack.controller to javafx.fxml;
    opens com.innertrack.util to javafx.fxml;

    exports com.innertrack.app;
    exports com.innertrack.model;
    exports com.innertrack.service;
    exports com.innertrack.security;
    exports com.innertrack.session;
    exports com.innertrack.controller;
    exports com.innertrack.util;

    opens com.innertrack.app to javafx.fxml;
}
