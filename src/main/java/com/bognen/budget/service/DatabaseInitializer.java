package com.bognen.budget.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {
    public static void initialize() {
        String createUsersTable = """
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                email TEXT NOT NULL UNIQUE
            );
        """;

        String createExpenseTable = """
            CREATE TABLE IF NOT EXISTS expense_items (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                description TEXT NOT NULL,
                isValid INTEGER NOT NULL
            );
        """;

        String createExpenseRelationsTable = """
            CREATE TABLE IF NOT EXISTS expense_items_relations (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                parent_id INTEGER NOT NULL,
                child_id INTEGER NOT NULL,
                FOREIGN KEY (parent_id) REFERENCES expenses (id) ON DELETE CASCADE,
                FOREIGN KEY (child_id) REFERENCES expenses (id) ON DELETE CASCADE
            );
        """;

        try (Connection conn = DatabaseService.getConnection();
            Statement stmt = conn.createStatement()) {

            stmt.execute(createUsersTable);
            stmt.execute(createExpenseTable);
            stmt.execute(createExpenseRelationsTable);
            System.out.println("Tables have been initialized successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}