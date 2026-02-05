package com.innertrack.utils;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;

public class DBConnection {
    private static DBConnection instance;
    private Connection connection;

    private final String URL = "jdbc:mysql://localhost:3306/testDB";
    private final String USER = "root";
    private final String PASSWORD = "";

    private DBConnection() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Database connection established.");
        } catch (SQLException e) {
            System.err.println("Error establishing database connection: " + e.getMessage());
        }
    }

    public static DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            }
        } catch (SQLException e) {
            System.err.println("Error re-establishing database connection: " + e.getMessage());
        }
        return connection;
    }
}
