package com.bognen.budget;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import com.bognen.budget.service.DatabaseService;
import com.bognen.budget.service.DatabaseInitializer;

import java.io.IOException;

public class BudgetApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        DatabaseInitializer.initialize();

        FXMLLoader fxmlLoader = new FXMLLoader(BudgetApplication.class.getResource("views/mainViewContext.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1424, 768);
        scene.getStylesheets().add(getClass().getResource("/com/bognen/budget/styles/budget.css").toExternalForm());
        stage.setTitle("Expense Tracker");
        stage.setScene(scene);
        stage.show();

        // Ensure proper connection cleanup on shutdown
        //+++ Runtime.getRuntime().addShutdownHook(new Thread(DatabaseService::closeConnection));
    }

    @Override
    public void stop() {
        // Close database connection
        DatabaseService.closeConnection();
    }


    public static void main(String[] args) {
        launch();
    }
}