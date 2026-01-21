module com.test.frontend {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;

    requires com.google.gson;
    requires atlantafx.base;

    opens com.testing.frontend to javafx.fxml;
}
