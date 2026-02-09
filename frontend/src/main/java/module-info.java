module com.test.frontend {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires java.sql;

    requires com.google.gson;
    requires atlantafx.base;

    opens com.innertrack to javafx.fxml;

}
