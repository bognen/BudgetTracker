package com.bognen.budget.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseService {
    private static final String URL = "jdbc:sqlite:expenseData.db";
    private static Connection connection = null;

    // Initialize connection
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                connection = DriverManager.getConnection(URL);
                System.out.println("Connected to SQLite database!");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Returning connection to database!");
        return connection;
    }

    // Close connection
    public static void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
                connection = null;
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}